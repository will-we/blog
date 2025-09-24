package online.willwe.blog.springboot4samples.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import online.willwe.blog.springboot4samples.config.ReTryConfig;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.retry.RetryException;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.core.retry.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class RetryController {
    @Resource
    private RetryTemplate retryTemplate;

    private int retryCount = 0;

    @GetMapping("/retry")
    public String retry() throws RetryException {
        retryTemplate.execute(new SimpleRetryable());
        return "retryCount";
    }

    class SimpleRetryable implements Retryable<@NonNull Boolean> {

        @Override
        public @Nullable Boolean execute() throws Throwable {
            if (retryCount++ < ReTryConfig.MAX_ATTEMPTS) {
                String message = "第%s重试失败".formatted(retryCount);
                log.info(message);
                throw new RetryException(message, new RuntimeException(message));
            }
            log.info("重试最终成功");
            return true;
        }

        @Override
        public @NonNull String getName() {
            return "重试作业";
        }
    }


}
