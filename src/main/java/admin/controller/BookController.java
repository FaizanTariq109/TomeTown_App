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

                    book.setImageData(doc.get("Image", org.bson.types.Binary.class) != null
                            ? doc.get("Image", org.bson.types.Binary.class).getData()
                            : null);

                    books.add(book);
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching books: " + e.getMessage());
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

    public void addBook(Book book) {
        try {
            MongoDatabase database = MongoConnection.getDatabase();
            MongoCollection<Document> collection = database.getCollection("books");

            Document doc = new Document()
                    .append("Title", book.getTitle())
                    .append("Author Name", book.getAuthorName())
                    .append("Genre", book.getGenre())
                    .append("Language", book.getLanguage())
                    .append("Quantity", book.getQuantity())
                    .append("Purchase Price", book.getPurchasePrice())
                    .append("Total Purchase", book.getTotalPurchase())
                    .append("Retail Price", book.getRetailPrice())
                    .append("Total Retail", book.getTotalRetail())
                    .append("Image", book.getImageData()); // <-- Add this line
            collection.insertOne(doc);
            System.out.println("Book inserted!");
        } catch (Exception e) {
            System.err.println("Error inserting book: " + e.getMessage());
        }
    }

    public void updateBook(Book original, Book updated) {
        try {
            MongoDatabase db = MongoConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("books");

            Document query = new Document("Title", original.getTitle())
                    .append("Author Name", original.getAuthorName());

            Document newData = new Document()
                    .append("Title", updated.getTitle())
                    .append("Author Name", updated.getAuthorName())
                    .append("Genre", updated.getGenre())
                    .append("Language", updated.getLanguage())
                    .append("Quantity", updated.getQuantity())
                    .append("Purchase Price", updated.getPurchasePrice())
                    .append("Total Purchase", updated.getTotalPurchase())
                    .append("Retail Price", updated.getRetailPrice())
                    .append("Image", updated.getImageData())
                    .append("Total Retail", updated.getTotalRetail());

            Document updateDoc = new Document("$set", newData);

            collection.updateOne(query, updateDoc);
            System.out.println("Book updated: " + updated.getTitle());

        } catch (Exception e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
    }
    public void updateBookQuantity(Book book) {
        Document query = new Document("title", book.getTitle());
        Document update = new Document("$set", new Document("quantity", book.getQuantity()));

        MongoConnection.getDatabase().getCollection("books").updateOne(query, update);
    }
    public void deleteBook(Book book) {
        try {
            MongoDatabase db = MongoConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("books");

            Document query = new Document("Title", book.getTitle()) // optionally match more fields for safety
                    .append("Author Name", book.getAuthorName());

            collection.deleteOne(query);
            System.out.println("Book deleted: " + book.getTitle());
        } catch (Exception e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }

}
