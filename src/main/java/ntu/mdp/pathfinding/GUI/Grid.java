package ntu.mdp.pathfinding.GUI;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import ntu.mdp.pathfinding.Car;
import ntu.mdp.pathfinding.Obstacle;
import ntu.mdp.pathfinding.Point;

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
    private static final int[][] dImageSquare = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    private static final int[][] dVirtualBlockSquare = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    private static final int[][] dArrowImageSquare = {{0, -1, 1, -1}, {-1, 0, -1, 1}, {0, 2, 1, 2}, {2, 0, 2, 1}};

    private URL imageBaseFolder;


    public Grid(int _m, int _n, double width, double height, double marginX, double marginY, URL url) {
        m = _m; n = _n;
        cWidth = width; cHeight = height;
        cells = new Cell[m][n];
        arrowImg = new Image[4];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double x = j * cWidth + marginX, y = i * cHeight + marginY;
                Cell cell =  new Cell(x, y, cWidth, cHeight, url);
                getChildren().add(cell);
                cells[i][j] = cell;
            }
        }
        imageBaseFolder = url;
        setUpArrowImage();
        setUpCarStartArea();
    }

    private void setUpArrowImage() {
        arrowImg[0] = new Image(imageBaseFolder.toExternalForm() + "Left.png", cWidth, cHeight, false, false);
        arrowImg[1] = new Image(imageBaseFolder.toExternalForm() + "Up.png", cWidth, cHeight, false, false);
        arrowImg[2] = new Image(imageBaseFolder.toExternalForm() + "Right.png", cWidth, cHeight, false, false);
        arrowImg[3] = new Image(imageBaseFolder.toExternalForm() + "Down.png", cWidth, cHeight, false, false);
    }

    public void setUpObstacles(Obstacle[] obstacles) {
        for (Obstacle obstacle : obstacles) {
            int r = obstacle.getR(), c = obstacle.getC(), imgIdx = obstacle.getImgIdx();
            for (int i = 0; i < 4; i++) {
                int nr = r+dImageSquare[i][0], nc = c+dImageSquare[i][1];
                cells[nr][nc].setImage(imgIdx, i);
                setUpVirtualObstacles(nr, nc, i);
            }
            for (int i = 0; i < 4; i += 2) {
                int imgDir = obstacle.getDir(), nr = r + dArrowImageSquare[imgDir][i], nc = c + dArrowImageSquare[imgDir][i+1];
                if (inRange(nr, nc)) cells[nr][nc].setImage(arrowImg[imgDir]);
            }
        }
        this.obstacles = obstacles;
    }

    private void setUpVirtualObstacles(int r, int c, int idx) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int nr = r + i * dVirtualBlockSquare[idx][0], nc = c + j * dVirtualBlockSquare[idx][1];
                if (inRange(nr, nc) && !cells[nr][nc].isImageSet()) cells[nr][nc].virtualBoarderHighlight();
            }
        }
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
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
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
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    private boolean inRange(int r, int c) {
        return r >= 0 && r < m && c >= 0 && c < n;
    }
}
