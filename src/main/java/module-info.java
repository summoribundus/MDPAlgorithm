module com.example.pathfinding {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.pathfinding to javafx.fxml;
    exports com.example.pathfinding;
}