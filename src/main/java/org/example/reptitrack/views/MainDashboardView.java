package org.example.reptitrack.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.views.AdminTerminalView;

public class MainDashboardView {

    public static Scene createMainScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Main Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Total Items Section
        Label totalItemsLabelText = new Label("Total Items in Inventory:");
        Label totalItemsLabel = new Label("0");
        totalItemsLabel.setStyle("-fx-font-weight: bold;");
        HBox totalItemsBox = new HBox(15, totalItemsLabelText, totalItemsLabel);

        // Low-Stock Alerts Section
        Label lowStockLabelText = new Label("Low-Stock Alerts:");
        Label lowStockLabel = new Label("None");
        lowStockLabel.setStyle("-fx-text-fill: red;");
        HBox lowStockBox = new HBox(15, lowStockLabelText, lowStockLabel);

        // Search Section
        Label searchLabel = new Label("Search: ");
        TextField searchField = new TextField();
        searchField.setPromptText("Enter product name...");
        Button searchButton = new Button("Search");

        searchButton.setOnAction(e -> {
            String query = searchField.getText();
            System.out.println("Search triggered for: " + query);
        });

        HBox searchBox = new HBox(10, searchLabel, searchField, searchButton);
        searchBox.setPadding(new Insets(10));

        // Navigation Buttons
        Button categoriesButton = new Button("Go to Categories");
        categoriesButton.setOnAction(e -> MainApplication.setRoot("Categories"));

        Button checkoutButton = new Button("Go to Checkout");
        checkoutButton.setOnAction(e -> MainApplication.setRoot("Checkout"));

        Button adminButton = new Button("Admin Terminal");
        adminButton.setOnAction(e -> {
            Scene adminScene = AdminTerminalView.createAdminScene(stage);
            stage.setScene(adminScene);
        });

        HBox navigationBox = new HBox(10, categoriesButton, checkoutButton, adminButton);

        VBox layout = new VBox(10, titleLabel, totalItemsBox, lowStockBox, searchBox, navigationBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        return new Scene(layout, 800, 600);
    }
}

