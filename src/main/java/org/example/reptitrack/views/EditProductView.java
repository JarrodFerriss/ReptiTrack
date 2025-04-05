package org.example.reptitrack.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.dao.*;
import org.example.reptitrack.models.Product;

public class EditProductView {

    public static Scene createScene(Stage stage, Product product, String category) {
        Label titleLabel = new Label("Edit Product - " + product.getProductName());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Form Fields
        TextField nameField = new TextField(product.getProductName());
        TextField quantityField = new TextField(String.valueOf(product.getStockQuantity()));
        TextField supplierField = new TextField(product.getSupplier());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField minStockField = new TextField(String.valueOf(product.getMinStockLevel()));

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        VBox formBox = new VBox(10, nameField, quantityField, supplierField, priceField, minStockField, errorLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(10));

        // Buttons
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            try {
                product.setProductName(nameField.getText().trim());
                product.setStockQuantity(Integer.parseInt(quantityField.getText().trim()));
                product.setSupplier(supplierField.getText().trim());
                product.setPrice(Double.parseDouble(priceField.getText().trim()));
                product.setMinStockLevel(Integer.parseInt(minStockField.getText().trim()));

                switch (category.toLowerCase()) {
                    case "animals" -> AnimalDAO.updateAnimal(product);
                    case "enclosures" -> EnclosureDAO.updateEnclosure(product);
                    case "feeders" -> FeederDAO.updateFeeder(product);
                    case "supplies" -> SupplyDAO.updateSupply(product);
                    default -> throw new IllegalArgumentException("Unsupported category: " + category);
                }

                stage.setScene(AdminTerminalView.createAdminScene(stage));

            } catch (Exception ex) {
                errorLabel.setText("❌ Invalid input: " + ex.getMessage());
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> stage.setScene(AdminTerminalView.createAdminScene(stage)));

        HBox buttonBar = new HBox(10, saveButton, cancelButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        VBox layout = new VBox(15, titleLabel, formBox, buttonBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(400);

        return new Scene(layout);
    }
}
