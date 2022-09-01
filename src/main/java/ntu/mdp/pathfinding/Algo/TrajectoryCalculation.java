package ntu.mdp.pathfinding.Algo;

import java.util.Arrays;
import java.util.HashMap;

/***
 * this is the trajectory calculation from one obstacle to another - for simulation
 */
public class TrajectoryCalculation {
    // relative Obstacle direction mapping
    final int[] relativeObsDir0 = new int[] {3, 0, 1, 2};
    final int[] relativeObsDir90 = new int[] {0, 1, 2, 3};
    final int[] relativeObsDir180 = new int[] {1, 2, 3, 0};
    final int[] relativeObsDir270 = new int[] {2, 3, 0, 1};


    // the real coordinates of the obstacle
    private final int targetC;
    private final int targetR;
    private final int obstacleDir;

    // the real coordinates of the robot
    private final int robotC;
    private final int robotR;
    private final double robotTheta;


//    public static void main (String[] args) {
//        TrajectoryCalculation traj = new TrajectoryCalculation(18, 10, 2, 8, 9, 270);
//        CurveTrajectoryResult res = (CurveTrajectoryResult) traj.trajectoryResult();
//        System.out.println("pt1: " + Arrays.toString(res.getPt1()));
//        System.out.println("pt2: " + Arrays.toString(res.getPt2()));
//        System.out.println("circle1: " + Arrays.toString(res.getCircle1()));
//        System.out.println("circle2: " + Arrays.toString(res.getCircle2()));
//        System.out.println("circle3: " + Arrays.toString(res.getCircle3()));
//        System.out.println("carMove: " + res.getCarMove());
//        System.out.println("isClockwiseTurn1: " + res.isClockwiseTurn1());
//        System.out.println("isClockwiseTurn2: " + res.isClockwiseTurn2());
//        System.out.println("isClockwiseTurn3: " + res.isClockwiseTurn3());
//    }


    public TrajectoryCalculation(int obsC, int obsR, int obsDir, int robotC, int robotR, int robotTheta){
        this.targetC = obsC;
        this.targetR = obsR;
        this.obstacleDir = obsDir; // of value [0, 1, 2, 3]
        this.robotC = robotC;
        this.robotR = robotR;
        this.robotTheta = robotTheta;
    }

    // calculate the obstacle circle center
    private int[] calculateObstacleCircleCenter(int targetC, int targetR, int robotC, int robotR, int obstacleDir){
        // turning right
        if (targetC > robotC) {
            if (obstacleDir == 1 || obstacleDir == 3)
                return new int[]{targetC - AlgoConstant.R, targetR};
            else if (targetR > robotR) // lower right
                return new int[]{targetC, targetR - AlgoConstant.R};
            else
                return new int[]{targetC, targetR + AlgoConstant.R};
        }
        // turning left
        else {
            if (obstacleDir == 1 || obstacleDir == 3)
                return new int[]{targetC + AlgoConstant.R, targetR};
            else if (targetR > robotR) // lower left
                return new int[]{targetC, targetR - AlgoConstant.R};
            else
                return new int[]{targetC, targetR + AlgoConstant.R};
        }
    }

    private int[] calculateRobotCenterOfCircle(int robotC, int robotR, int robotTheta, int quadrant){

        if (robotTheta == 0){
            if (quadrant == 1 || quadrant == 4)
                return new int[] {robotC, robotR + AlgoConstant.R};
            else
                return new int[] {robotC, robotR - AlgoConstant.R};
        }
        else if (robotTheta == 90){
            if (quadrant == 1 || quadrant == 4)
                return new int[] {robotC + AlgoConstant.R, robotR};
            else
                return new int[] {robotC - AlgoConstant.R, robotR};
        }
        else if (robotTheta == 180){
            if (quadrant == 1 || quadrant == 4)
                return new int[] {robotC, robotR - AlgoConstant.R};
            else
                return new int[] {robotC, robotR + AlgoConstant.R};
        }
        else // robotTheta = 270
        {
            if (quadrant == 1 || quadrant == 4)
                return new int[] {robotC - AlgoConstant.R, robotR};
            else
                return new int[] {robotC + AlgoConstant.R, robotR};
        }
    }

