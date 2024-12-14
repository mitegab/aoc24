import java.io.*;
import java.util.*;

public class Day12 {

    private static final int[][] DIRECTIONS = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} }; // Up, Right, Down, Left

    public static void main(String[] args) throws IOException {
        // Read input file
        String inputFileName = args.length >= 1 ? args[0] : "input.txt";
        List<String> gridLines = readInputFile(inputFileName);

        int rows = gridLines.size();
        int cols = gridLines.get(0).length();

        Set<String> visitedCells = new HashSet<>();
        int totalWeightedPerimeter = 0;
        int totalWeightedSides = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String cell = row + "," + col;
                if (visitedCells.contains(cell)) continue;

                Queue<int[]> queue = new ArrayDeque<>();
                queue.add(new int[] { row, col });

                int area = 0;
                int perimeter = 0;
                Map<int[], Set<String>> perimeterDetails = new HashMap<>();

                while (!queue.isEmpty()) {
                    int[] current = queue.poll();
                    int currentRow = current[0];
                    int currentCol = current[1];
                    String currentCell = currentRow + "," + currentCol;

                    if (visitedCells.contains(currentCell)) continue;
                    visitedCells.add(currentCell);
                    area++;

                    for (int[] direction : DIRECTIONS) {
                        int newRow = currentRow + direction[0];
                        int newCol = currentCol + direction[1];

                        if (isWithinBounds(newRow, newCol, rows, cols) &&
                                gridLines.get(newRow).charAt(newCol) == gridLines.get(currentRow).charAt(currentCol)) {
                            queue.add(new int[] { newRow, newCol });
                        } else {
                            perimeter++;
                            perimeterDetails.computeIfAbsent(direction, k -> new HashSet<>()).add(currentCell);
                        }
                    }
                }

                int sides = calculateSides(perimeterDetails);

                totalWeightedPerimeter += area * perimeter;
                totalWeightedSides += area * sides;
            }
        }

        System.out.println(totalWeightedPerimeter);
        System.out.println(totalWeightedSides);
    }

    private static boolean isWithinBounds(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private static int calculateSides(Map<int[], Set<String>> perimeterDetails) {
        int sides = 0;

        for (Map.Entry<int[], Set<String>> entry : perimeterDetails.entrySet()) {
            Set<String> directionPerimeter = entry.getValue();
            Set<String> processedPerimeter = new HashSet<>();

            for (String cell : directionPerimeter) {
                if (processedPerimeter.contains(cell)) continue;
                sides++;

                Queue<String> queue = new ArrayDeque<>();
                queue.add(cell);

                while (!queue.isEmpty()) {
                    String current = queue.poll();
                    if (processedPerimeter.contains(current)) continue;
                    processedPerimeter.add(current);

                    for (String neighbor : directionPerimeter) {
                        if (!processedPerimeter.contains(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        return sides;
    }

    private static List<String> readInputFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
