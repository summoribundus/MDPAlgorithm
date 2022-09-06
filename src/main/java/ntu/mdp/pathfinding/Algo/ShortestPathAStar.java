package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.GUI.SimulatorConstant;

import java.util.*;

public class ShortestPathAStar {

    private int currentC, currentR, currentDir;
    private int targetC, targetR, targetDir;

    private final Map<Integer, Integer> predMap;

    private PriorityQueue<int[]> visitedQueue;

    private int[] endPosition;

    private int totalCost;

    //int[][]

    public ShortestPathAStar(int currentC, int currentR, int currentDir, int targetC, int targetR, int targetDir){
        this.currentC = currentC;
        this.currentR = currentR;
        this.currentDir = currentDir;
        this.targetC = targetC;
        this.targetR = targetR;
        this.targetDir = targetDir;
        this.predMap = new HashMap<>();
        this.visitedQueue = new PriorityQueue<>(Comparator.comparing(k -> k[0]));
        this.endPosition = new int[3];
        this.totalCost = 0;
    }

    public void clear(){
        predMap.clear();
        visitedQueue.clear();
    }

    public AStarResult planPath(int startC, int startR, int startDir, int targetC, int targetR, int targetDir){

        if (0 > startC || startC >= SimulatorConstant.nColumnGrid || 0 > startR || startR >= SimulatorConstant.nRowGridGrid) {
            this.totalCost += 9999;
            return null;
        }
        clear();

        int endC, endR, endDir;
        List<int[]> path = new ArrayList<int[]>();
        List<CarMove> moves = new ArrayList<CarMove>();
        boolean goalFound = false;
        this.visitedQueue.add(new int[] {totalCost, currentC, currentR, currentDir, 0});

        // if the goal node is reachable

        // searching
        while (!goalFound && !visitedQueue.isEmpty()){
            int[] currentNode = visitedQueue.remove();


            int c = currentNode[1];
            int r = currentNode[2];
            int dir = currentNode[3];
            int currentNodeCost = currentNode[4];

            if (c == targetC && r == targetR){
                goalFound = true;
                endPosition = new int[]{currentC, currentR, currentDir};
                break;
            }

            //forwardLocation = getForwardNode()



        }


        return new AStarResult(moves, path);


    }


//    private int[] getForwardNode(int currentC, int currentR, int currentDir){
//        int forwardC;
//    }


}
