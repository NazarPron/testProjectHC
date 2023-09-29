package hc.test.queue;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class DocumentQueue {
    @Bean
    public BlockingQueue<String> getDocumentQueue() {
        return new LinkedBlockingQueue<>();
    }
}
