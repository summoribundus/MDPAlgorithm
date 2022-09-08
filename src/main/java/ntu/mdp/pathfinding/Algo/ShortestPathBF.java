package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.InputData;
import ntu.mdp.pathfinding.Obstacle;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ShortestPathBF {
    private final Map<Integer, Obstacle> idxMapping;
    private final int n;
    private final PriorityBlockingQueue<PathDistanceResult> pq;
    private PriorityQueue<PathDistanceResult> pqCopy;
    public ShortestPathBF(Obstacle[] obstacles, int x, int y) {
        n = obstacles.length;
        idxMapping = new HashMap<>();
        pq = new PriorityBlockingQueue<>();
        idxMapping.put(0, new Obstacle(x, y, 0, 0, true));
        for (int i = 0; i < n; i++) {
            idxMapping.put(i+1, obstacles[i]);
        }
        findPath();
    }

    public void findPath() {
        List<int[]> possiblePermutations = new ArrayList<>();
        int[] permStart = new int[n+1];
        boolean[] visited = new boolean[n+1];
        permStart[0] = 0;
        visited[0] = true;
        getPermutation(permStart, visited, 1, possiblePermutations);
        ExecutorService es = Executors.newCachedThreadPool(); // fixed pool available processor??
        for (int[] path : possiblePermutations) {
            PathDistance p = new PathDistance(path, idxMapping, pq);
            es.execute(p);
        }
        es.shutdown();
        try {
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }

    private void getPermutation(int[] perm, boolean[] marked, int d, List<int[]> result) {
        if (d >= n+1) {
            result.add(Arrays.copyOf(perm, n+1));
            return;
        }
        for (int i = 1; i <= n; i++) {
            if (marked[i]) continue;

            marked[i] = true;
            perm[d] = i;
            getPermutation(perm, marked, d + 1, result);
            marked[i] = false;
        }
    }

    public Map<Integer, Obstacle> getIdxMapping() {
        return idxMapping;
    }

    public void getPathSize() {
        System.out.println(pq.size());
    }

    public int[] getNextPath() {
        return pqCopy.poll().path();
    }

    public boolean hasNextPath() {
        return pqCopy != null && !pqCopy.isEmpty();
    }

    public void refreshPath() {
        pqCopy = new PriorityQueue<>(pq);
    }

    public static void main(String[] args) {
        ShortestPathBF shortestPathBF = new ShortestPathBF(InputData.getObstacles(), 0, 0);
        shortestPathBF.findPath();
        int[] path = shortestPathBF.getNextPath();
        for (int i = 1; i < path.length; i++) {
            System.out.println(shortestPathBF.idxMapping.get(path[i]));
        }
    }
}
