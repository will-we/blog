package online.willwe.dubbosample;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class SpringmvcRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringmvcRestApplication.class, args);
    }
}
