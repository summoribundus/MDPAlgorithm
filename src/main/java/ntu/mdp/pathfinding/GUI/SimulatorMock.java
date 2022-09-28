package ntu.mdp.pathfinding.GUI;

import ntu.mdp.pathfinding.Car;
import ntu.mdp.pathfinding.Obstacle;

public class SimulatorMock {
    public static Obstacle[] getObstacles() {
        return new Obstacle[]{
//                new Obstacle(10, 12, 2, 3, false),
//                new Obstacle(4, 26, 0, 8, false),
//                new Obstacle(18, 8, 1, 13, false),
//                new Obstacle(26, 36, 3, 19, false),
//                new Obstacle(32, 14, 2, 22, false),
        };
    }

    public static Car getCarWithPath() {
        Car car = new Car(0, 1);
//        car.goTo(1, 1);
//        car.goTo(2, 1);
//        car.goTo(3, 1);
//        car.goTo(4, 1);
//        car.goTo(5, 1);
//        car.goTo(5, 2);
//        car.goTo(5, 3);
//        car.goTo(5, 4);
//        car.goTo(5, 5);
        return car;
    }
}
