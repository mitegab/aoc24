// Day18p2.java
import java.io.*;
import java.util.*;

public class Day18p2 {

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

        for (int i = 1; i <= bytePositions.size(); i++) {
            char[][] grid = new char[gridSize][gridSize];
            for (char[] row : grid) {
                Arrays.fill(row, '.');
            }

            for (int j = 0; j < i; j++) {
                int[] pos = bytePositions.get(j);
                grid[pos[1]][pos[0]] = '#';
            }

            if (!isPathPossible(grid, gridSize)) {
                int[] lastByte = bytePositions.get(i - 1);
                System.out.println(lastByte[0] + "," + lastByte[1]);
                return;
            }
        }

        System.out.println("No blocking byte found");
    }

    private static boolean isPathPossible(char[][] grid, int gridSize) {
        int[] start = {0, 0};
        int[] end = {gridSize - 1, gridSize - 1};

        Queue<int[]> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        queue.add(new int[]{start[0], start[1]});
        visited.add(start[0] + "," + start[1]);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currX = current[0], currY = current[1];

            if (currX == end[0] && currY == end[1]) {
                return true;
            }

            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            for (int[] dir : directions) {
                int nextX = currX + dir[0];
                int nextY = currY + dir[1];

                if (nextX >= 0 && nextX < gridSize && nextY >= 0 && nextY < gridSize &&
                        grid[nextY][nextX] == '.' && !visited.contains(nextX + "," + nextY)) {
                    queue.add(new int[]{nextX, nextY});
                    visited.add(nextX + "," + nextY);
                }
            }
        }

        return false;
    }
}
