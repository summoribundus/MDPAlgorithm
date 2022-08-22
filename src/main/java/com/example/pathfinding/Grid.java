package com.example.pathfinding;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

public class Grid extends Pane {
    private int m, n;
    private double cWidth, cHeight;
    private Cell[][] cells;

    private Image[] arrowImg;
    private LinkedBlockingQueue<Point> path;
    private Point curPoint;
    private Obstacle[] obstacles;
    private Timeline timeline;
    private static final int[] dr = {0, -1, 0, 1};
    private static final int[] dc = {-1, 0, 1, 0};


    public Grid(int _m, int _n, double width, double height, double marginX, double marginY) {
        m = _m; n = _n;
        cWidth = width; cHeight = height;
        cells = new Cell[m][n];
        arrowImg = new Image[4];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double x = j * cWidth + marginX, y = i * cHeight + marginY;
                Cell cell =  new Cell(x, y, cWidth, cHeight);
                getChildren().add(cell);
                cells[i][j] = cell;
            }
        }
        setUpArrowImage();
        setUpCarStartArea();
    }

    private void setUpArrowImage() {
        URL url = getClass().getResource("assets");
        assert url != null;

        arrowImg[0] = new Image(url.toExternalForm() + "Left.png", cWidth, cHeight, false, false);
        arrowImg[1] = new Image(url.toExternalForm() + "Up.png", cWidth, cHeight, false, false);
        arrowImg[2] = new Image(url.toExternalForm() + "Right.png", cWidth, cHeight, false, false);
        arrowImg[3] = new Image(url.toExternalForm() + "Down.png", cWidth, cHeight, false, false);
    }

    public void setUpObstacles(Obstacle[] obstacles) {
        for (Obstacle obstacle : obstacles) {
            cells[obstacle.getR()][obstacle.getC()].setImage(obstacle.getImgIdx());
            int imgDir = obstacle.getDir(), nr = obstacle.getR() + dr[imgDir], nc = obstacle.getC() + dc[imgDir];
            if (inRange(nr, nc)) cells[nr][nc].setImage(arrowImg[imgDir]);
        }
        this.obstacles = obstacles;
    }

    public void clearObstaclesAndPath() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cells[i][j].unSetImage();
                cells[i][j].clearHighlight();
            }
        }
       setUpCarStartArea();
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
                    int x = curPoint.getX(), y = curPoint.getY();
                    cells[x][y].carPassHighlight();
                    cells[point.getX()][point.getY()].carHighlight();
                    curPoint = point;
                }
        );

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void clearGridRefresh() {
        timeline.stop();
    }

    private boolean inRange(int r, int c) {
        return r >= 0 && r < m && c >= 0 && c < n;
    }
}
