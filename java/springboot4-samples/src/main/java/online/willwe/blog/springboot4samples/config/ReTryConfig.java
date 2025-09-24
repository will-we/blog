package online.willwe.blog.springboot4samples.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.core.retry.Retryable;
import org.springframework.core.retry.support.CompositeRetryListener;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class ReTryConfig {

    public static final int MAX_ATTEMPTS = 3;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        RetryPolicy policy = RetryPolicy.builder().backOff(new FixedBackOff(2000, MAX_ATTEMPTS)).build();

        retryTemplate.setRetryPolicy(policy);

        retryTemplate.setRetryListener(new LogRetryListener());

        return retryTemplate;
    }

    static class LogRetryListener extends CompositeRetryListener {
        @Override
        public void beforeRetry(@NonNull RetryPolicy retryPolicy, Retryable<?> retryable) {
            log.info("Retrying operation: {}", retryable.toString());
        }

    }
}
