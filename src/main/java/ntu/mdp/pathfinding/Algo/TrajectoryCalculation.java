package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

import java.util.HashMap;

/***
 * this is the trajectory calculation from one obstacle to another - for simulation
 */
public class TrajectoryCalculation {


    // use enlarged virtual obstacles, robot as a point, the

    // the constant r, HARDCODED now
    final double r = 3.14;
    // the error interval epsilon, HARDCODED now
    final double epsilon = 0.1;
    // opposite direction mapping
    final HashMap<Integer, Integer> oppositeDir = new HashMap<Integer, Integer>() {{put(0, 2); put(1, 3); put(2, 0); put(3, 1);}};

    // the real coordinates of the obstacle
    private double target_x;
    private double target_y;
    private double target_theta;

    // the center coordinates of the obstacle
    private double obs_circle_x;
    private double obs_circle_y;

    // the real coordinates of the robot
    private double robot_x;
    private double robot_y;
    private double robot_theta;

    // the center coordinates of the robot
    private double robot_circle_x;
    private double robot_circle_y;

    public TrajectoryCalculation(Obstacle obs, int robot_x, int robot_y, double robot_theta){
//        this.target_x = obs.getTargetedX();
//        this.target_y = obs.getTargetedY();
        this.target_theta = oppositeDir.get(obs.getDir()) * Math.PI / 2; // represent the data in PI.
        this.robot_x = robot_x;
        this.robot_y = robot_y;
        this.robot_theta = robot_theta;
    }

    // to calculate the position of the circle
    public double[] calculateRobotStartingCenterOfCircle(double robot_x, double robot_y, double robot_theta, int robot_dir){
        // dir denotes left or right turning. 0 -> left turn, 1 -> right turn
        // turn left, to find circle point, clockwise. turn right, counterclockwise.
        double circlePoint_x = 0.0;
        double circlePoint_y = 0.0;

        // the unit vector v1 in direction of the robot.
        double v1_x = Math.cos(robot_theta);
        double v1_y = Math.sin(robot_theta);

        // find v1 counterclockwise/clockwise rotate to v2 according to the directions
        double v2_x = 0.0;
        double v2_y = 0.0;
        if (robot_dir == 0) //turning left, counterclockwise
        {
            v2_x = -v1_y;
            v2_y = v1_x;
        } else {
            v2_x = v1_y;
            v2_y = -v1_x;
        }

        // find the turning circle point.
        circlePoint_x = robot_x + v2_x * r;
        circlePoint_y = robot_y + v2_y * r;

        return new double[]{circlePoint_x, circlePoint_y};
    }



    // function to calculate euclidean distance of 2 functions
    public double EuclideanDistance(int x1, int y1, int x2, int y2){
        double delta_x = Math.abs(x1-x2);
        double delta_y = Math.abs(y1-y2);
        return Math.sqrt(Math.pow(delta_x, 2) + Math.pow(delta_y, 2));
    }

    // arc length computation
    public double ArcCalculation(double start_x, double start_y, double dest_x, double dest_y, int direc){
        // direc = 0 for turn left, direct = 1 for turn right
        // tan_x and tan_y are the coordinates for the starting point on the arc,
        // while dest_x and dest_y are the coordinates for the destination point on the arc
        double alpha = Math.atan2(dest_y, dest_x) - Math.atan2(start_y, start_x);
        if (alpha < 0 && direc == 0)
            alpha = alpha + 2*Math.PI;
        else if (alpha > 0 && direc == 1)
            alpha = alpha - 2*Math.PI;

        return Math.abs(alpha * r);

    }

