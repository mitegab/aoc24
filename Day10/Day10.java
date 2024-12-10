import java.io.*;
import java.util.*;

public class Day10 {
    private static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public static void main(String[] args) {
        String path = "input.txt";
        List<int[]> startPositions = new ArrayList<>();
        List<List<Integer>> mapHeights = new ArrayList<>();

        // Read input file
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int y = 0;
            while ((line = br.readLine()) != null) {
                List<Integer> row = new ArrayList<>();
                for (int x = 0; x < line.length(); x++) {
                    char height = line.charAt(x);
                    if (height == '0') {
                        startPositions.add(new int[]{x, y});
                    }
                    if (height == '.') {
                        row.add(-1);
                    } else {
                        row.add(Character.getNumericValue(height));
                    }
                }
                mapHeights.add(row);
                y++;
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        int width = mapHeights.get(0).size();
        int height = mapHeights.size();
        int totalBranches = 0;

        // Compute total branches
        for (int[] start : startPositions) {
            int branches = 0;
            Deque<int[]> open = new ArrayDeque<>();
            open.add(start);
            Set<String> seenEnds = new HashSet<>();

            while (!open.isEmpty()) {
                int[] current = open.pop();
                int x = current[0];
                int y = current[1];

                for (int[] dir : DIRECTIONS) {
                    int newX = x + dir[0];
                    int newY = y + dir[1];

                    if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
                        continue;
                    }

                    int currentHeight = mapHeights.get(y).get(x);
                    int newHeight = mapHeights.get(newY).get(newX);

                    if (newHeight - currentHeight != 1) {
                        continue;
                    }

                    if (newHeight == 9) {
                        String key = newX + "," + newY;
                        if (!seenEnds.contains(key)) {
                            branches++;
                            seenEnds.add(key);
                        }
                        continue;
                    }

                    open.add(new int[]{newX, newY});
                }
            }
            totalBranches += branches;
        }

        System.out.println("Solution:\n" + totalBranches);
    }
}
