package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.AStarResult;
import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.GUI.SimulatorConstant;

import java.util.*;

public class ShortestPathAStar {

    private int currentC, currentR, currentDirDegrees;

    private Arena arena;

    private int[][][][] grid; // grid of positions

    private int[]  currentNode;

    private int targetC, targetR, targetDir;

    private final Map<int[], int[]> predMap;

    private PriorityQueue<int[]> visitQueue;
    // int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)

    private int[] endPosition;

    private int totalCost;

    private int[] directionToDegreesMapping = new int[] {180, 90, 0, 270};
    private Map<Integer, Integer> degreesToDirectionsMapping  = new HashMap<>(){{
        put(180, 0);
        put(90, 1);
        put(270, 3);
        put(0, 2);
    }};

//    private int[] obstacleTargetDirMapping = new int[] {2, }

    //int[][]

    public ShortestPathAStar(int currentC, int currentR, int currentDirDegrees, int targetC, int targetR, int targetDir, Arena arena){
        this.arena = arena;
        this.currentC = currentC;
        this.currentR = currentR;
        this.currentDirDegrees = currentDirDegrees;
        this.targetC = targetC;
        this.targetR = targetR;
        this.targetDir = targetDir;
        this.predMap = new HashMap<>();
        this.visitQueue = new PriorityQueue<>(Comparator.comparing(k -> k[0]));
        this.endPosition = new int[3];
        this.totalCost = 0;
    }

    public void clear(){
        predMap.clear();

        // initialize the arrays
        for (int c = 0; c < SimulatorConstant.nRowGridGrid; c++){ // dimension c
            for (int r = 0; r < SimulatorConstant.nColumnGrid; r++){ // dimension r
                for (int dir = 0; dir < 4; dir++){
                    // int[] - 0: totalCost, 1: gCost, 2: hCost, 3: c, 4: r, 5: direction, 6: has been visited(0: false, 1: true)
                    grid[c][r][dir] = new int[] {
                            Integer.MAX_VALUE, // set total cost to be the max value first;
                            Integer.MAX_VALUE,
                            Integer.MAX_VALUE,
                            c,
                            r,
                            dir,
                            0
                    };
                }
            }
        }

        visitQueue.clear();
    }


    public AStarResult planPath(int startC, int startR, int startDir, int targetC, int targetR, int targetDir){

        if (0 > startC || startC >= SimulatorConstant.nColumnGrid || 0 > startR || startR >= SimulatorConstant.nRowGridGrid) {
            this.totalCost += 9999;
            return null;
        }
        clear();

        int endC, endR, endDir;
        List<int[]> path = new ArrayList<>();
        List<CarMove> moves = new ArrayList<CarMove>();
        boolean goalFound = false;

        int c, r, dir;
        int[] nextNode;
        int[] forwardLocation, backwardLocation, leftLocation, rightLocation;
        int currentGcost, hCost, gCost;

        int[] goalNode = grid[targetC][targetR][targetDir];
        this.currentNode = grid[startC][startR][startDir];
        this.visitQueue.add(currentNode);



        // if the goal node is reachable

        // searching
        while (!goalFound && !visitQueue.isEmpty()){
            currentNode = visitQueue.remove();


            c = currentNode[3];
            r = currentNode[4];
            dir = currentNode[5];
            currentGcost = currentNode[1];


            if (currentNode == goalNode){
                goalFound = true;
                endPosition = new int[]{c, r, dir};
                break;
            }

            forwardLocation = getForwardLocation(c, r, dir);
            leftLocation = getLeftLocation(c, r, dir);
            rightLocation = getRightLocation(c, r, dir);
            backwardLocation = getBackwardLocation(c, r, dir);

            if (forwardLocation != null){

                int nextC = forwardLocation[0];
                int nextR = forwardLocation[1];
                int nextDirDegrees = forwardLocation[2];
                // from degrees to direction conversion.
                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);
                nextNode = grid[nextC][nextR][nextDir];

                gCost = currentGcost + 1;
                hCost = heuristic(c, r, targetC, targetR);

                // if this node is already added, we will only change it when the cost is better.
                if (gCost < nextNode[1]){
                    predMap.put(currentNode, nextNode);
                    nextNode[1] = gCost;
                    nextNode[2] = hCost;
                    visitQueue.add(nextNode);
                }
            }

            if (backwardLocation != null){
                int nextC = backwardLocation[0];
                int nextR = backwardLocation[1];
                int nextDirDegrees = backwardLocation[2];

                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);
                nextNode = grid[nextC][nextR][nextDir];

                gCost = currentGcost + 1;
                hCost = heuristic(c, r, targetC, targetR);

                // if this node is already added, we will only change it when the cost is better.
                if (gCost < nextNode[1]){
                    predMap.put(currentNode, nextNode);
                    nextNode[1] = gCost;
                    nextNode[2] = hCost;
                    visitQueue.add(nextNode);
                }
            }

            if (leftLocation != null){
                int nextC = leftLocation[0];
                int nextR = leftLocation[1];
                int nextDirDegrees = leftLocation[2];

                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);
                nextNode = grid[nextC][nextR][nextDir];

                gCost = currentGcost + 1;
                hCost = heuristic(c, r, targetC, targetR);

