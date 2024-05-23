package org.liuxp.minioplus.config;

import org.springframework.context.annotation.Bean;

/**
 * MinioPlusProperties自动配置类
 * @author contact@liuxp.me
 */
public class MinioPlusConfig {

    @Bean
    public MinioPlusProperties tosProperties() {
        return new MinioPlusProperties();
    }

}