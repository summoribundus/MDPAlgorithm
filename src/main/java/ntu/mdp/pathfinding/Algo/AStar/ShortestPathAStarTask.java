package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ShortestPathAStarTask implements Callable<ShortestPathAStarResult> {
    private int[] path;
    private Map<Integer, Obstacle> idxMap;

    public ShortestPathAStarTask(int[] path, Map<Integer, Obstacle> idxMap) {
        this.path = path;
        this.idxMap = idxMap;
    }

    @Override
    public ShortestPathAStarResult call() throws Exception {
        List<int[]> pathGrids = new ArrayList<>();
        List<CarMove> carMoves = new ArrayList<>();

        Obstacle car = idxMap.get(path[0]);
        int carR = car.getR(), carC = car.getC(), theta = 270;
        boolean pathValid = true;

        for (int i = 1; i < path.length; i++) {
            Obstacle ob = idxMap.get(path[i]);

        }
    }
}
