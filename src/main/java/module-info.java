module org.example.reptitrack {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.reptitrack to javafx.fxml;
    exports org.example.reptitrack;
}