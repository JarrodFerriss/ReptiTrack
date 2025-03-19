package org.example.reptitrack.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.models.Product;

public class CheckoutController {

    @FXML private TableView<Product> cartTable;
    @FXML private TableColumn<Product, String> cartProductNameColumn;
    @FXML private TableColumn<Product, Integer> cartQuantityColumn;
    @FXML private TableColumn<Product, Double> cartPriceColumn;
    @FXML private TableColumn<Product, Double> cartSubtotalColumn;

    @FXML private Label totalLabel;

    @FXML
    public void initialize() {
        // Setup columns, e.g.:
        // cartProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // ...
        // cartSubtotalColumn.setCellValueFactory(...) // might be calculated from price * quantity
        //
        // Possibly load cart items from a shared "Cart" object or DAO
        // cartTable.setItems(cartService.getCartItems());
    }

    @FXML
    private void handleCompleteSale() {
        // Trigger the logic to finalize the sale:
        // 1) Decrement stock in DB
        // 2) Clear the cart
        // 3) Show confirmation to user
        System.out.println("Sale completed!");
    }

    @FXML
    private void goToDashboard() {
        MainApplication.setRoot("MainDashboard");
    }
}
