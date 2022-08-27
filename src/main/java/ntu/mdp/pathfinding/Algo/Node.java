package ntu.mdp.pathfinding.Algo;

public class Node {
    private boolean isRealBlock, isVirtualBlock;

    public Node() {
        isRealBlock = false;
        isVirtualBlock = false;
    }

    public boolean isRealBlock() {
        return isRealBlock;
    }

    public boolean isVirtualBlock() {
        return isVirtualBlock;
    }

    public void setRealBlock(boolean realBlock) {
        isRealBlock = realBlock;
    }

    public void setVirtualBlock(boolean virtualBlock) {
        isVirtualBlock = virtualBlock;
    }

    public boolean isBlocked() {
        return isRealBlock || isVirtualBlock;
    }
}
