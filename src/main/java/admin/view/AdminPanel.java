package admin.view;

import admin.controller.BookController;
import admin.model.Book;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdminPanel extends Application {

    private HBox hbox;
    private ScrollPane scrollPane;
    private List<Book> allBooks;
    private TextField searchField;
    private ComboBox<String> genreFilter;
    private ComboBox<String> languageFilter;
    private ComboBox<String> sortCombo;

    @Override
    public void start(Stage stage) {
        Button addButton = new Button("Add Book");
        Button refreshButton = new Button("Refresh");

        searchField = new TextField();
        searchField.setPromptText("Search by Title or Author...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll(
                "Sort: None",
                "Alphabet: A → Z",
                "Alphabet: Z → A",
                "Price: Low → High",
                "Price: High → Low"
        );
        sortCombo.setValue("Sort: None");
        sortCombo.setOnAction(e -> applyFilters());

        genreFilter = new ComboBox<>();
        genreFilter.setPromptText("Filter by Genre");
        genreFilter.setOnAction(e -> applyFilters());

        languageFilter = new ComboBox<>();
        languageFilter.setPromptText("Filter by Language");
        languageFilter.setOnAction(e -> applyFilters());

        addButton.setOnAction(e -> openAddBookForm(stage));
        refreshButton.setOnAction(e -> {
            searchField.clear();
            sortCombo.setValue("Sort: None");
            genreFilter.setValue("All Genres");
            languageFilter.setValue("All Languages");
            loadAllBooks();
        });

        HBox topBar = new HBox(10, addButton, refreshButton, searchField, sortCombo, genreFilter, languageFilter);
        topBar.setAlignment(Pos.CENTER_RIGHT);

        hbox = new HBox(15);
        hbox.setStyle("-fx-padding: 20;");
        scrollPane = new ScrollPane(hbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(15, topBar, scrollPane);
        root.setStyle("-fx-padding: 20;");

        loadAllBooks();

        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("TomeTown Admin Panel");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshBooks(List<Book> books) {
        hbox.getChildren().clear();

        for (Book book : books) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/view/BookTile.fxml"));
                VBox tile = loader.load();

                BookTileController tileController = loader.getController();
                tileController.setBook(book);

                hbox.getChildren().add(tile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedGenre = genreFilter.getValue();
        String selectedLang = languageFilter.getValue();
        String selectedSort = sortCombo.getValue();

        List<Book> filtered = allBooks.stream()
                .filter(book -> {
                    String title = book.getTitle() != null ? book.getTitle().toLowerCase() : "";
                    String author = book.getAuthorName() != null ? book.getAuthorName().toLowerCase() : "";
                    boolean matchesSearch = title.contains(searchText) || author.contains(searchText);

                    boolean matchesGenre = selectedGenre == null || selectedGenre.equals("All Genres") || selectedGenre.equals(book.getGenre());
                    boolean matchesLang = selectedLang == null || selectedLang.equals("All Languages") || selectedLang.equals(book.getLanguage());

                    return matchesSearch && matchesGenre && matchesLang;
                })
                .sorted((a, b) -> {
                    return switch (selectedSort) {
                        case "Alphabet: A → Z" -> String.valueOf(a.getTitle()).compareToIgnoreCase(String.valueOf(b.getTitle()));
                        case "Alphabet: Z → A" -> String.valueOf(b.getTitle()).compareToIgnoreCase(String.valueOf(a.getTitle()));
                        case "Price: Low → High" -> Double.compare(a.getRetailPrice(), b.getRetailPrice());
                        case "Price: High → Low" -> Double.compare(b.getRetailPrice(), a.getRetailPrice());
                        default -> 0;
                    };
                })
                .toList();

        refreshBooks(filtered);
    }

    private void loadAllBooks() {
        allBooks = new BookController().getAllBooks();
        refreshBooks(allBooks);

        genreFilter.getItems().clear();
        languageFilter.getItems().clear();

        genreFilter.getItems().add("All Genres");
        languageFilter.getItems().add("All Languages");

        allBooks.stream()
                .map(Book::getGenre)
                .filter(g -> g != null && !g.isBlank())
                .distinct()
                .sorted()
                .forEach(genreFilter.getItems()::add);

        allBooks.stream()
                .map(Book::getLanguage)
                .filter(l -> l != null && !l.isBlank())
                .distinct()
                .sorted()
                .forEach(languageFilter.getItems()::add);

        genreFilter.setValue("All Genres");
        languageFilter.setValue("All Languages");
    }

    private void openAddBookForm(Stage mainStage) {
        final byte[][] imageBytes = {null};

        Stage formStage = new Stage();
        formStage.setTitle("Add New Book");

        VBox form = new VBox(10);
        form.setPadding(new Insets(15));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author Name");

        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

        TextField languageField = new TextField();
        languageField.setPromptText("Language");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        TextField purchaseField = new TextField();
        purchaseField.setPromptText("Purchase Price");

        TextField retailField = new TextField();
        retailField.setPromptText("Retail Price");

        Button imageButton = new Button("Upload Image");
        imageButton.setOnAction(ev -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Book Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            java.io.File file = fileChooser.showOpenDialog(formStage);
            if (file != null) {
                try {
                    imageBytes[0] = java.nio.file.Files.readAllBytes(file.toPath());
                } catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR, "Could not read file.").showAndWait();
                }
            }
        });

        Button submit = new Button("Save");
        submit.setOnAction(e -> {
            try {
                Book newBook = new Book();
                newBook.setTitle(titleField.getText());
                newBook.setAuthorName(authorField.getText());
                newBook.setGenre(genreField.getText());
                newBook.setLanguage(languageField.getText());
                newBook.setQuantity(Integer.parseInt(quantityField.getText()));
                newBook.setPurchasePrice(Double.parseDouble(purchaseField.getText()));
                newBook.setRetailPrice(Double.parseDouble(retailField.getText()));
                newBook.setTotalPurchase(newBook.getPurchasePrice() * newBook.getQuantity());
                newBook.setTotalRetail(newBook.getRetailPrice() * newBook.getQuantity());
                newBook.setImageData(imageBytes[0]);

                new BookController().addBook(newBook);

                formStage.close();
                loadAllBooks();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to add book");
                alert.setContentText("Please ensure all fields are correctly filled.\n" + ex.getMessage());
                alert.showAndWait();
            }
        });

        form.getChildren().addAll(
                titleField, authorField, genreField, languageField,
                quantityField, purchaseField, retailField,
                imageButton, submit
        );

        Scene scene = new Scene(form, 300, 450);
        formStage.setScene(scene);
        formStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
