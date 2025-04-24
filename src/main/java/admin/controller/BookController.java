package admin.controller;

import admin.model.Book;
import admin.model.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BookController {
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try {
            MongoDatabase database = MongoConnection.getDatabase();
            MongoCollection<Document> collection = database.getCollection("books");

            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();

                    Book book = new Book();
                    book.setTitle(doc.getString("Title"));
                    book.setAuthorName(doc.getString("Author Name"));
                    book.setGenre(doc.getString("Genre"));
                    book.setLanguage(doc.getString("Language"));

                    book.setQuantity(getIntValue(doc, "Quantity"));
                    book.setPurchasePrice(getDoubleValue(doc, "Purchase Price"));
                    book.setTotalPurchase(getDoubleValue(doc, "Total Purchase"));
                    book.setRetailPrice(getDoubleValue(doc, "Retail Price"));
                    book.setTotalRetail(getDoubleValue(doc, "Total Retail"));

                    books.add(book);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error fetching books: " + e.getMessage());
        }

        return books;
    }

    private int getIntValue(Document doc, String key) {
        Object value = doc.get(key);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    private double getDoubleValue(Document doc, String key) {
        Object value = doc.get(key);
        return value instanceof Number ? ((Number) value).doubleValue() : 0.0;
    }
}
