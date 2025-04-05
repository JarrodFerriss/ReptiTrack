package org.example.reptitrack.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.dao.ProductDAO;
import org.example.reptitrack.models.Product;

public class MainDashboardView {

    public static Scene createMainScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Main Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Load all products
        ObservableList<Product> allProducts = FXCollections.observableArrayList(ProductDAO.getAllProducts());
        FilteredList<Product> filteredProducts = new FilteredList<>(allProducts, p -> true);

        // Table setup
        productTable = new TableView<>(filteredProducts);
        productTable.getColumns().addAll(
                createColumn("Name", "productName", 150),
                createColumn("Category", "category", 100),
                createColumn("Quantity", "stockQuantity", 100),
                createColumn("Supplier", "supplier", 150),
                createColumn("Price", "price", 100)
        );

        // Total & Low-Stock Labels
        int totalItems = allProducts.stream().mapToInt(Product::getStockQuantity).sum();
        long lowStockCount = allProducts.stream()
                .filter(p -> p.getStockQuantity() < p.getMinStockLevel())
                .count();

        Label totalItemsLabelText = new Label("Total Items in Inventory:");
        Label totalItemsLabel = new Label(String.valueOf(totalItems));
        totalItemsLabel.setStyle("-fx-font-weight: bold;");
        HBox totalItemsBox = new HBox(15, totalItemsLabelText, totalItemsLabel);

        Label lowStockLabelText = new Label("Low-Stock Alerts:");
        Label lowStockLabel = new Label(lowStockCount > 0 ? lowStockCount + " items low" : "None");
        lowStockLabel.setStyle("-fx-text-fill: red;");
        HBox lowStockBox = new HBox(15, lowStockLabelText, lowStockLabel);

        // Search UI
        Label searchLabel = new Label("Search: ");
        TextField searchField = new TextField();
        searchField.setPromptText("Enter product name...");
        Button searchButton = new Button("Search");

        searchButton.setOnAction(e -> {
            String query = searchField.getText().trim().toLowerCase();
            filteredProducts.setPredicate(product ->
                    query.isEmpty() || product.getProductName().toLowerCase().contains(query));
        });

        // Optional: live filtering as user types
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String query = newVal.toLowerCase();
            filteredProducts.setPredicate(product ->
                    query.isEmpty() || product.getProductName().toLowerCase().contains(query));
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

        // Layout
        VBox layout = new VBox(10, titleLabel, totalItemsBox, lowStockBox, searchBox, productTable, navigationBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        return new Scene(layout, 1000, 700);
    }

    private static TableView<Product> productTable;

    private static <T> TableColumn<Product, T> createColumn(String title, String property, int width) {
        TableColumn<Product, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }
}
