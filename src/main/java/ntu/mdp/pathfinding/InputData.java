package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 2;
    private static final int startC = 2;
    private static final Obstacle[] obstacles = new Obstacle[]{
                new Obstacle(35, 5, 1, 3, false),
                new Obstacle(35, 13, 1, 8, false),
                new Obstacle(35, 23, 1, 13, false), // tricky point
                new Obstacle(35, 33, 1,19, false),
                new Obstacle(35, 38, 1, 22, false),
//                new Obstacle(35, 36, 1, 25, false),
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
