package space.technological.modules.file;

import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.apache.tomcat.jni.Local;
import org.bson.BsonDocument;
import org.bson.Document;
import space.technological.DatabaseUtils;
import space.technological.Mongo;
import space.technological.Utils;
import space.technological.modules.auth.objects.User;
import space.technological.modules.file.objects.File;
import space.technological.modules.file.objects.LocalFileStorage;
import space.technological.modules.map.objects.Map;

import java.util.ArrayList;

public class FileDatabase {
    public static ArrayList<LocalFileStorage> getFileStorageUnits() {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("file_storage_units");
        FindIterable<Document> file_storage_units = collection.find();
        ArrayList<LocalFileStorage> arrayList = new ArrayList<>();
        MongoCursor<Document> mongoCursor = file_storage_units.iterator();
        while (mongoCursor.hasNext()) {
            Document document = mongoCursor.next();
            Utils.info(document.toJson());
            arrayList.add(new GsonBuilder().create().fromJson(document.toJson(), LocalFileStorage.class));
        }
        return arrayList;
    }

    public static void updateLocalFileStorageUnit(LocalFileStorage localFileStorage) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("file_storage_units");
        FindIterable<Document> localFileStorages = collection.find(Filters.all("localPath", localFileStorage.localPath));
        LocalFileStorage filedbentry = (LocalFileStorage) DatabaseUtils.getFirstIterable(localFileStorages, LocalFileStorage.class);
        try {
            if (filedbentry == null) {
            } else {
                collection.deleteOne(Document.parse(new GsonBuilder().create().toJson(filedbentry)));
            }
        } catch (Exception a) {
            a.printStackTrace();
        }
        collection.insertOne(Document.parse(new GsonBuilder().create().toJson(localFileStorage)));
    }

    public static void saveFile(File file) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus_files").getCollection("files_" + file.owner);
        collection.insertOne(
                Document.parse(new GsonBuilder().create().toJson(file))
        );
    }
}
