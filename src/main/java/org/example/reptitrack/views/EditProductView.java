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
 * Provides a user interface to edit a product in both
 * the product table and its corresponding category table.
 *
 * This view receives the selected product and category as parameters.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class EditProductView {

    /**
     * Creates a scene allowing the user to edit an existing product.
     *
     * @param stage    the main application window
     * @param product  the product to edit
     * @param category the category of the product (Animals, Feeders, etc.)
     * @return a Scene populated with the editing form
     */
    public static Scene createScene(Stage stage, Product product, String category) {
        Label titleLabel = new Label("Edit Product - " + product.getProductName());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ─────────────── Form Fields ───────────────
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

        // ─────────────── Buttons ───────────────
        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            try {
                // Parse and apply updated field values
                product.setProductName(nameField.getText().trim());
                product.setStockQuantity(Integer.parseInt(quantityField.getText().trim()));
                product.setSupplier(supplierField.getText().trim());
                product.setPrice(Double.parseDouble(priceField.getText().trim()));
                product.setMinStockLevel(Integer.parseInt(minStockField.getText().trim()));

                // Update in category table
                switch (category.toLowerCase()) {
                    case "animals"   -> AnimalDAO.updateAnimal(product);
                    case "enclosures"-> EnclosureDAO.updateEnclosure(product);
                    case "feeders"   -> FeederDAO.updateFeeder(product);
                    case "supplies"  -> SupplyDAO.updateSupply(product);
                    default -> throw new IllegalArgumentException("Unsupported category: " + category);
                }

                // Update in products table
                ProductDAO.updateProduct(product);

                // Return to admin terminal
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

        // ─────────────── Layout ───────────────
        VBox layout = new VBox(15, titleLabel, formBox, buttonBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(400);

        return new Scene(layout);
    }
}
