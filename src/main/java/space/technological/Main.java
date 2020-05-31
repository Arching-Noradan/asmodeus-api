package space.technological;

import com.mongodb.client.MongoCursor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;

@SpringBootApplication
public class Main {
    public static Config config = Config.loadConfig();
     public static void main(String[] args) {
         Mongo.createMongoClient();
         SpringApplication.run(Main.class);
    }
}
