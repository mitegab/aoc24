import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Day24p1 {

    private static final String INPUT_FILE = "input.txt";
    private static final int WIRE_MAX = 45;

    private static HashMap<String, Integer> wires = new HashMap<>();
    private static HashMap<String, String[]> gates = new HashMap<>();

    private static void readInput(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            // Read wires
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    break;
                }
                String[] parts = line.split(": ");
                wires.put(parts[0], Integer.parseInt(parts[1]));
            }

            // Read gates
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(" ");
                String wire1 = parts[0];
                String op = parts[1];
                String wire2 = parts[2];
                String outputWire = parts[4];
                gates.put(outputWire, new String[]{wire1, op, wire2});
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found: " + filename);
        }
    }

    private static void executeGate(String wire) {
        if (!gates.containsKey(wire)) {
            return; // Nothing to execute for this wire
        }

        String[] gate = gates.get(wire);
        String wire1Name = gate[0];
        String op = gate[1];
        String wire2Name = gate[2];

        if (!wires.containsKey(wire1Name) && gates.containsKey(wire1Name)) {
            executeGate(wire1Name);
        }
        if (!wires.containsKey(wire2Name) && gates.containsKey(wire2Name)) {
            executeGate(wire2Name);
        }

        if (wires.containsKey(wire1Name) && wires.containsKey(wire2Name)) {
            int value1 = wires.get(wire1Name);
            int value2 = wires.get(wire2Name);

            switch (op) {
                case "AND":
                    wires.put(wire, value1 & value2);
                    break;
                case "OR":
                    wires.put(wire, value1 | value2);
                    break;
                case "XOR":
                    wires.put(wire, value1 ^ value2);
                    break;
            }
        }
    }

    private static void executeAll() {
        for (int i = 0; i <= WIRE_MAX; i++) {
            String wire = String.format("z%02d", i);
            executeGate(wire);
        }
    }

    private static int computeResult() {
        int result = 0;
        for (int i = WIRE_MAX; i >= 0; i--) {
            String wire = String.format("z%02d", i);
            result <<= 1;
            if (wires.containsKey(wire)) {
                result |= wires.get(wire);
            }
        }
        return result;
    }

    public static int solution(String filename) {
        readInput(filename);
        executeAll();
        return computeResult();
    }

    public static void main(String[] args) {
        System.out.println(solution(INPUT_FILE));
    }
}