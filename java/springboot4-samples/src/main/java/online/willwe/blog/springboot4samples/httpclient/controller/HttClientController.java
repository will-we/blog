package online.willwe.blog.springboot4samples.httpclient.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import online.willwe.blog.springboot4samples.httpclient.UserApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class HttClientController {
    @Resource
    UserApi userApi;
    @RequestMapping("/user")
    public Map<String, Object> getUser(@RequestParam String id) {
        Map<String, Object> users = userApi.get(id);
        log.info("users: {}", users);
        return users;
    }
}
