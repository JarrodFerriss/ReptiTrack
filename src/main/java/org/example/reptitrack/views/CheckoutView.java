package org.example.reptitrack.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.dao.*;
import org.example.reptitrack.models.Product;
import org.example.reptitrack.services.CartService;

/**
 * View for the checkout process of the ReptiTrack system.
 * Allows the user to complete a transaction, apply tax, process payment,
 * and update stock levels in both the product and category tables.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class CheckoutView {

    /**
     * Generates the checkout scene where users can view cart contents,
     * select payment method, and complete the sale.
     *
     * @param stage the primary application stage
     * @return the checkout scene
     */
    public static Scene createCheckoutScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Checkout");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Product> cartTable = new TableView<>();
        cartTable.setItems(CartService.getInstance().getCartItems());
        cartTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        cartTable.setMaxWidth(570);

        // ─────────────── Table Columns ───────────────
        TableColumn<Product, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        nameCol.setPrefWidth(160);

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        qtyCol.setPrefWidth(60);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(80);

        TableColumn<Product, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            return new SimpleObjectProperty<>(p.getPrice() * p.getStockQuantity());
        });
        subtotalCol.setPrefWidth(90);

        TableColumn<Product, Double> taxCol = new TableColumn<>("Tax (13%)");
        taxCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            return new SimpleObjectProperty<>(p.getPrice() * p.getStockQuantity() * 0.13);
        });
        taxCol.setPrefWidth(90);

        TableColumn<Product, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cellData -> {
            double subtotal = cellData.getValue().getPrice() * cellData.getValue().getStockQuantity();
            return new SimpleObjectProperty<>(subtotal * 1.13);
        });
        totalCol.setPrefWidth(90);

        cartTable.getColumns().addAll(nameCol, qtyCol, priceCol, subtotalCol, taxCol, totalCol);

        // ─────────────── Totals Summary ───────────────
        Label subtotalLabel = new Label();
        Label taxLabel = new Label();
        Label totalLabel = new Label();

        subtotalLabel.setStyle("-fx-font-size: 14px;");
        taxLabel.setStyle("-fx-font-size: 14px;");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        updateTotalLabels(cartTable, subtotalLabel, taxLabel, totalLabel);
        VBox totalsBox = new VBox(5, subtotalLabel, taxLabel, totalLabel);
        totalsBox.setAlignment(Pos.CENTER_RIGHT);

        // ─────────────── Buttons ───────────────
        Button completeButton = new Button("Complete Sale");
        completeButton.setOnAction(e -> handlePayment(cartTable));

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bottomBar = new HBox(10, backButton, spacer, completeButton);
        bottomBar.setPadding(new Insets(10, 0, 0, 0));

        // ─────────────── Layout ───────────────
        HBox tableWrapper = new HBox(cartTable);
        tableWrapper.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, titleLabel, tableWrapper, totalsBox, bottomBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 600, 600);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        return scene;
    }

    /**
     * Handles the checkout logic for both cash and card payments.
     *
     * @param cartTable the table containing current cart items
     */
    private static void handlePayment(TableView<Product> cartTable) {
        double subtotal = cartTable.getItems().stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
        double tax = subtotal * 0.13;
        double total = subtotal + tax;

        ChoiceDialog<String> paymentDialog = new ChoiceDialog<>("Cash", "Cash", "Card");
        paymentDialog.setTitle("Payment");
        paymentDialog.setHeaderText("Select Payment Method");
        paymentDialog.setContentText("Payment Type:");
        paymentDialog.showAndWait().ifPresent(method -> {
            if (method.equals("Card")) {
                Alert cardAlert = new Alert(Alert.AlertType.INFORMATION,
                        "Please finalize the sale on the card terminal.\n\nTotal: $" + String.format("%.2f", total),
                        ButtonType.OK);
                cardAlert.setHeaderText("Card Payment");
                cardAlert.showAndWait();
                finishSale();

            } else if (method.equals("Cash")) {
                TextInputDialog cashDialog = new TextInputDialog();
                cashDialog.setTitle("Cash Payment");
                cashDialog.setHeaderText("Enter Cash Received");
                cashDialog.setContentText("Amount received from customer ($):");

                cashDialog.showAndWait().ifPresent(input -> {
                    try {
                        double cashReceived = Double.parseDouble(input);
                        double roundedTotal = Math.round(total * 20.0) / 20.0;

                        if (cashReceived < roundedTotal) {
                            new Alert(Alert.AlertType.ERROR,
                                    "Customer did not provide enough cash.\nAmount due: $" + String.format("%.2f", roundedTotal),
                                    ButtonType.OK).showAndWait();
                            return;
                        }

                        double change = cashReceived - roundedTotal;
                        double roundedChange = Math.round(change * 20.0) / 20.0;

                        Alert changeAlert = new Alert(Alert.AlertType.INFORMATION,
                                String.format("✅ Sale Completed!\n\nAmount Received: $%.2f\nTotal Owing: $%.2f\nChange Due: $%.2f",
                                        cashReceived, roundedTotal, roundedChange),
                                ButtonType.OK);
                        changeAlert.setHeaderText("Cash Payment");
                        changeAlert.showAndWait();
                        finishSale();

                    } catch (NumberFormatException ex) {
                        new Alert(Alert.AlertType.ERROR, "Invalid cash amount entered.", ButtonType.OK).showAndWait();
                    }
                });
            }
        });
    }

    /**
     * Updates summary labels based on the current cart contents.
     *
     * @param table         the product cart table
     * @param subtotalLabel label for subtotal
     * @param taxLabel      label for tax
     * @param totalLabel    label for total
     */
    private static void updateTotalLabels(TableView<Product> table, Label subtotalLabel, Label taxLabel, Label totalLabel) {
        double subtotal = table.getItems().stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();
        double tax = subtotal * 0.13;
        double total = subtotal + tax;

        subtotalLabel.setText(String.format("Subtotal: $%.2f", subtotal));
        taxLabel.setText(String.format("Tax (13%%): $%.2f", tax));
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    /**
     * Completes the sale by updating stock quantities and clearing the cart.
     * Also updates corresponding category tables.
     */
    private static void finishSale() {
        for (Product p : CartService.getInstance().getCartItems()) {
            int currentQty = ProductDAO.getQuantityById(p.getId());
            int updatedQty = Math.max(0, currentQty - p.getStockQuantity());
            p.setStockQuantity(updatedQty);

            ProductDAO.updateProduct(p);

            switch (p.getCategory().toLowerCase()) {
                case "animals"   -> AnimalDAO.updateAnimal(p);
                case "enclosures"-> EnclosureDAO.updateEnclosure(p);
                case "feeders"   -> FeederDAO.updateFeeder(p);
                case "supplies"  -> SupplyDAO.updateSupply(p);
            }
        }

        CartService.getInstance().clearCart();

        Alert receipt = new Alert(Alert.AlertType.INFORMATION, "🧾 Receipt complete. Thank you!", ButtonType.OK);
        receipt.setHeaderText(null);
        receipt.showAndWait();
        MainApplication.setRoot("MainDashboard");
    }
}
