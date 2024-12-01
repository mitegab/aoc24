import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorianHysteria {

    public static String calculateTotalDistance(String input) throws Exception {
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


        Collections.sort(leftList);
        Collections.sort(rightList);


        int totalDistance = 0;
        for (int i = 0; i < leftList.size(); i++) {
            totalDistance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        return String.valueOf(totalDistance);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java HistorianHysteria <input-file>");
            return;
        }

        String filePath = args[0];
        try {
            String input = Files.readString(Paths.get(filePath));
            String result = calculateTotalDistance(input);
            System.out.println("Total Distance: " + result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
