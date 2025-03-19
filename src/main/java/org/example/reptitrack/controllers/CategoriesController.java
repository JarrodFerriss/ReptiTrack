package org.example.reptitrack.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.models.Product;

public class CategoriesController {

    @FXML private TableView<Product> animalsTable;
    @FXML private TableColumn<Product, String> animalNameColumn;
    @FXML private TableColumn<Product, Integer> animalQtyColumn;
    @FXML private TableColumn<Product, String> animalSupplierColumn;
    @FXML private TableColumn<Product, Double> animalPriceColumn;

    // Repeat for enclosuresTable, feedersTable, suppliesTable columns, etc.

    @FXML
    public void initialize() {
        // Set up cell value factories for each column, e.g.:
        // animalNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // ...
        //
        // Then load the data from your DAO or in-memory store.
        //
        // animalsTable.setItems(DAO.getAnimalProducts());
        // enclosuresTable.setItems(DAO.getEnclosureProducts());
        // ...
    }

    @FXML
    private void goToDashboard() {
        MainApplication.setRoot("MainDashboard");
    }
}
