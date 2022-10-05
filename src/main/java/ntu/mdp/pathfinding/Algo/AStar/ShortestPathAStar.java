package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.CarFacingDir;
import ntu.mdp.pathfinding.CarMoveFlag;
import ntu.mdp.pathfinding.GUI.SimulatorConstant;
import ntu.mdp.pathfinding.InputData;
import ntu.mdp.pathfinding.Point;

import java.util.*;

public class ShortestPathAStar {

    private int currentC, currentR, currentDirDegrees;

    private Arena arena;

    private int[][][][] grid; // grid of positions

    private int[] currentNode;

    private int targetC, targetR, obstacleDir;

    private final Map<int[], int[]> predMap;

    private PriorityQueue<int[]> visitQueue;
    // int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)

    private List<Point> path = new ArrayList<>(); // list of points as the final path

    private int totalCost;

    private int[] directionToDegreesMapping = new int[] {180, 90, 0, 270};
    private Map<Integer, Integer> degreesToDirectionsMapping  = new HashMap<>(){{
        put(180, 0);
        put(90, 1);
        put(270, 3);
        put(0, 2);
    }};

    private int[] obstacleTargetDirMapping = new int[] {2, 3, 0, 1};

    final int movePenalty = 10;
    final int reversePenalty = 10;
    final int turnPenalty = 60;

    //int[][]

    public ShortestPathAStar(int currentC, int currentR, int currentDirDegrees, int targetC, int targetR, int obstacleDir, Arena arena){
        this.arena = arena;
        this.grid = new int[AlgoConstant.GridM][AlgoConstant.GridN][4][7];
        this.currentC = currentC;
        this.currentR = currentR;
        this.currentDirDegrees = currentDirDegrees;
        this.targetC = targetC;
        this.targetR = targetR;
        this.obstacleDir = obstacleDir;
        this.predMap = new HashMap<>();
        this.visitQueue = new PriorityQueue<>(Comparator.comparing(k -> k[0]));
        this.totalCost = 0;
    }

    public static void main (String[] args){
        Arena arena = new Arena(AlgoConstant.GridM, AlgoConstant.GridN, InputData.getObstacles());
        ShortestPathAStar astar = new ShortestPathAStar(2, 2, 270, 7, 4, 2, arena);
        ShortestPathAStarResult res = astar.planPath();
    }

    public void clear(){
        predMap.clear();

        // initialize the arrays
        for (int r = 0; r < SimulatorConstant.nRowGridGrid; r++){ // dimension c
            for (int c = 0; c < SimulatorConstant.nColumnGrid; c++){ // dimension r
                for (int dir = 0; dir < 4; dir++){
                    // int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)
                    grid[r][c][dir] = new int[] {
                            Integer.MAX_VALUE, // set total cost to be the max value first;
                            Integer.MAX_VALUE,
                            Integer.MAX_VALUE,
                            c,
                            r,
                            directionToDegreesMapping[dir],
                            0
                    };
                }
            }
        }

        visitQueue.clear();
    }

