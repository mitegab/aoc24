// Day18p1.java
import java.io.*;
import java.util.*;

public class Day18p1 {

    public static void main(String[] args) throws IOException {
        // Read input from "input.txt"
        List<int[]> bytePositions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.strip().split(",");
                bytePositions.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
            }
        }

        int gridSize = 71;
        char[][] grid = new char[gridSize][gridSize];
        for (char[] row : grid) {
            Arrays.fill(row, '.');
        }

        // Simulate the first 1024 bytes falling
        for (int i = 0; i < Math.min(1024, bytePositions.size()); i++) {
            int[] pos = bytePositions.get(i);
            grid[pos[1]][pos[0]] = '#';
        }

        int[] start = {0, 0};
        int[] end = {gridSize - 1, gridSize - 1};

        // BFS to find the shortest path
        Queue<int[]> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        queue.add(new int[]{start[0], start[1], 0}); // x, y, distance
        visited.add(start[0] + "," + start[1]);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currX = current[0], currY = current[1], dist = current[2];

            if (currX == end[0] && currY == end[1]) {
                System.out.println(dist);
                return;
            }

            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] dir : directions) {
                int nextX = currX + dir[0];
                int nextY = currY + dir[1];

                if (nextX >= 0 && nextX < gridSize && nextY >= 0 && nextY < gridSize &&
                        grid[nextY][nextX] == '.' && !visited.contains(nextX + "," + nextY)) {
                    queue.add(new int[]{nextX, nextY, dist + 1});
                    visited.add(nextX + "," + nextY);
                }
            }
        }

        System.out.println(-1); // No path found
    }
}
