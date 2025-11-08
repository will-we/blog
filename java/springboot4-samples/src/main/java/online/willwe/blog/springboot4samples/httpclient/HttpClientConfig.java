package online.willwe.blog.springboot4samples.httpclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.AbstractHttpServiceRegistrar;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "users", types = {UserApi.class})
public class HttpClientConfig {

    @SuppressWarnings({"unused", "NullableProblems"})
    static class UserServicesHttpServiceRegistrar extends AbstractHttpServiceRegistrar {

        @Override
        protected void registerHttpServices(GroupRegistry registry, AnnotationMetadata metadata) {
            // more registrations…
        }
    }

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer() {
        return groups -> groups.filterByName("users").forEachClient((group, builder) ->
                builder.baseUrl("http://localhost:8080/mockUser/"));
    }
}