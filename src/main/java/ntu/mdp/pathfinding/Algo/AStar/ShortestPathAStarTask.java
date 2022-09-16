package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Obstacle;
import ntu.mdp.pathfinding.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ShortestPathAStarTask implements Callable<ShortestPathAStarResult> {
    private final int[] path;
    private final Map<Integer, Obstacle> idxMap;

    private final Arena arena;

    public ShortestPathAStarTask(int[] path, Map<Integer, Obstacle> idxMap, Arena arena) {
        this.path = path;
        this.idxMap = idxMap;
        this.arena = arena;
    }

    @Override
    public ShortestPathAStarResult call() throws Exception {
        List<Point> pathGrids = new ArrayList<>();
        List<CarMove> carMoves = new ArrayList<>();

        int cost = 0;
        Obstacle car = idxMap.get(path[0]);
        int carR = car.getR(), carC = car.getC(), theta = 270;
        boolean pathValid = true;

        for (int i = 1; i < path.length; i++) {
            Obstacle ob = idxMap.get(path[i]);
            ShortestPathAStar shortestPathAStar = new ShortestPathAStar(carC, carR, theta, ob.getTargetedC(), ob.getTargetedR(), ob.getDir(), arena);
            ShortestPathAStarResult shortestPathAStarResult = shortestPathAStar.planPath();
            if (shortestPathAStarResult == null) { pathValid = false; break; }
            pathGrids.addAll(shortestPathAStarResult.getPointPath());
            carMoves.addAll(shortestPathAStarResult.getCarMoves());
            carC = ob.getTargetedC(); carR = ob.getTargetedR(); theta = ob.getTargetedDegree(); cost += shortestPathAStarResult.getCost();
            pathGrids.add(new Point(carR, carC, true));
        }

        if (!pathValid) return null;
        return new ShortestPathAStarResult(carMoves, pathGrids, cost);
    }
}
