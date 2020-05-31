package space.technological;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.Arrays;

public class Mongo {
    public static MongoClient mongoClient = null;

    public static void createMongoClient() {
        MongoCredential credential = MongoCredential.createCredential(Main.config.databaseUser, "admin", Main.config.databasePassword.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress(Main.config.databaseHost, Main.config.databasePort))))
                .build();
        mongoClient = MongoClients.create(settings);
    }
}
