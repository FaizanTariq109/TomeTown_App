package admin.view;

import admin.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class BookTileController {

    @FXML
    private ImageView bookImage;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label priceLabel;

    public void setBook(Book book) {
        titleLabel.setText(book.getTitle());
        authorLabel.setText("by " + book.getAuthorName());
        genreLabel.setText(book.getGenre());
        priceLabel.setText("Rs " + book.getRetailPrice() + " (Buy: " + book.getPurchasePrice() + ")");

        if (book.getImageData() != null) {
            Image img = new Image(new ByteArrayInputStream(book.getImageData()));
            bookImage.setImage(img);
        }
    }
}
