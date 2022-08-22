package com.example.pathfinding;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulatorApplication extends Application {

    private Obstacle[] obstacles;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Pane root = new Pane();

            Grid grid = new Grid( SimulatorConstant.nRowGridGrid, SimulatorConstant.nColumnGrid,
                    SimulatorConstant.cellWidth, SimulatorConstant.cellHeight, SimulatorConstant.marginX,
                    SimulatorConstant.marginY);
            grid.setUpObstacles(SimulatorMock.getObstacles());
            grid.setUpCarStartArea();
            grid.setUpCarStart(SimulatorMock.getCarWithPath());
            grid.setUpGridRefresh();

            double startX = SimulatorConstant.marginX + SimulatorConstant.nColumnGrid * SimulatorConstant.cellWidth +
                    SimulatorConstant.gridIRPGap;
            double startY = SimulatorConstant.marginY;
            ImageRecognizePane irp = new ImageRecognizePane(startX, startY, SimulatorConstant.IRPWidth,
                    SimulatorConstant.IRPHeight);

            root.getChildren().addAll(grid, irp);

            double width = startX + SimulatorConstant.IRPWidth + SimulatorConstant.marginX;
            double height = 2 * SimulatorConstant.marginY + SimulatorConstant.nRowGridGrid * SimulatorConstant.cellHeight;

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