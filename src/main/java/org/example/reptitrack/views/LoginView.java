package org.example.reptitrack.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.dao.UserDAO;

public class LoginView {

    public static Scene createLoginScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Admin Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label statusLabel = new Label(); // For error/success messages

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            boolean success = UserDAO.authenticate(username, password);
            if (success) {
                statusLabel.setText("✅ Login successful!");
                MainApplication.setRoot("MainDashboard");
            } else {
                statusLabel.setText("❌ Invalid credentials. Try again.");
            }
        });

        VBox layout = new VBox(10, titleLabel, usernameField, passwordField, loginButton, statusLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setPrefSize(400, 300);

        return new Scene(layout);
    }
}
