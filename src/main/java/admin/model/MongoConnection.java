package admin.model;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.List;

public class MongoConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://Admin:LAYP%40dmins@tometowndb.cealnqn.mongodb.net/";
    private static final String DATABASE_NAME = "StallApp";

    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
                mongoClient.listDatabaseNames().forEach(name -> System.out.println("🗂 DB: " + name));
                database = mongoClient.getDatabase(DATABASE_NAME);
                database.listCollectionNames().forEach(col -> System.out.println(" Collection: " + col));
                System.out.println("Connected to MongoDB");
            } catch (MongoException e) {
                System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            }
        }
        return database;
    }

    public static void savePayment(Payment payment) {
        MongoCollection<Document> collection = getDatabase().getCollection("payments");

        Document paymentDoc = new Document()
                .append("customerName", payment.getCustomerName())
                .append("customerNote", payment.getCustomerNote())
                .append("instaAccount", payment.getInstaAccount())
                .append("phoneNumber", payment.getPhoneNumber())
                .append("totalAmount", payment.getTotalAmount())
                .append("dateTime", payment.getDateTime().toString());

        // Convert book orders into embedded documents
        List<Document> bookDocs = payment.getBooks().stream().map(bookOrder -> {
            Book book = bookOrder.getBook();
            return new Document()
                    .append("title", book.getTitle())
                    .append("author", book.getAuthorName())
                    .append("pricePerUnit", book.getRetailPrice())
                    .append("quantity", bookOrder.getQuantity())
                    .append("totalPrice", bookOrder.getTotalPrice());
        }).toList();

        paymentDoc.append("books", bookDocs);

        collection.insertOne(paymentDoc);
        System.out.println("Payment saved.");
    }
}
