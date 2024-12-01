import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HistorianSimilarity {

    public static String calculateSimilarityScore(String input) throws Exception {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();


        for (String line : input.split("\n")) {
            String[] ids = line.trim().split("\\s+");
            if (ids.length < 2) {
                throw new Exception("Invalid input format. Each line must contain two integers.");
            }
            leftList.add(Integer.parseInt(ids[0]));
            rightList.add(Integer.parseInt(ids[1]));
        }


        int similarityScore = 0;
        for (int locationId : leftList) {
            int occurrenceCount = 0;

            for (int rightLocationId : rightList) {
                if (locationId == rightLocationId) {
                    occurrenceCount++;
                }
            }
            similarityScore += locationId * occurrenceCount;
        }

        return String.valueOf(similarityScore);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java HistorianSimilarity <input-file>");
            return;
        }

        String filePath = args[0];
        try {
            String input = Files.readString(Paths.get(filePath));
            String result = calculateSimilarityScore(input);
            System.out.println("Similarity Score: " + result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
