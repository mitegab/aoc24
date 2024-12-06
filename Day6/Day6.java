import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day6 {
    enum Direction {
        NORTH, SOUTH, EAST, WEST;

        Direction turnRight() {
            return switch (this) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
            };
        }

        int[] toVector() {
            return switch (this) {
                case NORTH -> new int[]{0, -1};
                case SOUTH -> new int[]{0, 1};
                case EAST -> new int[]{1, 0};
                case WEST -> new int[]{-1, 0};
            };
        }
    }

    public static void main(String[] args) {
        try {
            String input = Files.readString(Path.of("input.txt"));
            System.out.println("Part 1 Result: " + processPart1(input));
            System.out.println("Part 2 Result: " + processPart2(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String processPart1(String input) {
        var data = parseInput(input);
        Set<String> visited = new HashSet<>();
        int[] playerPosition = data.getKey();
        Map<String, Character> walls = data.getValue();

        Direction direction = Direction.NORTH;
        visited.add(positionKey(playerPosition));

        while (inBounds(playerPosition, walls)) {
            int[] nextPosition = move(playerPosition, direction.toVector());

            if (walls.containsKey(positionKey(nextPosition))) {
                direction = direction.turnRight();
            } else {
                playerPosition = nextPosition;
                visited.add(positionKey(playerPosition));
            }
        }

        return String.valueOf(visited.size() - 1);
    }

    static String processPart2(String input) {
        var data = parseInput(input);
        Map<String, Character> walls = data.getValue();
        int[] originalPosition = data.getKey();
        Direction direction = Direction.NORTH;

        Set<String> visited = new HashSet<>();
        visited.add(positionKey(originalPosition));

        while (inBounds(originalPosition, walls)) {
            int[] nextPosition = move(originalPosition, direction.toVector());

            if (walls.containsKey(positionKey(nextPosition))) {
                direction = direction.turnRight();
            } else {
                originalPosition = nextPosition;
                visited.add(positionKey(originalPosition));
            }
        }

        final int[] originalPositionCopy = new int[] { originalPosition[0], originalPosition[1] };

        // Debug: Print total visited positions
        System.out.println("Total Visited Positions in Part 2: " + visited.size());

        // Check accessible walls
        long accessibleWalls = visited.stream()
                .filter(pos -> {
                    System.out.println("Checking wall at position: " + pos); // Debug statement

                    int[] tempPos = new int[] { originalPositionCopy[0], originalPositionCopy[1] };
                    Direction tempDirection = Direction.NORTH;

                    Set<String> innerVisited = new HashSet<>();
                    innerVisited.add(pos);

                    for (int i = 0; i < 10000; i++) { // Cap iterations
                        int[] nextPos = move(tempPos, tempDirection.toVector());
                        String nextKey = positionKey(nextPos);

                        if (walls.containsKey(nextKey)) {
                            tempDirection = tempDirection.turnRight();
                        } else if (!inBounds(nextPos, walls)) {
                            break;
                        } else if (innerVisited.contains(nextKey + "_" + tempDirection)) {
                            return true;
                        } else {
                            tempPos = nextPos;
                            innerVisited.add(nextKey + "_" + tempDirection);
                        }

                        // Debug: Log the current position and direction
                        System.out.println("Temp Position: " + positionKey(tempPos) + ", Temp Direction: " + tempDirection);
                    }

                    // Debug: If loop ends
                    System.out.println("Finished checking wall: " + pos);
                    return false;
                })
                .count();

        return String.valueOf(accessibleWalls);
    }




    static boolean isAccessible(String newWall, Map.Entry<int[], Map<String, Character>> data, Map<String, Character> walls) {
        int[] originalPosition = data.getKey();
        Direction direction = Direction.NORTH;

        Set<String> visited = new HashSet<>();
        visited.add(positionKey(originalPosition) + "_" + direction);

        while (true) {
            int[] nextPosition = move(originalPosition, direction.toVector());
            String nextKey = positionKey(nextPosition);

            if (walls.containsKey(nextKey) || nextKey.equals(newWall)) {
                direction = direction.turnRight();
            } else if (visited.contains(nextKey + "_" + direction)) {
                return true;
            } else if (inBounds(nextPosition, walls)) {
                originalPosition = nextPosition;
                visited.add(nextKey + "_" + direction);
            } else {
                return false;
            }
        }
    }

    static Map.Entry<int[], Map<String, Character>> parseInput(String input) {
        int[] playerPosition = null;
        Map<String, Character> walls = new HashMap<>();

        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            char[] chars = lines[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                char c = chars[x];
                if (c == '^') {
                    playerPosition = new int[]{x, y};
                } else if (c == '#') {
                    walls.put(positionKey(new int[]{x, y}), c);
                }
            }
        }

        if (playerPosition == null) {
            throw new IllegalStateException("Player position (^) not found in input.");
        }

        return Map.entry(playerPosition, walls);
    }

    static int[] move(int[] position, int[] direction) {
        return new int[]{position[0] + direction[0], position[1] + direction[1]};
    }

    static boolean inBounds(int[] position, Map<String, Character> walls) {
        List<Integer> xCoordinates = walls.keySet().stream()
                .map(key -> Integer.parseInt(key.split(",")[0]))
                .collect(Collectors.toList());

        List<Integer> yCoordinates = walls.keySet().stream()
                .map(key -> Integer.parseInt(key.split(",")[1]))
                .collect(Collectors.toList());

        int x = position[0], y = position[1];
        return x >= Collections.min(xCoordinates) && x <= Collections.max(xCoordinates)
                && y >= Collections.min(yCoordinates) && y <= Collections.max(yCoordinates);
    }

    static String positionKey(int[] position) {
        return position[0] + "," + position[1];
    }
}
