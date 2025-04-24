package admin.view;

import admin.model.Book;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CartPanel {

    private final VBox cartBox = new VBox(10);
    private final Label totalLabel = new Label("Total: Rs 0.0");

    public VBox createPanel() {
        cartBox.setPadding(new Insets(10));
        cartBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc;");
        updateCartView();
        return cartBox;
    }

    public void refresh() {
        updateCartView();
    }

    public void updateCartView() {
        cartBox.getChildren().clear();

        if (CartManager.getInstance().getItems().isEmpty()) {
            cartBox.getChildren().add(new Label("Cart is empty"));
            return;
        }

        for (var entry : CartManager.getInstance().getItems().entrySet()) {
            Book book = entry.getKey();
            int qty = entry.getValue();

            VBox itemBox = new VBox(5);
            itemBox.setStyle("-fx-border-color: #ddd; -fx-padding: 10;");
            itemBox.setPrefWidth(250);

            Label title = new Label(book.getTitle());
            Label quantity = new Label("Qty: " + qty);
            Label price = new Label("Rs " + (book.getRetailPrice() * qty));

            HBox buttons = new HBox(5);
            Button removeOne = new Button("-1");
            Button removeAll = new Button("Remove");

            removeOne.setOnAction(e -> {
                CartManager.getInstance().removeFromCart(book);
                updateCartView();
            });

            removeAll.setOnAction(e -> {
                CartManager.getInstance().removeCompletely(book);
                updateCartView();
            });

            buttons.getChildren().addAll(removeOne, removeAll);

            itemBox.getChildren().addAll(title, quantity, price, buttons);
            cartBox.getChildren().add(itemBox);
        }

        double total = CartManager.getInstance().getTotalCost();
        totalLabel.setText("Total: Rs " + total);

        if (!cartBox.getChildren().contains(totalLabel)) {
            cartBox.getChildren().add(new Separator());
            cartBox.getChildren().add(totalLabel);
        }
    }
}
