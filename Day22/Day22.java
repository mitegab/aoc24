import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 {

    public static void main(String[] args) {
        System.out.println("\n--- Day 22: Monkey Market ---");

        String filename = "input.txt";

        List<Integer> secrets = parseInput(filename);

        System.out.println("\n--- Part 1 ---");

        assert mix(42, 15) == 37 : "'mix' is broken";
        assert prune(100000000) == 16113920 : "'prune' is broken";

        long finalSum = 0;
        List<List<Integer>> allChains = new ArrayList<>();
        for (int s : secrets) {
            List<Integer> chain = new ArrayList<>();
            chain.add(getPrice(s));
            for (int i = 0; i < 2000; i++) {
                s = evolve(s);
                chain.add(getPrice(s));
            }
            finalSum += s;
            allChains.add(chain);
        }

        System.out.println(finalSum);

        System.out.println("\n--- Part 2 ---");

        Map<List<Integer>, Integer> allSequences = new HashMap<>();

        for (List<Integer> chain : allChains) {
            Map<List<Integer>, Integer> priceList = new HashMap<>();
            List<Integer> diffChain = new ArrayList<>();
            for (int i = 0; i < chain.size() - 1; i++) {
                diffChain.add(chain.get(i + 1) - chain.get(i));
            }

            List<Integer> currentChain = new ArrayList<>(chain);
            List<Integer> currentDiffChain = new ArrayList<>(diffChain);

            while (currentDiffChain.size() >= 4) {
                List<Integer> seq = currentDiffChain.subList(currentDiffChain.size() - 4, currentDiffChain.size());
                priceList.put(new ArrayList<>(seq), currentChain.get(currentChain.size() - 1));

                currentDiffChain.remove(currentDiffChain.size() - 1);
                currentChain.remove(currentChain.size() - 1);
            }

            for (Map.Entry<List<Integer>, Integer> entry : priceList.entrySet()) {
                allSequences.put(entry.getKey(), allSequences.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        int maxValue = 0;
        for (int value : allSequences.values()) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        System.out.println(maxValue);
    }

    static List<Integer> parseInput(String filename) {
        List<Integer> secrets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String val = line.trim();
                if (val.isEmpty()) {
                    break;
                }
                secrets.add(Integer.parseInt(val));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return secrets;
    }

    static int evolve(int val) {
        val = prune(mix(val, val << 6));
        val = prune(mix(val, val >> 5));
        val = prune(mix(val, val << 11));
        return val;
    }

    static int mix(int a, int b) {
        return a ^ b;
    }

    static int prune(int val) {
        return val % 16777216;
    }

    static int getPrice(int val) {
        return val % 10;
    }
}