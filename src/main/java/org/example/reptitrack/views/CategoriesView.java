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

/**
 * Displays categorized tables of products for user browsing.
 * Each category is shown in a tab within a TabPane.
 * <p>
 * This view is read-only and returns to the main dashboard via a button.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class CategoriesView {

    /**
     * Creates and returns the scene for the Categories view.
     *
     * @param stage the main application stage
     * @return the scene showing category-based product tabs
     */
    public static Scene createCategoriesScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Categories");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ─────────────── Category Tabs ───────────────
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createCategoryTab("Animals", FXCollections.observableArrayList(AnimalDAO.getAllAnimals())));
        tabPane.getTabs().add(createCategoryTab("Enclosures", FXCollections.observableArrayList(EnclosureDAO.getAllEnclosures())));
        tabPane.getTabs().add(createCategoryTab("Feeders", FXCollections.observableArrayList(FeederDAO.getAllFeeders())));
        tabPane.getTabs().add(createCategoryTab("Supplies", FXCollections.observableArrayList(SupplyDAO.getAllSupplies())));

        // ─────────────── Navigation ───────────────
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> MainApplication.setRoot("MainDashboard"));

        HBox bottomBar = new HBox(backButton);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(10, 0, 0, 10));

        // ─────────────── Layout ───────────────
        VBox layout = new VBox(10, titleLabel, tabPane, bottomBar);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 600, 500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        return scene;
    }

    /**
     * Creates a tab containing a table of products for a given category.
     *
     * @param title the title of the tab (e.g. "Animals")
     * @param data  the product data to show in the table
     * @return a non-closable Tab for the category
     */
    private static Tab createCategoryTab(String title, ObservableList<Product> data) {
        TableView<Product> table = new TableView<>();
        table.setItems(data);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        table.getColumns().addAll(
                createColumn("Name", "productName", 150),
                createColumn("Quantity", "stockQuantity", 100),
                createColumn("Supplier", "supplier", 150),
                createColumn("Price", "price", 100)
        );

        VBox wrapper = new VBox(table);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(10));

        Tab tab = new Tab(title, wrapper);
        tab.setClosable(false);
        return tab;
    }

    /**
     * Helper method to create a table column for a specific property.
     *
     * @param title    the column title
     * @param property the Product class property
     * @param width    the column width
     * @param <T>      the type of the property
     * @return the created TableColumn
     */
    private static <T> TableColumn<Product, T> createColumn(String title, String property, int width) {
        TableColumn<Product, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }
}
