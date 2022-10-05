package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 2;
    private static final int startC = 2;
    private static final Obstacle[] obstacles = new Obstacle[]{
                new Obstacle(10, 18, 0, 1, 3, false),
                new Obstacle(14, 28, 1, 2, 8, false),
                new Obstacle(24, 18, 3, 3, 13, false), // tricky point
                new Obstacle(30, 30, 0,4, 19, false),
                new Obstacle(30, 8, 1, 5, 22, false),
//                new Obstacle(4,  26, 2, 25, false),
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
