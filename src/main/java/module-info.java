module org.example.reptitrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.reptitrack to javafx.fxml;
    exports org.example.reptitrack;
    exports org.example.reptitrack.controllers;
    opens org.example.reptitrack.controllers to javafx.fxml;
}