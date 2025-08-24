package online.willwe.blog.websocket.websocketsamples.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 */
@Data
public class ChatMessage {
    
    /**
     * 消息类型
     */
    public enum MessageType {
        CHAT,    // 聊天消息
        JOIN,    // 用户加入
        LEAVE    // 用户离开
    }
    
    /**
     * 消息类型
     */
    private MessageType type;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 发送者用户名
     */
    private String sender;
    
    /**
     * 接收者用户名（私聊时使用，群聊时为空）
     */
    private String receiver;
    
    /**
     * 发送时间
     */
    private LocalDateTime timestamp;
    
    /**
     * 是否为私聊消息
     */
    private boolean isPrivate;
    
    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ChatMessage(MessageType type, String content, String sender) {
        this();
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.isPrivate = false;
    }
    
    public ChatMessage(MessageType type, String content, String sender, String receiver) {
        this(type, content, sender);
        this.receiver = receiver;
        this.isPrivate = true;
    }
}