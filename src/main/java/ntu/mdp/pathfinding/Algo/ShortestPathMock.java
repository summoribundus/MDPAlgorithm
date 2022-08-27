package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

public class ShortestPathMock {
    public static Obstacle[] getObstacles() {
        return new Obstacle[]{
                new Obstacle(5, 6, 2, 3, false),
                new Obstacle(2, 13, 0, 8, false),
                new Obstacle(9, 4, 1, 13, false),
                new Obstacle(13, 18, 3, 19, false),
                new Obstacle(16, 7, 2, 22, false),
        };
    }
}
