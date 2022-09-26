package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 2;
    private static final int startC = 2;
    private static final Obstacle[] obstacles = new Obstacle[]{
                new Obstacle(36, 2, 1, 3, false),
                new Obstacle(24, 12, 3, 8, false),
                new Obstacle(14, 20, 2, 13, false), // tricky point
                new Obstacle(32, 30, 0,19, false),
                new Obstacle(18, 38, 0, 22, false),
                new Obstacle(4,  26, 2, 25, false),
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
