package ntu.mdp.pathfinding;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class GridControlPane extends Pane {
    private Button setUp, start, clear;
    private Grid grid;
    public GridControlPane(double startX, double startY, double width, double height, double space, Grid grid) {
        this.grid = grid;

        setUp = new Button("Set up");
        setUp.setLayoutX(startX);
        setUp.setLayoutY(startY);
        setUp.setPrefWidth(width);
        setUp.setPrefHeight(height);

        start = new Button("Start");
        start.setLayoutX(startX + width + space);
        start.setLayoutY(startY);
        start.setPrefWidth(width);
        start.setPrefHeight(height);

        clear = new Button("Clear");
        clear.setLayoutX(startX);
        clear.setLayoutY(startY + height + space);
        clear.setPrefWidth(width);
        clear.setPrefHeight(height);

        setOnMoveClickedEvent();
        getChildren().addAll(setUp, start, clear);
    }

    private void setOnMoveClickedEvent() {
        setUp.setOnMouseClicked(mouseEvent -> {
            grid.setUpObstacles(SimulatorMock.getObstacles());
            grid.setUpCarStart(SimulatorMock.getCarWithPath());
            mouseEvent.consume();
        });

        start.setOnMouseClicked(mouseEvent -> {
            grid.setUpGridRefresh();
            mouseEvent.consume();
        });

        clear.setOnMouseClicked(mouseEvent -> {
            grid.clearGridRefresh();
            grid.clearObstaclesAndPath();
        });
    }
}
