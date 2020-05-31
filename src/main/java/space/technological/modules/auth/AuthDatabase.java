package space.technological.modules.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import space.technological.DatabaseUtils;
import space.technological.Mongo;
import space.technological.Utils;
import space.technological.api_objects.APIObject;
import space.technological.modules.auth.objects.Token;
import space.technological.modules.auth.objects.User;

public class AuthDatabase {

    public static boolean checkIfEmailUsed(String email) {
        if (getUserByEMail(email) != null) {
            return true;
        }
        return false;
    }

    public static boolean checkIfUsernameUsed (String username) {
        if (getUserByUsername(username) != null) {
            return true;
        }
        return false;
    }

    public static boolean isTokenValid(String tokeninput) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("tokens");
        FindIterable<Document> tokens = collection.find(Filters.all("token", tokeninput));
        MongoCursor<Document> mongoCursor = tokens.iterator();
        if (mongoCursor.hasNext()) {
            Token token = (Token) DatabaseUtils.getFirstIterable(tokens, Token.class);
            try {
                if (token.expiration_date >= Utils.getUnixTime()) {
                    return true;
                } else {
                    collection.deleteOne(Document.parse(new GsonBuilder().create().toJson(token)));
                    return false;
                }
            } catch (Exception a) {
                a.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }

    public static void deleteToken(Token token) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("tokens");
        collection.deleteOne(Document.parse(new GsonBuilder().create().toJson(token)));
    }

    public static User getUserByEMail(String email) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("users");
        FindIterable<Document> users = collection.find(Filters.all("email", email));
        return (User) DatabaseUtils.getFirstIterable(users, User.class);
    }

    public static User getUserByUsername(String username) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("users");
        FindIterable<Document> users = collection.find(Filters.all("username", username));
        return (User) DatabaseUtils.getFirstIterable(users, User.class);
    }

    public static User getUserByKey(String key) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("users");
        FindIterable<Document> users = collection.find(Filters.all("emailVerificationCode", key));
        return (User) DatabaseUtils.getFirstIterable(users, User.class);
    }

    public static User getUserByToken(Token token) {
        return getUserByUsername(token.user);
    }

    public static Token getToken(String token_key) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("tokens");
        FindIterable<Document> tokens = collection.find(Filters.all("token", token_key));
        Token token = (Token) DatabaseUtils.getFirstIterable(tokens, Token.class);
        return token;
    }

    public static void insertUser(User user) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("users");
        FindIterable<Document> users = collection.find(Filters.all("username", user.username));
        User userdb = (User) DatabaseUtils.getFirstIterable(users, User.class);
        try {
            if (userdb == null) {
            } else {
                collection.deleteOne(Document.parse(new GsonBuilder().create().toJson(userdb)));
            }
        } catch (Exception a) {
            a.printStackTrace();
        }
        collection.insertOne(Document.parse(new GsonBuilder().create().toJson(user)));
    }

    public static void insertToken(Token tokens) {
        MongoCollection collection = Mongo.mongoClient.getDatabase("asmodeus").getCollection("tokens");
        collection.insertOne(Document.parse(new GsonBuilder().create().toJson(tokens)));
    }
}
