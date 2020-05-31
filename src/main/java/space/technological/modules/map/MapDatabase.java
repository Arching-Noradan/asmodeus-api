package space.technological.modules.map;

import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import space.technological.DatabaseUtils;
import space.technological.Mongo;
import space.technological.modules.auth.objects.User;
import space.technological.modules.map.objects.Map;

import java.util.ArrayList;

public class MapDatabase {
    public static ArrayList<Map> getMaps(String username) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus-maps").getCollection(username + "-maps");
        FindIterable<Document> maps = collection.find();
        ArrayList<Map> arrayList = new ArrayList<>();
        MongoCursor<Document> mongoCursor = maps.iterator();
        while (mongoCursor.hasNext()) {
            arrayList.add(new GsonBuilder().create().fromJson(mongoCursor.next().toJson(), Map.class));
        }
        return arrayList;
    }

    public static Map getMap(String username, String mapid) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus-maps").getCollection(username + "-maps");
        FindIterable<Document> maps = collection.find(Filters.all("uid", mapid));
        return (Map) DatabaseUtils.getFirstIterable(maps, Map.class);
    }
}
