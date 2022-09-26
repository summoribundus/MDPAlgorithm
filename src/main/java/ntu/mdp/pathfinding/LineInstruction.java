package ntu.mdp.pathfinding;

import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Algo.CarMoveInstructions;

import java.util.List;

public class LineInstruction implements Instruction {
    private String command;
    private List<Point> gridPath;

    public LineInstruction(int moveLength) {
        command = (moveLength > 0 ? CarMoveInstructions.moveForwardPrefix : CarMoveInstructions.moveBackwardPrefix) + conversionToInstructionFormat(Math.abs(moveLength));
    }

    private String conversionToInstructionFormat(int moveLength){
        int moveLenInCM = moveLength * 5; // one grid is 5 cm

        if (moveLenInCM / 100 == 0) // 2 digit number
            return "0" + moveLenInCM;
        else
            return Integer.toString(moveLenInCM);
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
