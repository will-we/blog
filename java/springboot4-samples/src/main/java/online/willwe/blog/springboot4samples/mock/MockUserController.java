package online.willwe.blog.springboot4samples.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mockUser")
public class MockUserController {
    final static Logger log = LoggerFactory.getLogger(MockUserController.class);
    @GetMapping("/user/{id}")
    public User get(@PathVariable int id) {
        User user = new User(id, "Peter", 25);
        log.info("returning mock user {}", user);
        return user;
    }

    public record User(int id, String name, int age) {
    }
}
