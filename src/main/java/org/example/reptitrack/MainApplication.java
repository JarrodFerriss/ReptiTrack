package org.example.reptitrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.reptitrack.dao.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

import java.io.IOException;


public class MainApplication extends Application {
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the initial view (e.g., MainDashboard)
        Parent root = loadFXML("MainDashboard");
        mainScene = new Scene(root, 800, 600);

        stage.setScene(mainScene);
        stage.setTitle("ReptiTrack");
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Public static method to switch scenes
    public static void setRoot(String fxml) {
        try {
            mainScene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // **Test Database Connection Before Launching JavaFX**
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("✅ Connected to database: " + conn.getCatalog());
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.exit(1);  // Exit the application if database connection fails
        }

        // **Launch JavaFX Application**
        launch();
    }
}
