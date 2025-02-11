package com.terte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "httpTaskExecutor")
    public Executor httpTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 기본 스레드 개수
        executor.setMaxPoolSize(50);   // 최대 스레드 개수
        executor.setQueueCapacity(200);// 대기 큐 크기
        executor.setThreadNamePrefix("HttpExecutor-");
        executor.initialize();
        return executor;
    }
}
