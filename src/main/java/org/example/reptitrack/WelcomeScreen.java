package org.example.reptitrack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeScreen extends Application {

    @Override
    public void start(Stage stage) {

        VBox welcomeLayout = new VBox(30);
        welcomeLayout.setStyle("-fx-background-color: #4CAF50; -fx-padding: 50px;");


        Text welcomeText = new Text("Welcome to ReptiTrack!");
        welcomeText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: white;");


        Button btnStart = new Button("Start");
        btnStart.setStyle("-fx-font-size: 18px; -fx-padding: 15px 30px; -fx-background-color: #FF5722; -fx-text-fill: white; -fx-border-radius: 5;");

        
        welcomeLayout.getChildren().addAll(welcomeText, btnStart);

        // Create the scene for the welcome screen
        Scene welcomeScene = new Scene(welcomeLayout, 600, 400);
        stage.setTitle("ReptiTrack - Welcome");
        stage.setScene(welcomeScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
