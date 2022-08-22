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
    private static final int nColumnImagePane = 2;
    private static final int nRowImagePane = 15;
    private static final int cWidth = 40;
    private static final int cHeight = 40;
    private static final int imageGap = 10;

    private Obstacle[] obstacles;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Pane root = new Pane();

            Grid grid = new Grid( nRowGridGrid, nColumnGrid, cWidth, cHeight, 20, 20);

//            double marginImgPX = 20 + nColumnGrid * cWidth + 80;
//            double marginImgPY = 20;
//            ImagePane imP = new ImagePane(nRowImagePane, nColumnImagePane, cWidth, cHeight, marginImgPX, marginImgPY, imageGap);
            root.getChildren().addAll(grid);

            double width = 20 + nColumnGrid * cWidth + 80 + nColumnImagePane * cWidth + imageGap * (nColumnImagePane - 1) + 20;
            double height = 20 + nRowGridGrid * cHeight + 20;
            System.out.println(width + " " + height);

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