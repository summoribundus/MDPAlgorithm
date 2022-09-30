package ntu.mdp.pathfinding.Instruction;

import ntu.mdp.pathfinding.Algo.CarMoveInstructions;
import ntu.mdp.pathfinding.Point;

import java.util.List;

public class LineInstruction implements Instruction {
    private String command;
    private String gridPath;

    public LineInstruction(int moveLength) {
        command = "STM:" + (moveLength > 0 ? CarMoveInstructions.moveForwardPrefix : CarMoveInstructions.moveBackwardPrefix) + conversionToInstructionFormat(Math.abs(moveLength));
    }

    private String conversionToInstructionFormat(int moveLength){
        int moveLenInCM = moveLength * 5; // one grid is 5 cm

        if (moveLenInCM / 100 == 0) // 2 digit number
            return "0" + moveLenInCM;
        else
            return Integer.toString(moveLenInCM);
    }

    public void setGridPath(List<Point> gridPath) {
        if (gridPath == null) return;
        StringBuilder sb = new StringBuilder().append("Android:");
        for (Point point : gridPath)
            sb.append(point).append(";");
        this.gridPath = sb.toString();
    }

    @Override
    public String command() {
        return command;
    }

    @Override
    public String gridPath() {
        return gridPath;
    }
}