                // if this node is already added, we will only change it when the cost is better.
                if (gCost < nextNode[1]){
                    predMap.put(currentNode, nextNode);
                    nextNode[1] = gCost;
                    nextNode[2] = hCost;
                    visitQueue.add(nextNode);
                }
            }

            if (rightLocation != null){
                int nextC = rightLocation[0];
                int nextR = rightLocation[1];
                int nextDirDegrees = rightLocation[2];

                int nextDir = degreesToDirectionsMapping.get(nextDirDegrees);
                nextNode = grid[nextC][nextR][nextDir];

                gCost = currentGcost + 1;
                hCost = heuristic(c, r, targetC, targetR);

                // if this node is already added, we will only change it when the cost is better.
                if (gCost < nextNode[1]){
                    predMap.put(currentNode, nextNode);
                    nextNode[1] = gCost;
                    nextNode[2] = hCost;
                    visitQueue.add(nextNode);
                }
            }

            // the node has been visited.
            currentNode[6] = 1;

        }

        if (!goalFound){
            this.totalCost += 9999;
            return null;
        }

        return new AStarResult(moves, path);

    }

    private int[] getForwardLocation(int currentC, int currentR, int currentDirDegrees){
        int[] forwardPosition;

        switch(currentDirDegrees){
            case 0:
                forwardPosition = new int[]{currentC + 1, currentR, currentDirDegrees};
                break;
            case 90:
                forwardPosition = new int[]{currentC, currentR - 1, currentDirDegrees};
                break;
            case 180:
                forwardPosition = new int[]{currentC - 1, currentR, currentDirDegrees};
                break;
            case 270:
                forwardPosition = new int[]{currentC, currentR + 1, currentDirDegrees};
                break;
            default:
                forwardPosition = null;
                break;
        }
        if (forwardPosition != null && isSafePosition(currentC, currentR))
            return forwardPosition;
        return null;
    }

    private int[] getBackwardLocation(int currentC, int currentR, int currentDirDegrees){
        int[] backwardPos;

        switch(currentDirDegrees){
            case 0:
                backwardPos = new int[] {currentC - 1, currentR, currentDirDegrees};
                break;
            case 90:
                backwardPos = new int[] {currentC, currentR + 1, currentDirDegrees};
                break;
            case 180:
                backwardPos = new int[] {currentC + 1, currentR, currentDirDegrees};
                break;
            case 270:
                backwardPos = new int[] {currentC, currentR - 1, currentDirDegrees};
                break;
            default:
                backwardPos = null;
                break;
        }
        if (backwardPos != null & isSafePosition(currentC, currentR))
            return backwardPos;
        return null;
    }

    private int[] getLeftLocation(int currentC, int currentR, int currentDirDegrees) {
        int[] leftPos;
        int[] circleCenter;
        List<int[]> leftTurnPath;

        switch (currentDirDegrees){
            case 0:
                leftPos = new int[] {currentC + AlgoConstant.R, currentR - AlgoConstant.R, 90};
                circleCenter = new int[] {currentC, currentR - AlgoConstant.R};
                break;
            case 90:
                leftPos = new int[] {currentC - AlgoConstant.R, currentR - AlgoConstant.R, 180};
                circleCenter = new int[] {currentC - AlgoConstant.R,  currentR};
                break;
            case 180:
                leftPos = new int[] {currentC - AlgoConstant.R, currentR + AlgoConstant.R, 270};
                circleCenter = new int[] {currentC, currentR + AlgoConstant.R};
                break;
            case 270:
                leftPos = new int[] {currentC + AlgoConstant.R, currentR + AlgoConstant.R, 0};
                circleCenter = new int[] {currentC + AlgoConstant.R, currentR};
                break;
            default:
                leftPos = null;
                circleCenter = null;
                break;
        }
        // check if the grid is satisfiable.
        if (leftPos != null && isSafePosition(circleCenter[0], circleCenter[1])) {
            leftTurnPath = TrajectoryToArenaGrid.findGridCirclePath(currentC, currentR,
                    leftPos[0], leftPos[1],
                    circleCenter[0], circleCenter[1],
                    false);

        }


        return leftPos;
    }

    private int[] getRightLocation(int currentC, int currentR, int currentDirDegrees) {
        int[] rightPos;
        int[] circleCenter;
        List<int[]> rightTurnPath;

        switch (currentDirDegrees){
            case 0:
                rightPos = new int[] {currentC + AlgoConstant.R, currentR + AlgoConstant.R, 270};
                circleCenter = new int[] {currentC, currentR + AlgoConstant.R};
                break;
            case 90:
                rightPos = new int[] {currentC + AlgoConstant.R, currentR - AlgoConstant.R, 0};
                circleCenter = new int[] {currentC + AlgoConstant.R, currentR};
                break;
            case 180:
                rightPos = new int[] {currentC - AlgoConstant.R, currentR - AlgoConstant.R, 90};
                circleCenter = new int[] {currentC, currentR - AlgoConstant.R};
                break;
            case 270:
                rightPos = new int[] {currentC - AlgoConstant.R, currentR + AlgoConstant.R, 180};
                circleCenter = new int[] {currentC - AlgoConstant.R, currentR};
                break;
            default:
                rightPos = null;
                circleCenter = null;
                break;
        }
        if (rightPos != null && isSafePosition(circleCenter[0], circleCenter[1]) ){
            rightTurnPath = TrajectoryToArenaGrid.findGridCirclePath(currentC, currentR,
                    rightPos[0], rightPos[1],
                    circleCenter[0], circleCenter[1],
                    true);
        }
        return rightPos;
    }

    private boolean isSafePosition(int c, int r){
        if (c > 0 && c < SimulatorConstant.nColumnGrid &&  r > 0 && r < SimulatorConstant.nRowGridGrid ){
            return arena.checkWithCorrespondingBlock(r, c);
        }
        return false;
    }

    // using Manhattan distance as heuristic. Note that the Manhattan distance denotes the right angle distance,
    // whereas the euclidean distance denotes the straight line distance
    private int heuristic(int curC, int curR, int nextC, int nextR){
        int absC = Math.abs(nextC - curC);
        int absR = Math.abs(nextR - curR);

        return absC + absR;
    }


}
