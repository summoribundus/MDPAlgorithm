package ntu.mdp.pathfinding;

import java.util.List;

public interface Instruction {
    public String command();
    public List<Point> gridPath();
}
