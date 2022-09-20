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
    private final static String IP = "localhost";
    private final static int port = 2223;
    private static DatagramSocket socket;

    public static void main(String[] args) throws IOException {
        socket = new DatagramSocket();

//        String message = "Hello";
//        byte[] buf = message.getBytes(StandardCharsets.UTF_8);
//        byte[] buffer = new byte[512];
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//
//        socket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName(IP), port));
//        socket.receive(packet);
//        System.out.println(new String(packet.getData()));

        byte[] buffer = new byte[256];

        List<CarMove> carMoves = findShortestPath();
        if (carMoves == null) {
            System.out.println("No solution found. Not making movement");
            closeConnection();
            return;
        }

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
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP), port);
        socket.send(packet);
    }
    private static List<CarMove> findShortestPath() {
        ShortestPathBF shortestPathBF = new ShortestPathBF(InputData.getObstacles(), InputData.getStartR(), InputData.getStartC());
        Arena arena = new Arena(AlgoConstant.GridM, AlgoConstant.GridN, InputData.getObstacles());
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
