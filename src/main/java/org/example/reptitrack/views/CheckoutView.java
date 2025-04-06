package org.example.reptitrack.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.models.Product;
import org.example.reptitrack.services.CartService;
import org.example.reptitrack.dao.AnimalDAO;
import org.example.reptitrack.dao.EnclosureDAO;
import org.example.reptitrack.dao.FeederDAO;
import org.example.reptitrack.dao.SupplyDAO;
import org.example.reptitrack.dao.ProductDAO;

public class CheckoutView {

    public static Scene createCheckoutScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Checkout");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Product> cartTable = new TableView<>();
        cartTable.setItems(CartService.getInstance().getCartItems());

        // Table Columns
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

        TableColumn<Product, Double> taxCol = new TableColumn<>("Tax (13%)");
        taxCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            return new SimpleObjectProperty<>(p.getPrice() * p.getStockQuantity() * 0.13);
        });

        TableColumn<Product, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cellData -> {
            Product p = cellData.getValue();
            double subtotal = p.getPrice() * p.getStockQuantity();
            return new SimpleObjectProperty<>(subtotal + (subtotal * 0.13));
        });

        nameCol.setPrefWidth(160);
        qtyCol.setPrefWidth(60);
        priceCol.setPrefWidth(80);
        subtotalCol.setPrefWidth(90);
        taxCol.setPrefWidth(90);
        totalCol.setPrefWidth(90);

        cartTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        cartTable.getColumns().addAll(nameCol, qtyCol, priceCol, subtotalCol, taxCol, totalCol);

        cartTable.setMaxWidth(570);

        // Summary Labels
        Label subtotalLabel = new Label();
        Label taxLabel = new Label();
        Label totalLabel = new Label();

        subtotalLabel.setStyle("-fx-font-size: 14px;");
        taxLabel.setStyle("-fx-font-size: 14px;");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        updateTotalLabels(cartTable, subtotalLabel, taxLabel, totalLabel);

        VBox totalsBox = new VBox(5, subtotalLabel, taxLabel, totalLabel);
        totalsBox.setAlignment(Pos.CENTER_RIGHT);

        // Buttons
        Button completeButton = new Button("Complete Sale");
        completeButton.setOnAction(e -> {
            double subtotal = cartTable.getItems().stream()
                    .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                    .sum();
            double tax = subtotal * 0.13;
            double total = subtotal + tax;

            ChoiceDialog<String> paymentDialog = new ChoiceDialog<>("Cash", "Cash", "Card");
            paymentDialog.setHeaderText("Select Payment Method");
            paymentDialog.setContentText("Payment Type:");
            paymentDialog.setTitle("Payment");

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
                    cashDialog.setHeaderText("Enter Cash Received");
                    cashDialog.setContentText("Amount received from customer ($):");
                    cashDialog.setTitle("Cash Payment");

                    cashDialog.showAndWait().ifPresent(input -> {
                        try {
                            double cashReceived = Double.parseDouble(input);

                            // Round total to nearest $0.05
                            double roundedTotal = Math.round(total * 20.0) / 20.0;

                            if (cashReceived < roundedTotal) {
                                Alert underpaid = new Alert(Alert.AlertType.ERROR,
                                        "Customer did not provide enough cash.\nAmount due: $" + String.format("%.2f", roundedTotal),
                                        ButtonType.OK);
                                underpaid.setHeaderText("Insufficient Payment");
                                underpaid.showAndWait();
                                return;
                            }

                            double change = cashReceived - roundedTotal;
                            // Round change to nearest $0.05
                            double roundedChange = Math.round(change * 20.0) / 20.0;

                            Alert changeAlert = new Alert(Alert.AlertType.INFORMATION,
                                    String.format("✅ Sale Completed!\n\nAmount Received: $%.2f\nTotal Owing: $%.2f\nChange Due: $%.2f",
                                            cashReceived, roundedTotal, roundedChange),
                                    ButtonType.OK);
                            changeAlert.setHeaderText("Cash Payment");
                            changeAlert.showAndWait();

                            finishSale();

                        } catch (NumberFormatException ex) {
                            Alert error = new Alert(Alert.AlertType.ERROR, "Invalid cash amount entered.", ButtonType.OK);
                            error.setHeaderText("Input Error");
                            error.showAndWait();
                        }
                    });
                }
            });
        });

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bottomBar = new HBox(10, backButton, spacer, completeButton);
        bottomBar.setPadding(new Insets(10, 0, 0, 0));

        // Layout
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

    private static void finishSale() {
        for (Product p : CartService.getInstance().getCartItems()) {
            // 1. Reduce quantity
            int currentQty = ProductDAO.getQuantityById(p.getId());
            int updatedQty = Math.max(0, currentQty - p.getStockQuantity());
            p.setStockQuantity(updatedQty);

            // 2. Update product table
            ProductDAO.updateProduct(p);

            // 3. Update category table
            switch (p.getCategory().toLowerCase()) {
                case "animals" -> AnimalDAO.updateAnimal(p);
                case "enclosures" -> EnclosureDAO.updateEnclosure(p);
                case "feeders" -> FeederDAO.updateFeeder(p);
                case "supplies" -> SupplyDAO.updateSupply(p);
            }
        }

        CartService.getInstance().clearCart();
        Alert thankYou = new Alert(Alert.AlertType.INFORMATION, "🧾 Receipt complete. Thank you!", ButtonType.OK);
        thankYou.setHeaderText(null);
        thankYou.showAndWait();
        MainApplication.setRoot("MainDashboard");
    }
}
