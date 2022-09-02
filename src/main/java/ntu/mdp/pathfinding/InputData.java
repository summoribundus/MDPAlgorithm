package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 0;
    private static final int startC = 0;
    private static final Obstacle[] obstacles = new Obstacle[]{
                new Obstacle(10, 12, 2, 3, false),
                new Obstacle(4, 26, 0, 8, false),
//                new Obstacle(18, 8, 1, 13, false),
                new Obstacle(26, 36, 3, 19, false),
//                new Obstacle(32, 14, 2, 22, false),
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
