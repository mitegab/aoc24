import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.mariuszgromada.math.mxparser.*;
import org.apache.commons.math3.linear.*;

public class Day13 {

    private static final String INPUT_FILE_NAME = "input.in";
    private static final double EPSILON = 1e-10;

    public static void main(String[] args) throws IOException {
        File inputFile = new File(INPUT_FILE_NAME);
        List<String> inputLines = readInputFile(inputFile);

        int width = inputLines.get(0).length();
        int height = inputLines.size();

        int tokenSum = 0;
        List<String> machines = Arrays.asList(String.join("\n", inputLines).split("\n\n"));
        List<String> singularMachines = new ArrayList<>();

        for (String machine : machines) {
            List<String> machineLines = filterNonEmptyLines(machine.split("\n"));
            List<Integer> extractedNumbers = extractNumbersFromLines(machineLines);

            RealMatrix matrixA = createMatrixA(extractedNumbers);
            double[] vectorG = createVectorG(extractedNumbers);

            if (isSingular(matrixA)) {
                singularMachines.add(machine);
                tokenSum += 0;
            } else {
                RealMatrix invertedA = MatrixUtils.inverse(matrixA);
                double[] solutionN = invertedA.operate(vectorG);
                tokenSum += calculateTokens(solutionN);
            }
        }

        System.out.println(tokenSum);

        // Part 2
        tokenSum = 0;
        for (String machine : machines) {
            List<String> machineLines = filterNonEmptyLines(machine.split("\n"));
            List<Integer> extractedNumbers = extractNumbersFromLines(machineLines);

            adjustVectorG(extractedNumbers);

            RealMatrix matrixA = createMatrixA(extractedNumbers);
            double[] vectorG = createVectorG(extractedNumbers);

            if (isSingular(matrixA)) {
                singularMachines.add(machine);
                tokenSum += 0;
            } else {
                RealMatrix invertedA = MatrixUtils.inverse(matrixA);
                double[] solutionN = invertedA.operate(vectorG);
                tokenSum += calculateTokens(solutionN);
            }
        }

        System.out.println(tokenSum);
    }

    private static List<String> readInputFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static List<String> filterNonEmptyLines(String[] lines) {
        List<String> filteredLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.isEmpty()) {
                filteredLines.add(line);
            }
        }
        return filteredLines;
    }

    private static List<Integer> extractNumbersFromLines(List<String> lines) {
        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                numbers.add(Integer.parseInt(matcher.group()));
            }
        }
        return numbers;
    }

    private static RealMatrix createMatrixA(List<Integer> numbers) {
        return MatrixUtils.createRealMatrix(new double[][] {
                { numbers.get(0), numbers.get(1) },
                { numbers.get(2), numbers.get(3) }
        });
    }

    private static double[] createVectorG(List<Integer> numbers) {
        return new double[] { numbers.get(4), numbers.get(5) };
    }

    private static void adjustVectorG(List<Integer> numbers) {
        numbers.set(4, numbers.get(4) + 10_000_000_000_000);
        numbers.set(5, numbers.get(5) + 10_000_000_000_000);
    }

    private static boolean isSingular(RealMatrix matrix) {
        return Math.abs(new LUDecomposition(matrix).getDeterminant()) < EPSILON;
    }

    private static int calculateTokens(double[] solutionN) {
        if (isValidSolution(solutionN)) {
            return (int) Math.round(3 * solutionN[0] + solutionN[1]);
        }
        return 0;
    }

    private static boolean isValidSolution(double[] solutionN) {
        return solutionN[0] > 0 && solutionN[1] > 0
                && Math.abs(solutionN[0] - Math.round(solutionN[0])) < EPSILON
                && Math.abs(solutionN[1] - Math.round(solutionN[1])) < EPSILON;
    }
}
