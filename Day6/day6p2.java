import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class day6p2 {

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get("input.txt"));
        System.out.println(process(input));
    }

    public static String process(String input) {
        ParsedResult parsedResult = parse(input);
        Position playerPosition = parsedResult.playerPosition;
        Position originalGuardPosition = playerPosition.clone();
        Map<Position, Character> walls = parsedResult.walls;

        IntSummaryStatistics xStats = walls.keySet().stream().mapToInt(pos -> pos.x).summaryStatistics();
        IntSummaryStatistics yStats = walls.keySet().stream().mapToInt(pos -> pos.y).summaryStatistics();

        int[] xMinMax = new int[]{xStats.getMin(), xStats.getMax()};
        int[] yMinMax = new int[]{yStats.getMin(), yStats.getMax()};


        Direction direction = Direction.NORTH;
        Set<Position> visitedPositions = new HashSet<>();
        visitedPositions.add(playerPosition);

        while (true) {
            Position nextPosition = playerPosition.add(direction.toPosition());

            if (walls.containsKey(nextPosition)) {
                direction = direction.turnRight();
            } else if (isWithinBounds(nextPosition, xMinMax, yMinMax)) {
                playerPosition = nextPosition;
                visitedPositions.add(playerPosition);
            } else {
                break;
            }
        }

        visitedPositions.remove(originalGuardPosition);

        long results = visitedPositions.stream().filter(newWall -> {
            Position tempPlayerPosition = originalGuardPosition.clone();
            Direction tempDirection = Direction.NORTH;

            Set<PlayerState> tempVisitedPositions = new HashSet<>();
            tempVisitedPositions.add(new PlayerState(tempPlayerPosition, tempDirection));

            while (true) {
                Position nextPosition = tempPlayerPosition.add(tempDirection.toPosition());
                if (walls.containsKey(nextPosition) || nextPosition.equals(newWall)) {
                    tempDirection = tempDirection.turnRight();
                    continue;
                }

                if (tempVisitedPositions.contains(new PlayerState(nextPosition, tempDirection))) {
                    return true;
                } else if (isWithinBounds(nextPosition, xMinMax, yMinMax)) {
                    tempPlayerPosition = nextPosition;
                    tempVisitedPositions.add(new PlayerState(tempPlayerPosition, tempDirection));
                } else {
                    return false;
                }
            }
        }).count();

        return String.valueOf(results);
    }

    private static boolean isWithinBounds(Position pos, int[] xMinMax, int[] yMinMax) {
        return pos.x >= xMinMax[0] && pos.x <= xMinMax[1] && pos.y >= yMinMax[0] && pos.y <= yMinMax[1];
    }

    private static ParsedResult parse(String input) {
        Map<Position, Character> walls = new HashMap<>();
        Position playerPosition = null;

        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            char[] chars = lines[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                char token = chars[x];
                Position pos = new Position(x, y);
                if (token == '^') {
                    playerPosition = pos;
                } else if (token == '#') {
                    walls.put(pos, token);
                }
            }
        }

        if (playerPosition == null) {
            throw new IllegalStateException("Player position not found!");
        }

        return new ParsedResult(playerPosition, walls);
    }

    static class Position {
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Position add(Position other) {
            return new Position(this.x + other.x, this.y + other.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        protected Position clone() {
            return new Position(this.x, this.y);
        }
    }

    enum Direction {
        NORTH, SOUTH, EAST, WEST;

        Direction turnRight() {
            switch (this) {
                case NORTH:
                    return EAST;
                case EAST:
                    return SOUTH;
                case SOUTH:
                    return WEST;
                case WEST:
                    return NORTH;
            }
            throw new IllegalStateException("Invalid direction!");
        }

        Position toPosition() {
            switch (this) {
                case NORTH:
                    return new Position(0, -1);
                case SOUTH:
                    return new Position(0, 1);
                case EAST:
                    return new Position(1, 0);
                case WEST:
                    return new Position(-1, 0);
            }
            throw new IllegalStateException("Invalid direction!");
        }
    }

    static class PlayerState {
        Position position;
        Direction direction;

        PlayerState(Position position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PlayerState that = (PlayerState) o;
            return Objects.equals(position, that.position) && direction == that.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction);
        }
    }

    static class ParsedResult {
        Position playerPosition;
        Map<Position, Character> walls;

        ParsedResult(Position playerPosition, Map<Position, Character> walls) {
            this.playerPosition = playerPosition;
            this.walls = walls;
        }
    }
}
