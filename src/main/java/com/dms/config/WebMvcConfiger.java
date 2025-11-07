package com.dms.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public interface WebMvcConfiger {
    void addResourceHandlers(ResourceHandlerRegistry registry);
}
