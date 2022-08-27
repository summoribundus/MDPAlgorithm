package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

import java.util.HashMap;

import java.util.HashMap;

/***
 * this is the trajectory calculation from one obstacle to another - for simulation
 */
public class TrajectoryCalculation {


    // use enlarged virtual obstacles, robot as a point, the

    // the constant r, HARDCODED now
    final int r = 23;
    // opposite direction mapping
    final HashMap<Integer, Integer> oppositeDir = new HashMap<Integer, Integer>() {{put(0, 2); put(1, 3); put(2, 0); put(3, 1);}};

    // the real coordinates of the obstacle
    private int targetR;
    private int targetC;
    private int targetTheta;
    private int obstacleDir;

    // the real coordinates of the robot
    private int robotR;
    private int robotC;
    private int robotTheta;


    public TrajectoryCalculation(Obstacle obs, int robotR, int robotC, int robotTheta){
        this.targetR = obs.getTargetedR();
        this.targetC = obs.getTargetedC();
        this.obstacleDir = obs.getDir(); // of value [0, 1, 2, 3]
        this.targetTheta = (int)(oppositeDir.get(obs.getDir()) * Math.PI / 2); // represent the data in PI.
        this.robotR = robotR;
        this.robotC = robotC;
        this.robotTheta = robotTheta;
    }

    // to calculate the position of the circle
    public int[] calculateRobotStartingCenterOfCircle(int robotR, int robotC, int robot_theta, int targetC){
        // dir denotes left or right turning. 0 -> left turn, 1 -> right turn
        // turn left, to find circle point, clockwise. turn right, counterclockwise.
        double circlePointR = 0;
        double circlePointC = 0;

        // the unit vector v1 in direction of the robot.
        double v1R = Math.cos(robot_theta);
        double v1C = Math.sin(robot_theta);

        // find v1 counterclockwise/clockwise rotate to v2 according to the directions
        double v2R = 0;
        double v2C = 0;
        if (targetC > robotR) //turning left, counterclockwise
        {
            v2R = -v1C;
            v2C = v1R;
        } else {
            v2R = v1C;
            v2C = -v1R;
        }

        // find the turning circle point.
        circlePointR = robotR + v2R * r;
        circlePointC = robotC + v2C * r;

        return new int[]{(int)circlePointR, (int)circlePointC};
    }

    // calculate the obstacle circle center
    public int[] calculateObstacleCircleCenter(int targetX, int targetY, int robotX, int robotY, int obstacleDir){
        // turning right
        if (targetX > robotX) {
            if (obstacleDir == 1 || obstacleDir == 3)
                return new int[]{targetX - r, targetY};
            else if (targetY > robotY) // upper right
                return new int[]{targetX, targetY - r};
            else
                return new int[]{targetX, targetY + r};
        }
        // turning left
        else {
            if (obstacleDir == 1 || obstacleDir == 3)
                return new int[]{targetX + r, targetY};
            else if (targetY > robotY) // upper left
                return new int[]{targetX, targetY - r};
            else
                return new int[]{targetX, targetY + r};
        }
    }



    // function to calculate euclidean distance of 2 functions
    public int calculateEuclideanDistance(int r1, int c1, int r2, int c2){
        int deltaR = Math.abs(r1-r2);
        int deltaC = Math.abs(c1-c2);
        return (int)Math.sqrt(Math.pow(deltaR, 2) + Math.pow(deltaC, 2));
    }

    // arc angle computation
    public double calculateArcAngle(int startR, int startC, int destR, int destC, int dir) {
        double alpha = Math.atan2(destC, destR) - Math.atan2(startC, startR);
        if (alpha < 0 && dir == 0)
            alpha = alpha + 2*Math.PI;
        else if (alpha > 0 && dir == 1)
            alpha = alpha - 2*Math.PI;

        return alpha;
    }


