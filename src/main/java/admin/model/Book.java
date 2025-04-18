package admin.model;

public class Book {
    private String name;
    private String author;
    private String category;
    private double price;
    private int stock;

    public Book(String name, String author, String category, double price, int stock) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // Setters
    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isInWarehouse() {
        return stock == 0;
    }
}
