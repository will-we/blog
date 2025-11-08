package online.willwe.blog.springboot4samples.httpclient;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@HttpExchange("/user")
public interface UserApi {
    @GetExchange("/{id}")
    Map<String, Object> get(@PathVariable String id);
}
