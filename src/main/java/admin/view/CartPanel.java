package admin.view;

import admin.model.Book;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CartPanel {

    private final VBox cartBox = new VBox(10);
    private final Label totalLabel = new Label("Total: Rs 0.0");

    private void openOrderForm() {
        // Show a dialog to collect customer details
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Customer Information");
        nameDialog.setHeaderText("Enter Customer Details");
        nameDialog.setContentText("Name:");
        String customerName = nameDialog.showAndWait().orElse("");

        TextInputDialog noteDialog = new TextInputDialog();
        noteDialog.setTitle("Customer Information");
        noteDialog.setHeaderText("Enter Note");
        noteDialog.setContentText("Note (optional):");
        String customerNote = noteDialog.showAndWait().orElse("");

        TextInputDialog instaDialog = new TextInputDialog();
        instaDialog.setTitle("Customer Information");
        instaDialog.setHeaderText("Enter Instagram Account");
        instaDialog.setContentText("Instagram Account (optional):");
        String instaAccount = instaDialog.showAndWait().orElse("");

        TextInputDialog phoneDialog = new TextInputDialog();
        phoneDialog.setTitle("Customer Information");
        phoneDialog.setHeaderText("Enter Phone Number");
        phoneDialog.setContentText("Phone Number:");
        String phoneNumber = phoneDialog.showAndWait().orElse("");

        // Place the order
        CartManager.getInstance().placeOrder(customerName, customerNote, instaAccount, phoneNumber);

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed");
        alert.setContentText("Your order has been placed successfully!");
        alert.showAndWait();

        // Refresh the cart view
        updateCartView();
    }

    public VBox createPanel() {
        cartBox.setPadding(new Insets(10));
        cartBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc;");
        updateCartView();
        Button placeOrderButton = new Button("Place Order");
        placeOrderButton.setOnAction(e -> openOrderForm());
        cartBox.getChildren().add(placeOrderButton);
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
