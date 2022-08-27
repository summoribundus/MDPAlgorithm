package ntu.mdp.pathfinding;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ntu.mdp.pathfinding.Algo.ShortestPathAlgo;
import ntu.mdp.pathfinding.GUI.Grid;
import ntu.mdp.pathfinding.GUI.GridControlPane;
import ntu.mdp.pathfinding.GUI.ImageRecognizePane;
import ntu.mdp.pathfinding.GUI.SimulatorConstant;

import java.io.IOException;
import java.util.List;


class ShortestPathRunner implements Runnable {

    @Override
    public void run() {
        Car car = InputData.getCar();
        ShortestPathAlgo algo = new ShortestPathAlgo(40, 40, InputData.getObstacles(), InputData.getStartR(), InputData.getStartC());
        algo.findShortestValidPath();
        List<int[]> points = algo.getLastPathGrids();
        for (int[] p : points) {
            car.goTo(p[0], p[1]);
        }
    }
}

public class SimulatorApplication extends Application {

    @Override
    public void init() throws IOException {
        Thread thread = new Thread(new ShortestPathRunner());
        thread.start();
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Pane root = new Pane();

            Grid grid = new Grid( SimulatorConstant.nRowGridGrid, SimulatorConstant.nColumnGrid,
                    SimulatorConstant.cellWidth, SimulatorConstant.cellHeight, SimulatorConstant.marginX,
                    SimulatorConstant.marginY);

            double startX = SimulatorConstant.marginX + SimulatorConstant.nColumnGrid * SimulatorConstant.cellWidth +
                    SimulatorConstant.gridIRPGap;
            double startY = SimulatorConstant.marginY;
            ImageRecognizePane irp = new ImageRecognizePane(startX, startY, SimulatorConstant.IRPWidth,
                    SimulatorConstant.IRPHeight);

            startY +=  SimulatorConstant.IRPHeight + 60;
            GridControlPane gControlP = new GridControlPane(startX, startY, SimulatorConstant.GControlPWidth,
                    SimulatorConstant.GControlPHeight, SimulatorConstant.GControlPGap, grid);

            root.getChildren().addAll(grid, irp, gControlP);

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