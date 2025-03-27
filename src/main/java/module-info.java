module org.example.reptitrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports org.example.reptitrack;
    exports org.example.reptitrack.views;
    exports org.example.reptitrack.models;

    opens org.example.reptitrack.models to javafx.base;
}
