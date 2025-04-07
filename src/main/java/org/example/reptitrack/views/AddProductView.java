package org.example.reptitrack.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.dao.*;
import org.example.reptitrack.models.Product;

/**
 * View for adding a new product to the system.
 * Inserts the product into both the category table and the Products table.
 * Returns to the Admin Terminal after submission or cancellation.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class AddProductView {

    /**
     * Generates the scene for creating a new product under a specific category.
     *
     * @param stage    the main stage
     * @param category the category to which the product is being added
     * @return the assembled JavaFX Scene
     */
    public static Scene createScene(Stage stage, String category) {
        Label titleLabel = new Label("Add New Product to " + category);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ─────────────── Form Inputs ───────────────
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

        VBox formBox = new VBox(10,
                nameField, quantityField, supplierField, priceField, minStockField, errorLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(10));

        // ─────────────── Action Buttons ───────────────
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

        HBox buttonBar = new HBox(10, saveButton, cancelButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        // ─────────────── Layout Assembly ───────────────
        VBox layout = new VBox(15, titleLabel, formBox, buttonBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(400);

        return new Scene(layout);
    }
}
