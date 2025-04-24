package admin;

import admin.controller.BookController;
import admin.model.Book;

import java.util.List;

public class MainAdmin {
    public static void main(String[] args) {
        BookController controller = new BookController();
        List<Book> books = controller.getAllBooks();
        for (Book book : books) {
            System.out.println("📘 " + book.getTitle() + " by " + book.getAuthorName());
        }


        for (Book book : books) {
            System.out.println("📚 " + book.getTitle() + " by " + book.getAuthorName());
        }
    }
}
