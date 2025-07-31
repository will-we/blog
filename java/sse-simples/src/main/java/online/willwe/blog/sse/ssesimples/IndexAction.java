package online.willwe.blog.sse.ssesimples;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

@Controller
public class IndexAction {
    @Autowired
    private SseClient sseClient;

    @GetMapping("/")
    public String index(ModelMap model) {
        String uid = IdUtil.fastUUID();
        model.put("uid", uid);
        return "index";
    }

    @CrossOrigin
    @GetMapping("/createSse")
    public SseEmitter createConnect(@RequestParam("uid") String uid) {
        new Thread(() -> {
            while (true) {
                sseClient.sendMessage(uid, "no", LocalDateTime.now().toString());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return sseClient.createSse(uid);
    }

    /**
     * 关闭连接
     */
    @CrossOrigin
    @GetMapping("/closeSse")
    public void closeConnect(String uid) {
        sseClient.closeSse(uid);
    }
}