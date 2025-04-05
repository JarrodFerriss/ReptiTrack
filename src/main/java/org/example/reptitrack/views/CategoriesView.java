package org.example.reptitrack.views;

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
import org.example.reptitrack.dao.*;
import org.example.reptitrack.models.Product;

public class CategoriesView {

    public static Scene createCategoriesScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Categories");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // TabPane with categories
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createCategoryTab("Animals", FXCollections.observableArrayList(AnimalDAO.getAllAnimals())));
        tabPane.getTabs().add(createCategoryTab("Enclosures", FXCollections.observableArrayList(EnclosureDAO.getAllEnclosures())));
        tabPane.getTabs().add(createCategoryTab("Feeders", FXCollections.observableArrayList(FeederDAO.getAllFeeders())));
        tabPane.getTabs().add(createCategoryTab("Supplies", FXCollections.observableArrayList(SupplyDAO.getAllSupplies())));

        // Navigation Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        HBox bottomBar = new HBox(backButton);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(10, 0, 0, 10));

        VBox layout = new VBox(10, titleLabel, tabPane, bottomBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout);
        stage.setResizable(false);
        stage.sizeToScene();
        return scene;
    }

    private static Tab createCategoryTab(String title, ObservableList<Product> data) {
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