    // choose the path according to the relative position of
    public void chooseCSCPath(int robot_x, int robot_y,
                           int obs_x, int obs_y,
                           int robot_circle_x, int robot_circle_y, double robot_theta,
                           int obs_circle_x, int obs_circle_y, double obs_theta) {

        // 1. obstacle is at the right of the robot
        if (obs_x > robot_x) {

        }



        // 1. obstacle is at the upper right of the robot
        // facing up/left
        if (obs_x > robot_x && obs_y > robot_x &&
                (checkEqualWithinEpsilon(obs_theta, Math.PI/2) || checkEqualWithinEpsilon(obs_theta, Math.PI)))
            rsrORlsl(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);
        // facing down/right
        else if (obs_x > robot_x && obs_y > robot_x &&
                (checkEqualWithinEpsilon(obs_theta, 0) || checkEqualWithinEpsilon(obs_theta, -Math.PI/2)))
            rslORlsr(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);

        // 2. obstacle is at the upper left of the robot
        // facing up/right
        if (obs_x < robot_x && obs_y > robot_y &&
                ((checkEqualWithinEpsilon(obs_theta, Math.PI/2) || checkEqualWithinEpsilon(obs_theta, 0))))
            rsrORlsl(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);
        // facing down/left
        else if (obs_x < robot_x && obs_y > robot_y &&
                ((checkEqualWithinEpsilon(obs_theta, -Math.PI/2) || checkEqualWithinEpsilon(obs_theta, Math.PI))))
            rslORlsr(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);

        // 3. obstacle is at the lower right of the robot
        // facing up/right
        if (obs_x > robot_x && obs_y < robot_y &&
                ((checkEqualWithinEpsilon(obs_theta, Math.PI/2) || checkEqualWithinEpsilon(obs_theta, 0))))
            rsrORlsl(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);
        // facing down/left
        else if (obs_x > robot_x && obs_y < robot_y &&
                ((checkEqualWithinEpsilon(obs_theta, -Math.PI/2) || checkEqualWithinEpsilon(obs_theta, Math.PI))))
            rslORlsr(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);

        // 4. obstacle is at the lower left of the robot
        // facing up/left
        if (
                obs_x < robot_x && obs_y < robot_y &&
                        ((checkEqualWithinEpsilon(obs_theta, Math.PI/2) || checkEqualWithinEpsilon(obs_theta, Math.PI)))
        )
            rsrORlsl(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);
        // facing down/right
        else if (
                obs_x < robot_x && obs_y < robot_y &&
                        ((checkEqualWithinEpsilon(obs_theta, -Math.PI/2) || checkEqualWithinEpsilon(obs_theta, 0)))
        )
            rslORlsr(robot_x, robot_y, obs_x, obs_y, robot_circle_x, robot_circle_y, robot_theta, obs_circle_x, obs_circle_y, obs_theta);


    }

    public boolean checkEqualWithinEpsilon(double num1, double num2) {
        double upperbound = num2 + epsilon;
        double lowerbound = num2 - epsilon;
        if ( num1 <= upperbound && num2 >= lowerbound)
            return true;
        return false;
    }


    // the rsr type length
    public void rsrORlsl(int robot_x, int robot_y,
                            int target_x, int target_y,
                            int robot_circle_x, int robot_circle_y, double robot_theta,
                            int obs_circle_x, int obs_circle_y, double obs_theta) {
        double l = EuclideanDistance(obs_circle_x, obs_circle_y, robot_circle_x, robot_circle_y);
        // calculate v1 which points from center p1 to center p2
        double v1_x = obs_circle_x - robot_circle_x;
        double v1_y = obs_circle_y - robot_circle_y;
        // v2 is rotated by pi/2
        double v2_x = -v1_y;
        double v2_y = v1_x;

        // get the intermidiate pt1 position
        double pt1_x = robot_circle_x + v2_x * r/l;
        double pt1_y = robot_circle_y + v2_y * r/l;
        double pt1_theta = Math.atan2(pt1_y, pt1_x);

        // get the intermidiate pt2 position
        double pt2_x = pt1_x + v1_x;
        double pt2_y = pt1_y + v1_y;
        double pt2_theta = Math.atan2(pt2_y, pt2_x);

        // calculate the 2 arcs
        double p1p_x = robot_x - robot_circle_x;
        double p1p_y = robot_y - robot_circle_y;
        double p1pt1_x = pt1_x - robot_circle_x;
        double p1pt1_y = pt1_y - robot_circle_y;

        double arc1 = ArcCalculation(p1p_x, p1p_y, p1pt1_x, p1pt1_y, 1);

        double p2pt2_x = pt2_x - obs_circle_x;
        double p2pt2_y = pt2_y - obs_circle_y;
//        double p2p_x = obs_x - obs_circle_x;
        double p2p_y = target_y - obs_circle_y;

//        double arc2 = ArcCalculation(p2pt2_x, p2pt2_y, p2p_x, p2p_y, 1);

//        double totalLength = arc1 + arc2 + l;


    }

