package admin.view;

import admin.model.Book;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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

    // Total items count
    public int getTotalItemCount() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }
}
