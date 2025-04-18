package admin.view;

import admin.model.Book;
import admin.model.MongoConnection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AdminPanel extends JFrame {
    private JTextField nameField, authorField, categoryField, priceField, stockField;
    private JButton addButton;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private ArrayList<Book> bookList = new ArrayList<>();

    public AdminPanel() {
        setTitle("Admin Panel - TomeTown Stall 📚");
        setSize(900, 600);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        // TOP: Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        nameField = new JTextField();
        authorField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        addButton = new JButton("Add Book");

        formPanel.add(new JLabel("Name"));
        formPanel.add(new JLabel("Author"));
        formPanel.add(new JLabel("Category"));
        formPanel.add(new JLabel("Price"));
        formPanel.add(new JLabel("Stock"));
        formPanel.add(new JLabel("")); // filler

        formPanel.add(nameField);
        formPanel.add(authorField);
        formPanel.add(categoryField);
        formPanel.add(priceField);
        formPanel.add(stockField);
        formPanel.add(addButton);

        add(formPanel, BorderLayout.NORTH);

        // CENTER: Table Panel
        String[] columns = {"Name", "Author", "Category", "Price", "Stock", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(bookTable);

        add(tableScroll, BorderLayout.CENTER);

        // Add button action
        addButton.addActionListener(e -> addBook());
    }

    private void addBook() {
        try {
            String name = nameField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            Book book = new Book(name, author, category, price, stock);
            bookList.add(book);

            // Add to Table
            tableModel.addRow(new Object[]{
                book.getName(), book.getAuthor(), book.getCategory(), 
                book.getPrice(), book.getStock(),
                (book.isInWarehouse() ? "In Warehouse" : "Available")
            });

            // Save to MongoDB
            saveBookToMongo(book);

            // Clear form
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveBookToMongo(Book book) {
        MongoCollection<Document> collection = MongoConnection.getDatabase("stallapp").getCollection("books");

        Document doc = new Document()
                .append("name", book.getName())
                .append("author", book.getAuthor())
                .append("category", book.getCategory())
                .append("price", book.getPrice())
                .append("stock", book.getStock());

        collection.insertOne(doc);
    }

    private void clearForm() {
        nameField.setText("");
        authorField.setText("");
        categoryField.setText("");
        priceField.setText("");
        stockField.setText("");
    }
}
