package online.willwe.blog.websocket.websocketsamples.endpoint;

import cn.hutool.json.JSONUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import online.willwe.blog.websocket.websocketsamples.entity.ChatMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static online.willwe.blog.websocket.websocketsamples.entity.ChatMessage.MessageType.CHAT;
import static online.willwe.blog.websocket.websocketsamples.entity.ChatMessage.MessageType.JOIN;

@Slf4j
@ServerEndpoint("/chat/{username}")
@Component
public class ChatEndpoint {
    /**
     * 静态变量，用来记录当前在线连接数
     */
    private static volatile AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 使用CopyOnWriteArraySet来存放每个客户端对应的WebSocket对象
     * 线程安全的Set，用于存放每个客户端对应的Session对象
     */
    private static CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    /**
     * 用户名和Session的映射关系
     */
    private static ConcurrentHashMap<String, Session> userSessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        int count = onlineCount.incrementAndGet();
        log.info("[id：{}]用户{}连接成功，当前在线人数为：{}", session.getId(), username, count);
        sessions.add(session);
        userSessionMap.put(username, session);
        // 发送用户加入通知
        ChatMessage joinMessage = new ChatMessage(JOIN, username + " 加入了聊天室", "系统");
        // 发送当前在线用户列表
        sendMessageToAll(JSONUtil.toJsonStr(joinMessage));
        // 发送更新后的在线用户列表
        sendOnlineUserList();
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.info("收到来自用户{}的消息：{}", username, message);
        
        try {
            // 尝试解析JSON消息
            ChatMessage receivedMessage = JSONUtil.toBean(message, ChatMessage.class);
            receivedMessage.setSender(username);
            receivedMessage.setTimestamp(LocalDateTime.now());
            
            // 如果有接收者，设置为私聊消息
            if (StringUtils.hasLength(receivedMessage.getReceiver())) {
                receivedMessage.setPrivate(true);
            }
            
            sendMessage(receivedMessage);
        } catch (Exception e) {
            // 如果不是JSON格式，当作普通群聊消息处理
            ChatMessage chatMessage = new ChatMessage(CHAT, message, username);
            sendMessage(chatMessage);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(session);
        if (username != null) {
            userSessionMap.remove(username);
        }

        log.info("用户{}断开连接，当前在线人数为：{}", username, onlineCount.decrementAndGet());

        // 发送用户离开通知
        if (username != null) {
            ChatMessage leaveMessage = new ChatMessage(ChatMessage.MessageType.LEAVE,
                    username + " 离开了聊天室", "系统");
            sendMessageToAll(JSONUtil.toJsonStr(leaveMessage));

            // 发送更新后的在线用户列表
            sendOnlineUserList();
        }
    }


    public void sendMessage(ChatMessage chatMessage) {
        String jsonMessage = JSONUtil.toJsonStr(chatMessage);
        if (chatMessage.isPrivate()) {
            // 私聊消息：发送给接收者和发送者
            sendMessageToUser(chatMessage.getReceiver(), jsonMessage);
            sendMessageToUser(chatMessage.getSender(), jsonMessage);
        } else {
            // 群聊消息：发送给所有人
            sendMessageToAll(jsonMessage);
        }
    }

    /**
     * 群发消息
     */
    private void sendMessageToAll(String message) {
        // 使用迭代器安全地遍历和移除无效会话
        sessions.removeIf(session -> {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                    return false; // 保留有效会话
                } else {
                    log.warn("发现已关闭的会话，将其移除");
                    return true; // 移除已关闭的会话
                }
            } catch (IOException e) {
                log.error("发送消息失败，移除异常会话：", e);
                return true; // 移除异常会话
            }
        });
    }

    /**
     * 发送消息给指定用户
     */
    private void sendMessageToUser(String username, String message) {
        Session session = userSessionMap.get(username);
        if (session != null) {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                } else {
                    log.warn("用户{}的会话已关闭，清理会话映射", username);
                    userSessionMap.remove(username);
                    sessions.remove(session);
                }
            } catch (IOException e) {
                log.error("发送私聊消息失败，清理用户{}的会话：", username, e);
                userSessionMap.remove(username);
                sessions.remove(session);
            }
        }
    }

    private void sendOnlineUserList() {
        ChatMessage userListMessage = new ChatMessage(CHAT, String.join(",", userSessionMap.keySet()), "在线用户");
        sendMessageToAll(JSONUtil.toJsonStr(userListMessage));
    }
}