    // arc length computation
    public int calculateArcLength(int startR, int startC, int destR, int destC, int dir){
        // dir = 0 for turn left, direct = 1 for turn right
        // tan_x and tan_y are the coordinates for the starting point on the arc,
        // while destR and destC are the coordinates for the destination point on the arc
        double alpha = calculateArcAngle(startR, startC, destR, destC, dir);

        return (int)Math.abs(alpha * r);

    }

    // choose the path according to the relative position of
    public TrajectoryResult chooseCSCPath(int robotR, int robotC,
                                          int targetR, int targetC,
                                          int robotTheta, int obstacleDir) {

        int[] centerOfCircle = calculateRobotStartingCenterOfCircle(robotR, robotC, robotTheta, targetR); //calculate the center of the robot turning trajectory
        int robotCircleR = centerOfCircle[0];
        int robotCircleC =centerOfCircle[1];

        int[] obstacleCircle = calculateObstacleCircleCenter(targetR, targetC, robotR, robotC, obstacleDir);
        int obsCircleR = obstacleCircle[0];
        int obsCircleC = obstacleCircle[1];

        // 1. obstacle is at the right of the robot
        if (targetR > robotR) {

            // 1(a) if obstacle is in the upper right corner of the robot.
            if (targetC > robotC) {
                if (obstacleDir == 0 || obstacleDir == 1)
                    // rsr
                    return calculateRSR(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);
                else
                    // rsl
                    return calculateRSL(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);
            }
            // 1(b) if obstacle is in the lower right corner of the robot.
            else {
                if (obstacleDir == 1 || obstacleDir == 2)
                    // rsr
                    return calculateRSR(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);
                else
                    // rsl
                    return calculateRSL(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);
            }

        }
        // 2. obstacle is at the left of the robot
        else {

            // 2(a) if obstacle is at the upper left of the robot
            if (targetC > robotC){
                //lsl
                if (obstacleDir == 1 || obstacleDir == 2)
                    return calculateLSL(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);
                    //lsr
                else
                    return calculateLSR(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);

            }
            // 2(b) if obstacle is at the lower left of the robot
            else {
                //lsl
                if (obstacleDir == 0 || obstacleDir == 1)
                    return calculateLSL(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);
                    //lsr
                else
                    return calculateLSR(robotR, robotC, targetR, targetC, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC);

            }
        }
    }

    // calculate rsr type path.
    public TrajectoryResult calculateRSR(int robotR, int robotC,
                                         int targetR, int targetC,
                                         int robotCircleR, int robotCircleC,
                                         int obsCircleR, int obsCircleC){

        int l = calculateEuclideanDistance(obsCircleR, obsCircleC, robotCircleR, robotCircleC);
        // calculate v1 which points from center p1 to center p2
        double v1R = obsCircleR - robotCircleR;
        double v1C = obsCircleC - robotCircleC;
        // v2 is rotated by pi/2
        double v2R = -v1C;
        double v2C = v1R;

        // get the intermidiate pt1 position
        int pt1R = robotCircleR + (int)(v2R * r/l);
        int pt1C = robotCircleC + (int)(v2C * r/l);

        // get the intermidiate pt2 position
        int pt2R = pt1R + (int)v1R;
        int pt2C = pt1C + (int)v1C;

        // calculate the 2 arcs
        int p1pR = robotR - robotCircleR;
        int p1pC = robotC - robotCircleC;
        int p1pt1R = pt1R - robotCircleR;
        int p1pt1C = pt1C - robotCircleC;

        int alpha1 = (int)calculateArcAngle(p1pR, p1pC, p1pt1R, p1pt1C, 1);
        int arc1 = calculateArcLength(p1pR, p1pC, p1pt1R, p1pt1C, 1);

        int p2pt2R = pt2R - obsCircleR;
        int p2pt2C = pt2C - obsCircleC;
        int p2pR = targetR - obsCircleR;
        int p2pC = targetC - obsCircleC;

        int alpha2 = (int)calculateArcAngle(p2pt2R, p2pt2C, p2pR, p2pC, 1);
        int arc2 = calculateArcLength(p2pt2R, p2pt2C, p2pR, p2pC, 1);

        int totalLength = arc1 + arc2 + l;

        TrajectoryResult res = trajectoryResult(new int[]{pt1R, pt1C}, new int[]{pt2R, pt2C}, new int[]{robotCircleR, robotCircleC}, new int[]{obsCircleR, obsCircleC}, alpha1, alpha2, totalLength);

        return res;
    }

