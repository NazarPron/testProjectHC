/*
package hc.test.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;

public class MongoConfig extends AbstractMongoClientConfiguration {


    @Override
    protected String getDatabaseName() {
        return "hc";
    }



    @Override
    public MongoClient mongoClient() {

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        ConnectionString connectionString = null;
        try {
            connectionString = new ConnectionString("mongodb+srv://pronnazar:"+ URLEncoder.encode("S2Ldk78cwZsJ1hQy","UTF-8") +"@cluster0.tthamty.mongodb.net/?retryWrites=true&w=majority");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(serverApi)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("hc.test");
    }


    @Bean
    public MongoTemplate getTemplate(){
       return this.getTemplate();
    }
}
*/
