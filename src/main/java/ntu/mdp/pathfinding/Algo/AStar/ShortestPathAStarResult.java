package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.Point;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

public class ShortestPathAStarResult implements Comparable<ShortestPathAStarResult> {
    private List<CarMove> carMoves;
    private List<Point> pointPath;

    private int cost;

    public ShortestPathAStarResult(List<Point> pointPath, int cost){
        this.pointPath = pointPath;
        this.cost = cost;
    }

    public List<CarMove> getCarMoves() {
        return carMoves;
    }

    public List<Point> getPointPath() {
        return pointPath;
    }

    public int getCost() {
        return cost;
    }

    public void computeCompressedCarMove() {
        carMoves = new ArrayList<>();
        List<Point> pathSegment = new ArrayList<>();

        int lastDir = -1;
        Point st = null;
        int i = 0, len = pointPath.size();
        while (i < len) {
            Point point = pointPath.get(i++);
            if (point.getMoveFlag() == lastDir) {
                pathSegment.add(point);
                continue;
            }

            if (st != null) {
                int rLen = point.getX() - st.getX(), cLen = point.getY() - st.getY();
                CarMove cm = new CarMove(rLen == 0 ? cLen : rLen);
                List<List<Point>> path = new ArrayList<>();
                path.add(pathSegment);
                cm.setPath(path);
                carMoves.add(new CarMove(rLen == 0 ? cLen : rLen));
                st = null;
            }

            if (point.getMoveFlag() == 0 || point.getMoveFlag() == 1) {
                Point endPoint = pathSegment.get(i++);
                int rLen = endPoint.getX() - point.getX(), cLen = endPoint.getY() - point.getY();
                if (point.getMoveFlag() == 0) {
                    rLen *= -1; cLen *= -1;
                }
                int circleR = cLen > 0 ? Math.min(point.getX(), endPoint.getX()) : Math.max(point.getX(), endPoint.getX());
                int circleC = rLen > 0 ? Math.max(point.getY(), endPoint.getY()) : Math.min(point.getY(), endPoint.getY());
                List<Point> segment = TrajectoryToArenaGrid.findGridCirclePath(point.getX(), point.getY(), endPoint.getX(), endPoint.getY(), circleR, circleC, point.getMoveFlag() == 1);

                List<List<Point>> paths = new ArrayList<>();
                paths.add(segment);
                CarMove cm = new CarMove(90, point.getMoveFlag() == 1);
                cm.setPath(paths);
            } else {
                st = point;
                pathSegment = new ArrayList<>();
            }

            lastDir = point.getMoveFlag();
        }
    }

    @Override
    public int compareTo(ShortestPathAStarResult o) {
        return Integer.compare(cost, o.cost);
    }
}
