package org.example.reptitrack.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.reptitrack.MainApplication;

public class MainDashboardController {

    @FXML
    private Label totalItemsLabel;
    @FXML
    private Label lowStockLabel;
    @FXML
    private TextField searchField;

    // Called automatically after FXML loads
    @FXML
    public void initialize() {
        // Example placeholders:
        totalItemsLabel.setText("42");     // Replace with actual data from DB
        lowStockLabel.setText("3 items"); // Or "None" if no low-stock items
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        // In a real app, you'd search your database or product list.
        System.out.println("Search triggered for: " + query);
    }

    @FXML
    private void goToCategories() {
        MainApplication.setRoot("Categories"); // We'll show how to do this below
    }

    @FXML
    private void goToCheckout() {
        MainApplication.setRoot("Checkout");
    }
}
