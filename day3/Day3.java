import java.io.*;
import java.util.regex.*;
import java.util.*;

public class Day3 {
    public static void main(String[] args) {
        String inputFileName = "input.txt";
        try {
            // Read the input file
            StringBuilder input = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    input.append(line);
                }
            }

            // Regex pattern for valid mul(X,Y) instructions
            Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
            Matcher matcher = pattern.matcher(input.toString());

            int totalSum = 0;

            // Find and process all valid matches
            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                totalSum += x * y;
            }

            // Print the result
            System.out.println("Total sum of valid mul instructions: " + totalSum);

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}