    private int checkRelativePosition(int robotC, int robotR, int robotTheta, int targetC, int targetR) {
        // robot theta 0, facing left
        if (robotTheta == 0) {
            if (targetC >= robotC && targetR >= robotR)
                return 1;
            else if (targetC >= robotC && targetR <= robotR)
                return 2;
            else if (targetC <= robotC && targetR <= robotR)
                return 3;
            else
                return 4;
        }

        // robot theta 90, facing north
        else if (robotTheta == 90) {
            if (targetC >= robotC && targetR <= robotR)
                return 1;
            else if (targetC <= robotC && targetR <= robotR)
                return 2;
            else if (targetC <= robotC && targetR >= robotR)
                return 3;
            else
                return 4;
        }

        // robot theta 180, facing west
        else if (robotTheta == 180) {
            if (targetC <= robotC && targetR <= robotR)
                return 1;
            if (targetC <= robotC && targetR >= robotR)
                return 2;
            if (targetC >= robotC && targetR >= robotR)
                return 3;
            else
                return 4;
        }

        // robot theta 270, facing south
        else {
            System.out.println("theta = 270, calculating...");
            if (targetC <= robotC && targetR >= robotR)
                return 1;
            if (targetC >= robotC && targetR >= robotR)
                return 2;
            if (targetC >= robotC && targetR <= robotR) {
                return 3;
            }
            else
                return 4;
        }
    }

    // function to calculate euclidean distance of 2 functions
    private double calculateEuclideanDistance(double c1, double r1, double c2, double r2){
        double deltaC = Math.abs(c1-c2);
        double deltaR = Math.abs(r1-r2);
        return Math.sqrt(Math.pow(deltaC, 2) + Math.pow(deltaR, 2));
    }

    // arc angle computation
    private double calculateArcAngle(double startC, double startR, double destC, double destR, int dir) {
        double alpha =Math.abs(Math.atan2(destR, destC) - Math.atan2(startR, startC));

        if (alpha < 0 && dir == 0)
            alpha = alpha - 2*Math.PI;
        else if (alpha > 0 && dir == 1)
            alpha = alpha - 2*Math.PI;

        return Math.abs(alpha);
    }

    // arc length computation
    private int calculateArcLength(double startC, double startR, double destC, double destR, int dir){
        // dir = 0 for turn left, direct = 1 for turn right
        // tan_x and tan_y are the coordinates for the starting point on the arc,
        // while destC and destR are the coordinates for the destination point on the arc
        double alpha = calculateArcAngle(startC, startR, destC, destR, dir);

        return (int)Math.abs(alpha * AlgoConstant.R);
    }

    private int selectObsRelativeDir(int obstacleDir, int robotTheta){
        if (robotTheta == 0)
            return relativeObsDir0[obstacleDir];
        else if (robotTheta == 90)
            return relativeObsDir90[obstacleDir];
        else if (robotTheta == 180)
            return relativeObsDir180[obstacleDir];
        else
            return relativeObsDir270[obstacleDir];
    }

