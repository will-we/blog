package online.willwe.blog.springboot4samples.resilience.retry.services;

import online.willwe.blog.springboot4samples.resilience.retry.config.ReTryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BizService {
    final static Logger log = LoggerFactory.getLogger(BizService.class);
    int retryCount = 0;

    @org.springframework.resilience.annotation.Retryable(
            maxRetries = 4L, delay = 2000L, multiplier = 2,timeUnit = TimeUnit.MILLISECONDS
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
