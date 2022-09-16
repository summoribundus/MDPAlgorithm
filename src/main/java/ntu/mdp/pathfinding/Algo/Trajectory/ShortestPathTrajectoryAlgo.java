package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Algo.AlgoConstant;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.InputData;
import ntu.mdp.pathfinding.Obstacle;
import ntu.mdp.pathfinding.Point;

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
        shortestPathBF.refreshPath();
        int threadLimit = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threadLimit);

        Map<Integer, Obstacle> idxMap = shortestPathBF.getIdxMapping();
        ShortestPathTrajectoryResult lastMinResult = null;

        while (shortestPathBF.hasNextPath()) {
            List<ShortestPathTrajectoryTask> tasks = new ArrayList<>();
            for (int i = 0; i < threadLimit && shortestPathBF.hasNextPath(); i++) {
                int[] path = shortestPathBF.getNextPath();
                ShortestPathTrajectoryTask runnable = new ShortestPathTrajectoryTask(path,
                        idxMap, arena);
                tasks.add(runnable);
            }

            try {
                List<Future<ShortestPathTrajectoryResult>> futures = service.invokeAll(tasks);
                for (Future<ShortestPathTrajectoryResult> future : futures) {
                    if (!future.isDone()) continue;
                    ShortestPathTrajectoryResult result = future.get();
                    if (result == null) continue;

                    if (lastMinResult == null) lastMinResult = result;
                    else if (lastMinResult.compareTo(result) < 0) {
                        service.shutdown();
                        return lastMinResult;
                    } else lastMinResult = result;
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
            List<Point> path = result.getPathGrids();
            for (Point p : path) {
                System.out.println(p);
            }
        }
    }
}
