package ntu.mdp.pathfinding;

public class Obstacle {
    private int r, c;
    private int dir;

    private int imgIdx;

    public Obstacle(int r, int c, int dir, int imgIdx) {
        this.r = r;
        this.c = c;
        this.dir = dir;
        this.imgIdx = imgIdx;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public int getDir() {
        return dir;
    }

    public int getImgIdx() {
        return imgIdx;
    }
}
