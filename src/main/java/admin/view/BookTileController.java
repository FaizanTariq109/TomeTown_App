package admin.view;

import admin.controller.BookController;
import admin.model.Book;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class BookTileController {

    @FXML private ImageView bookImage;
    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label genreLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label languageLabel;

    private Book book;

    public void setBook(Book book) {
        this.book = book;

        titleLabel.setText(book.getTitle());
        authorLabel.setText("by " + book.getAuthorName());
        genreLabel.setText(book.getGenre());
        priceLabel.setText("Rs " + book.getRetailPrice() + " (Buy: " + book.getPurchasePrice() + ")");
        quantityLabel.setText("Qty: " + book.getQuantity());
        languageLabel.setText("Lang: " + book.getLanguage());

        if (book.getImageData() != null) {
            Image img = new Image(new ByteArrayInputStream(book.getImageData()));
            bookImage.setImage(img);
        }
    }

    @FXML
    private void onEditClicked() {
        Stage formStage = new Stage();
        formStage.setTitle("Edit Book: " + book.getTitle());

        VBox form = new VBox(10);
        form.setPadding(new Insets(15));

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthorName());
        TextField genreField = new TextField(book.getGenre());
        TextField languageField = new TextField(book.getLanguage());
        TextField quantityField = new TextField(String.valueOf(book.getQuantity()));
        TextField purchaseField = new TextField(String.valueOf(book.getPurchasePrice()));
        TextField retailField = new TextField(String.valueOf(book.getRetailPrice()));

        final byte[][] imageBytes = { book.getImageData() };

        Button imageButton = new Button("Replace Image");
        imageButton.setOnAction(ev -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select New Book Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            java.io.File file = fileChooser.showOpenDialog(formStage);
            if (file != null) {
                try {
                    imageBytes[0] = Files.readAllBytes(file.toPath());
                } catch (IOException ex) {
                    new Alert(AlertType.ERROR, "Could not read file.").showAndWait();
                }
            }
        });

        Button update = new Button("Update");
        update.setOnAction(e -> {
            try {
                Book updatedBook = new Book();
                updatedBook.setTitle(titleField.getText());
                updatedBook.setAuthorName(authorField.getText());
                updatedBook.setGenre(genreField.getText());
                updatedBook.setLanguage(languageField.getText());
                updatedBook.setQuantity(Integer.parseInt(quantityField.getText()));
                updatedBook.setPurchasePrice(Double.parseDouble(purchaseField.getText()));
                updatedBook.setRetailPrice(Double.parseDouble(retailField.getText()));
                updatedBook.setTotalPurchase(updatedBook.getPurchasePrice() * updatedBook.getQuantity());
                updatedBook.setTotalRetail(updatedBook.getRetailPrice() * updatedBook.getQuantity());
                updatedBook.setImageData(imageBytes[0]);

                new BookController().updateBook(book, updatedBook);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Book updated! Please refresh the app.");
                alert.showAndWait();
                formStage.close();
            } catch (Exception ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please check fields.\n" + ex.getMessage());
                alert.showAndWait();
            }
        });

        form.getChildren().addAll(
                titleField, authorField, genreField, languageField,
                quantityField, purchaseField, retailField,
                imageButton, update
        );

        Scene scene = new Scene(form, 300, 450);
        formStage.setScene(scene);
        formStage.show();
    }

    @FXML
    private void onDeleteClicked() {
        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Delete Book");
        confirm.setHeaderText("Are you sure you want to delete this book?");
        confirm.setContentText(book.getTitle());

        confirm.showAndWait().ifPresent(response -> {
            if (response.getText().equalsIgnoreCase("OK")) {
                new BookController().deleteBook(book);
                Alert deleted = new Alert(AlertType.INFORMATION);
                deleted.setContentText("Book deleted. Please refresh the app.");
                deleted.showAndWait();
            }
        });
    }
}