    // calculate lsl type path.
    public TrajectoryResult calculateLSL(int robotR, int robotC,
                                         int targetR, int targetC,
                                         int robotCircleR, int robotCircleC,
                                         int obsCircleR, int obsCircleC){

        int l = calculateEuclideanDistance(obsCircleR, obsCircleC, robotCircleR, robotCircleC);
        // calculate v1 which points from center p1 to center p2
        double v1R = obsCircleR - robotCircleR;
        double v1C = obsCircleC - robotCircleC;
        // v2 is rotated by pi/2
        double v2R = -v1C;
        double v2C = v1R;

        // get the intermidiate pt1 position
        int pt1R = robotCircleR + (int)(v2R * r/l);
        int pt1C = robotCircleC + (int)(v2C * r/l);

        // get the intermidiate pt2 position
        int pt2R = pt1R + (int)v1R;
        int pt2C = pt1C + (int)v1C;

        // calculate the 2 arcs
        int p1pR = robotR - robotCircleR;
        int p1pC = robotC - robotCircleC;
        int p1pt1R = pt1R - robotCircleR;
        int p1pt1C = pt1C - robotCircleC;

        int alpha1 = (int)calculateArcAngle(p1pR, p1pC, p1pt1R, p1pt1C, 0);
        int arc1 = calculateArcLength(p1pR, p1pC, p1pt1R, p1pt1C, 0);

        int p2pt2R = pt2R - obsCircleR;
        int p2pt2C = pt2C - obsCircleC;
        int p2pR = targetR - obsCircleR;
        int p2pC = targetC - obsCircleC;

        int alpha2 = (int)calculateArcAngle(p2pt2R, p2pt2C, p2pR, p2pC, 0);
        int arc2 = calculateArcLength(p2pt2R, p2pt2C, p2pR, p2pC, 0);

        int totalLength = arc1 + arc2 + l;

        TrajectoryResult res = trajectoryResult(new int[]{pt1R, pt1C}, new int[]{pt2R, pt2C}, new int[]{robotCircleR, robotCircleC}, new int[]{obsCircleR, obsCircleC}, alpha1, alpha2, totalLength);

        return res;
    }


    // calculate lsr type path.
    public TrajectoryResult calculateLSR(int robotR, int robotC,
                                         int targetR, int targetC,
                                         int robotCircleR, int robotCircleC,
                                         int obsCircleR, int obsCircleC) {

        int d = calculateEuclideanDistance(robotCircleR, robotCircleC, obsCircleR, obsCircleC);
        int l = (int)Math.sqrt(Math.pow(d, 2) - 4*Math.pow(r, 2));

        int theta = (int)Math.acos(2*r/d); // return a value from 0 to pi

        // the vector from p1 to p2
        double v1R = obsCircleR - robotCircleR;
        double v1C = obsCircleC - robotCircleC;

        // rotation of v1 by angle theta
        // if rsl check if turning right
        double v2R;
        double v2C;
        v2R = (v1R * Math.cos(theta) + v1C * Math.sin(theta));
        v2C = (- v1R * Math.sin(theta) + v1C * Math.cos(theta));

        // point pt1
        double pt1R = obsCircleR + (r/d) * v2R;
        double pt1C = obsCircleC + (r/d) * v2C;


        // reversing the direction of v2 to get v3.
        double v3R = -v2R;
        double v3C = -v2C;

        // point pt2
        double pt2R = obsCircleR + r/d * v3R;
        double pt2C = obsCircleC + r/d * v3C;

        // calculate the 2 arcs
        int p1pR = robotR - robotCircleR;
        int p1pC = robotC - robotCircleC;
        int p1pt1R = (int)pt1R - robotCircleR;
        int p1pt1C = (int)pt1C - robotCircleC;

        int alpha1 = (int)calculateArcAngle(p1pR, p1pC, p1pt1R, p1pt1C, 0);
        int arc1 = calculateArcLength(p1pR, p1pC, p1pt1R, p1pt1C, 0);

        int p2pt2R = (int)pt2R - obsCircleR;
        int p2pt2C = (int)pt2C - obsCircleC;
        int p2pR = targetR - obsCircleR;
        int p2pC = targetC - obsCircleC;

        int alpha2 = (int)calculateArcAngle(p2pt2R, p2pt2C, p2pR, p2pC, 1);
        int arc2 = calculateArcLength(p2pt2R, p2pt2C, p2pR, p2pC, 1);

        int totalLength = arc1 + arc2 + l;

        TrajectoryResult res = trajectoryResult(new int[]{(int)pt1R, (int)pt1C}, new int[]{(int)pt2R, (int)pt2C}, new int[]{robotCircleR, robotCircleC}, new int[]{obsCircleR, obsCircleC}, alpha1, alpha2, totalLength);

        return res;

    }