    public ShortestPathAStarResult planPath(){
        if (0 > currentC || currentC >= SimulatorConstant.nColumnGrid || 0 > currentR || currentR >= SimulatorConstant.nRowGridGrid) {
            this.totalCost += 9999;

            return null;
        }
        clear();

        int startDir = degreesToDirectionsMapping.get(currentDirDegrees);

        boolean goalFound = false;
        if (!isSafePosition(targetC, targetR)) return null;

        int c, r, dirDegrees;
        int[] nextNode;
        int[] forwardLocation, backwardLocation, leftLocation, rightLocation;
        int currentGCost, hCost, gCost, totalNodeCost;

        // start searching
        int[] goalNode = grid[targetR][targetC][obstacleTargetDirMapping[obstacleDir]];

        this.currentNode = grid[currentR][currentC][startDir];

        setCost(grid, currentR, currentC, startDir, 0, heuristic(currentC, currentR, targetC, targetR));

        this.visitQueue.add(currentNode);
        // searching
        while (!visitQueue.isEmpty()){

            int[] nowNode = visitQueue.remove();

            c = nowNode[3];
            r = nowNode[4];
            dirDegrees = nowNode[5];
            currentGCost = nowNode[1];

            if (nodeMatchesGoal(nowNode, goalNode)){
                goalFound = true;
                break;
            }

            forwardLocation = getForwardLocation(c, r, dirDegrees);
            leftLocation = getLeftLocation(c, r, dirDegrees);
            rightLocation = getRightLocation(c, r, dirDegrees);
            backwardLocation = getBackwardLocation(c, r, dirDegrees);

            if (forwardLocation != null){
                int nextC = forwardLocation[0];
                int nextR = forwardLocation[1];
                int nextDirDegrees = forwardLocation[2];
                // from degrees to direction conversion.
                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);

                nextNode = grid[nextR][nextC][nextDir];

                gCost = currentGCost + greedyPenaltyForMove(nowNode, nextNode);
                hCost = heuristic(nextC, nextR, targetC, targetR);

                totalNodeCost = gCost + hCost;

                // if this node is already added, we will only change it when the cost is better.
                if (totalNodeCost < nextNode[0]) {
                    predMap.put(nextNode, nowNode);
                    setCost(grid, nextR, nextC, nextDir, gCost, hCost);
                    visitQueue.add(nextNode);
                }
            }

            if (backwardLocation != null){
                int nextC = backwardLocation[0];
                int nextR = backwardLocation[1];
                int nextDirDegrees = backwardLocation[2];

                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);
                nextNode = grid[nextR][nextC][nextDir];

                gCost = currentGCost + greedyPenaltyForMove(nowNode, nextNode);

                hCost = heuristic(nextC, nextR, targetC, targetR);
                totalNodeCost = gCost + hCost;

                // if this node is already added, we will only change it when the cost is better.
                if (totalNodeCost < nextNode[0]){
                    predMap.put(nextNode, nowNode);
                    setCost(grid, nextR, nextC, nextDir, gCost, hCost);
                    visitQueue.add(nextNode);
                }
            }

            if (leftLocation != null) {
                int nextC = leftLocation[0];
                int nextR = leftLocation[1];
                int nextDirDegrees = leftLocation[2];

                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);
                nextNode = grid[nextR][nextC][nextDir];

                gCost = currentGCost + greedyPenaltyForMove(nowNode, nextNode);
                hCost = heuristic(nextC, nextR, targetC, targetR);
                totalNodeCost = gCost + hCost;

