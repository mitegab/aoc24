import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day14p2 {

    private static void printMap(Set<Position> positions, int width, int height) {
        char[][] grid = new char[height][width];
        for (char[] row : grid) {
            Arrays.fill(row, '.');
        }
        for (Position pos : positions) {
            grid[pos.y][pos.x] = '#';
        }
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
    }

    private static List<Position> getFuturePositions(List<Robot> robots, int timeSteps, int width, int height) {
        List<Position> futurePositions = new ArrayList<>();
        for (Robot robot : robots) {
            int nx = (robot.px + robot.vx * timeSteps) % width;
            int ny = (robot.py + robot.vy * timeSteps) % height;
            if (nx < 0) nx += width;
            if (ny < 0) ny += height;
            futurePositions.add(new Position(nx, ny));
        }
        return futurePositions;
    }

    private static Result getEasterEggTime(List<Robot> robots, int width, int height) {
        int easterEggTime = 0;
        while (true) {
            List<Position> futurePositions = getFuturePositions(robots, easterEggTime, width, height);
            Set<Position> distinctPositions = new HashSet<>(futurePositions);
            if (distinctPositions.size() == futurePositions.size()) {
                return new Result(easterEggTime, distinctPositions);
            }
            easterEggTime++;
        }
    }

    private static List<Robot> getInput(String fileName) {
        List<Robot> robots = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" v=");
                String[] position = parts[0].substring(2).split(",");
                String[] velocity = parts[1].split(",");

                int px = Integer.parseInt(position[0]);
                int py = Integer.parseInt(position[1]);
                int vx = Integer.parseInt(velocity[0]);
                int vy = Integer.parseInt(velocity[1]);

                robots.add(new Robot(px, py, vx, vy));
            }
        } catch (IOException e) {
            System.err.println("The file " + fileName + " does not exist or cannot be read.");
            System.exit(1);
        }
        return robots;
    }

    public static void main(String[] args) {
        String fileName = "input.txt";
        List<Robot> robots = getInput(fileName);
        int width = 101;
        int height = 103;

        Result result = getEasterEggTime(robots, width, height);
        System.out.println("The easter egg will appear at time " + result.easterEggTime);
        printMap(result.easterEggMap, width, height);
    }

    private static class Robot {
        int px, py, vx, vy;

        Robot(int px, int py, int vx, int vy) {
            this.px = px;
            this.py = py;
            this.vx = vx;
            this.vy = vy;
        }
    }

    private static class Position {
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
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
    }

    private static class Result {
        int easterEggTime;
        Set<Position> easterEggMap;

        Result(int easterEggTime, Set<Position> easterEggMap) {
            this.easterEggTime = easterEggTime;
            this.easterEggMap = easterEggMap;
        }
    }
}
