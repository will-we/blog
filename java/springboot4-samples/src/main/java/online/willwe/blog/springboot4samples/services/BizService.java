package online.willwe.blog.springboot4samples.services;

import lombok.extern.slf4j.Slf4j;
import online.willwe.blog.springboot4samples.config.ReTryConfig;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BizService {
    int retryCount = 0;

    @org.springframework.resilience.annotation.Retryable(
            maxAttempts = 4L, delay = 1000L, multiplier = 2,timeUnit = TimeUnit.MILLISECONDS
    )
    public void actual() {
        if (retryCount++ < ReTryConfig.MAX_ATTEMPTS) {
            String message = "第%s重试失败".formatted(retryCount);
            log.info(message);
            throw new RuntimeException(message);
        }
        log.info("重试最终成功");
        retryCount = 0;
    }
}
