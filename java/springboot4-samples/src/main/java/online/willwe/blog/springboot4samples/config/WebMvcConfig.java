package online.willwe.blog.springboot4samples.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 配置版本策略（使用Accept头方式）
    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.useQueryParam("version");
    }
}