package com.kk215.kishanBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class KishanBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KishanBackendApplication.class, args);
    }

    /**
     * Configuration class to serve static resources (images, CSS, JS)
     * Maps /image/** requests to classpath:/static/image/
     */
    @Configuration
    public static class WebMvcConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/image/**")
                    .addResourceLocations("classpath:/static/image/");
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/static/");
        }
    }
}
