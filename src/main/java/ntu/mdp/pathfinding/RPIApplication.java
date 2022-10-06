package ntu.mdp.pathfinding;

import ntu.mdp.pathfinding.Algo.AStar.ShortestPathAStarAlgo;
import ntu.mdp.pathfinding.Algo.AStar.ShortestPathAStarResult;
import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.Algo.Trajectory.ShortestPathTrajectoryAlgo;
import ntu.mdp.pathfinding.Algo.Trajectory.ShortestPathTrajectoryResult;
import ntu.mdp.pathfinding.Instruction.Instruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;

public class RPIApplication {
    private final static String RPI = "192.168.17.17";
    private final static int RPI_PORT = 4444;
    private final static String HOST = "10.27.182.66";
    private final static int Host_PORT = 2223;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) throws IOException {
        socket = new Socket(RPI, RPI_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
        String boardConfigStr = in.readLine();
        System.out.println(boardConfigStr);
//        String boardConfigStr = "1-1,11-0-3;17-5-1;6-7-2;11-10-1;0-14-2;14-15-1;7-16-1";
        String[] boardConfigStrSplit = boardConfigStr.split(",");
        Obstacle[] obstacles = constructObstacleFromString(boardConfigStrSplit[1]);
        String[] carConfig = boardConfigStrSplit[0].split("-");

        List<Instruction> instructions = findShortestPath(obstacles, Integer.parseInt(carConfig[1]) * 2, Integer.parseInt(carConfig[0]) * 2);
        if (instructions == null) {
            System.out.println("No solution found. Not making movement");
            closeConnection();
            return;
        }

        long startTime = System.currentTimeMillis();
//        while (true) {
            for (Instruction ins : instructions) {
//                System.out.println("New ins:");
                System.out.println(ins.command());
                System.out.println(ins.gridPath());
                out.println(ins.command());
                if (ins.gridPath() != null)
                    out.println(ins.gridPath());
                in.readLine();
                if (System.currentTimeMillis() - startTime > 360000) {
                    System.out.println("6 minutes reached!");
                    out.println("00000"); // stop signal
                    closeConnection();
                    return;
                }
            }
//        }
    }

    public static Obstacle[] constructObstacleFromString(String obstacleStrs) {
        String[] obstacleStrsSplit = obstacleStrs.split(";");
        Obstacle[] obstacles = new Obstacle[obstacleStrsSplit.length];
        int cnt = 0;
        for (String obstacleStr : obstacleStrsSplit) {
            String[] obstacleIdx = obstacleStr.split("-");
            obstacles[cnt] = new Obstacle(Integer.parseInt(obstacleIdx[1]) * 2,
                    Integer.parseInt(obstacleIdx[0]) * 2,
                    Integer.parseInt(obstacleIdx[2]), cnt, 1, false);
            cnt++;
        }
        return obstacles;
    }
    public static List<Instruction> findShortestPath(Obstacle[] obstacles, int r, int c) {
        ShortestPathBF shortestPathBF = new ShortestPathBF(obstacles, r, c);
        Arena arena = new Arena(AlgoConstant.GridM, AlgoConstant.GridN, obstacles);
//        ShortestPathTrajectoryAlgo algo = new ShortestPathTrajectoryAlgo(arena, shortestPathBF);
//        ShortestPathTrajectoryResult result = algo.findShortestPath();
//        if (result != null) {
//            System.out.println("Trajectory Solution Found");
//            return result.getInstructions();
//        }

        ShortestPathAStarAlgo aStarAlgo = new ShortestPathAStarAlgo(arena, shortestPathBF);
        ShortestPathAStarResult aStarResult = aStarAlgo.findBackupShortestPath();
        if (aStarResult != null) {
            System.out.println("Back Solution Found");
            aStarResult.computeCompressedCarMove();
            return aStarResult.getInstructions();
        }
        return null;
    }

    private static void closeConnection() throws IOException {
        socket.close();
    }
}
