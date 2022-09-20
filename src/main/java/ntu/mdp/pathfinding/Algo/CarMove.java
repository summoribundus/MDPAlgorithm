package ntu.mdp.pathfinding.Algo;

import java.util.ArrayList;

public class CarMove {
    private int turnTheta1, turnTheta2, turnTheta3;
    private boolean isClockwise1, isClockwise2, isClockwise3;
    private int moveLength;
    private String[] instructionList;

    // car move type of Curve-StraightLine-Curve.
    public CarMove(int turnTheta1, boolean isClockwise1,
                   int turnTheta2, boolean isClockwise2,
                   int moveLength) {
        this.turnTheta1 = turnTheta1;
        this.isClockwise1 = isClockwise1;
        this.turnTheta2 = turnTheta2;
        this.isClockwise2 = isClockwise2;
        this.moveLength = moveLength;
        this.instructionList = new String[3];

        // adding instructions into the arraylist.

        if (isClockwise1) // first curve turning right
            instructionList[0] = CarMoveInstructions.turnRightPrefix + turnTheta1;
        else instructionList[0] = CarMoveInstructions.turnLeftPrefix + turnTheta1;

        if (moveLength > 0)
            instructionList[1] = CarMoveInstructions.moveForwardPrefix + conversionToInstructionFormat(moveLength); // goingStraight
        else instructionList[1] = CarMoveInstructions.moveBackwardPrefix + conversionToInstructionFormat(Math.abs(moveLength));

        if (isClockwise2)
            instructionList[2] = CarMoveInstructions.turnRightPrefix + turnTheta2;
        else instructionList[2] = CarMoveInstructions.turnLeftPrefix + turnTheta2;
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
        this.instructionList = new String[3];

        // adding instructions into the arraylist

        if (isClockwise1)
            instructionList[0] = CarMoveInstructions.turnRightPrefix + turnTheta1;
        else instructionList[0] = CarMoveInstructions.turnLeftPrefix + turnTheta1;

        if (isClockwise2)
            instructionList[1] = CarMoveInstructions.turnRightPrefix + turnTheta2;
        else instructionList[1] = CarMoveInstructions.turnLeftPrefix + turnTheta2;

        if (isClockwise3)
            instructionList[2] = CarMoveInstructions.turnRightPrefix + turnTheta3;
        else instructionList[2] = CarMoveInstructions.turnLeftPrefix + turnTheta3;
    }

    private String conversionToInstructionFormat(int moveLength){
        int moveLenInCM = moveLength * 5; // one grid is 5 cm

        if (moveLenInCM / 100 == 0) // 2 digit number
            return "0" + moveLenInCM;
        else
            return Integer.toString(moveLenInCM);
    }


    @Override
    public String toString() {
        return "CarMove{" +
                "turnTheta1=" + turnTheta1 +
                ", turnTheta2=" + turnTheta2 +
                ", moveLength=" + moveLength +
                '}';
    }

    public String[] getInstructions(){
        return this.instructionList;
    }
}
