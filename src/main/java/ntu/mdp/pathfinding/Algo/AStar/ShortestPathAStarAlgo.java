package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.Obstacle;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShortestPathAStarAlgo {
    private final Arena arena;
    private final ShortestPathBF shortestPathBF;

    public ShortestPathAStarAlgo(Arena arena, ShortestPathBF shortestPathBF) {
        this.arena = arena;
        this.shortestPathBF = shortestPathBF;
    }

    public void findBackupShortestPath() {
        int batch = 0, threadLimit = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(threadLimit);

        Map<Integer, Obstacle> idxMap = shortestPathBF.getIdxMapping();

        while (shortestPathBF.hasNextPath()) {

        }
    }
}
