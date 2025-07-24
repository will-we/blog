package online.willwe.dubbosample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserService {

    @GetMapping("findUserId/{id}")
    String findUserNameById(@PathVariable String id);

    @GetMapping("hello")
    String sayHello();
}
