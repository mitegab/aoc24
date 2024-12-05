import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day5 {
    private static final int DAY_NUM = 5;
    private static final String DAY_DESC = "Day 5: Print Queue";

    public static void main(String[] args) {
        String inputFileName = findInputFile(args);
        if (inputFileName == null) {
            System.out.println("Unable to find input file!\nSpecify filename on the command line.");
            System.exit(1);
        }
        System.out.println("Using '" + inputFileName + "' as input file:");

        List<String> values = readInputFile(inputFileName);
        System.out.println("Running " + DAY_DESC + ":");

        run(values);
    }

    private static void run(List<String> values) {
        System.out.println(calc(values, 1));
        System.out.println(calc(values, 2));
    }

    private static int calc(List<String> values, int mode) {
        List<int[]> rules = new ArrayList<>();
        int result = 0;

        for (String row : values) {
            if (row.contains("|")) {
                String[] parts = row.split("\\|");
                rules.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
            } else if (row.contains(",")) {
                Map<Integer, Integer> order = new LinkedHashMap<>();
                int index = 0;
                for (String num : row.split(",")) {
                    order.put(Integer.parseInt(num), index++);
                }

                boolean first = true;
                while (true) {
                    List<int[]> problems = new ArrayList<>();
                    for (int[] rule : rules) {
                        int a = rule[0], b = rule[1];
                        if (order.containsKey(a) && order.containsKey(b)) {
                            if (order.get(a) > order.get(b)) {
                                problems.add(new int[]{a, b});
                                break;
                            }
                        }
                    }

                    if (mode == 1 || (mode == 2 && !first && problems.isEmpty())) {
                        if (problems.isEmpty()) {
                            int middleValue = (order.size() - 1) / 2;
                            result += order.entrySet().stream()
                                    .filter(entry -> entry.getValue() == middleValue)
                                    .mapToInt(Map.Entry::getKey)
                                    .sum();
                        }
                        break;
                    } else {
                        if (first && problems.isEmpty()) {
                            break;
                        }
                        first = false;
                        int a = problems.get(0)[0];
                        int b = problems.get(0)[1];
                        int temp = order.get(a);
                        order.put(a, order.get(b));
                        order.put(b, temp);
                    }
                }
            }
        }
        return result;
    }

    private static String findInputFile(String[] args) {
        // Check if any command-line arguments are passed
        for (String arg : args) {
            File file = new File(arg);
            if (file.exists()) {
                return file.getPath();
            }
        }

        // Check common filenames in the current directory
        List<String> potentialFiles = Arrays.asList(
                "input.txt",
                String.format("day_%d_input.txt", DAY_NUM),
                String.format("day_%02d_input.txt", DAY_NUM)
        );

        for (String fn : potentialFiles) {
            File file = new File(fn);
            if (file.exists()) {
                return file.getPath();
            }
        }

        return null; // Return null if no file is found
    }


    private static List<String> readInputFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
