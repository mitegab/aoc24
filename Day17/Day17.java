import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day17 {

    public static void main(String[] args) throws IOException {
        List<Integer> numbers = new ArrayList<>();

        // Read input from input.txt and extract integers
        for (String num : Files.readString(Paths.get("input.txt")).split("\\D+")) {
            if (!num.isEmpty()) {
                numbers.add(Integer.parseInt(num));
            }
        }

        int a = numbers.get(0);
        int b = numbers.get(1);
        int c = numbers.get(2);
        List<Integer> prog = numbers.subList(3, numbers.size());

        System.out.println("Part 1: " + run(prog, a));
        System.out.println("Part 2: " + findA(prog));
    }

    private static String run(List<Integer> prog, int a) {
        int ip = 0, b = 0, c = 0;
        List<Integer> out = new ArrayList<>();

        while (ip >= 0 && ip < prog.size()) {
            int lit = prog.get(ip + 1);
            int combo = switch (prog.get(ip + 1)) {
                case 0, 1, 2, 3 -> prog.get(ip + 1);
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> 99999;
            };

            switch (prog.get(ip)) {
                case 0 -> a = (int) (a / Math.pow(2, combo)); // adv
                case 1 -> b ^= lit; // bxl
                case 2 -> b = combo % 8; // bst
                case 3 -> ip = (a == 0) ? ip : lit - 2; // jnz
                case 4 -> b ^= c; // bxc
                case 5 -> out.add(combo % 8); // out
                case 6 -> b = (int) (a / Math.pow(2, combo)); // bdv
                case 7 -> c = (int) (a / Math.pow(2, combo)); // cdv
            }
            ip += 2;
        }

        return String.join(",", out.stream().map(String::valueOf).toList());
    }

    private static int findA(List<Integer> prog) {
        List<Integer> target = new ArrayList<>(prog);
        java.util.Collections.reverse(target);

        return findAHelper(prog, target, 0, 0);
    }

    private static int findAHelper(List<Integer> prog, List<Integer> target, int a, int depth) {
        if (depth == target.size()) {
            return a;
        }

        for (int i = 0; i < 8; i++) {
            List<Integer> output = runAsList(prog, a * 8 + i);
            if (!output.isEmpty() && output.get(0) == target.get(depth)) {
                int result = findAHelper(prog, target, a * 8 + i, depth + 1);
                if (result != 0) {
                    return result;
                }
            }
        }

        return 0;
    }

    private static List<Integer> runAsList(List<Integer> prog, int a) {
        int ip = 0, b = 0, c = 0;
        List<Integer> out = new ArrayList<>();

        while (ip >= 0 && ip < prog.size()) {
            int lit = prog.get(ip + 1);
            int combo = switch (prog.get(ip + 1)) {
                case 0, 1, 2, 3 -> prog.get(ip + 1);
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> 99999;
            };

            switch (prog.get(ip)) {
                case 0 -> a = (int) (a / Math.pow(2, combo)); // adv
                case 1 -> b ^= lit; // bxl
                case 2 -> b = combo % 8; // bst
                case 3 -> ip = (a == 0) ? ip : lit - 2; // jnz
                case 4 -> b ^= c; // bxc
                case 5 -> out.add(combo % 8); // out
                case 6 -> b = (int) (a / Math.pow(2, combo)); // bdv
                case 7 -> c = (int) (a / Math.pow(2, combo)); // cdv
            }
            ip += 2;
        }

        return out;
    }
}
