package admin.model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static final String CONNECTION_STRING = "dmins@tometowndb.cealnqn.mongodb.net"; // Change if using Atlas
    private static MongoClient mongoClient = null;

    public static MongoDatabase getDatabase(String dbName) {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient.getDatabase(dbName);
    }
}
