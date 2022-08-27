module com.example.pathfinding {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens ntu.mdp.pathfinding to javafx.fxml;
    exports ntu.mdp.pathfinding;
    exports ntu.mdp.pathfinding.GUI;
    opens ntu.mdp.pathfinding.GUI to javafx.fxml;
}