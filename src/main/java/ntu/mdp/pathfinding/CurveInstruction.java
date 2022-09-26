package ntu.mdp.pathfinding;

import ntu.mdp.pathfinding.Algo.CarMoveInstructions;

import java.util.List;

public class CurveInstruction implements Instruction {
    private String command;
    private List<Point> gridPath;

    public CurveInstruction(int theta, boolean isClockwise) {
        command = (isClockwise? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix) + conversionToInstructionFormatTheta(theta);
    }

    private String conversionToInstructionFormatTheta(int theta){
        if (theta / 100 ==0)
            return "0" + Integer.toString(theta);
        return Integer.toString(theta);
    }

    public void setGridPath(List<Point> gridPath) {
        this.gridPath = gridPath;
    }

    @Override
    public String command() {
        return command;
    }

    @Override
    public List<Point> gridPath() {
        return gridPath;
    }
}
