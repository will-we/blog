package online.willwe.blog.websocket.websocketsamples;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 配置静态资源访问和视图控制器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置视图控制器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 根路径重定向到聊天页面
        registry.addRedirectViewController("/", "/chat.html");
        registry.addRedirectViewController("/index", "/chat.html");
        registry.addRedirectViewController("/home", "/chat.html");
    }
}