                // if this node is already added, we will only change it when the cost is better.
                if (totalNodeCost < nextNode[0]) {
                    predMap.put(nextNode, nowNode);
                    setCost(grid, nextR, nextC, nextDir, gCost, hCost);
                    visitQueue.add(nextNode);
                }
            }

            if (rightLocation != null) {
                int nextC = rightLocation[0];
                int nextR = rightLocation[1];
                int nextDirDegrees = rightLocation[2];

                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);
                nextNode = grid[nextR][nextC][nextDir];

                gCost = currentGCost + greedyPenaltyForMove(nowNode, nextNode);
                hCost = heuristic(nextC, nextR, targetC, targetR);
                totalNodeCost = gCost + hCost;

                // if this node is already added, we will only change it when the cost is better.
                if (totalNodeCost < nextNode[0]){

                    predMap.put(nextNode, nowNode);
                    setCost(grid, nextR, nextC, nextDir, gCost, hCost);
                    visitQueue.add(nextNode);
                }
            }
            // the node has been visited.
            nowNode[6] = 1;
        }

        if (!goalFound){
            this.totalCost += 9999;
            return null;
        }

        this.totalCost += goalNode[1];

        path = backtrack(goalNode);

        return new ShortestPathAStarResult(path, totalCost);
    }

    // backtrack the map to get the path
    private List<Point> backtrack(int[] endNode){

        List<Point> path = new ArrayList<>();
        int[] curr,prev;

        curr = endNode;

        boolean turnLeft;
        boolean reversing;
        int lineStartC, lineStartR;
        int lineEndC, lineEndR;
        while (curr != null) {
            reversing = false;
            prev = predMap.get(curr); // get the previous node in the backtrack

            int currDirInDegrees = curr[5];// int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)
            if (prev == null) break; // if this is the starting node, handle the special case.

            lineStartC = prev[3];
            lineStartR = prev[4];
            lineEndC = curr[3];
            lineEndR = curr[4];

            // int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)
            if  (prev[5] == currDirInDegrees){
                switch (currDirInDegrees) { // check if reversing
                    case 0:
                        if (lineEndC < lineStartC) {
                            reversing = true;
                        }
                        break;
                    case 90:
                        if (lineEndR > lineStartR) {
                            reversing = true;
                        }
                        break;
                    case 180:
                        if (lineEndC > lineStartC) {
                            reversing = true;
                        }
                    case 270:
                        if (lineEndR < lineStartR) {
                            reversing = true;
                        }
                }
                path.add(new Point(curr[4], curr[3], reversing? CarMoveFlag.MoveBackward: CarMoveFlag.MoveForward, CarFacingDir.getDirFromDegree(currDirInDegrees)));

            } else { // otherwise, only look for points where direction changes to construct the line segments
                int prevDirInDegrees = prev[5];
                int prevC = prev[3];
                int prevR = prev[4];

                turnLeft = (prevDirInDegrees + 90) % 360 == currDirInDegrees;

                CarMoveFlag newFlag = turnLeft? CarMoveFlag.TurnLeft: CarMoveFlag.TurnRight;
                if (path.size() >= 1) {
                    Point nowStart = path.get(path.size() - 1);
                    if (nowStart.getMoveFlag() == CarMoveFlag.MoveForward || nowStart.getMoveFlag() == CarMoveFlag.MoveBackward) {
                        path.add(new Point(curr[4], curr[3], nowStart.getMoveFlag(), nowStart.getFacingDir()));
                   }
                }

                path.add(new Point(curr[4], curr[3], newFlag, CarFacingDir.getDirFromDegree(currDirInDegrees)));
                path.add(new Point(prevR, prevC, newFlag, CarFacingDir.getDirFromDegree(prevDirInDegrees)));

            }
            curr = prev;
        }

        Collections.reverse(path); // reverse the path and put it in the correct order
        return path;
    }

    private int[] getForwardLocation(int currentC, int currentR, int currentDirDegrees){
        int[] forwardPosition = switch (currentDirDegrees) {
            case 0 -> new int[]{currentC + 1, currentR, currentDirDegrees};
            case 90 -> new int[]{currentC, currentR - 1, currentDirDegrees};
            case 180 -> new int[]{currentC - 1, currentR, currentDirDegrees};
            case 270 -> new int[]{currentC, currentR + 1, currentDirDegrees};
            default -> null;
        };

        if (forwardPosition != null && isSafePosition(forwardPosition[0], forwardPosition[1]))
            return forwardPosition;
        return null;
    }

    private int[] getBackwardLocation(int currentC, int currentR, int currentDirDegrees){
        int[] backwardPos = switch (currentDirDegrees) {
            case 0 -> new int[]{currentC - 1, currentR, currentDirDegrees};
            case 90 -> new int[]{currentC, currentR + 1, currentDirDegrees};
            case 180 -> new int[]{currentC + 1, currentR, currentDirDegrees};
            case 270 -> new int[]{currentC, currentR - 1, currentDirDegrees};
            default -> null;
        };

        if (backwardPos != null & isSafePosition(backwardPos[0], backwardPos[1]))
            return backwardPos;
        return null;
    }

    private int[] getLeftLocation(int currentC, int currentR, int currentDirDegrees) {
        int[] leftPos;
        int[] circleCenter;
        List<Point> leftTurnPath;

        switch (currentDirDegrees) {
            case 0 -> {
                leftPos = new int[]{currentC + AlgoConstant.leftR, currentR - AlgoConstant.leftR, 90};
                circleCenter = new int[]{currentC, currentR - AlgoConstant.leftR};
            }
            case 90 -> {
                leftPos = new int[]{currentC - AlgoConstant.leftR, currentR - AlgoConstant.leftR, 180};
                circleCenter = new int[]{currentC - AlgoConstant.leftR, currentR};
            }
            case 180 -> {
                leftPos = new int[]{currentC - AlgoConstant.leftR, currentR + AlgoConstant.leftR, 270};
                circleCenter = new int[]{currentC, currentR + AlgoConstant.leftR};
            }
            case 270 -> {
                leftPos = new int[]{currentC + AlgoConstant.leftR, currentR + AlgoConstant.leftR, 0};
                circleCenter = new int[]{currentC + AlgoConstant.leftR, currentR};
            }
            default -> {
                leftPos = null;
                circleCenter = null;
            }
        }
        // check if the grid is satisfiable.
        if (leftPos != null && isSafePosition(circleCenter[0], circleCenter[1])) {
            leftTurnPath = TrajectoryToArenaGrid.findGridCirclePath(currentR, currentC,
                    leftPos[1], leftPos[0],
                    circleCenter[1], circleCenter[0],
                    false, CarFacingDir.getDirFromDegree(currentDirDegrees));
            if (leftTurnPath != null && arena.validatePoint(leftTurnPath))
                return leftPos;
        }

        return null;
    }

    private int[] getRightLocation(int currentC, int currentR, int currentDirDegrees) {
        int[] rightPos;
        int[] circleCenter;
        List<Point> rightTurnPath;

        switch (currentDirDegrees) {
            case 0 -> {
                rightPos = new int[]{currentC + AlgoConstant.rightR, currentR + AlgoConstant.rightR, 270};
                circleCenter = new int[]{currentC, currentR + AlgoConstant.rightR};
            }
            case 90 -> {
                rightPos = new int[]{currentC + AlgoConstant.rightR, currentR - AlgoConstant.rightR, 0};
                circleCenter = new int[]{currentC + AlgoConstant.rightR, currentR};
            }
            case 180 -> {
                rightPos = new int[]{currentC - AlgoConstant.rightR, currentR - AlgoConstant.rightR, 90};
                circleCenter = new int[]{currentC, currentR - AlgoConstant.rightR};
            }
            case 270 -> {
                rightPos = new int[]{currentC - AlgoConstant.rightR, currentR + AlgoConstant.rightR, 180};
                circleCenter = new int[]{currentC - AlgoConstant.rightR, currentR};
            }
            default -> {
                rightPos = null;
                circleCenter = null;
            }
        }
        if (rightPos != null && isSafePosition(circleCenter[0], circleCenter[1]) ){
            rightTurnPath = TrajectoryToArenaGrid.findGridCirclePath(currentR, currentC,
                    rightPos[1], rightPos[0],
                    circleCenter[1], circleCenter[0],
                    true, CarFacingDir.getDirFromDegree(currentDirDegrees));
            if (rightTurnPath != null && arena.validatePoint(rightTurnPath))
                return rightPos;
        }

        return null;
    }

    private boolean isSafePosition(int c, int r){
        return !arena.checkWithCorrespondingBlock(r, c);
    }

    // using Manhattan distance as heuristic. Note that the Manhattan distance denotes the right angle distance,
    // whereas the euclidean distance denotes the straight line distance
    private int heuristic(int curC, int curR, int nextC, int nextR){
        int absC = Math.abs(nextC - curC);
        int absR = Math.abs(nextR - curR);

        return (absC + absR) * movePenalty;
    }

    private int greedyPenaltyForMove(int[] node1, int[] node2){
        int dirInDegrees1 = node1[5];
        int dirInDegrees2 = node2[5];
        int c1 = node1[3];
        int r1 = node1[4];
        int c2 = node2[3];
        int r2 = node2[4];

        int penalty = movePenalty;
        int turningPenalty = 0;

        if (dirInDegrees1 != dirInDegrees2) {
            turningPenalty = turnPenalty;
        } else {
            switch(node1[5]){
                case 0:
                    if (c2 < c1)
                        penalty = reversePenalty;
                    break;
                case 90:
                    if (r2 > r1)
                        penalty = reversePenalty;
                    break;
                case 180:
                    if (c2 > c1)
                        penalty = reversePenalty;
                    break;
                case 270:
                    if (r2 < r1)
                        penalty = reversePenalty;
                    break;
            }
        }

        return penalty + turningPenalty;
    }

    private void setCost(int[][][][] grid, int r, int c, int dir, int g, int h){
        // int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)
        grid[r][c][dir][0] = g+h;
        grid[r][c][dir][1] = g;
        grid[r][c][dir][2] = h;
    }

    private boolean nodeMatchesGoal(int[] currentNode, int[] goalNode){
        return currentNode[3] == goalNode[3] && currentNode[4] == goalNode[4] && currentNode[5] == goalNode[5];
    }
}
