package online.willwe.dubbosample.prvider;

import online.willwe.dubbosample.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;

@DubboService
public class UserServiceImpl implements UserService {

    public Map<String, String> userRepository = Map.of("1", "dubbo", "2", "apache");

    @Override
    public String findUserNameById(String id) {
        if (!userRepository.containsKey(id)) {
            throw new RuntimeException(id + " not found");
        }
        return "id : " + id + " name : " + userRepository.get(id);
    }

    @Override
    public String sayHello() {
        return "hell world, dubbo";
    }
}
