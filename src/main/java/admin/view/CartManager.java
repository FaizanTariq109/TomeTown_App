package admin.view;

import admin.controller.BookController;
import admin.model.Book;
import admin.model.BookOrder;
import admin.model.MongoConnection;
import admin.model.Payment;

import java.util.*;

public class CartManager {

    private static final CartManager instance = new CartManager();

    private final Map<Book, Integer> cart = new LinkedHashMap<>();

    private CartManager() {
        // Private constructor for Singleton pattern
    }

    public static CartManager getInstance() {
        return instance;
    }

    // Add book to cart (increment if already exists)
    public void addToCart(Book book) {
        cart.merge(book, 1, Integer::sum);
    }

    // Remove a book from the cart
    public void removeFromCart(Book book) {
        if (cart.containsKey(book)) {
            int qty = cart.get(book);
            if (qty > 1) {
                cart.put(book, qty - 1);
            } else {
                cart.remove(book);
            }
        }
    }

    // Remove completely
    public void removeCompletely(Book book) {
        cart.remove(book);
    }

    // Get cart contents
    public Map<Book, Integer> getItems() {
        return Collections.unmodifiableMap(cart);  // To prevent external modification
    }

    // Clear cart
    public void clearCart() {
        cart.clear();
    }

    // Total cost (optional helper)
    public double getTotalCost() {
        return cart.entrySet().stream()
                .mapToDouble(e -> e.getKey().getRetailPrice() * e.getValue())
                .sum();
    }
    public void placeOrder(String customerName, String customerNote, String instaAccount, String phoneNumber) {
        double totalAmount = 0.0;
        List<BookOrder> bookOrders = new ArrayList<>();

        for (Map.Entry<Book, Integer> entry : cart.entrySet()) {
            Book book = entry.getKey();
            int quantityOrdered = entry.getValue();

            // Check if there is enough stock
            if (book.getQuantity() >= quantityOrdered) {
                // Update the quantity of the book
                book.setQuantity(book.getQuantity() - quantityOrdered);
                totalAmount += book.getRetailPrice() * quantityOrdered;

                // Create a BookOrder
                bookOrders.add(new BookOrder(book, quantityOrdered, book.getRetailPrice() * quantityOrdered));

                // Update the book in DB (use BookController or MongoDB directly here)
                new BookController().updateBookQuantity(book);
            } else {
                // Handle the case where quantity is insufficient, e.g., show an error
                System.out.println("Not enough stock for: " + book.getTitle());
            }
        }

        // Save the payment record
        Payment payment = new Payment(customerName, customerNote, instaAccount, phoneNumber, bookOrders, totalAmount);
        MongoConnection.savePayment(payment);

        // Clear the cart after successful payment
        cart.clear();
    }

    // Total items count
    public int getTotalItemCount() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }
}
