package ntu.mdp.pathfinding;

public class Obstacle {
    private int r, c;
    private int targetedR, targetedC;
    private int dir;

    private int imgIdx;

    public static int[][] imgDirToTargetMapping = new int[][]{{0, -5}, {-5, 0}, {0, 6}, {6, 0}};

    // 10 based r and c
    public Obstacle(int r, int c, int dir, int imgIdx, boolean isFakeObstacle) {
        this.r = r;
        this.c = c;
        this.dir = dir;
        this.imgIdx = imgIdx;
        this.targetedR = !isFakeObstacle ? r + imgDirToTargetMapping[dir][0] : r;
        this.targetedC = !isFakeObstacle ? c + imgDirToTargetMapping[dir][1] : c;
        System.out.println(this.targetedR + " " + this.targetedC);
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public int getTargetedR() {
        return targetedR;
    }

    public int getTargetedC() {
        return targetedC;
    }

    public int getDir() {
        return dir;
    }

    public int getImgIdx() {
        return imgIdx;
    }

    @Override
    public String toString() {
        return r + " " + c;
    }
}
