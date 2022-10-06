package ntu.mdp.pathfinding;

public class InputData {
    private static final int startR = 2;
    private static final int startC = 2;
    private static final Obstacle[] obstacles = new Obstacle[]{
//                new Obstacle(28, 12, 1, 1, 3, false),
//                new Obstacle(18, 16, 2, 2, 8, false),
//                new Obstacle(34, 22, 1, 3, 13, false), // tricky point
//                new Obstacle(20, 26, 3,4, 19, false),
//                new Obstacle(4, 30, 3, 5, 22, false),
//                new Obstacle(20,  36, 2, 6, 25, false),
//            new Obstacle(10, 18, 0, 1, 3, false),
//            new Obstacle(14, 28, 1, 2, 8, false),
//            new Obstacle(24, 18, 3, 3, 13, false), // tricky point
//            new Obstacle(30, 30, 0,4, 19, false),
//            new Obstacle(30, 8, 1, 5, 22, false),
//            new Obstacle(18, 0, 2, 6, 24, false),
//            new Obstacle(38, 36, 1, 7, 10, false),

            new Obstacle(28, 0, 2, 1, 1, false),
            new Obstacle(14, 10, 2, 2, 2, false),
            new Obstacle(20, 22, 1, 3, 3, false),
            new Obstacle(0, 22, 3, 4, 4, false),
            new Obstacle(10, 34, 1, 5, 5, false),
            new Obstacle(32, 14, 1, 6, 6, false),
            new Obstacle(30, 28, 1, 7, 7, false)
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
