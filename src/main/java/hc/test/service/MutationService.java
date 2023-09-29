package hc.test.service;

import hc.test.queue.DocumentQueue;
import hc.test.queue.DocumentToStoreQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class MutationService {

    private final ScriptEngine engine;
    private final BlockingQueue<String> documentQueue;
    private final BlockingQueue<String> documentToStoreQueue;

    @Value("${script.function}")
    private String function;

    @Autowired
    public MutationService(ScriptEngine engine, DocumentQueue documentQueue, DocumentToStoreQueue documentToStoreQueue) {
        this.engine = engine;
        this.documentQueue = documentQueue.getDocumentQueue();
        this.documentToStoreQueue = documentToStoreQueue.getDocumentToStoreQueue();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void run() {
        while (true) {
            String doc = null;
            try {
                doc = documentQueue.take();
                String modifiedDocument = exec(doc);
                if (modifiedDocument != null) {
                    documentToStoreQueue.put(modifiedDocument);
                }
            } catch (Exception e) {
                log.error("Can`t modify document:" +doc,e);
            }
        }
    }

    public String exec(String json) throws ScriptException, NoSuchMethodException {
        Invocable inv = (Invocable) engine;
        return (String) inv.invokeFunction(function, json);


    }

}
