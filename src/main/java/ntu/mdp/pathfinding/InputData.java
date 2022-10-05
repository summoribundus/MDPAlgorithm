package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 2;
    private static final int startC = 2;
    private static final Obstacle[] obstacles = new Obstacle[]{
                new Obstacle(28, 12, 1, 1, 3, false),
                new Obstacle(18, 16, 2, 2, 8, false),
                new Obstacle(34, 22, 1, 3, 13, false), // tricky point
                new Obstacle(20, 26, 3,4, 19, false),
                new Obstacle(4, 30, 3, 5, 22, false),
                new Obstacle(20,  36, 2, 6, 25, false),
//
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
