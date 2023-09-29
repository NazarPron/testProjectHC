package hc.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorsConfiguration {

    @Bean("mongoExecutor")
    public TaskExecutor getKesTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(1);
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setThreadNamePrefix("publish-");
        executor.setKeepAliveSeconds(30);
        return executor;
    }
}
