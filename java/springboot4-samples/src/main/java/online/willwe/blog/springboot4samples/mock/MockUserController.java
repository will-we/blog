package online.willwe.blog.springboot4samples.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mockUser")
public class MockUserController {
    @GetMapping("/user/{id}")
    public User get(@PathVariable int id) {
        User user = new User(id, "Peter", 25);
        log.info("returning mock user {}", user);
        return user;
    }

    public record User(int id, String name, int age) {
    }
}
