package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.*;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.Instruction.CurveInstruction;
import ntu.mdp.pathfinding.Instruction.Instruction;
import ntu.mdp.pathfinding.Instruction.LineInstruction;
import ntu.mdp.pathfinding.Instruction.TakePictureInstruction;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathAStarResult implements Comparable<ShortestPathAStarResult> {
    private List<Instruction> instructions;
    private List<Point> pointPath;

    private int cost;

    public ShortestPathAStarResult(List<Point> pointPath, int cost){
        this.pointPath = pointPath;
        this.cost = cost;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public List<Point> getPointPath() {
        return pointPath;
    }

    public int getCost() {
        return cost;
    }

    public void computeCompressedCarMove() {
        instructions = new ArrayList<>();
        List<Point> pathSegment = new ArrayList<>();

        int lastDir = -1;
        Point st = null, point = null;
        System.out.println(pointPath);
        int i = 0, len = pointPath.size();
        while (i < len) {
            point = pointPath.get(i++);

            if (point.getMoveFlag() == lastDir) {
                pathSegment.add(point);
                continue;
            }

            if (st != null) {
                int rLen = point.getR() - st.getR(), cLen = point.getC() - st.getC();
                LineInstruction li = new LineInstruction(rLen == 0 ? cLen : rLen);
                li.setGridPath(pathSegment);
                instructions.add(li);
                st = null;
            }

            if (point.isMatchingPoint())
                instructions.add(new TakePictureInstruction(point.getMatchingObstacleID()));

            if (point.getMoveFlag() == 0 || point.getMoveFlag() == 1) {
                Point endPoint = pointPath.get(i++);
                int rLen = endPoint.getR() - point.getR(), cLen = endPoint.getC() - point.getC();
                if (point.getMoveFlag() == 0) {
                    rLen *= -1; cLen *= -1;
                }
                int circleR = cLen < 0 ? Math.min(point.getR(), endPoint.getR()) : Math.max(point.getR(), endPoint.getR());
                int circleC = rLen < 0 ? Math.max(point.getC(), endPoint.getC()) : Math.min(point.getC(), endPoint.getC());
                List<Point> segment = TrajectoryToArenaGrid.findGridCirclePath(point.getR(), point.getC(), endPoint.getR(), endPoint.getC(), circleR, circleC, point.getMoveFlag() == 1);

                CurveInstruction ci = new CurveInstruction(90, point.getMoveFlag() == 1);
                ci.setGridPath(segment);
                instructions.add(ci);
            } else {
                st = point;
                pathSegment = new ArrayList<>();
                pathSegment.add(st);
            }

            lastDir = point.getMoveFlag();
        }
    }

    @Override
    public int compareTo(ShortestPathAStarResult o) {
        return Integer.compare(cost, o.cost);
    }
}
