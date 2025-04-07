package org.example.reptitrack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.reptitrack.dao.DatabaseConnection;
import org.example.reptitrack.views.CategoriesView;
import org.example.reptitrack.views.CheckoutView;
import org.example.reptitrack.views.LoginView;
import org.example.reptitrack.views.MainDashboardView;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main entry point for the ReptiTrack inventory management application.
 * Handles JavaFX application lifecycle, database initialization, and view switching.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class MainApplication extends Application {

    private static Stage primaryStage;

    /**
     * Initializes and displays the login view when the application starts.
     *
     * @param stage the primary JavaFX stage
     */
    @Override
    public void start(Stage stage) {
        System.out.println("🚀 Launching ReptiTrack...");
        primaryStage = stage;

        // Test DB connection
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("✅ Connected to database: " + conn.getCatalog());
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }

        try {
            Scene loginScene = LoginView.createLoginScene(stage);
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("ReptiTrack");
            primaryStage.show();

            System.out.println("🪟 Login window displayed successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize login scene.");
            e.printStackTrace();

            Label errorLabel = new Label("Critical Error: Unable to load UI.");
            Scene errorScene = new Scene(errorLabel, 400, 200);
            primaryStage.setScene(errorScene);
            primaryStage.setTitle("ReptiTrack");
            primaryStage.show();
        }
    }

    /**
     * Switches the current view to the one specified by its string identifier.
     *
     * @param viewName The name of the view to load (e.g. "MainDashboard", "Categories", "Checkout")
     */
    public static void setRoot(String viewName) {
        try {
            Scene newScene = switchScene(viewName);
            primaryStage.setScene(newScene);
        } catch (Exception e) {
            System.err.println("❌ Failed to switch to view: " + viewName);
            e.printStackTrace();
        }
    }

    /**
     * Helper method for loading specific views based on provided view name.
     *
     * @param viewName the name of the view
     * @return a new Scene for the requested view
     */
    private static Scene switchScene(String viewName) {
        return switch (viewName) {
            case "MainDashboard" -> MainDashboardView.createMainScene(primaryStage);
            case "Categories"    -> CategoriesView.createCategoriesScene(primaryStage);
            case "Checkout"      -> CheckoutView.createCheckoutScene(primaryStage);
            default              -> throw new IllegalArgumentException("Unknown view: " + viewName);
        };
    }

    /**
     * Main method — entry point of the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
