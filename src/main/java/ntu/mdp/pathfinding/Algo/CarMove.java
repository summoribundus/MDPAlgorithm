package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Point;

import java.util.ArrayList;
import java.util.List;

public class CarMove {
    private int turnTheta1, turnTheta2, turnTheta3;
    private boolean isClockwise1, isClockwise2, isClockwise3;
    private int moveLength;
    private ArrayList<String> instructionList;

    private List<List<Point>> path;

    // car move type of Curve-StraightLine-Curve.
    public CarMove(int turnTheta1, boolean isClockwise1,
                   int turnTheta2, boolean isClockwise2,
                   int moveLength) {
        this.turnTheta1 = turnTheta1;
        this.isClockwise1 = isClockwise1;
        this.turnTheta2 = turnTheta2;
        this.isClockwise2 = isClockwise2;
        this.moveLength = moveLength;
        this.instructionList = new ArrayList<>();

        // adding instructions into the arraylist.
        instructionList.add((isClockwise1? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix) + turnTheta1);

        instructionList.add((moveLength >= 0)? CarMoveInstructions.moveForwardPrefix : CarMoveInstructions.moveBackwardPrefix
                + conversionToInstructionFormat(Math.abs(moveLength))); // goingStraight

        instructionList.add((isClockwise2? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix) + turnTheta2);

    }

    // car move type of Curve-Curve-CurveType.
    public CarMove(int turnTheta1, boolean isClockwise1,
                   int turnTheta2, boolean isClockwise2,
                   int turnTheta3, boolean isClockwise3){
        this.turnTheta1 = turnTheta1;
        this.isClockwise1 = isClockwise1;
        this.turnTheta2 = turnTheta2;
        this.isClockwise2 = isClockwise2;
        this.turnTheta3 = turnTheta3;
        this.isClockwise3 = isClockwise3;
        this.instructionList = new ArrayList<>();

        // adding instructions into the arraylist

        instructionList.add((isClockwise1? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix) + turnTheta1);

        instructionList.add((isClockwise2? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix) + turnTheta2);

        instructionList.add((isClockwise3? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix) + turnTheta3);

    }

    // car move type of straight line only
    public CarMove(int moveLength){

        this.instructionList = new ArrayList<>();

        instructionList.add((moveLength >= 0? CarMoveInstructions.moveForwardPrefix : CarMoveInstructions.moveBackwardPrefix)
                + conversionToInstructionFormat(Math.abs(moveLength))); // goingStraight

    }

    public CarMove(int turnTheta, boolean isClockwise) {
        this.instructionList = new ArrayList<>();
        instructionList.add(isClockwise ? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix + turnTheta);
    }

    private String conversionToInstructionFormat(int moveLength){
        int moveLenInCM = moveLength * 5; // one grid is 5 cm

        if (moveLenInCM / 100 == 0) // 2 digit number
            return "0" + moveLenInCM;
        else
            return Integer.toString(moveLenInCM);
    }

    private String conversionToInstructionFormatTheta(int theta){
        if (theta / 100 ==0)
            return "0" + Integer.toString(theta);
        return Integer.toString(theta);
    }


    @Override
    public String toString() {
        return "CarMove{" +
                "turnTheta1=" + turnTheta1 +
                ", turnTheta2=" + turnTheta2 +
                ", moveLength=" + moveLength +
                '}';
    }

    public void setPath(List<List<Point>> p){
        this.path = p;
    }

    public List<List<Point>> getPath(){
        return path;
    }

    public ArrayList<String> getInstructions(){
        return this.instructionList;
    }
    
}
