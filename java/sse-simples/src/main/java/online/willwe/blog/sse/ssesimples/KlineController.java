package online.willwe.blog.sse.ssesimples;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/kline")
public class KlineController {

    @Autowired
    private SseClient sseClient;

    private final Random random = new Random();

    /**
     * 获取K线数据
     */
    @GetMapping("/data")
    public String getKlineData() {
        try {
            ClassPathResource resource = new ClassPathResource("static/kline-data.json");
            return FileUtil.readString(resource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("读取K线数据失败", e);
            return "[]";
        }
    }

    /**
     * 创建K线SSE连接
     */
    @CrossOrigin
    @GetMapping("/stream")
    public SseEmitter createKlineStream() {
        String uid = IdUtil.fastUUID();
        
        // 启动一个线程模拟K线数据更新
        new Thread(() -> {
            try {
                // 读取初始K线数据
                ClassPathResource resource = new ClassPathResource("static/kline-data.json");
                String klineData = FileUtil.readString(resource.getFile(), StandardCharsets.UTF_8);
                
                // 先发送初始数据，使用事件名称而不是ID
                sseClient.sendMessage(uid, null, klineData, "init");
                
                // 从初始数据中获取最后一个收盘价
                double lastClose = 169.73; // 默认初始收盘价
                try {
                    // 尝试解析JSON获取最后一个数据点的收盘价
                    if (klineData.contains("close")) {
                        int lastCloseIndex = klineData.lastIndexOf("close");
                        if (lastCloseIndex > 0) {
                            int valueStart = klineData.indexOf(":", lastCloseIndex) + 1;
                            int valueEnd = klineData.indexOf(",", valueStart);
                            if (valueEnd == -1) { // 可能是JSON数组中最后一个对象的最后一个属性
                                valueEnd = klineData.indexOf("}", valueStart);
                            }
                            if (valueStart > 0 && valueEnd > valueStart) {
                                String closeValue = klineData.substring(valueStart, valueEnd).trim();
                                lastClose = Double.parseDouble(closeValue);
                                log.info("从初始数据中获取到最后收盘价: {}", lastClose);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("解析初始收盘价失败，使用默认值: {}", e.getMessage());
                }
                
                // 每秒更新一次数据
                while (true) {
                    // 模拟新的K线数据点
                    double change = (random.nextDouble() - 0.5) * 5; // 随机变化
                    double open = lastClose;
                    double close = open + change;
                    double high = Math.max(open, close) + random.nextDouble() * 2;
                    double low = Math.min(open, close) - random.nextDouble() * 2;
                    int volume = 15000 + random.nextInt(5000);
                    
                    // 更新最后收盘价
                    lastClose = close;
                    
                    String newDataPoint = String.format(
                            "{\"time\": \"%s\", \"open\": %.2f, \"high\": %.2f, \"low\": %.2f, \"close\": %.2f, \"volume\": %d}",
                            LocalDateTime.now(), open, high, low, close, volume
                    );
                    
                    sseClient.sendMessage(uid, null, newDataPoint, "update");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                log.error("K线数据推送异常", e);
                sseClient.closeSse(uid);
            }
        }).start();
        
        return sseClient.createSse(uid);
    }
}