    // calculate rsl type path.
    public TrajectoryResult calculateRSL(int robotR, int robotC,
                                         int targetR, int targetC,
                                         int robotCircleR, int robotCircleC,
                                         int obsCircleR, int obsCircleC) {

        int d = calculateEuclideanDistance(robotCircleR, robotCircleC, obsCircleR, obsCircleC);
        int l = (int)Math.sqrt(Math.pow(d, 2) - 4*Math.pow(r, 2));

        int theta = (int)Math.acos(2*r/d); // return a value from 0 to pi

        // the vector from p1 to p2
        double v1R = obsCircleR - robotCircleR;
        double v1C = obsCircleC - robotCircleC;

        // rotation of v1 by angle theta
        // if rsl check if turning right
        double v2R;
        double v2C;
        v2R = (v1R * Math.cos(theta) - v1C * Math.sin(theta));
        v2C = (v1R * Math.sin(theta) + v1C * Math.cos(theta));

        // point pt1
        double pt1R = obsCircleR + (r/d) * v2R;
        double pt1C = obsCircleC + (r/d) * v2C;


        // reversing the direction of v2 to get v3.
        double v3R = -v2R;
        double v3C = -v2C;

        // point pt2
        double pt2R = obsCircleR + r/d * v3R;
        double pt2C = obsCircleC + r/d * v3C;

        // calculate the 2 arcs
        int p1pR = robotR - robotCircleR;
        int p1pC = robotC - robotCircleC;
        int p1pt1R = (int)pt1R - robotCircleR;
        int p1pt1C = (int)pt1C - robotCircleC;

        int alpha1 = (int)calculateArcAngle(p1pR, p1pC, p1pt1R, p1pt1C, 1);
        int arc1 = calculateArcLength(p1pR, p1pC, p1pt1R, p1pt1C, 1);

        int p2pt2R = (int)pt2R - obsCircleR;
        int p2pt2C = (int)pt2C - obsCircleC;
        int p2pR = targetR - obsCircleR;
        int p2pC = targetC - obsCircleC;

        int alpha2 = (int)calculateArcAngle(p2pt2R, p2pt2C, p2pR, p2pC, 0);
        int arc2 = calculateArcLength(p2pt2R, p2pt2C, p2pR, p2pC, 0);

        int totalLength = arc1 + arc2 + l;

        TrajectoryResult res = trajectoryResult(new int[]{(int)pt1R, (int)pt1C}, new int[]{(int)pt2R, (int)pt2C}, new int[]{robotCircleR, robotCircleC}, new int[]{obsCircleR, obsCircleC}, alpha1, alpha2, totalLength);

        return res;

    }


    public TrajectoryResult trajectoryResult(int[] pt1, int[] pt2, int[] circle1, int[] circle2, int theta1, int theta2, int length) {
        // pt1x, pt1y theta1, circle1, r, pt2x, pt2y, theta2, circle2
        return new TrajectoryResult(pt1, pt2, circle1, circle2, theta1, theta2, length);
    }

}
