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

public class AdminTerminalView {

    public static Scene createAdminScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Admin Terminal");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createProductTab("Animals", FXCollections.observableArrayList(AnimalDAO.getAllAnimals()), stage));
        tabPane.getTabs().add(createProductTab("Enclosures", FXCollections.observableArrayList(EnclosureDAO.getAllEnclosures()), stage));
        tabPane.getTabs().add(createProductTab("Feeders", FXCollections.observableArrayList(FeederDAO.getAllFeeders()), stage));
        tabPane.getTabs().add(createProductTab("Supplies", FXCollections.observableArrayList(SupplyDAO.getAllSupplies()), stage));

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        HBox bottomBar = new HBox(backBtn);
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

    private static Tab createProductTab(String title, ObservableList<Product> data, Stage stage) {
        TableView<Product> table = new TableView<>(data);

        table.getColumns().addAll(
                createColumn("Name", "productName", 150),
                createColumn("Category", "category", 100),
                createColumn("Quantity", "stockQuantity", 100),
                createColumn("Supplier", "supplier", 150),
                createColumn("Price", "price", 100),
                createColumn("Min Stock Level", "minStockLevel", 100)
        );

        Button addButton = new Button("➕ Add");
        addButton.setOnAction(e -> stage.setScene(AddProductView.createScene(stage, title)));

        Button editButton = new Button("✏️ Edit Selected");
        editButton.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                stage.setScene(EditProductView.createScene(stage, selected, title));
            }
        });

        Button deleteButton = new Button("❌ Delete Selected");
        deleteButton.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                switch (title.toLowerCase()) {
                    case "animals" -> AnimalDAO.deleteAnimal(selected.getId());
                    case "enclosures" -> EnclosureDAO.deleteEnclosure(selected.getId());
                    case "feeders" -> FeederDAO.deleteFeeder(selected.getId());
                    case "supplies" -> SupplyDAO.deleteSupply(selected.getId());
                }
                ProductDAO.deleteProductById(selected.getId());
                table.setItems(getFreshData(title));
            }
        });

        HBox controls = new HBox(10, addButton, editButton, deleteButton);
        controls.setAlignment(Pos.CENTER);
        VBox content = new VBox(10, table, controls);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10));

        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private static ObservableList<Product> getFreshData(String category) {
        return switch (category.toLowerCase()) {
            case "animals" -> FXCollections.observableArrayList(AnimalDAO.getAllAnimals());
            case "enclosures" -> FXCollections.observableArrayList(EnclosureDAO.getAllEnclosures());
            case "feeders" -> FXCollections.observableArrayList(FeederDAO.getAllFeeders());
            case "supplies" -> FXCollections.observableArrayList(SupplyDAO.getAllSupplies());
            default -> FXCollections.observableArrayList();
        };
    }

    private static <T> TableColumn<Product, T> createColumn(String title, String property, int width) {
        TableColumn<Product, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }
}