    // choose the path according to the relative position of
    public TrajectoryResult trajectoryResult() {

        int quadrant = checkRelativePosition(robotC, robotR, (int)robotTheta, targetC, targetR);

        int[] centerOfCircle = calculateRobotCenterOfCircle(robotC, robotR, (int)robotTheta, quadrant); //calculate the center of the robot turning trajectory
        int robotCircleR = centerOfCircle[0];
        int robotCircleC =centerOfCircle[1];

        int[] obstacleCircle = calculateObstacleCircleCenter(targetC, targetR, robotC, robotR, obstacleDir);
        int obsCircleR = obstacleCircle[0];
        int obsCircleC = obstacleCircle[1];

        int relativeObsDir = selectObsRelativeDir(obstacleDir, (int)robotTheta);

        //if (borderClash(robotCircleC, robotCircleR, obsCircleC, obsCircleR))
            // null;
        double centerDist = calculateEuclideanDistance(obsCircleC, obsCircleR, robotCircleC, robotCircleR);
        boolean isSmallerThan4R = (centerDist <= 4*AlgoConstant.R)? true: false;
//
//        if (borderClash(robotCircleC, robotCircleR, obsCircleC, obsCircleR))
//            return null;

        if (isSmallerThan4R) {
            if (quadrant == 1){
                if (relativeObsDir == 3)
                    return calculateCurveRLR(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, true);
                else{
                    return calculateCurveRLR(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, false);
                }
            } else if (quadrant == 4){
                if (relativeObsDir == 0)
                    return calculateCurveRLR(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, true);
                else{
                    return calculateCurveRLR(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, false);
                }
            } else if (quadrant == 2) {
                System.out.println("relative obstacle direction: " + relativeObsDir);
                if (relativeObsDir == 3)
                    return calculateCurveLRL(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, true);
                else
                    return calculateCurveLRL(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, false);

            } else {
                if (relativeObsDir == 2)
                    return calculateCurveLRL(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, true);
                else
                    return calculateCurveLRL(robotC, robotR, targetC, targetR, robotCircleR,
                            robotCircleC, obsCircleR, obsCircleC, false);
            }

        }


        if (quadrant == 1) // upper right
        {
            if (relativeObsDir == 1 || relativeObsDir == 0) {
                //rsr
                return calculateRSR(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
            else // (relativeObsDir == 3 || relativeObsDir == 2)
            {
                //rsl
                return calculateRSL(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
        }

        if (quadrant == 2) // upper left
        {
            if (relativeObsDir == 1 || relativeObsDir == 2) {
                //lsl
                return calculateLSL(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
            else {
                //lsr
                return calculateLSR(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
        }

        if (quadrant == 3) // lower left
        {
            if (relativeObsDir == 1 || relativeObsDir == 0) {
                //lsl
                return calculateLSL(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
            else {
                //lsr
                return calculateLSR(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
        }

        else { // lower right
            if (relativeObsDir == 1 || relativeObsDir == 2 ) {
                //rsr
                return calculateRSR(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
            else {
                //rsl
                return calculateRSL(robotC, robotR, targetC, targetR, robotCircleR,
                        robotCircleC, obsCircleR, obsCircleC);
            }
        }
    }

    // calculate rsr type path.
    private TrajectoryResult calculateRSR(int robotC, int robotR,
                                         int targetC, int targetR,
                                         int robotCircleC, int robotCircleR,
                                         int obsCircleC, int obsCircleR){
        System.out.println("calculateRSR is called.");
        double l = calculateEuclideanDistance(obsCircleC, obsCircleR, robotCircleC, robotCircleR);
        // calculate v1 which points from center p1 to center p2
        double v1C = obsCircleC - robotCircleC;
        double v1R = obsCircleR - robotCircleR;
        // v2 is rotated by pi/2
        double v2C = v1R;
        double v2R = - v1C;

        // get the intermidiate pt1 position
        double pt1C = robotCircleC + (v2C * AlgoConstant.R/l);
        double pt1R = robotCircleR + (v2R * AlgoConstant.R/l);

        // get the intermidiate pt2 position
        double pt2C = pt1C + v1C;
        double pt2R = pt1R + v1R;

        // calculate the 2 arcs
        double p1pC = robotC - robotCircleC;
        double p1pR = robotR - robotCircleR;
        double p1pt1C = pt1C - robotCircleC;
        double p1pt1R = pt1R - robotCircleR;

//        double alpha1 = calculateArcAngle(p1pC, p1pR, p1pt1C, p1pt1R, 1);


        double alpha1 = Math.abs(Math.atan2(p1pt1R, p1pt1C) - Math.atan2(p1pR, p1pC));

        double p2pt2C = pt2C - obsCircleC;
        double p2pt2R = pt2R - obsCircleR;
        double p2pC = targetC - obsCircleC;
        double p2pR = targetR - obsCircleR;

//        double alpha2 = calculateArcAngle(p2pt2C, p2pt2R, p2pC, p2pR, 1);
        double alpha2 = Math.abs(Math.atan2(p2pR, p2pC) - Math.atan2(p2pt2R, p2pt2C));

        Curve startCurve = new Curve(new int[]{robotC, robotR}, new int[] {(int)pt1C, (int)pt1R}, new int[] {robotCircleC, robotCircleR}, (int) alpha1, true);
        Line line = new Line(new int[] {(int)pt1C, (int)pt1R}, new int[]{(int) pt2C, (int) pt2R});
        Curve endCurve = new Curve(new int[]{(int) pt2C, (int) pt2R}, new int[] {targetC, targetR}, new int[] {obsCircleC, obsCircleR}, (int) alpha2, true);

        TrajectoryResult res = new TrajectoryResult(startCurve, line, endCurve);

        return res;
    }

    // calculate lsl type path.
    private TrajectoryResult calculateLSL(int robotC, int robotR,
                                         int targetC, int targetR,
                                         int robotCircleC, int robotCircleR,
                                         int obsCircleC, int obsCircleR){
        System.out.println("calculateLSL is called.");
        double l = calculateEuclideanDistance(obsCircleC, obsCircleR, robotCircleC, robotCircleR);
        // calculate v1 which points from center p1 to center p2
        double v1C = obsCircleC - robotCircleC;
        double v1R = obsCircleR - robotCircleR;
        // v2 is rotated by pi/2
        double v2C = -v1R;
        double v2R = v1C;

        // get the intermidiate pt1 position
        double pt1C = robotCircleC + (v2C * AlgoConstant.R/l);
        double pt1R = robotCircleR + (v2R * AlgoConstant.R/l);

        // get the intermidiate pt2 position
        double pt2C = pt1C + v1C;
        double pt2R = pt1R + v1R;

        // calculate the 2 arcs
        double p1pC = robotC - robotCircleC;
        double p1pR = robotR - robotCircleR;
        double p1pt1C = pt1C - robotCircleC;
        double p1pt1R = pt1R - robotCircleR;

//        double alpha1 = calculateArcAngle(p1pC, p1pR, p1pt1C, p1pt1R, 0);
//        double arc1 = calculateArcLength(p1pC, p1pR, p1pt1C, p1pt1R, 0);
        double alpha1 = Math.abs(Math.atan2(p1pt1R, p1pt1C) - Math.atan2(p1pR, p1pC));

        double p2pt2C = pt2C - obsCircleC;
        double p2pt2R = pt2R - obsCircleR;
        double p2pC = targetC - obsCircleC;
        double p2pR = targetR - obsCircleR;

//        double alpha2 = calculateArcAngle(p2pt2C, p2pt2R, p2pC, p2pR, 0);
//        double arc2 = calculateArcLength(p2pt2C, p2pt2R, p2pC, p2pR, 0);
        double alpha2 = Math.abs(Math.atan2(p2pR, p2pC) - Math.atan2(p2pt2R, p2pt2C));

        Curve startCurve = new Curve(new int[]{robotC, robotR}, new int[] {(int)pt1C, (int)pt1R}, new int[] {robotCircleC, robotCircleR}, (int) alpha1, false);
        Line line = new Line(new int[] {(int)pt1C, (int)pt1R}, new int[]{(int) pt2C, (int) pt2R});
        Curve endCurve = new Curve(new int[]{(int) pt2C, (int) pt2R}, new int[] {targetC, targetR}, new int[] {obsCircleC, obsCircleR}, (int) alpha2, false);

        TrajectoryResult res = new TrajectoryResult(startCurve, line, endCurve);

        return res;
    }


    // calculate lsr type path.
    private TrajectoryResult calculateLSR(int robotC, int robotR,
                                         int targetC, int targetR,
                                         int robotCircleC, int robotCircleR,
                                         int obsCircleC, int obsCircleR) {
        double d = calculateEuclideanDistance(robotCircleC, robotCircleR, obsCircleC, obsCircleR);
        double l = Math.sqrt(Math.pow(d, 2) - 4*Math.pow(AlgoConstant.R, 2));

        double theta_radius = Math.acos(2*AlgoConstant.R/d); // return a value from 0 to pi



        // the vector from p1 to p2
        double v1C = obsCircleC - robotCircleC;
        double v1R = obsCircleR - robotCircleR;

        // rotation of v1 by angle theta
        // if rsl check if turning right
        double v2C;
        double v2R;
        v2C = (v1C * Math.cos(theta_radius) - v1R * Math.sin(theta_radius));
        v2R = (v1C * Math.sin(theta_radius) + v1R * Math.cos(theta_radius));


        // point pt1
        double pt1C = robotCircleC + (AlgoConstant.R/d) * v2C;
        double pt1R = robotCircleR + (AlgoConstant.R/d) * v2R;

        // reversing the direction of v2 to get v3.
        double v3C = -v2C;
        double v3R = -v2R;

        // point pt2
        double pt2C = obsCircleC + AlgoConstant.R/d * v3C;
        double pt2R = obsCircleR + AlgoConstant.R/d * v3R;

        // calculate the 2 arcs
        double p1pC = robotC - robotCircleC;
        double p1pR = robotR - robotCircleR;
        double p1pt1C = pt1C - robotCircleC;
        double p1pt1R = pt1R - robotCircleR;

//        double alpha1 = calculateArcAngle(p1pC, p1pR, p1pt1C, p1pt1R, 0);
//        double arc1 = calculateArcLength(p1pC, p1pR, p1pt1C, p1pt1R, 0);
        double alpha1 = Math.abs(Math.atan2(p1pt1R, p1pt1C) - Math.atan2(p1pR, p1pC));



        double p2pt2C = pt2C - obsCircleC;
        double p2pt2R = pt2R - obsCircleR;
        double p2pC = targetC - obsCircleC;
        double p2pR = targetR - obsCircleR;

//        double alpha2 = calculateArcAngle(p2pt2C, p2pt2R, p2pC, p2pR, 1);
//        double arc2 = calculateArcLength(p2pt2C, p2pt2R, p2pC, p2pR, 1);
        double alpha2 = Math.abs(Math.atan2(p2pR, p2pC) - Math.atan2(p2pt2R, p2pt2C));


        Curve startCurve = new Curve(new int[]{robotC, robotR}, new int[] {(int)pt1C, (int)pt1R}, new int[] {robotCircleC, robotCircleR}, (int) alpha1, false);
        Line line = new Line(new int[] {(int)pt1C, (int)pt1R}, new int[]{(int) pt2C, (int) pt2R});
        Curve endCurve = new Curve(new int[]{(int) pt2C, (int) pt2R}, new int[] {targetC, targetR}, new int[] {obsCircleC, obsCircleR}, (int) alpha2, true);

        TrajectoryResult res = new TrajectoryResult(startCurve, line, endCurve);

        return res;

    }

    // calculate rsl type path.
    private TrajectoryResult calculateRSL(int robotC, int robotR,
                                         int targetC, int targetR,
                                         int robotCircleC, int robotCircleR,
                                         int obsCircleC, int obsCircleR) {
        double d = calculateEuclideanDistance(robotCircleC, robotCircleR, obsCircleC, obsCircleR);
        double l = Math.sqrt(Math.pow(d, 2) - 4*Math.pow(AlgoConstant.R, 2));

        double theta = Math.acos(2*AlgoConstant.R/d); // return a value from 0 to pi

        // the vector from p1 to p2
        double v1C = obsCircleC - robotCircleC;
        double v1R = obsCircleR - robotCircleR;

        // rotation of v1 by angle theta
        // if rsl check if turning right
        double v2C;
        double v2R;
        v2C = (v1C * Math.cos(theta) + v1R * Math.sin(theta));
        v2R = ( - v1C * Math.sin(theta) + v1R * Math.cos(theta));

        // point pt1
        double pt1C = robotCircleC + (AlgoConstant.R/d) * v2C;
        double pt1R = robotCircleR + (AlgoConstant.R/d) * v2R;


        // reversing the direction of v2 to get v3.
        double v3C = -v2C;
        double v3R = -v2R;

        // point pt2
        double pt2C = obsCircleC + AlgoConstant.R/d * v3C;
        double pt2R = obsCircleR + AlgoConstant.R/d * v3R;

        // calculate the 2 arcs
        double p1pC = robotC - robotCircleC;
        double p1pR = robotR - robotCircleR;
        double p1pt1C = pt1C - robotCircleC;
        double p1pt1R = pt1R - robotCircleR;

        double alpha1 = Math.abs(Math.atan2(p1pt1R, p1pt1C) - Math.atan2(p1pR, p1pC));

        double p2pt2C = pt2C - obsCircleC;
        double p2pt2R = pt2R - obsCircleR;
        double p2pC = targetC - obsCircleC;
        double p2pR = targetR - obsCircleR;

        double alpha2 = Math.abs(Math.atan2(p2pR, p2pC) - Math.atan2(p2pt2R, p2pt2C));


        Curve startCurve = new Curve(new int[]{robotC, robotR}, new int[] {(int)pt1C, (int)pt1R}, new int[] {robotCircleC, robotCircleR}, (int) alpha1, true);
        Line line = new Line(new int[] {(int)pt1C, (int)pt1R}, new int[]{(int) pt2C, (int) pt2R});
        Curve endCurve = new Curve(new int[]{(int) pt2C, (int) pt2R}, new int[] {targetC, targetR}, new int[] {obsCircleC, obsCircleR}, (int) alpha2, false);

        TrajectoryResult res = new TrajectoryResult(startCurve, line, endCurve);
        return res;

    }

    private TrajectoryResult calculateCurveRLR(int robotC, int robotR,
                                                    int targetC, int targetR,
                                                    int robotCircleC, int robotCircleR,
                                                    int obsCircleC, int obsCircleR,
                                                    boolean isBigCurve
                                                    ){
        System.out.println("now calling RLR curve...");

        // distance between the centres of the two turning circles p1 and p2
        double d = calculateEuclideanDistance(robotCircleC, robotCircleR, obsCircleC, obsCircleR);

        // the middle point between p1 and p2 is point q
        double qC = (robotCircleC + obsCircleC)/2;
        double qR = (robotCircleR + obsCircleR)/2;

        // the vector from p1 to p2 is vector v1
        double v1C = obsCircleC - robotCircleC;
        double v1R = obsCircleR - robotCircleR;

        double v2C, v2R;

        if (!isBigCurve) {
            // the vector v2, obtained by rotate v1 by pi/2 CLOCKWISE
            v2C = v1R;
            v2R = -v1C;
        } else {
            v2C = -v1R;
            v2R = v1C;
        }

        // the distance from p3 to q is:
        double d1 = Math.sqrt(4*Math.pow(AlgoConstant.R, 2) - 0.25*Math.pow(d, 2));

        // the point p3 is (0, 0) to the mid-point q plus d1*
        double p3C = qC + d1/d*v2C;
        double p3R = qR + d1/d*v2R;

        // pt1 is the midpoint of p1p3
        double pt1C = (robotCircleC + p3C)/2;
        double pt1R = (robotCircleR + p3R)/2;

        // pt2 is the midpoint of the p2p3
        double pt2C = (obsCircleC + p3C)/2;
        double pt2R = (obsCircleR + p3R)/2;

        // calculate the angles and arcs
        // arc 1:
        double p1pC = robotC - robotCircleC;
        double p1pR = robotR - robotCircleR;
        double p1pt1C = pt1C - robotCircleC;
        double p1pt1R = pt1R - robotCircleR;

        double alpha1 = Math.abs(Math.atan2(p1pt1R, p1pt1C) - Math.atan2(p1pR, p1pC));

        // arc 2:
        double p3pt2C = pt2C - p3C;
        double p3pt2R = pt2R - p3R;
        double p3pt1C = pt1C - p3C;
        double p3pt1R = pt1R - p3R;

        double alpha2 = Math.abs(Math.atan2(p3pt2R, p3pt2C) - Math.atan2(p3pt1R, p3pt1C));

        // arc 3:
        double p2pt2C = pt2C - obsCircleC;
        double p2pt2R = pt2R - obsCircleR;
        double p2pC = targetC - obsCircleC;
        double p2pR = targetR - obsCircleR;

        double alpha3 = Math.abs(Math.atan2(p2pR, p2pC) - Math.atan2(p2pt2R, p2pt2C));


        Curve startCurve = new Curve(new int[]{robotC, robotR}, new int[] {(int)pt1C, (int)pt1R}, new int[] {robotCircleC, robotCircleR}, (int) alpha1, true);
        Curve intermediateCurve = new Curve(new int[] {(int)pt1C, (int)pt1R}, new int[]{(int) pt2C, (int) pt2R}, new int[] {(int)p3C, (int)p3R}, (int)alpha2, false);
        Curve endCurve = new Curve(new int[]{(int) pt2C, (int) pt2R}, new int[] {targetC, targetR}, new int[] {obsCircleC, obsCircleR}, (int) alpha3, true);

        TrajectoryResult res = new TrajectoryResult(startCurve, intermediateCurve, endCurve);

        return res;

    }


    private TrajectoryResult calculateCurveLRL(int robotC, int robotR,
                                                    int targetC, int targetR,
                                                    int robotCircleC, int robotCircleR,
                                                    int obsCircleC, int obsCircleR,
                                                    boolean isBigCurve
    ){

        System.out.println("now calling LRL curve...");

        // distance between the centres of the two turning circles p1 and p2
        double d = calculateEuclideanDistance(robotCircleC, robotCircleR, obsCircleC, obsCircleR);

        // the middle point between p1 and p2 is point q
        double qC = (robotCircleC + obsCircleC)/2;
        double qR = (robotCircleR + obsCircleR)/2;

        // the vector from p1 to p2 is vector v1
        double v1C = obsCircleC - robotCircleC;
        double v1R = obsCircleR - robotCircleR;
        System.out.println("================== v1 calculation ================== ");
        System.out.println("v1C: " + v1C);
        System.out.println("v1R: " + v1R);

        double v2C, v2R;

        if (!isBigCurve) {
            // the vector v2, obtained by rotate v1 by pi/2 CLOCKWISE
            v2C = -v1R;
            v2R = v1C;
        } else {
            v2C = v1R;
            v2R = -v1C;
        }

        System.out.println("v2C: " + v2C);
        System.out.println("v2R: " + v2R);

        // the distance from p3 to q is:
        double d1 = Math.sqrt(4*Math.pow(AlgoConstant.R, 2) - 0.25*Math.pow(d, 2));

        // the point p3 is (0, 0) to the mid-point q plus d1*
        double p3C = qC + d1/d*v2C;
        double p3R = qR + d1/d*v2R;
        System.out.println("================== p3 calculation ================== ");
        System.out.println("d1/d*v2C: " + d1/d*v2C);
        System.out.println("d1/d*v2R: " + d1/d*v2R);

        // pt1 is the midpoint of p1p3
        double pt1C = (robotCircleC + p3C)/2;
        double pt1R = (robotCircleR + p3R)/2;

        // pt2 is the midpoint of the p2p3
        double pt2C = (obsCircleC + p3C)/2;
        double pt2R = (obsCircleR + p3R)/2;

        // calculate the angles and arcs
        // arc 1:
        double p1pC = robotC - robotCircleC;
        double p1pR = robotR - robotCircleR;
        double p1pt1C = pt1C - robotCircleC;
        double p1pt1R = pt1R - robotCircleR;

        double alpha1 = Math.abs(Math.atan2(p1pt1R, p1pt1C) - Math.atan2(p1pR, p1pC));
        double arc1 = alpha1 * AlgoConstant.R;

        // arc 2:
        double p3pt2C = pt2C - p3C;
        double p3pt2R = pt2R - p3R;
        double p3pt1C = pt1C - p3C;
        double p3pt1R = pt1R - p3R;

        double alpha2 = Math.abs(Math.atan2(p3pt2R, p3pt2C) - Math.atan2(p3pt1R, p3pt1C));

        // arc 3:
        double p2pt2C = pt2C - obsCircleC;
        double p2pt2R = pt2R - obsCircleR;
        double p2pC = targetC - obsCircleC;
        double p2pR = targetR - obsCircleR;

        double alpha3 = Math.abs(Math.atan2(p2pR, p2pC) - Math.atan2(p2pt2R, p2pt2C));


        Curve startCurve = new Curve(new int[]{robotC, robotR}, new int[] {(int)pt1C, (int)pt1R}, new int[] {robotCircleC, robotCircleR}, (int) alpha1, true);
        Curve intermediateCurve = new Curve(new int[] {(int)pt1C, (int)pt1R}, new int[]{(int) pt2C, (int) pt2R}, new int[] {(int)p3C, (int)p3R}, (int)alpha2, false);
        Curve endCurve = new Curve(new int[]{(int) pt2C, (int) pt2R}, new int[] {targetC, targetR}, new int[] {obsCircleC, obsCircleR}, (int) alpha3, true);

        TrajectoryResult res = new TrajectoryResult(startCurve, intermediateCurve, endCurve);

        return res;

    }

    private int toDegrees(double inRadius) {
        return (int)(inRadius*360/(2*Math.PI));
    }

    // public int boundary check
    private boolean borderClash(double circle1C, double circle1R, double circle2C, double circle2R){
        if ((circle1R >= 0 && circle1C >= 0 && circle1C < AlgoConstant.GridN && circle1R <= AlgoConstant.GridM)
         && (circle2R >= 0 && circle2C >= 0 && circle2C <= AlgoConstant.GridN && circle2R <= AlgoConstant.GridM))
            return true;// no clash
        else return false;
    }

}
