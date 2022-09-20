package ntu.mdp.pathfinding;

import ntu.mdp.pathfinding.Algo.AStar.ShortestPathAStarAlgo;
import ntu.mdp.pathfinding.Algo.AStar.ShortestPathAStarResult;
import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.Algo.Trajectory.ShortestPathTrajectoryAlgo;
import ntu.mdp.pathfinding.Algo.Trajectory.ShortestPathTrajectoryResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RPIApplication {
    private final static String RPI = "localhost";
    private final static int RPI_PORT = 2223;
    private final static String HOST = "10.27.182.66";
    private final static int Host_PORT = 2223;
    private static DatagramSocket socket;

    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(Host_PORT);
        byte[] obstacleBuf = new byte[128];
        DatagramPacket packet = new DatagramPacket(obstacleBuf, obstacleBuf.length);
        serverSocket.receive(packet);
        serverSocket.close();

        String boardConfigStr = new String(packet.getData(), 0, packet.getLength());
        String[] boardConfigStrSplit = boardConfigStr.split(",");
        Obstacle[] obstacles = constructObstacleFromString(boardConfigStrSplit[1]);
        String[] carConfig = boardConfigStrSplit[0].split(":");

        List<CarMove> carMoves = findShortestPath(obstacles, Integer.parseInt(carConfig[1]), Integer.parseInt(carConfig[0]));
        if (carMoves == null) {
            System.out.println("No solution found. Not making movement");
            closeConnection();
            return;
        }
//        String message = "Hello";
//        byte[] buf = message.getBytes(StandardCharsets.UTF_8);
//        byte[] buffer = new byte[512];
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//
//        socket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName(IP), port));
//        socket.receive(packet);
//        System.out.println(new String(packet.getData()));

        socket = new DatagramSocket();
        byte[] buffer = new byte[256];

        long startTime = System.currentTimeMillis();
        while (true) {
            for (CarMove carMove : carMoves) {
                for (String command : carMove.getInstructions()) {
                    sendCommand(command);
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(receivePacket);
                    if (System.currentTimeMillis() - startTime > 360000) {
                        System.out.println("6 minutes reached!");
                        sendCommand("00000"); // stop signal
                        closeConnection();
                        return;
                    }
                }
            }
        }
    }

    private static void sendCommand(String command) throws IOException {
        byte[] buf = command.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(RPI), RPI_PORT);
        socket.send(packet);
    }

    private static Obstacle[] constructObstacleFromString(String obstacleStrs) {
        String[] obstacleStrsSplit = obstacleStrs.split(";");
        Obstacle[] obstacles = new Obstacle[obstacleStrs.length()];
        int cnt = 0;
        for (String obstacleStr : obstacleStrsSplit) {
            String[] obstacleIdx = obstacleStr.split(":");
            obstacles[cnt++] = new Obstacle(Integer.parseInt(obstacleIdx[1]) * 2,
                    Integer.parseInt(obstacleIdx[0]) * 2,
                    Integer.parseInt(obstacleIdx[2]) * 2, 1, false);
        }
        return obstacles;
    }
    private static List<CarMove> findShortestPath(Obstacle[] obstacles, int r, int c) {
        ShortestPathBF shortestPathBF = new ShortestPathBF(obstacles, r, c);
        Arena arena = new Arena(AlgoConstant.GridM, AlgoConstant.GridN, obstacles);
        ShortestPathTrajectoryAlgo algo = new ShortestPathTrajectoryAlgo(arena, shortestPathBF);
        ShortestPathTrajectoryResult result = algo.findShortestPath();
        if (result != null) {
            System.out.println("Trajectory Solution Found");
            return result.getCarMoves();
        }

        ShortestPathAStarAlgo aStarAlgo = new ShortestPathAStarAlgo(arena, shortestPathBF);
        ShortestPathAStarResult aStarResult = aStarAlgo.findBackupShortestPath();
        if (aStarResult != null) {
            System.out.println("Back Solution Found");
            return aStarResult.getCarMoves();
        }
        return null;
    }

    private static void closeConnection() {
        socket.close();
    }
}
