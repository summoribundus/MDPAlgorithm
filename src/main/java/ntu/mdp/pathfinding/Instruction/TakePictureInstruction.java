package ntu.mdp.pathfinding.Instruction;

public class TakePictureInstruction implements Instruction {

    private String command;
    public TakePictureInstruction() {
        command = "RPI:picture";
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
