package admin.view;

import admin.controller.BookController;
import admin.model.Book;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdminPanel extends Application {

    @Override
    public void start(Stage stage) {
        BookController controller = new BookController();
        List<Book> books = controller.getAllBooks();
        System.out.println("📦 Fetched books: " + books.size());


        ScrollPane scrollPane = new ScrollPane();
        HBox hbox = new HBox(15); // 15 px spacing between book tiles
        hbox.setStyle("-fx-padding: 20;");

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

        scrollPane.setContent(hbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1000, 600);
        stage.setTitle("📚 TomeTown Admin Panel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
