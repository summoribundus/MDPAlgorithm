package ntu.mdp.pathfinding.Instruction;

import ntu.mdp.pathfinding.Algo.CarMoveInstructions;
import ntu.mdp.pathfinding.Point;

import java.util.List;

public class CurveInstruction implements Instruction {
    private String command;
    private String gridPath;

    public CurveInstruction(int theta, boolean isClockwise) {
        command = "STM:" + (isClockwise? CarMoveInstructions.turnRightPrefix : CarMoveInstructions.turnLeftPrefix) + conversionToInstructionFormatTheta(theta);
    }

    private String conversionToInstructionFormatTheta(int theta){
        if (theta / 100 ==0)
            return "0" + Integer.toString(theta);
        return Integer.toString(theta);
    }

    public void setGridPath(List<Point> gridPath) {
        StringBuilder sb = new StringBuilder().append("Android:");
//        System.out.println("the command of this is:" + this.command);
//        for (Point p : gridPath)
//            System.out.println("point in grid path is " + p.toString());
        for (Point point : gridPath) {
            sb.append(point).append(";");
        }
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
