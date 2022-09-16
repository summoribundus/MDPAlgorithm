package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
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

    private int[] endPosition;


    private List<Point> path = new ArrayList<>(); // list of points as the final path

    private List<CarMove> moves = new ArrayList<CarMove>();  // the moves of the final path

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
        this.endPosition = new int[3];
        this.totalCost = 0;
    }

    public static void main (String[] args){
        Arena arena = new Arena(AlgoConstant.GridM, AlgoConstant.GridN, InputData.getObstacles());
        ShortestPathAStar astar = new ShortestPathAStar(2, 2, 270, 7, 4, 2, arena);
        ShortestPathAStarResult res = astar.planPath();
        List<Point> path = res.getPointPath();
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

        int endC, endR, endDir;

        int startDir = degreesToDirectionsMapping.get(currentDirDegrees);

        boolean goalFound = false;


        if (!isSafePosition(targetC, targetR)) {
            return null;
        }



        int c, r, dirDegrees;
        int[] nextNode;
        int[] forwardLocation, backwardLocation, leftLocation, rightLocation;
        int currentGCost, hCost, gCost, totalNodeCost;

        // start searching

        int[] goalNode = grid[targetR][targetC][obstacleTargetDirMapping[obstacleDir]];


        this.currentNode = grid[currentR][currentC][startDir];


        setCost(grid, currentR, currentC, startDir, 0, heuristic(currentC, currentR, targetC, targetR));

        this.visitQueue.add(currentNode);

        int i = 0;

        // searching
        while (!visitQueue.isEmpty()){

            int[] nowNode = visitQueue.remove();

            c = nowNode[3];
            r = nowNode[4];
            dirDegrees = nowNode[5];
            totalNodeCost = nowNode[0];
            currentGCost = nowNode[1];

            if (nodeMatchesGoal(nowNode, goalNode)){
                goalFound = true;
                endPosition = new int[]{c, r, dirDegrees};
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

            if (leftLocation != null){
                int nextC = leftLocation[0];
                int nextR = leftLocation[1];
                int nextDirDegrees = leftLocation[2];

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

            if (rightLocation != null){
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
            i++;
            // the node has been visited.
            nowNode[6] = 1;

        }

        if (!goalFound){
            this.totalCost += 9999;
            return null;
        }

        this.totalCost += goalNode[1];

        path = backtrack(goalNode);

        return new ShortestPathAStarResult(moves, path, totalCost);

    }

    // backtrack the map to get the path
    private List<Point> backtrack(int[] endNode){

        List<Point> path = new ArrayList<>();
        int[] curr,prev;

        curr = endNode;

        boolean turnLeft;
        boolean reversing;
        int lineStartC, lineStartR;
        int lineEndC = endNode[3];
        int lineEndR = endNode[4];


        while (curr != null) {

            reversing = false;
            prev = predMap.get(curr); // get the previous node in the backtrack

            int currDirInDegrees = curr[5];// int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)
            if (prev == null) // if this is the starting node, handle the special case.
                {
                    path.add(new Point(curr[4], curr[3]));
                    break;
                }

            lineStartC = prev[3];
            lineStartR = prev[4];
            lineEndC = curr[3];
            lineEndR = curr[4];

            // int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)
            if (prev[5] == currDirInDegrees){
                path.add(new Point(curr[4], curr[3]));
                int moveLength = 0;
                switch (currDirInDegrees) { // check if reversing
                    case 0:
                        if (lineEndC < lineStartC) {
                            reversing = true;
                        }
                        moveLength = Math.abs(lineEndC - lineStartC);
                        break;
                    case 90:
                        if (lineEndR > lineStartR) {
                            reversing = true;
                        }
                        moveLength = Math.abs(lineStartR - lineEndR);
                        break;
                    case 180:
                        if (lineEndC > lineStartC) {
                            reversing = true;
                        }
                        moveLength = Math.abs(lineStartC - lineEndC);
                        break;
                    case 270:
                        if (lineEndR < lineStartR) {
                            reversing = true;
                        }
                        moveLength = Math.abs(lineEndR - lineStartR);
                        break;
                    default:
                }
                if (reversing)
                    moves.add(new CarMove(0, true, 0, true, -1*moveLength));
                else
                    moves.add(new CarMove(0, true, 0, true, moveLength));
            } else { // otherwise, only look for points where direction changes to construct the line segments

                int prevDirInDegrees = prev[5];
                int prevC = prev[3];
                int prevR = prev[4];

                List<Point> pathSegments;

                boolean isClockWise;

                if ((prevDirInDegrees + 90) % 360 == currDirInDegrees) {
                    turnLeft = true;
                    isClockWise = false;
                } else {
                    turnLeft = false;
                    isClockWise = true;
                }
                if (turnLeft){
                    pathSegments = getPathSegmentsForLeftTurning(prevC, prevR, prevDirInDegrees);
                } else {
                    pathSegments = getPathSegmentsForRightTurning(prevC, prevR, prevDirInDegrees);
                }
                moves.add(new CarMove(90, isClockWise, 0, true,0 ));
                Collections.reverse(pathSegments);
                path.addAll(pathSegments);
            }
            curr = prev;
        }


        Collections.reverse(path); // reverse the path and put it in the correct order
        Collections.reverse(moves);
        
        return path;


    }

    private List<Point> getPathSegmentsForLeftTurning(int currentC, int currentR, int currentDirDegrees) {
        int[] leftPos;
        int[] circleCenter;
        List<Point> leftTurnPath;

        switch (currentDirDegrees) {
            case 0:
                leftPos = new int[]{currentC + AlgoConstant.R, currentR - AlgoConstant.R, 90};
                circleCenter = new int[]{currentC, currentR - AlgoConstant.R};
                break;
            case 90:
                leftPos = new int[]{currentC - AlgoConstant.R, currentR - AlgoConstant.R, 180};
                circleCenter = new int[]{currentC - AlgoConstant.R, currentR};
                break;
            case 180:
                leftPos = new int[]{currentC - AlgoConstant.R, currentR + AlgoConstant.R, 270};
                circleCenter = new int[]{currentC, currentR + AlgoConstant.R};
                break;
            case 270:
                leftPos = new int[]{currentC + AlgoConstant.R, currentR + AlgoConstant.R, 0};
                circleCenter = new int[]{currentC + AlgoConstant.R, currentR};
                break;
            default:
                leftPos = null;
                circleCenter = null;
                break;
        }

        leftTurnPath = TrajectoryToArenaGrid.findGridCirclePath(currentR, currentC,
                leftPos[1], leftPos[0], circleCenter[1], circleCenter[0], false);

        return leftTurnPath;
    }

    private List<Point> getPathSegmentsForRightTurning(int currentC, int currentR, int currentDirDegrees){
        int[] rightPos;
        int[] circleCenter;
        List<Point> rightTurnPath;

        switch (currentDirDegrees) {
            case 0 -> {
                rightPos = new int[]{currentC + AlgoConstant.R, currentR + AlgoConstant.R, 270};
                circleCenter = new int[]{currentC, currentR + AlgoConstant.R};
            }
            case 90 -> {
                rightPos = new int[]{currentC + AlgoConstant.R, currentR - AlgoConstant.R, 0};
                circleCenter = new int[]{currentC + AlgoConstant.R, currentR};
            }
            case 180 -> {
                rightPos = new int[]{currentC - AlgoConstant.R, currentR - AlgoConstant.R, 90};
                circleCenter = new int[]{currentC, currentR - AlgoConstant.R};
            }
            case 270 -> {
                rightPos = new int[]{currentC - AlgoConstant.R, currentR + AlgoConstant.R, 180};
                circleCenter = new int[]{currentC - AlgoConstant.R, currentR};
            }
            default -> {
                rightPos = null;
                circleCenter = null;
            }
        }

        rightTurnPath = TrajectoryToArenaGrid.findGridCirclePath(currentR, currentC,
                    rightPos[1], rightPos[0], circleCenter[1], circleCenter[0], true);


        return rightTurnPath;
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
                leftPos = new int[]{currentC + AlgoConstant.R, currentR - AlgoConstant.R, 90};
                circleCenter = new int[]{currentC, currentR - AlgoConstant.R};
            }
            case 90 -> {
                leftPos = new int[]{currentC - AlgoConstant.R, currentR - AlgoConstant.R, 180};
                circleCenter = new int[]{currentC - AlgoConstant.R, currentR};
            }
            case 180 -> {
                leftPos = new int[]{currentC - AlgoConstant.R, currentR + AlgoConstant.R, 270};
                circleCenter = new int[]{currentC, currentR + AlgoConstant.R};
            }
            case 270 -> {
                leftPos = new int[]{currentC + AlgoConstant.R, currentR + AlgoConstant.R, 0};
                circleCenter = new int[]{currentC + AlgoConstant.R, currentR};
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
                    false);
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
                rightPos = new int[]{currentC + AlgoConstant.R, currentR + AlgoConstant.R, 270};
                circleCenter = new int[]{currentC, currentR + AlgoConstant.R};
            }
            case 90 -> {
                rightPos = new int[]{currentC + AlgoConstant.R, currentR - AlgoConstant.R, 0};
                circleCenter = new int[]{currentC + AlgoConstant.R, currentR};
            }
            case 180 -> {
                rightPos = new int[]{currentC - AlgoConstant.R, currentR - AlgoConstant.R, 90};
                circleCenter = new int[]{currentC, currentR - AlgoConstant.R};
            }
            case 270 -> {
                rightPos = new int[]{currentC - AlgoConstant.R, currentR + AlgoConstant.R, 180};
                circleCenter = new int[]{currentC - AlgoConstant.R, currentR};
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
                    true);
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
