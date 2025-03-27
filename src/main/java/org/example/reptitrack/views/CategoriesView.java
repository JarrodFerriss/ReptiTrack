package org.example.reptitrack.views;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.dao.*;
import org.example.reptitrack.models.Product;

public class CategoriesView {

    public static Scene createCategoriesScene(Stage stage) {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().add(createCategoryTab("Animals", FXCollections.observableArrayList(AnimalDAO.getAllAnimals())));
        tabPane.getTabs().add(createCategoryTab("Enclosures", FXCollections.observableArrayList(EnclosureDAO.getAllEnclosures())));
        tabPane.getTabs().add(createCategoryTab("Feeders", FXCollections.observableArrayList(FeederDAO.getAllFeeders())));
        tabPane.getTabs().add(createCategoryTab("Supplies", FXCollections.observableArrayList(SupplyDAO.getAllSupplies())));

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        VBox layout = new VBox(10, tabPane, backButton);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPrefSize(800, 600);

        return new Scene(layout);
    }

    private static Tab createCategoryTab(String title, javafx.collections.ObservableList<Product> data) {
        TableView<Product> table = new TableView<>();
        table.setItems(data);

        table.getColumns().addAll(
                createColumn("Name", "productName", 150),
                createColumn("Quantity", "stockQuantity", 100),
                createColumn("Supplier", "supplier", 150),
                createColumn("Price", "price", 100)
        );

        Tab tab = new Tab(title, table);
        tab.setClosable(false);
        return tab;
    }

    private static <T> TableColumn<Product, T> createColumn(String title, String property, int width) {
        TableColumn<Product, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }
}
