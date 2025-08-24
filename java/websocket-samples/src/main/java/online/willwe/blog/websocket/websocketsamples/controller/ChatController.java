package online.willwe.blog.websocket.websocketsamples.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天室控制器
 */
@Controller
@RequestMapping("/")
public class ChatController {

    /**
     * 聊天室首页
     */
    @GetMapping
    public String index() {
        return "redirect:/chat.html";
    }

    /**
     * 聊天室页面
     */
    @GetMapping("/chatroom")
    public String chat() {
        return "redirect:/chat.html";
    }

    /**
     * 获取服务器状态
     */
    @GetMapping("/api/status")
    @ResponseBody
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "running");
        status.put("message", "WebSocket聊天室服务正常运行");
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }

    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    @ResponseBody
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "websocket-chat");
        health.put("timestamp", System.currentTimeMillis());
        return health;
    }
}