    public void rslORlsr(int robot_x, int robot_y,
                    int target_x, int target_y,
                    int robot_circle_x, int robot_circle_y, double robot_theta,
                    int obs_circle_x, int obs_circle_y, double obs_theta) {

        double d = EuclideanDistance(robot_circle_x, robot_circle_y, obs_circle_x, obs_circle_y);
        double l = Math.sqrt(Math.pow(d, 2) - 4*Math.pow(r, 2));

        double theta = Math.acos(2*r/d); // return a value from 0 to pi

        // the vector from p1 to p2
        double v1x = obs_circle_x - robot_circle_x;
        double v1y = obs_circle_y - robot_circle_y;

        // rotation of v1 by angle theta
            // if rsl check if turning right
        double v2x;
        double v2y;
        if (target_x > robot_x) {
            v2x = v1x * Math.cos(theta) - v1y * Math.sin(theta);
            v2y = v1x * Math.sin(theta) + v1y * Math.cos(theta);
        }
            // if lsr
        else {
            v2x = v1x * Math.cos(theta) + v1y * Math.sin(theta);
            v2y = - v1x * Math.sin(theta) + v1y * Math.cos(theta);
        }

        // point pt1
        double pt1_x = obs_circle_x + r/d * v2x;
        double pt1_y = obs_circle_y + r/d * v2y;
        double pt1_theta = Math.atan2(pt1_y, pt1_x);


        // reversing the direction of v2 to get v3.
        double v3x = -v2x;
        double v3y = -v2y;

        // point pt2
        double pt2_x = obs_circle_x + r/d * v3x;
        double pt2_y = obs_circle_y + r/d * v3y;
        double pt2_theta = Math.atan2(pt2_y, pt2_x);

        // calculate the 2 arcs
        double p1p_x = robot_x - robot_circle_x;
        double p1p_y = robot_y - robot_circle_y;
        double p1pt1_x = pt1_x - robot_circle_x;
        double p1pt1_y = pt1_y - robot_circle_y;

        double arc1 = ArcCalculation(p1p_x, p1p_y, p1pt1_x, p1pt1_y, 1);

        double p2pt2_x = pt2_x - obs_circle_x;
        double p2pt2_y = pt2_y - obs_circle_y;
        double p2p_x = target_x - obs_circle_x;
        double p2p_y = target_y - obs_circle_y;

        double arc2 = ArcCalculation(p2pt2_x, p2pt2_y, p2p_x, p2p_y, 1);

        double totalLength = arc1 + arc2 + l;
    }

    // check if the robot is facing the obstacle, if facing, then correct, if not, need to reverse and/or rotate.
    public boolean checkIfRobotFacingObstacle(double robotTheta, double obsTheta) {
        // condition 1 - facing the obs' side that is pasted with sticker
        if ((double) Math.round(robotTheta * 10) / 10
                + (double) Math.round(obsTheta * 10) / 10
                == (double) Math.round(Math.PI * 10) / 10)
            // what is the error interval / limits of accuracy that we allow?
            // say, 1 decimal places
            return true;

        // condition 2 - the distance between the VIRTUAL obstacle and the robot > 0???


        return false;
    }

    public TrajectoryResult trajectoryResult() {
        // pt1x, pt1y theta1, circle1, r, pt2x, pt2y, theta2, circle2
        return new TrajectoryResult(new int[]{1, 1}, new int[]{1, 1}, new int[]{1, 1}, new int[]{1, 1}, 90, 90, 1);
    }
}
