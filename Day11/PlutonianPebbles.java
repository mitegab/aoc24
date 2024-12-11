// Solution by Mitegab
import java.io.*;
import java.util.*;

public class PlutonianPebbles {

    private static Map<String, Integer> memo = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String fname = "input.txt";
        List<Integer> stones = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
            String[] data = br.readLine().trim().split(" ");
            for (String x : data) {
                stones.add(Integer.parseInt(x));
            }
        }

        int part1 = 0;
        int part2 = 0;

        for (int s : stones) {
            part1 += plutonianPebbles(s, 25);
            part2 += plutonianPebbles(s, 75);
        }

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    private static int plutonianPebbles(int x, int i) {
        String key = x + "," + i;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int ans;
        if (i == 0) {
            return 1;
        } else if (x == 0) {
            ans = plutonianPebbles(1, i - 1);
        } else {
            String sX = Integer.toString(x);
            int lenS = sX.length();

            if (lenS % 2 == 0) {
                int left = Integer.parseInt(sX.substring(0, lenS / 2));
                int right = Integer.parseInt(sX.substring(lenS / 2));
                ans = plutonianPebbles(left, i - 1) + plutonianPebbles(right, i - 1);
            } else {
                ans = plutonianPebbles(x * 2024, i - 1);
            }
        }

        memo.put(key, ans);
        return ans;
    }
}
