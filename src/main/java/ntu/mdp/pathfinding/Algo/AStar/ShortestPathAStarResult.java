package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.*;
import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.Instruction.CurveInstruction;
import ntu.mdp.pathfinding.Instruction.Instruction;
import ntu.mdp.pathfinding.Instruction.LineInstruction;
import ntu.mdp.pathfinding.Instruction.TakePictureInstruction;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathAStarResult implements Comparable<ShortestPathAStarResult> {
    private List<Instruction> instructions;
    private List<Point> rawPointPath;
    private List<Point> pointPath;
    private int cost;

    public ShortestPathAStarResult(List<Point> pointPath, int cost){
        this.rawPointPath = pointPath;
        this.pointPath = new ArrayList<>();
        this.cost = cost;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public List<Point> getPointPath() {
        return pointPath;
    }

    public List<Point> getRawPointPath() {
        return rawPointPath;
    }

    public int getCost() {
        return cost;
    }

    public void computeCompressedCarMove() {
        instructions = new ArrayList<>();
        List<Point> pathSegment = new ArrayList<>();
        System.out.println(rawPointPath);

        CarMoveFlag lastDir = CarMoveFlag.NotInitialized;
        Point st = null, point = null;
        int i = 0, len = rawPointPath.size();
        while (i < len) {
            point = rawPointPath.get(i++);
            pointPath.add(point);

            if (lastDir == CarMoveFlag.TakePicture) {
                pathSegment.get(pathSegment.size()-1).setFacingDir(point.getFacingDir());
                pathSegment.add(point);
                lastDir = point.getMoveFlag();
                continue;
            }

            if (point.getMoveFlag() == lastDir) {
                pathSegment.add(point);
                continue;
            }

            if (st != null) {
                int rLen = point.getR() - st.getR(), cLen = point.getC() - st.getC();
                LineInstruction li = new LineInstruction(rLen == 0 ? cLen : rLen, lastDir);
                li.setGridPath(pathSegment);
                instructions.add(li);
                st = null;
            }

            if (point.isMatchingPoint()) instructions.add(new TakePictureInstruction(point.getMatchingObstacleID()));

            if (point.getMoveFlag() == CarMoveFlag.TurnLeft || point.getMoveFlag() == CarMoveFlag.TurnRight) {
                Point endPoint = rawPointPath.get(i++);
                int rLen = endPoint.getR() - point.getR(), cLen = endPoint.getC() - point.getC();
                if (point.getMoveFlag() == CarMoveFlag.TurnLeft) {
                    rLen *= -1; cLen *= -1;
                }
                int circleR = cLen > 0 ? Math.min(point.getR(), endPoint.getR()) + AlgoConstant.R : Math.max(point.getR(), endPoint.getR()) - AlgoConstant.R;
                int circleC = rLen < 0 ? Math.min(point.getC(), endPoint.getC()) + AlgoConstant.R : Math.max(point.getC(), endPoint.getC()) - AlgoConstant.R;
                List<Point> segment = TrajectoryToArenaGrid.findGridCirclePath(point.getR(), point.getC(), endPoint.getR(),
                        endPoint.getC(), circleR, circleC, point.getMoveFlag() == CarMoveFlag.TurnRight, point.getFacingDir());

                pointPath.addAll(segment);
                CurveInstruction ci = new CurveInstruction(90, point.getMoveFlag() == CarMoveFlag.TurnRight);
                ci.setGridPath(segment);
                instructions.add(ci);
                lastDir = CarMoveFlag.NotInitialized;
            } else {
                st = point;
                pathSegment = new ArrayList<>();
                pathSegment.add(st);
                lastDir = point.getMoveFlag();
            }
        }
    }

    @Override
    public int compareTo(ShortestPathAStarResult o) {
        return Integer.compare(cost, o.cost);
    }
}
