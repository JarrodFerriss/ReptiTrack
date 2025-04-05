package org.example.reptitrack.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.reptitrack.MainApplication;
import org.example.reptitrack.dao.UserDAO;

public class LoginView {

    public static Scene createLoginScene(Stage stage) {
        Label titleLabel = new Label("ReptiTrack - Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Username and password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Status label for login feedback
        Label statusLabel = new Label();

        // Login button
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

        VBox formBox = new VBox(10, usernameField, passwordField, loginButton, statusLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));

        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);

        HBox headerBar = new HBox(10);
        headerBar.setPadding(new Insets(0, 10, 10, 10));

        VBox layout = new VBox(10, titleLabel, headerBar, formBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout);
        stage.setResizable(false);
        stage.sizeToScene();
        return scene;
    }
}
