package org.example.reptitrack.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.reptitrack.dao.*;
import org.example.reptitrack.models.Product;

public class AddProductView {

    public static Scene createScene(Stage stage, String category) {
        Label titleLabel = new Label("Add New Product to " + category);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        TextField supplierField = new TextField();
        supplierField.setPromptText("Supplier");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField minStockField = new TextField();
        minStockField.setPromptText("Minimum Stock Level");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                int qty = Integer.parseInt(quantityField.getText().trim());
                String supplier = supplierField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int minStock = Integer.parseInt(minStockField.getText().trim());

                Product product = new Product(0, name, category, qty, supplier, price, minStock);

                switch (category.toLowerCase()) {
                    case "animals" -> AnimalDAO.insertAnimal(product);
                    case "enclosures" -> EnclosureDAO.insertEnclosure(product);
                    case "feeders" -> FeederDAO.insertFeeder(product);
                    case "supplies" -> SupplyDAO.insertSupply(product);
                    default -> throw new IllegalArgumentException("Unsupported category: " + category);
                }

                stage.setScene(AdminTerminalView.createAdminScene(stage));

            } catch (Exception ex) {
                errorLabel.setText("❌ Invalid input: " + ex.getMessage());
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> stage.setScene(AdminTerminalView.createAdminScene(stage)));

        VBox layout = new VBox(10, titleLabel, nameField, quantityField, supplierField, priceField, minStockField, errorLabel, saveButton, cancelButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(400);

        return new Scene(layout);
    }
}
