package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 2;
    private static final int startC = 2;
    private static final Obstacle[] obstacles = new Obstacle[]{
                new Obstacle(5, 12, 3, 3, false),
                new Obstacle(20, 20, 1, 8, false),
                new Obstacle(33, 17, 0, 13, false), // tricky point
                new Obstacle(15, 32, 1, 19, false),
                new Obstacle(27, 2, 2, 22, false),
                new Obstacle(35, 36, 1, 25, false),
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
