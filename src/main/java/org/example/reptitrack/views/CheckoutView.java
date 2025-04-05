package org.example.reptitrack.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.models.Product;

public class CheckoutView {

    public static Scene createCheckoutScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Checkout");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Product> cartTable = new TableView<>();
        cartTable.setItems(getMockCart()); // Replace with actual cartItems if sharing globally

        TableColumn<Product, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            return new SimpleObjectProperty<>(p.getPrice() * p.getStockQuantity());
        });

        cartTable.getColumns().addAll(nameCol, qtyCol, priceCol, subtotalCol);

        Label totalLabel = new Label();
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        updateTotalLabel(cartTable, totalLabel);

        Button completeButton = new Button("Complete Sale");
        completeButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "✅ Sale Completed!", ButtonType.OK);
            alert.showAndWait();
            // TODO: Clear cart, write to DB, etc.
        });

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        HBox bottomBar = new HBox(10, backButton, completeButton);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(10, 0, 0, 0));

        VBox layout = new VBox(10, titleLabel, cartTable, totalLabel, bottomBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout);
        stage.setResizable(false);
        stage.sizeToScene();
        return scene;
    }

    private static void updateTotalLabel(TableView<Product> table, Label label) {
        double total = table.getItems().stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
        label.setText(String.format("Total: $%.2f", total));
    }

    // 🔁 Replace with your actual cartItems list from MainDashboardView if you share it globally
    private static ObservableList<Product> getMockCart() {
        return FXCollections.observableArrayList(
                new Product(1, "Heat Lamp", "Supplies", 2, "ZooMed", 29.99, 1),
                new Product(2, "Crickets", "Feeders", 10, "Bug Supply Co", 0.50, 5)
        );
    }
}
