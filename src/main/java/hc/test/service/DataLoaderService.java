package hc.test.service;

import com.google.gson.*;
import hc.test.queue.DocumentQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class DataLoaderService {

    private final RestTemplate restTemplate;
    private final BlockingQueue<String> docQueue;

    @Autowired
    public DataLoaderService(RestTemplate restTemplate,DocumentQueue docQueue) {
        this.restTemplate = restTemplate;
        this.docQueue = docQueue.getDocumentQueue();
    }


    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void loadData() {
        loadData("https://api.logicahealth.org/HevelianTestSandbox/open/Patient");
    }

    private void loadData(String url) {
        ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            JsonObject json = new Gson().fromJson(entity.getBody(),JsonObject.class);
            if(json!=null){
                JsonArray links = json.getAsJsonArray("link");
                if (links != null && links.size() > 1) {
                    for (JsonElement link : links) {
                        if (link.getAsJsonObject().get("relation").getAsString().equalsIgnoreCase("next")) {
                            loadData(link.getAsJsonObject().get("url").getAsString());
                            break;
                        }
                    }
                }
                sendEntry(json);
            }
        }
        else {
            log.error("Executing request fro url error"+url,entity.getStatusCode());
        }
    }

    private void sendEntry(JsonObject jsonObject){
       JsonArray entryList =  jsonObject.getAsJsonArray("entry");
       entryList.forEach(entry->{
           try {
               docQueue.put(entry.toString());
           }
           catch (Exception e){
               log.error("Send entry error",e);
           }
       });
    }

    private static HttpEntity<Void> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return new HttpEntity<>(headers);
    }


}
