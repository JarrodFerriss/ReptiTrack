package org.example.reptitrack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.reptitrack.models.Product;

public class MainDashboardController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    // An ObservableList to hold dummy data
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure table columns to map to Product fields
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Create some dummy Products
        productList.add(new Product(1, "Corn Snake", 10));
        productList.add(new Product(2, "Leopard Gecko", 5));
        productList.add(new Product(3, "Terrarium (Small)", 2));
        productList.add(new Product(4, "Crickets (50-pack)", 20));

        // Set the ObservableList in the TableView
        productTable.setItems(productList);
    }
}
