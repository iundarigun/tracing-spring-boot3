package com.wefox.spring3.spring3.configuration

import io.micrometer.context.ContextExecutorService
import io.micrometer.context.ContextScheduledExecutorService
import io.micrometer.context.ContextSnapshot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.*

//@Configuration(proxyBeanMethods = false)
class CommonConfig {
//    @EnableAsync
//    @Configuration(proxyBeanMethods = false)
    class AsyncConfig : AsyncConfigurer, WebMvcConfigurer {
        override fun getAsyncExecutor(): Executor? {
            return ContextExecutorService.wrap(Executors.newCachedThreadPool()) {
                ContextSnapshot.captureAll()
            }
        }
        override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
            configurer.setTaskExecutor(SimpleAsyncTaskExecutor { r: Runnable? ->
                Thread(ContextSnapshot.captureAll().wrap(r!!))
            })
        }
    }

//    @Bean(name = ["taskExecutor"], destroyMethod = "shutdown")
    fun threadPoolTaskScheduler(): ThreadPoolTaskScheduler {
        val threadPoolTaskScheduler = object: ThreadPoolTaskScheduler() {
            override fun initializeExecutor(
                threadFactory: ThreadFactory,
                rejectedExecutionHandler: RejectedExecutionHandler
            ): ExecutorService {
                val executorService = super.initializeExecutor(threadFactory, rejectedExecutionHandler);
                return ContextExecutorService.wrap(executorService) {
                    ContextSnapshot.captureAll()
                }
            }

            override fun getScheduledExecutor(): ScheduledExecutorService {
                return ContextScheduledExecutorService.wrap(super.getScheduledExecutor())
            }
        }
        threadPoolTaskScheduler.initialize()
        return threadPoolTaskScheduler
    }
}