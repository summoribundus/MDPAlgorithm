package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.InputData;
import ntu.mdp.pathfinding.Obstacle;

import java.util.*;
import java.util.concurrent.*;

public class ShortestPathTrajectoryAlgo {
    private final Arena arena;
    private final ShortestPathBF shortestPathBF;

    public ShortestPathTrajectoryAlgo(Arena arena, ShortestPathBF shortestPathBF) {
        this.arena = arena;
        this.shortestPathBF = shortestPathBF;
    }

    public ShortestPathTrajectoryResult findShortestPath() {
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
        ShortestPathBF shortestPathBF = new ShortestPathBF(InputData.getObstacles(), InputData.getStartR(), InputData.getStartC());
        Arena arena = new Arena(AlgoConstant.GridM, AlgoConstant.GridN, InputData.getObstacles());
        ShortestPathTrajectoryAlgo algo = new ShortestPathTrajectoryAlgo(arena, shortestPathBF);
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
