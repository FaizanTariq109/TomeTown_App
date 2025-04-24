package admin.model;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://Admin:LAYP%40dmins@tometowndb.cealnqn.mongodb.net/"
    ;
    private static final String DATABASE_NAME = "StallApp";

    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
                mongoClient.listDatabaseNames().forEach(name -> System.out.println("🗂 DB: " + name));
                database = mongoClient.getDatabase(DATABASE_NAME);
                database.listCollectionNames().forEach(col -> System.out.println("📁 Collection: " + col));
                System.out.println("✅ Connected to MongoDB");
            } catch (MongoException e) {
                System.err.println("❌ Failed to connect to MongoDB: " + e.getMessage());
            }
        }
        return database;
    }
}
