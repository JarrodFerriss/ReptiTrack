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
import org.example.reptitrack.services.CartService;

/**
 * Main dashboard view that allows users to browse products,
 * manage the shopping cart, and navigate through the application.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class MainDashboardView {

    private static TableView<Product> productTable;
    private static TableView<Product> cartTable;
    private static final ObservableList<Product> cartItems = CartService.getInstance().getCartItems();
    private static final Label cartTotalLabel = new Label("Total: $0.00");

    /**
     * Creates and returns the main dashboard scene.
     *
     * @param stage the main stage
     * @return JavaFX scene containing the dashboard UI
     */
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

        // Double-click to add to cart
        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    CartService.getInstance().addItem(row.getItem());
                    cartTable.refresh();
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

        // Cart Buttons
        Button removeItemButton = new Button("Remove Selected");
        removeItemButton.setOnAction(e -> {
            Product selected = cartTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                CartService.getInstance().removeItem(selected);
                cartTable.refresh();
                updateCartTotal();
            }
        });

        Button clearCartButton = new Button("Clear Cart");
        clearCartButton.setOnAction(e -> {
            CartService.getInstance().clearCart();
            cartTable.refresh();
            updateCartTotal();
        });

        Button proceedButton = new Button("Proceed to Checkout");
        proceedButton.setOnAction(e -> {
            if (CartService.getInstance().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Your cart is empty. Please add items before proceeding.", ButtonType.OK);
                alert.setHeaderText("Cart is Empty");
                alert.showAndWait();
            } else {
                MainApplication.setRoot("Checkout");
            }
        });

        HBox cartControls = new HBox(10, removeItemButton, clearCartButton, proceedButton);
        VBox cartBox = new VBox(10, new Label("Cart"), cartTable, cartTotalLabel, cartControls);
        cartBox.setPadding(new Insets(10));

        HBox tableWrapper = new HBox(20, productTable, cartBox);
        tableWrapper.setAlignment(Pos.TOP_LEFT);
        tableWrapper.setPadding(new Insets(10));

        // Low Stock Alerts
        long lowStockCount = allProducts.stream()
                .filter(p -> p.getStockQuantity() <= p.getMinStockLevel())
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

        // Header
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

        // Main Layout
        VBox layout = new VBox(10, titleLabel, headerBar, tableWrapper, bottomBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout);
        stage.setResizable(false);
        stage.sizeToScene();
        return scene;
    }

    /**
     * Updates the cart total label based on current items in the cart.
     */
    private static void updateCartTotal() {
        double total = CartService.getInstance().getCartTotal();
        cartTotalLabel.setText(String.format("Total: $%.2f", total));
    }

    /**
     * Helper to create a table column with the given title, property, and width.
     */
    private static <T> TableColumn<Product, T> createColumn(String title, String property, int width) {
        TableColumn<Product, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    /**
     * Opens a modal window showing all low-stock items (qty ≤ min stock).
     */
    private static void showLowStockWindow(Stage owner, ObservableList<Product> allProducts) {
        Stage lowStockStage = new Stage();
        lowStockStage.initModality(Modality.WINDOW_MODAL);
        lowStockStage.initOwner(owner);
        lowStockStage.setTitle("Low Stock Items");

        TableView<Product> lowStockTable = new TableView<>();
        ObservableList<Product> lowStockItems = allProducts.filtered(p -> p.getStockQuantity() <= p.getMinStockLevel());

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
