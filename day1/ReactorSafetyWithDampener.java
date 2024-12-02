import java.io.*;
import java.util.*;

public class ReactorSafetyWithDampener {
    public static void main(String[] args) {
        String fileName = "input.txt";
        int safeCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.trim().split("\\s+");
                List<Integer> levels = new ArrayList<>();

                for (String part : parts) {
                    levels.add(Integer.parseInt(part));
                }


                if (isSafe(levels) || canBeSafeWithOneRemoval(levels)) {
                    safeCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        System.out.println("Number of safe reports (with Dampener): " + safeCount);
    }


    private static boolean isSafe(List<Integer> levels) {
        if (levels.size() < 2) {
            return false;
        }

        boolean increasing = levels.get(1) > levels.get(0);
        boolean decreasing = levels.get(1) < levels.get(0);

        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);

            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false;
            }

            if (increasing && diff <= 0) {
                return false;
            }

            if (decreasing && diff >= 0) {
                return false;
            }
        }

        return true;
    }


    private static boolean canBeSafeWithOneRemoval(List<Integer> levels) {
        for (int i = 0; i < levels.size(); i++) {

            List<Integer> modifiedLevels = new ArrayList<>(levels);
            modifiedLevels.remove(i);


            if (isSafe(modifiedLevels)) {
                return true;
            }
        }
        return false;
    }
}
