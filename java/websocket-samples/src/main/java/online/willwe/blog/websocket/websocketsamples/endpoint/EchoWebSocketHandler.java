package online.willwe.blog.websocket.websocketsamples.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
public class EchoWebSocketHandler extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload(); // 接收消息
        log.info("接收服务端消息内容：[{}]", payload);
        session.sendMessage(new TextMessage("Echo: " + payload)); // 发送响应
    }
}