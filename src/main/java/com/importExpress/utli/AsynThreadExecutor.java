package com.importExpress.utli;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * *****************************************************************************************
 *
 * @ClassName AsynThreadExecutor
 * @Author: cjc
 * @Descripeion 通过注解的实行，实现线程池的管理和异步操作
 * @Date： 2019/5/22 14:51:37
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       14:51:372019/5/22     cjc                       初版
 * ******************************************************************************************
 */
@Configuration
@ComponentScan({"com.importExpress","com.cbt"})
@EnableAsync// 使用该注解开启异步任务支持
public class AsynThreadExecutor extends AsyncConfigurerSupport {//配置类实现AsyncConfigurer接口 并重写getAsyncExecutor方法
    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor thread = new ThreadPoolTaskExecutor();
        //核心线程数，核心线程会一直存活，即使没有任务需要处理。当线程数小于核心线程数时，
        //即使现有的线程空闲，线程池也会优先创建新线程来处理任务，而不是直接交给现有的线程处理。
        // 核心线程在allowCoreThreadTimeout被设置为true时会超时退出，默认情况下不会退出。
        //线程池维护的最小线程数 / 核心线程数
        thread.setCorePoolSize(50);
        //线程池最大的线程数
        //当线程数大于或等于核心线程，且任务队列已满时，线程池会创建新的线程，
        // 直到线程数量达到maxPoolSize。如果线程数已等于maxPoolSize，且任务队列已满，则已超出线程池的处理能力，
        // 线程池会拒绝处理任务而抛出异常。
        thread.setMaxPoolSize(500);
        //任务队列容量。从maxPoolSize的描述上可以看出，
        // 任务队列的容量会影响到线程的变化，因此任务队列的长度也需要恰当的设置。
        //线程池所使用的缓冲队列
        thread.setQueueCapacity(200);
        //线程池所允许的空闲时间
        thread.setKeepAliveSeconds(30000);
        thread.initialize();
        return thread;
    }
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
