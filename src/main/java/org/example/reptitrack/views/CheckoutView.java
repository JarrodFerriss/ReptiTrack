package org.example.reptitrack.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.models.Product;

public class CheckoutView {

    public static Scene createCheckoutScene(Stage stage) {
        return new Scene(buildLayout(stage), 800, 600);
    }

    private static VBox buildLayout(Stage stage) {
        // 🏷️ Title
        Label titleLabel = new Label("ReptiTrack - Checkout");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // 🛒 Cart Table
        TableView<Product> cartTable = new TableView<>();
        cartTable.setItems(getMockCart());

        TableColumn<Product, String> productNameCol = new TableColumn<>("Product");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Qty");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            double subtotal = product.getPrice() * product.getQuantity();
            return new SimpleObjectProperty<>(subtotal);
        });

        @SuppressWarnings("unchecked")
        var _ = cartTable.getColumns().addAll(productNameCol, quantityCol, priceCol, subtotalCol);

        // 💰 Total Label
        double total = cartTable.getItems().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();

        Label totalLabel = new Label(String.format("Total: $%.2f", total));
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // ✅ Complete Sale Button
        Button completeSaleButton = new Button("Complete Sale");
        completeSaleButton.setOnAction(e -> {
            System.out.println("✅ Sale completed!");
            // Add logic here (e.g., update DB, reset cart)
        });

        // 🔙 Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        // 📦 Layout
        VBox layout = new VBox(10, titleLabel, cartTable, totalLabel, completeSaleButton, backButton);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        return layout;
    }

    private static ObservableList<Product> getMockCart() {
        return FXCollections.observableArrayList(
                new Product(101, "Reptile Heat Lamp", 2, "Herp Supply Co.", 29.99),
                new Product(102, "Water Dish", 1, "ReptiCo", 9.99),
                new Product(103, "Gecko Food Pack", 3, "Lizard Snacks Ltd.", 4.50)
        );
    }
}
