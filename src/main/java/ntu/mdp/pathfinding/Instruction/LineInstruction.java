package ntu.mdp.pathfinding.Instruction;

import ntu.mdp.pathfinding.Algo.CarMoveInstructions;
import ntu.mdp.pathfinding.CarMoveFlag;
import ntu.mdp.pathfinding.Point;

import java.util.List;

public class LineInstruction implements Instruction {
    private String command;
    private String gridPath;

    public LineInstruction(int moveLength) {
        command = "STM:" + (moveLength > 0 ? CarMoveInstructions.moveForwardPrefix : CarMoveInstructions.moveBackwardPrefix) + conversionToInstructionFormat(Math.abs(moveLength));
    }

    public LineInstruction(int moveLength, CarMoveFlag moveDir) {
        command = "STM:" + (moveDir == CarMoveFlag.MoveForward ? CarMoveInstructions.moveForwardPrefix : CarMoveInstructions.moveBackwardPrefix) + conversionToInstructionFormat(Math.abs(moveLength));
    }
    private String conversionToInstructionFormat(int moveLength){
        String moveLen = Integer.toString(moveLength * 5); // one grid is 5 cm
        int toPrepend = 3 - moveLen.length();
        StringBuilder sb = new StringBuilder();
        while (toPrepend-- > 0)
            sb.append('0');
        sb.append(moveLen);
        return sb.toString();
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
