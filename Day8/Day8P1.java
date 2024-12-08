import java.io.*;
import java.util.*;

public class Day8p1 {
    public static void main(String[] args) throws IOException {
        // Read input from "input.txt"
        List<String> lines = readInput("input.txt");

        // Create grid from input
        char[][] grid = createGrid(lines);

        // Preprocess antennas
        Map<Character, List<int[]>> antennas = preprocess(grid);

        // Solve Part 1
        int part1Result = solvePart1(grid, antennas);
        System.out.println("Part 1: " + part1Result);

        // Solve Part 2
        int part2Result = solvePart2(grid, antennas);
        System.out.println("Part 2: " + part2Result);
    }

    private static List<String> readInput(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        }
    }

    private static char[][] createGrid(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    private static Map<Character, List<int[]>> preprocess(char[][] grid) {
        Map<Character, List<int[]>> antennas = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                char cell = grid[i][j];
                if (cell != '.') {
                    antennas.computeIfAbsent(cell, k -> new ArrayList<>()).add(new int[]{i, j});
                }
            }
        }
        return antennas;
    }

    private static int solvePart1(char[][] grid, Map<Character, List<int[]>> antennas) {
        Set<String> antinodes = new HashSet<>();
        for (char key : antennas.keySet()) {
            List<int[]> coords = antennas.get(key);
            for (int i = 0; i < coords.size(); i++) {
                for (int j = i + 1; j < coords.size(); j++) {
                    addAntinodes(grid, antinodes, coords.get(i), coords.get(j));
                }
            }
        }
        return antinodes.size();
    }

    private static void addAntinodes(char[][] grid, Set<String> antinodes, int[] coord1, int[] coord2) {
        int diffX = coord2[0] - coord1[0];
        int diffY = coord2[1] - coord1[1];
        int antinodeX1 = coord1[0] - diffX;
        int antinodeY1 = coord1[1] - diffY;
        int antinodeX2 = coord2[0] + diffX;
        int antinodeY2 = coord2[1] + diffY;

        addIfValid(grid, antinodes, antinodeX1, antinodeY1);
        addIfValid(grid, antinodes, antinodeX2, antinodeY2);
    }

    private static void addIfValid(char[][] grid, Set<String> antinodes, int x, int y) {
        if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            antinodes.add(x + "," + y);
        }
    }

    private static int solvePart2(char[][] grid, Map<Character, List<int[]>> antennas) {
        Set<String> antinodes = new HashSet<>();
        for (char key : antennas.keySet()) {
            List<int[]> coords = antennas.get(key);
            for (int i = 0; i < coords.size(); i++) {
                for (int j = i + 1; j < coords.size(); j++) {
                    calculateAntinodesAlongLine(grid, antinodes, coords.get(i), coords.get(j));
                }
            }
        }
        return antinodes.size();
    }

    private static void calculateAntinodesAlongLine(char[][] grid, Set<String> antinodes, int[] coord1, int[] coord2) {
        int diffX = coord2[0] - coord1[0];
        int diffY = coord2[1] - coord1[1];
        for (int g = -50; g <= 50; g++) {
            int x = coord1[0] + g * diffX;
            int y = coord1[1] + g * diffY;
            if (!addIfValidWithBounds(grid, antinodes, x, y, g)) {
                break;
            }
        }
    }

    private static boolean addIfValidWithBounds(char[][] grid, Set<String> antinodes, int x, int y, int g) {
        if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            antinodes.add(x + "," + y);
            return true;
        }
        return g <= 0;
    }
}
