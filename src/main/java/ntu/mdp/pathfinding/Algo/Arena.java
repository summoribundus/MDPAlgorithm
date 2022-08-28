package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

import java.util.Arrays;

public class Arena {
    private Node[][] arena;
    private int m, n;

    public Arena(int m, int n, Obstacle[] obstacles) {
        this.m = m;
        this.n = n;
        arena = new Node[m][n];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                arena[i][j] = new Node();

        for (Obstacle ob : obstacles) {
            int r = ob.getR(), c = ob.getC();
            arena[r][c].setRealBlock(true);
            arena[r+1][c].setRealBlock(true);
            arena[r][c+1].setRealBlock(true);
            arena[r+1][c+1].setRealBlock(true);
            setUpVirtualBlock(r, c);
        }
    }

    private void setUpVirtualBlock(int r, int c) {
        walkForVirtualBlock(r, c, -1, -1);
        walkForVirtualBlock(r+1, c, 1, -1);
        walkForVirtualBlock(r, c+1, -1, 1);
        walkForVirtualBlock(r+1, c+1,  1, 1);
    }

    private void walkForVirtualBlock(int r, int c, int rStep, int cStep) {
        int nr, nc;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                nr = i * rStep; nc = j * cStep;
                if (inRange(nr, nc))
                    arena[nr][nc].setVirtualBlock(true);
            }
    }

    // 5 based r and c
    public boolean checkWithCorrespondingBlock(int r, int c) {
        if (!inRange(r, c)) return false;
        return arena[r][c].isBlocked();
    }

    private boolean inRange(int x, int y) {
        return 0 <= x && x < m && 0 <= y && y < n;
    }
}
