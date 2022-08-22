package com.example.pathfinding;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static final int nColumnGrid = 20;
    private static final int nRowGridGrid = 20;
    private static final int cWidth = 40;
    private static final int cHeight = 40;

    private Obstacle[] obstacles;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Pane root = new Pane();

            Grid grid = new Grid( nRowGridGrid, nColumnGrid, cWidth, cHeight, 20, 20);
            root.getChildren().addAll(grid);

            double width = 20 + nColumnGrid * cWidth + 20;
            double height = 20 + nRowGridGrid * cHeight + 20;

            // create scene and stage
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("cell.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}