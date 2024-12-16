import java.io.*;
import java.util.*;

public class Day16 {

    private static final int[][] DIRECTIONS = {
            {0, 1},  // Right
            {1, 0},  // Down
            {0, -1}, // Left
            {-1, 0}  // Up
    };

    public static void main(String[] args) throws IOException {
        List<String> grid = readInput("input.txt");
        int rows = grid.size();
        int cols = grid.get(0).length();

        Position start = findPosition(grid, rows, cols, 'S');
        Position end = findPosition(grid, rows, cols, 'E');

        Map<State, Integer> distances = calculateDistances(grid, List.of(new State(start, 0)));
        int minimumDistance = Integer.MAX_VALUE;

        for (int direction = 0; direction < 4; direction++) {
            minimumDistance = Math.min(minimumDistance, distances.getOrDefault(new State(end, direction), Integer.MAX_VALUE));
        }
        System.out.println(minimumDistance);

        Set<Position> goodPositions = findGoodPositions(grid, end, distances, minimumDistance);
        System.out.println(goodPositions.size());
    }

    private static List<String> readInput(String fileName) throws IOException {
        List<String> grid = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                grid.add(line.trim());
            }
        }
        return grid;
    }

    private static Position findPosition(List<String> grid, int rows, int cols, char target) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid.get(i).charAt(j) == target) {
                    return new Position(i, j);
                }
            }
        }
        throw new IllegalArgumentException("Target character not found: " + target);
    }

    private static Map<State, Integer> calculateDistances(List<String> grid, List<State> starts) {
        SortedSet<StateWithDistance> priorityQueue = new TreeSet<>();
        Map<State, Integer> distances = new HashMap<>();

        for (State start : starts) {
            priorityQueue.add(new StateWithDistance(0, start));
            distances.put(start, 0);
        }

        while (!priorityQueue.isEmpty()) {
            StateWithDistance current = priorityQueue.first();
            priorityQueue.remove(current);

            int currentDistance = current.distance;
            State currentState = current.state;

            int row = currentState.position.row;
            int col = currentState.position.col;
            int direction = currentState.direction;

            int newRow = row + DIRECTIONS[direction][0];
            int newCol = col + DIRECTIONS[direction][1];

            if (grid.get(newRow).charAt(newCol) != '#') {
                State newState = new State(new Position(newRow, newCol), direction);
                addOrUpdateDistance(priorityQueue, distances, newState, currentDistance + 1);
            }

            for (int newDirection : new int[]{direction ^ 1, direction ^ 3}) {
                State newState = new State(new Position(row, col), newDirection);
                addOrUpdateDistance(priorityQueue, distances, newState, currentDistance + 1000);
            }
        }

        return distances;
    }

    private static void addOrUpdateDistance(SortedSet<StateWithDistance> priorityQueue, Map<State, Integer> distances, State state, int newDistance) {
        if (!distances.containsKey(state) || distances.get(state) > newDistance) {
            distances.put(state, newDistance);
            priorityQueue.add(new StateWithDistance(newDistance, state));
        }
    }

    private static Set<Position> findGoodPositions(List<String> grid, Position end, Map<State, Integer> distances, int endDistance) {
        Set<Position> goodPositions = new HashSet<>();

        List<State> endStates = new ArrayList<>();
        for (int direction = 0; direction < 4; direction++) {
            endStates.add(new State(end, direction));
        }

        Map<State, Integer> reverseDistances = calculateDistances(grid, endStates);

        for (Map.Entry<State, Integer> entry : reverseDistances.entrySet()) {
            State state = entry.getKey();
            int reverseDistance = entry.getValue();

            if (distances.containsKey(state) && distances.get(state) + reverseDistance == endDistance) {
                goodPositions.add(state.position);
            }
        }

        return goodPositions;
    }

    private static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static class State {
        Position position;
        int direction;

        State(Position position, int direction) {
            this.position = position;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            State state = (State) obj;
            return direction == state.direction && Objects.equals(position, state.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction);
        }
    }

    private static class StateWithDistance implements Comparable<StateWithDistance> {
        int distance;
        State state;

        StateWithDistance(int distance, State state) {
            this.distance = distance;
            this.state = state;
        }

        @Override
        public int compareTo(StateWithDistance other) {
            if (this.distance != other.distance) {
                return Integer.compare(this.distance, other.distance);
            }
            return this.state.hashCode() - other.state.hashCode();
        }
    }
}
