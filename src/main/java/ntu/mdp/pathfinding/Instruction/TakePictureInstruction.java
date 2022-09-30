package ntu.mdp.pathfinding.Instruction;

public class TakePictureInstruction implements Instruction {

    private String command;
    public TakePictureInstruction(int obstacleID) {
        command = "RPI:picture-" + obstacleID;
    }
    @Override
    public String command() {
        return command;
    }

    @Override
    public String gridPath() {
        return null;
    }
}