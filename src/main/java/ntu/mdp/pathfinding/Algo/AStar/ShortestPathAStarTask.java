package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.AStarResult;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ShortestPathAStarTask implements Callable<AStarResult> {
    private int[] path;
    private Map<Integer, Obstacle> idxMap;

    private Arena arena;

    public ShortestPathAStarTask(int[] path, Map<Integer, Obstacle> idxMap, Arena arena) {
        this.path = path;
        this.idxMap = idxMap;
        this.arena = arena;
    }

    @Override
    public AStarResult call() throws Exception {
        List<int[]> pathGrids = new ArrayList<>();
        List<CarMove> carMoves = new ArrayList<>();

        int cost = 0;
        Obstacle car = idxMap.get(path[0]);
        int carR = car.getR(), carC = car.getC(), theta = 270;
        boolean pathValid = true;

        for (int i = 1; i < path.length; i++) {
            Obstacle ob = idxMap.get(path[i]);
            ShortestPathAStar shortestPathAStar = new ShortestPathAStar(carC, carR, theta, ob.getTargetedC(), ob.getTargetedR(), ob.getDir(), arena);
            AStarResult aStarResult = shortestPathAStar.planPath();
            if (aStarResult == null) { pathValid = false; break; }
            pathGrids.addAll(aStarResult.getPointPath());
            carMoves.addAll(aStarResult.getCarMoves());
            cost += aStarResult.getCost();
        }

        if (!pathValid) return null;
        return new AStarResult(carMoves, pathGrids, cost);
    }
}
