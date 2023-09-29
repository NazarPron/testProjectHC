package hc.test.service;

import com.mongodb.bulk.BulkWriteResult;
import hc.test.queue.DocumentToStoreQueue;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MongoDocumentWriterService {

    private final BlockingQueue<String> documentToStoreQueue;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDocumentWriterService(DocumentToStoreQueue documentToStoreQueue, MongoTemplate mongoTemplate) {
        this.documentToStoreQueue = documentToStoreQueue.getDocumentToStoreQueue();
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void write() {
        mongoTemplate.getCollectionNames().forEach(System.out::println);
        List<Document> documents = new ArrayList<>();
        while (true) {

            try {
                String document = documentToStoreQueue.poll(1, TimeUnit.SECONDS);
                if(document!=null) {
                    if (documents.size() < 10) {
                        documents.add(Document.parse(document));
                    } else {
                        write(documents);
                        documents.clear();
                    }
                }
                else {
                    write(documents);
                    documents.clear();
                }
            } catch (Exception e) {
               log.error("Write documents error",e);
            }
        }
    }

    private void write(List<Document> documents) {
        if(documents.size()>0) {
            BulkOperations bulkInsertion = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Document.class, "ent");
            documents.forEach(bulkInsertion::insert);
            BulkWriteResult bulkWriteResult = bulkInsertion.execute();
            bulkWriteResult.getInsertedCount();
        }

    }
}
