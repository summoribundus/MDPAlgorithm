package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.InputData;
import ntu.mdp.pathfinding.Obstacle;

import java.util.*;
import java.util.concurrent.*;

public class ShortestPathAlgo {
    private final Arena arena;
    private final ShortestPathBF shortestPathBF;

    public ShortestPathAlgo(int m, int n, Obstacle[] obstacles, int r, int c) {
        this.arena = new Arena(m, n, obstacles);
        this.shortestPathBF = new ShortestPathBF(obstacles, r, c);
    }

    public ShortestPathTrajectoryResult findShortestPath() {
        shortestPathBF.findPath();

        int batch = 0, threadLimit = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threadLimit);

        Map<Integer, Obstacle> idxMap = shortestPathBF.getIdxMapping();
        ShortestPathTrajectoryResult lastMinResult = null;

        while (shortestPathBF.hasNextPath()) {
            List<ShortestPathTrajectoryTask> tasks = new ArrayList<>();
            for (int i = 0; i < threadLimit && shortestPathBF.hasNextPath(); i++) {
                int[] path = shortestPathBF.getNextPath();
                ShortestPathTrajectoryTask runnable = new ShortestPathTrajectoryTask(batch, i, path,
                        idxMap, arena);
                tasks.add(runnable);
            }
//            System.out.println("Trajectory task prepared");

            try {
                List<Future<ShortestPathTrajectoryResult>> futures = service.invokeAll(tasks);
                List<ShortestPathTrajectoryResult> results = new ArrayList<>();
                for (Future<ShortestPathTrajectoryResult> future : futures) {
                    if (!future.isDone()) continue;
                    ShortestPathTrajectoryResult result = future.get();
                    if (result == null) continue;
                    results.add(result);
                }

                if (results.isEmpty()) continue;
                for (ShortestPathTrajectoryResult res : results) {
                    if (lastMinResult == null) lastMinResult = res;
                    else if (lastMinResult.compareTo(res) < 0) {
                        service.shutdown();
                        return lastMinResult;
                    } else lastMinResult = res;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        service.shutdown();
        return lastMinResult;
    }

    public static void main(String[] args) {
        ShortestPathAlgo algo = new ShortestPathAlgo(AlgoConstant.GridM, AlgoConstant.GridN, InputData.getObstacles(), InputData.getStartR(), InputData.getStartC());
        ShortestPathTrajectoryResult result = algo.findShortestPath();
        if (result == null)
            System.out.println("No solution found");
        else {
            List<int[]> path = result.getPathGrids();
            for (int[] p : path) {
                System.out.println(Arrays.toString(p));
            }
        }
    }
}
