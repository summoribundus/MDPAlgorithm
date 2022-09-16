package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ShortestPathAStarAlgo {
    private final Arena arena;
    private final ShortestPathBF shortestPathBF;

    public ShortestPathAStarAlgo(Arena arena, ShortestPathBF shortestPathBF) {
        this.arena = arena;
        this.shortestPathBF = shortestPathBF;
    }

    public ShortestPathAStarResult findBackupShortestPath() {
        shortestPathBF.refreshPath();
        int threadLimit = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threadLimit);
        ShortestPathAStarResult lastMinResult = null;
        Map<Integer, Obstacle> idxMap = shortestPathBF.getIdxMapping();

        while (shortestPathBF.hasNextPath()) {
            List<ShortestPathAStarTask> tasks = new ArrayList<>();
            for (int i = 0; i < threadLimit && shortestPathBF.hasNextPath(); i++) {
                int[] path = shortestPathBF.getNextPath();
                ShortestPathAStarTask task = new ShortestPathAStarTask(path, idxMap, arena);
                tasks.add(task);
            }

            try {
                List<Future<ShortestPathAStarResult>> futures = service.invokeAll(tasks);
                for (Future<ShortestPathAStarResult> future : futures) {
                    if (!future.isDone()) continue;
                    ShortestPathAStarResult result = future.get();
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
}
