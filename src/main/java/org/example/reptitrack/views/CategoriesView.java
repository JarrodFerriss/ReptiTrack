package org.example.reptitrack.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.models.Product;

public class CategoriesView {

    public static Scene createCategoriesScene(Stage stage) {
        // Create Tabs
        TabPane tabPane = new TabPane();

        // 🐍 Animals Tab (typed)
        TableView<Product> animalsTable = new TableView<>();
        animalsTable.setItems(getSampleProductData());

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, String> supplierCol = new TableColumn<>("Supplier");
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        animalsTable.getColumns().add(nameCol);
        animalsTable.getColumns().add(qtyCol);
        animalsTable.getColumns().add(supplierCol);
        animalsTable.getColumns().add(priceCol);

        Tab animalsTab = new Tab("Animals", animalsTable);
        animalsTab.setClosable(false);

        // 🧱 Enclosures Tab (empty placeholder)
        TableView<Product> enclosuresTable = new TableView<>();
        Tab enclosuresTab = new Tab("Enclosures", enclosuresTable);
        enclosuresTab.setClosable(false);

        // 🐛 Feeders Tab (empty placeholder)
        TableView<Product> feedersTable = new TableView<>();
        Tab feedersTab = new Tab("Feeders", feedersTable);
        feedersTab.setClosable(false);

        // 🧼 Supplies Tab (empty placeholder)
        TableView<Product> suppliesTable = new TableView<>();
        Tab suppliesTab = new Tab("Supplies", suppliesTable);
        suppliesTab.setClosable(false);

        // 🗂 Add all tabs
        tabPane.getTabs().addAll(animalsTab, enclosuresTab, feedersTab, suppliesTab);

        // 🔙 Navigation
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(event -> MainApplication.setRoot("MainDashboard"));

        // 📦 Layout
        VBox layout = new VBox(10, tabPane, backButton);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefSize(800, 600);

        return new Scene(layout);
    }

    /**
     * Generates sample product data for display in the Animals tab.
     */
    private static ObservableList<Product> getSampleProductData() {
        return FXCollections.observableArrayList(
                new Product(1, "Leopard Gecko", 15, "ReptiCo", 89.99),
                new Product(2, "Corn Snake", 8, "Slither Inc.", 129.49),
                new Product(3, "Bearded Dragon", 5, "ScalyPets", 159.99),
                new Product(4, "Red-Eyed Tree Frog", 12, "FrogMart", 49.95)
        );
    }
}
