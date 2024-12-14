import java.io.*;
import java.util.*;

public class Day14p1 {

    public static void main(String[] args) {
        List<Robot> robots = getInput("input.txt");
        int width = 101, height = 103, timeSteps = 100;

        List<int[]> futurePositions = getFuturePositions(robots, timeSteps, width, height);
        int[] quadrantCounts = getQuadrantCounts(futurePositions, width, height);

        int safetyFactor = quadrantCounts[0] * quadrantCounts[1] * quadrantCounts[2] * quadrantCounts[3];
        System.out.println("The safety factor is " + safetyFactor);
    }

    static class Robot {
        int px, py, vx, vy;

        Robot(int px, int py, int vx, int vy) {
            this.px = px;
            this.py = py;
            this.vx = vx;
            this.vy = vy;
        }
    }

    private static List<Robot> getInput(String fileName) {
        List<Robot> robots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(" v=");
                String[] position = parts[0].substring(2).split(",");
                String[] velocity = parts[1].split(",");

                int px = Integer.parseInt(position[0]);
                int py = Integer.parseInt(position[1]);
                int vx = Integer.parseInt(velocity[0]);
                int vy = Integer.parseInt(velocity[1]);

                robots.add(new Robot(px, py, vx, vy));
            }
        } catch (IOException e) {
            System.out.println("The file " + fileName + " does not exist.");
            System.exit(1);
        }
        return robots;
    }

    private static List<int[]> getFuturePositions(List<Robot> robots, int timeSteps, int width, int height) {
        List<int[]> futurePositions = new ArrayList<>();

        for (Robot robot : robots) {
            int nx = (robot.px + robot.vx * timeSteps) % width;
            int ny = (robot.py + robot.vy * timeSteps) % height;

            // Handle negative modulo results
            if (nx < 0) nx += width;
            if (ny < 0) ny += height;

            futurePositions.add(new int[]{nx, ny});
        }

        return futurePositions;
    }

    private static int[] getQuadrantCounts(List<int[]> futurePositions, int width, int height) {
        int[] quadrantCounts = new int[4];
        int midX = width / 2, midY = height / 2;

        for (int[] position : futurePositions) {
            int x = position[0];
            int y = position[1];

            if (x == midX || y == midY) {
                continue;
            }

            if (x > midX && y > midY) {
                quadrantCounts[0]++;
            } else if (x < midX && y > midY) {
                quadrantCounts[1]++;
            } else if (x < midX && y < midY) {
                quadrantCounts[2]++;
            } else if (x > midX && y < midY) {
                quadrantCounts[3]++;
            }
        }

        return quadrantCounts;
    }
}
