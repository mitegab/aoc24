import java.io.*;
import java.util.*;

public class Day7 {


    private static boolean evaluatePartOne(long target, List<Long> numbers, int index, long currentValue) {
        if (index == numbers.size()) {
            return currentValue == target;
        }

        long nextNumber = numbers.get(index);


        if (evaluatePartOne(target, numbers, index + 1, currentValue + nextNumber)) {
            return true;
        }


        if (evaluatePartOne(target, numbers, index + 1, currentValue * nextNumber)) {
            return true;
        }

        return false;
    }


    private static boolean evaluatePartTwo(long target, List<Long> numbers, int index, long currentValue) {
        if (index == numbers.size()) {
            return currentValue == target;
        }

        long nextNumber = numbers.get(index);


        if (evaluatePartTwo(target, numbers, index + 1, currentValue + nextNumber)) {
            return true;
        }


        if (evaluatePartTwo(target, numbers, index + 1, currentValue * nextNumber)) {
            return true;
        }


        long concatenatedValue = Long.parseLong(currentValue + "" + nextNumber);
        if (evaluatePartTwo(target, numbers, index + 1, concatenatedValue)) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        String inputFileName = "input.txt";
        long partOneResult = 0;
        long partTwoResult = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Parse each line
                String[] parts = line.split(":");
                long target = Long.parseLong(parts[0].trim());
                String[] numberStrings = parts[1].trim().split(" ");
                List<Long> numbers = new ArrayList<>();

                for (String num : numberStrings) {
                    numbers.add(Long.parseLong(num));
                }


                if (evaluatePartOne(target, numbers, 1, numbers.get(0))) {
                    partOneResult += target;
                }


                if (evaluatePartTwo(target, numbers, 1, numbers.get(0))) {
                    partTwoResult += target;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in input file: " + e.getMessage());
        }


        System.out.println("Part One Solution: " + partOneResult);
        System.out.println("Part Two Solution: " + partTwoResult);
    }
}
