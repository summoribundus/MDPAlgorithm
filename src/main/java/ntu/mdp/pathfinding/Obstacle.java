package ntu.mdp.pathfinding;

public class Obstacle {
    private int r, c;
    private int targetedR, targetedC;
    private int dir;

    private int imgIdx;

    private int obstacleID;

    public static int[][] imgDirToTargetMapping = new int[][]{{0, -5}, {-5, 0}, {0, 6}, {6, 0}};
    private static int[] imgDirToTargetDegree = new int[]{0, 270, 180, 90};

    // 10 based r and c
    public Obstacle(int r, int c, int dir, int obstacleID, int imgIdx, boolean isFakeObstacle) {
        this.r = r;
        this.c = c;
        this.dir = dir;
        this.obstacleID = obstacleID;
        this.imgIdx = imgIdx;
        this.targetedR = !isFakeObstacle ? r + imgDirToTargetMapping[dir][0] : r;
        this.targetedC = !isFakeObstacle ? c + imgDirToTargetMapping[dir][1] : c;
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

    public int getTargetedDegree() {
        return imgDirToTargetDegree[dir];
    }

    public int getDir() {
        return dir;
    }

    public int getImgIdx() {
        return imgIdx;
    }

    public int getObstacleID() {return obstacleID;}

    @Override
    public String toString() {
        return "id = " + obstacleID + ", " + r + " " + c;
    }
}
