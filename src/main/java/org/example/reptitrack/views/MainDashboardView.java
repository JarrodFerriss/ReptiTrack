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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.dao.ProductDAO;
import org.example.reptitrack.models.Product;

public class MainDashboardView {

    private static TableView<Product> productTable;
    private static TableView<Product> cartTable;
    private static final ObservableList<Product> cartItems = FXCollections.observableArrayList();
    private static final Label cartTotalLabel = new Label("Total: $0.00");

    public static Scene createMainScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Main Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ObservableList<Product> allProducts = FXCollections.observableArrayList(ProductDAO.getAllProducts());
        FilteredList<Product> filteredProducts = new FilteredList<>(allProducts, p -> true);

        // Product Table
        productTable = new TableView<>(filteredProducts);
        productTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        productTable.setPrefWidth(470);
        productTable.getColumns().addAll(
                createColumn("Name", "productName", 150),
                createColumn("Category", "category", 100),
                createColumn("Quantity", "stockQuantity", 100),
                createColumn("Price", "price", 100)
        );

        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Product clicked = row.getItem();
                    boolean found = false;

                    for (Product item : cartItems) {
                        if (item.getId() == clicked.getId()) {
                            item.setStockQuantity(item.getStockQuantity() + 1);
                            cartTable.refresh();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        cartItems.add(new Product(
                                clicked.getId(),
                                clicked.getProductName(),
                                clicked.getCategory(),
                                1,
                                clicked.getSupplier(),
                                clicked.getPrice(),
                                clicked.getMinStockLevel()
                        ));
                    }

                    updateCartTotal();
                }
            });
            return row;
        });

        // Cart Table
        cartTable = new TableView<>(cartItems);
        cartTable.setPrefWidth(300);
        cartTable.getColumns().addAll(
                createColumn("Product", "productName", 150),
                createColumn("Qty", "stockQuantity", 70),
                createColumn("Price", "price", 80)
        );

        // Cart Controls
        Button removeItemButton = new Button("Remove Selected");
        removeItemButton.setOnAction(e -> {
            Product selected = cartTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                cartItems.remove(selected);
                updateCartTotal();
            }
        });

        Button clearCartButton = new Button("Clear Cart");
        clearCartButton.setOnAction(e -> {
            cartItems.clear();
            updateCartTotal();
        });

        Button proceedButton = new Button("Proceed to Checkout");
        proceedButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proceeding to checkout!", ButtonType.OK);
            alert.showAndWait();
        });

        HBox cartControls = new HBox(10, removeItemButton, clearCartButton, proceedButton);
        VBox cartBox = new VBox(10, new Label("Cart"), cartTable, cartTotalLabel, cartControls);
        cartBox.setPadding(new Insets(10));

        HBox tableWrapper = new HBox(20, productTable, cartBox);
        tableWrapper.setAlignment(Pos.TOP_LEFT);
        tableWrapper.setPadding(new Insets(10));

        // Low-Stock Alerts Section
        long lowStockCount = allProducts.stream()
                .filter(p -> p.getStockQuantity() < p.getMinStockLevel())
                .count();

        Label lowStockLabelText = new Label("Low-Stock Alerts:");
        Label lowStockLabel = new Label(lowStockCount > 0 ? lowStockCount + " items low" : "None");
        lowStockLabel.setStyle("-fx-text-fill: red;");
        HBox lowStockBox = new HBox(5, lowStockLabelText, lowStockLabel);
        lowStockBox.setAlignment(Pos.CENTER_LEFT);

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

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String query = newVal.toLowerCase();
            filteredProducts.setPredicate(product ->
                    query.isEmpty() || product.getProductName().toLowerCase().contains(query));
        });

        HBox searchBox = new HBox(10, searchLabel, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_RIGHT);

        // Header Bar
        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);
        HBox headerBar = new HBox(10, lowStockBox, spacerTop, searchBox);
        headerBar.setPadding(new Insets(0, 10, 10, 10));
        headerBar.setAlignment(Pos.CENTER_LEFT);

        // Navigation Buttons
        Button viewLowStockButton = new Button("🔍 View Low Stock Items");
        viewLowStockButton.setOnAction(e -> showLowStockWindow(stage, allProducts));

        Button categoriesButton = new Button("Go to Categories");
        categoriesButton.setOnAction(e -> MainApplication.setRoot("Categories"));

        Button adminButton = new Button("Admin Terminal");
        adminButton.setOnAction(e -> stage.setScene(AdminTerminalView.createAdminScene(stage)));

        HBox leftControls = new HBox(10, categoriesButton, adminButton, viewLowStockButton);
        Region spacerBottom = new Region();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);

        HBox bottomBar = new HBox(10, leftControls, spacerBottom);
        bottomBar.setPadding(new Insets(10, 10, 0, 10));

        VBox layout = new VBox(10, titleLabel, headerBar, tableWrapper, bottomBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout);
        stage.setResizable(false);
        stage.sizeToScene();
        return scene;
    }

    private static void updateCartTotal() {
        double total = 0.0;
        for (Product item : cartItems) {
            total += item.getPrice() * item.getStockQuantity();
        }
        cartTotalLabel.setText(String.format("Total: $%.2f", total));
    }

    private static <T> TableColumn<Product, T> createColumn(String title, String property, int width) {
        TableColumn<Product, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    private static void showLowStockWindow(Stage owner, ObservableList<Product> allProducts) {
        Stage lowStockStage = new Stage();
        lowStockStage.initModality(Modality.WINDOW_MODAL);
        lowStockStage.initOwner(owner);
        lowStockStage.setTitle("Low Stock Items");

        TableView<Product> lowStockTable = new TableView<>();
        ObservableList<Product> lowStockItems = allProducts.filtered(p -> p.getStockQuantity() < p.getMinStockLevel());

        lowStockTable.setItems(lowStockItems);
        lowStockTable.getColumns().addAll(
                createColumn("Name", "productName", 150),
                createColumn("Category", "category", 100),
                createColumn("Quantity", "stockQuantity", 100),
                createColumn("Supplier", "supplier", 150),
                createColumn("Price", "price", 100),
                createColumn("Min Stock", "minStockLevel", 100)
        );

        VBox layout = new VBox(10, new Label("Low Stock Items:"), lowStockTable);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 800, 500);
        lowStockStage.setScene(scene);
        lowStockStage.show();
    }
}
