package online.willwe.blog.springboot4samples.httpclient.controller;

import jakarta.annotation.Resource;
import online.willwe.blog.springboot4samples.httpclient.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HttClientController {
    final static Logger log = LoggerFactory.getLogger(HttClientController.class);
    @Resource
    UserApi userApi;
    @RequestMapping("/user")
    public Map<String, Object> getUser(@RequestParam String id) {
        Map<String, Object> users = userApi.get(id);
        log.info("users: {}", users);
        return users;
    }
}
