package org.example.reptitrack.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.dao.UserDAO;

/**
 * View for the login screen of the ReptiTrack application.
 *
 * Provides fields for username and password input, and handles
 * authentication logic via UserDAO.
 *
 * @author Jarrod
 * @since 2025-04-06
 */
public class LoginView {

    /**
     * Creates and returns the login scene for the application.
     *
     * @param stage the main application stage
     * @return a Scene representing the login interface
     */
    public static Scene createLoginScene(Stage stage) {
        // ──────────────── Title ────────────────
        Label titleLabel = new Label("ReptiTrack - Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ──────────────── Form Fields ────────────────
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // ──────────────── Feedback Label ────────────────
        Label statusLabel = new Label();

        // ──────────────── Login Button ────────────────
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (UserDAO.authenticate(username, password)) {
                statusLabel.setText("✅ Login successful");
                MainApplication.setRoot("MainDashboard");
            } else {
                statusLabel.setText("❌ Invalid credentials");
            }
        });

        VBox formBox = new VBox(10, usernameField, passwordField, loginButton, statusLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));

        // ──────────────── Layout ────────────────
        HBox headerBar = new HBox(); // Reserved for future header elements if needed
        headerBar.setPadding(new Insets(0, 10, 10, 10));

        VBox layout = new VBox(10, titleLabel, headerBar, formBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        // ──────────────── Final Scene ────────────────
        Scene scene = new Scene(layout);
        stage.setResizable(false);
        stage.sizeToScene();
        return scene;
    }
}
