package online.willwe.blog.springboot4samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class Springboot4SamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot4SamplesApplication.class, args);
    }

}
