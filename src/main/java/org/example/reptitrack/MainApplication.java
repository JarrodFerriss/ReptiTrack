package org.example.reptitrack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.reptitrack.views.CategoriesView;
import org.example.reptitrack.views.CheckoutView;
import org.example.reptitrack.views.MainDashboardView;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main entry point for the ReptiTrack application.
 * Handles launching the JavaFX application and managing scene switching.
 */
public class MainApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        System.out.println("🚀 Launching ReptiTrack...");

        primaryStage = stage;

        try {
            // 🌐 Attempt DB connection
            try (Connection conn = org.example.reptitrack.dao.DatabaseConnection.getConnection()) {
                System.out.println("✅ Connected to database: " + conn.getCatalog());
            } catch (SQLException e) {
                System.err.println("❌ Database connection failed: " + e.getMessage());
            }

            // 🔐 Start with LoginView instead of Dashboard
            Scene scene = org.example.reptitrack.views.LoginView.createLoginScene(stage);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ReptiTrack - Login");
            primaryStage.show();

            System.out.println("🪟 Login window displayed successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize login scene.");
            e.printStackTrace();

            Label fallbackLabel = new Label("Critical Error: Unable to load UI.");
            Scene fallbackScene = new Scene(fallbackLabel, 400, 200);
            primaryStage.setScene(fallbackScene);
            primaryStage.setTitle("ReptiTrack - Error");
            primaryStage.show();
        }
    }

    /**
     * Replaces the current scene with a new one generated by the corresponding View class.
     *
     * @param viewName The name of the view to switch to (e.g., "MainDashboard", "Categories", "Checkout").
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

    private static Scene switchScene(String viewName) {
        return switch (viewName) {
            case "MainDashboard" -> MainDashboardView.createMainScene(primaryStage);
            case "Categories" -> CategoriesView.createCategoriesScene(primaryStage);
            case "Checkout" -> CheckoutView.createCheckoutScene(primaryStage);
            default -> throw new IllegalArgumentException("Unknown view: " + viewName);
        };
    }

    public static void main(String[] args) {
        launch();
    }
}
