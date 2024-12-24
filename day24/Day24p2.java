import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day24p2 {

    private static final String INPUT_FILE = "input.txt";
    private static final int WIRE_MAX = 45;

    private static HashMap<String, String> wires = new HashMap<>();
    private static HashMap<String, String[]> gates = new HashMap<>();

    private static class FormulaResult {
        List<String> formula;
        List<String> carry;

        public FormulaResult(List<String> formula, List<String> carry) {
            this.formula = formula;
            this.carry = carry;
        }
    }

    private static void readInput(String filename) {
        wires.clear();
        gates.clear();
        try (Scanner scanner = new Scanner(new File(filename))) {
            // Read wires
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    break;
                }
                String[] parts = line.split(": ");
                wires.put(parts[0], parts[0]);
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
                if (wire1.startsWith("y") && wire2.startsWith("x")) {
                    String temp = wire1;
                    wire1 = wire2;
                    wire2 = temp;
                }
                gates.put(outputWire, new String[]{wire1, op, wire2});
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found: " + filename);
        }
    }

    private static FormulaResult computeFormulas() {
        List<String> formula = new ArrayList<>();
        List<String> carry = new ArrayList<>();
        formula.add("(x00 XOR y00)");
        carry.add("(x00 AND y00)");
        for (int i = 1; i < WIRE_MAX; i++) {
            formula.add(String.format("((x%02d XOR y%02d) XOR %s)", i, i, carry.get(i - 1)));
            carry.add(String.format("((x%02d AND y%02d) OR ((x%02d XOR y%02d) AND %s))", i, i, i, i, carry.get(i - 1)));
        }
        formula.add(carry.get(WIRE_MAX - 1));
        return new FormulaResult(formula, carry);
    }

    private static void executeGate(String wire, HashMap<String, String> currentWires) {
        if (!gates.containsKey(wire)) {
            return;
        }

        String[] gate = gates.get(wire);
        String wire1Name = gate[0];
        String op = gate[1];
        String wire2Name = gate[2];

        if (!currentWires.containsKey(wire1Name) && gates.containsKey(wire1Name)) {
            executeGate(wire1Name, currentWires);
        }
        if (!currentWires.containsKey(wire2Name) && gates.containsKey(wire2Name)) {
            executeGate(wire2Name, currentWires);
        }

        if (currentWires.containsKey(wire1Name) && currentWires.containsKey(wire2Name)) {
            String val1 = currentWires.get(wire1Name);
            String val2 = currentWires.get(wire2Name);

            List<String> operands = Arrays.asList(val1, val2);
            Collections.sort(operands, (s1, s2) -> {
                if (s1.startsWith("x") && !s2.startsWith("x")) return -1;
                if (!s1.startsWith("x") && s2.startsWith("x")) return 1;
                return Integer.compare(s1.length(), s2.length());
            });

            currentWires.put(wire, String.format("(%s %s %s)", operands.get(0), op, operands.get(1)));
        }
    }

    private static void executeAll(HashMap<String, String> currentWires) {
        for (int i = 0; i <= WIRE_MAX; i++) {
            String wire = String.format("z%02d", i);
            executeGate(wire, currentWires);
        }
    }

    private static String findReplacement(String wire, String value, int sType, HashMap<String, String> currentWires) {
        for (String repl : gates.keySet()) {
            if (currentWires.containsKey(repl) && currentWires.get(repl).equals(value)) {
                String[] temp = gates.get(wire);
                gates.put(wire, gates.get(repl));
                gates.put(repl, temp);
                return String.format("%s,%s", wire, repl);
            }
        }
        return null;
    }

    private static String findSwap(HashMap<String, String> initialWires, FormulaResult formulaResult) {
        HashMap<String, String> currentWires = new HashMap<>(initialWires);
        executeAll(currentWires);
        List<String> formula = formulaResult.formula;
        List<String> carry = formulaResult.carry;

        for (int i = 0; i <= WIRE_MAX; i++) {
            String wire0 = String.format("z%02d", i);
            String formula0 = formula.get(i);

            if (!currentWires.containsKey(wire0) || !currentWires.get(wire0).equals(formula0)) {
                String swapResult;

                // Type 0
                if ((swapResult = findReplacement(wire0, formula0, 0, currentWires)) != null) {
                    return swapResult;
                }

                if (!gates.containsKey(wire0)) continue;
                String[] gate0 = gates.get(wire0);
                String wire1Name = gate0[0];
                String wire2Name = gate0[2];
                String formula1 = String.format("(x%02d XOR y%02d)", i, i);
                String formula2 = (i > 0) ? carry.get(i - 1) : "(x00 AND y00)";

                if (currentWires.containsKey(wire2Name) && currentWires.get(wire2Name).equals(formula1) ||
                        currentWires.containsKey(wire1Name) && currentWires.get(wire1Name).equals(formula2)) {
                    String temp = wire1Name;
                    wire1Name = wire2Name;
                    wire2Name = temp;
                }

                // Type 1
                if (currentWires.containsKey(wire1Name) && !currentWires.get(wire1Name).equals(formula1)) {
                    if ((swapResult = findReplacement(wire1Name, formula1, 1, currentWires)) != null) {
                        return swapResult;
                    }
                }

                // Type 2
                if (currentWires.containsKey(wire2Name) && !currentWires.get(wire2Name).equals(formula2)) {
                    if ((swapResult = findReplacement(wire2Name, formula2, 2, currentWires)) != null) {
                        return swapResult;
                    }

                    if (i > 0 && gates.containsKey(wire2Name)) {
                        String[] gate2 = gates.get(wire2Name);
                        String wire3Name = gate2[0];
                        String wire4Name = gate2[2];
                        String formula3 = String.format("(x%02d AND y%02d)", i - 1, i - 1);
                        String formula4 = (i > 1) ? String.format("((x%02d XOR y%02d) AND %s)", i - 1, i - 1, carry.get(i - 2)) : "((x00 XOR y00) AND (x00 AND y00))";

                        if (currentWires.containsKey(wire4Name) && currentWires.get(wire4Name).equals(formula3) ||
                                currentWires.containsKey(wire3Name) && currentWires.get(wire3Name).equals(formula4)) {
                            String temp2 = wire3Name;
                            wire3Name = wire4Name;
                            wire4Name = temp2;
                        }

                        // Type 3
                        if (currentWires.containsKey(wire3Name) && !currentWires.get(wire3Name).equals(formula3)) {
                            if ((swapResult = findReplacement(wire3Name, formula3, 3, currentWires)) != null) {
                                return swapResult;
                            }
                        }

                        // Type 4
                        if (currentWires.containsKey(wire4Name) && !currentWires.get(wire4Name).equals(formula4)) {
                            if ((swapResult = findReplacement(wire4Name, formula4, 4, currentWires)) != null) {
                                return swapResult;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String solution(String filename) {
        readInput(filename);
        FormulaResult formulaResult = computeFormulas();
        List<String> swaps = new ArrayList<>();
        String swap;
        while ((swap = findSwap(wires, formulaResult)) != null) {
            swaps.add(swap);
            String[] swappedWires = swap.split(",");
            readInput(filename); // Re-read input to reset gates
            String[] temp = gates.get(swappedWires[0]);
            gates.put(swappedWires[0], gates.get(swappedWires[1]));
            gates.put(swappedWires[1], temp);
        }
        Collections.sort(swaps);
        return String.join(",", swaps);
    }

    public static void main(String[] args) {
        System.out.println(solution(INPUT_FILE));
    }
}