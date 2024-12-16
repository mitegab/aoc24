import java.io.*;
import java.util.*;

public class Day15 {

    public static int calculateGpsSum(char[][] warehouseMap) {
        int gpsSum = 0;
        for (int i = 0; i < warehouseMap.length; i++) {
            for (int j = 0; j < warehouseMap[i].length; j++) {
                if (warehouseMap[i][j] == '[') {
                    gpsSum += 100 * i + j;
                }
            }
        }
        return gpsSum;
    }

    public static char[][] expandWarehouseMap(char[][] warehouseMap) {
        List<char[]> expandedMap = new ArrayList<>();
        Map<Character, char[]> tileMap = Map.of(
                '.', new char[]{'.', '.'},
                '@', new char[]{'@', '.'},
                'O', new char[]{'[', ']'},
                '#', new char[]{'#', '#'}
        );

        for (char[] row : warehouseMap) {
            List<Character> expandedRow = new ArrayList<>();
            for (char cell : row) {
                for (char expandedCell : tileMap.getOrDefault(cell, new char[]{cell, cell})) {
                    expandedRow.add(expandedCell);
                }
            }
            expandedMap.add(expandedRow.stream().map(Object::toString).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString().toCharArray());
        }

        char[][] result = new char[expandedMap.size()][];
        for (int i = 0; i < expandedMap.size(); i++) {
            result[i] = expandedMap.get(i);
        }

        return result;
    }

    public static int[] findStartingPosition(char[][] warehouseMap) {
        for (int i = 0; i < warehouseMap.length; i++) {
            for (int j = 0; j < warehouseMap[i].length; j++) {
                if (warehouseMap[i][j] == '@') {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static char[][] executeCommands(char[][] warehouseMap, String commands) {
        int[] robotPosition = findStartingPosition(warehouseMap);
        if (robotPosition == null) {
            throw new IllegalStateException("Starting position not found.");
        }

        Map<Character, int[]> directions = Map.of(
                '^', new int[]{-1, 0},
                'v', new int[]{1, 0},
                '<', new int[]{0, -1},
                '>', new int[]{0, 1}
        );

        for (char command : commands.toCharArray()) {
            int[] direction = directions.get(command);
            int newRow = robotPosition[0] + direction[0];
            int newCol = robotPosition[1] + direction[1];

            if (warehouseMap[newRow][newCol] == '#') {
                continue;
            } else if (warehouseMap[newRow][newCol] == '.') {
                warehouseMap[robotPosition[0]][robotPosition[1]] = '.';
                robotPosition[0] = newRow;
                robotPosition[1] = newCol;
                warehouseMap[newRow][newCol] = '@';
            } else if (warehouseMap[newRow][newCol] == '[' || warehouseMap[newRow][newCol] == ']') {
                robotPosition = moveBox(robotPosition, direction, warehouseMap);
            }
        }

        return warehouseMap;
    }

    public static int[] moveBox(int[] robotPosition, int[] direction, char[][] warehouseMap) {
        int newRow = robotPosition[0] + direction[0];
        int newCol = robotPosition[1] + direction[1];

        if (warehouseMap[newRow][newCol] == '.') {
            warehouseMap[newRow][newCol] = '@';
            warehouseMap[robotPosition[0]][robotPosition[1]] = '.';
            robotPosition[0] = newRow;
            robotPosition[1] = newCol;
        }

        return robotPosition;
    }

    public static Object[] parseInput(String fileName) throws IOException {
        List<char[]> mapLines = new ArrayList<>();
        StringBuilder commandBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (line.startsWith("#")) {
                    mapLines.add(line.toCharArray());
                } else {
                    commandBuilder.append(line);
                }
            }
        }

        char[][] warehouseMap = mapLines.toArray(new char[0][]);
        return new Object[]{warehouseMap, commandBuilder.toString()};
    }

    public static void main(String[] args) {
        try {
            Object[] input = parseInput("input.txt");
            char[][] warehouseMap = (char[][]) input[0];
            String commands = (String) input[1];

            char[][] expandedMap = expandWarehouseMap(warehouseMap);
            char[][] finalMap = executeCommands(expandedMap, commands);
            int gpsSum = calculateGpsSum(finalMap);

            System.out.println("The sum of all GPS coordinates is: " + gpsSum);
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }
}
