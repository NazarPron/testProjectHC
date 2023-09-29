package hc.test.queue;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class DocumentToStoreQueue {

    @Bean
    public BlockingQueue<String> getDocumentToStoreQueue() {
        return new LinkedBlockingQueue<>();
    }
}
