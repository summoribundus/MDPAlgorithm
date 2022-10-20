package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 2;
    private static final int startC = 2;
    private static final Obstacle[] obstacles = new Obstacle[]{
            new Obstacle(28, 0, 2, 1, 1, false),
            new Obstacle(14, 10, 2, 2, 2, false),
            new Obstacle(20, 22, 1, 3, 3, false),
            new Obstacle(2, 22, 3, 4, 4, false),
            new Obstacle(10, 34, 0, 5, 5, false),
            new Obstacle(30, 28, 1, 7, 7, false)
    };

    private static final Car car = new Car(startR, startC);
    public static Obstacle[] getObstacles() {
        return obstacles;
    }

    public static Car getCar() {
        return car;
    }

    public static int getStartR() {
        return startR;
    }

    public static int getStartC() {
        return startC;
    }
}
