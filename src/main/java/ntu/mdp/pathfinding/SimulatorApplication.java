package ntu.mdp.pathfinding;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ntu.mdp.pathfinding.Algo.*;
import ntu.mdp.pathfinding.Algo.AStar.ShortestPathAStarAlgo;
import ntu.mdp.pathfinding.Algo.AStar.ShortestPathAStarResult;
import ntu.mdp.pathfinding.Algo.Trajectory.ShortestPathTrajectoryAlgo;
import ntu.mdp.pathfinding.Algo.Trajectory.ShortestPathTrajectoryResult;
import ntu.mdp.pathfinding.GUI.Grid;
import ntu.mdp.pathfinding.GUI.GridControlPane;
import ntu.mdp.pathfinding.GUI.ImageRecognizePane;
import ntu.mdp.pathfinding.GUI.SimulatorConstant;

import java.io.IOException;
import java.net.URL;
import java.util.List;


class ShortestPathRunner implements Runnable {

    @Override
    public void run() {
        Car car = InputData.getCar();
        ShortestPathBF shortestPathBF = new ShortestPathBF(InputData.getObstacles(), InputData.getStartR(), InputData.getStartC());
        Arena arena = new Arena(AlgoConstant.GridM, AlgoConstant.GridN, InputData.getObstacles());
//        ShortestPathTrajectoryAlgo algo = new ShortestPathTrajectoryAlgo(arena, shortestPathBF);
//        ShortestPathTrajectoryResult result = algo.findShortestPath();
//        if (result != null) {
//            System.out.println("Trajectory Solution Found");
//            List<Point> path = result.getPathGrids();
//            for (Point p : path) {
//                car.goTo(p);
//            }
//            return;
//        }
//        System.out.println("No Trajectory Solution Found");
        ShortestPathAStarAlgo aStarAlgo = new ShortestPathAStarAlgo(arena, shortestPathBF);
        ShortestPathAStarResult starResult = aStarAlgo.findBackupShortestPath();
        if (starResult != null) {
            System.out.println("Backup Solution Found");
            List<Point> path = starResult.getPointPath();
            for (Point p : path) {
                car.goTo(p);
            }
            return;
        }
        System.out.println("No Backup Solution Found");
    }
}

public class SimulatorApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Pane root = new Pane();
            URL url = getClass().getResource("assets");

            double startX = SimulatorConstant.marginX + SimulatorConstant.nColumnGrid * SimulatorConstant.cellWidth +
                    SimulatorConstant.gridIRPGap;
            double startY = SimulatorConstant.marginY;
            ImageRecognizePane irp = new ImageRecognizePane(startX, startY, SimulatorConstant.IRPWidth,
                    SimulatorConstant.IRPHeight, url);

            Grid grid = new Grid( SimulatorConstant.nRowGridGrid, SimulatorConstant.nColumnGrid,
                    SimulatorConstant.cellWidth, SimulatorConstant.cellHeight, SimulatorConstant.marginX,
                    SimulatorConstant.marginY, url, irp);

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
        Thread thread = new Thread(new ShortestPathRunner());
        thread.start();
        launch();
    }
}