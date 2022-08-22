package com.example.pathfinding;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class Grid extends Pane {
    private int m, n;
    private double cWidth, cHeight;
    private Cell[][] cells;

    private Image arrowImg;
    private LinkedBlockingQueue<Point> path;
    private Point curPoint;
    private static final int[] dr = {0, -1, 0, 1};
    private static final int[] dc = {-1, 0, 1, 0};


    public Grid(int _m, int _n, double width, double height, double marginX, double marginY) {
        m = _m; n = _n;
        cWidth = width; cHeight = height;
        cells = new Cell[m][n];
        arrowImg = new Image(getClass().getResource("assets/arrow.png").toExternalForm(), width, height, true, false);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double x = j * cWidth + marginX, y = i * cHeight + marginY;
                Cell cell =  new Cell(x, y, cWidth, cHeight);
                getChildren().add(cell);
                cells[i][j] = cell;
            }
        }
    }

    public void setUpObstacles(Obstacle[] obstacles) {
        for (Obstacle obstacle : obstacles) {
            cells[obstacle.getR()][obstacle.getC()].setImage(obstacle.getImgIdx());
            int imgDir = obstacle.getImgIdx(), nr = obstacle.getR() + dr[imgDir], nc = obstacle.getC() + dc[imgDir];
            if (inRange(nr, nc)) cells[nr][nc].setImage(arrowImg);
        }
    }

    public void setUpCarStartArea() {
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                cells[i][j].carStartHighlight();
    }

    public void setUpCarStart(Car carStart) {
        path = carStart.getQueue();
        curPoint = carStart.getQueue().poll();
        assert curPoint != null;

        cells[curPoint.getX()][curPoint.getY()].carHighlight();
    }

    public void setUpGridRefresh() {
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(1),
                actionEvent -> {
                    Point point = path.poll();
                    if (point == null) return;
                    cells[curPoint.getX()][curPoint.getY()].highlight();
                    cells[point.getX()][point.getY()].carHighlight();
                    curPoint = point;
                }
        );

        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private boolean inRange(int r, int c) {
        return r >= 0 && r < m && c >= 0 && c < n;
    }
}
