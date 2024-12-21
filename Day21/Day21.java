import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

enum KeypadType {
    NUMPAD,
    DIRPAD;

    public Map<String, Position> keyPositions() {
        return this == NUMPAD ? NUMPAD_POSITIONS : DIRPAD_POSITIONS;
    }
}

class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class Day19 {

    static final List<String[]> NUMPAD = List.of(
            new String[]{"7", "8", "9"},
            new String[]{"4", "5", "6"},
            new String[]{"1", "2", "3"},
            new String[]{" ", "0", "A"}
    );

    static final Map<String, Position> NUMPAD_POSITIONS = createKeypadPositions(NUMPAD);

    static final List<String[]> DIRPAD = List.of(
            new String[]{" ", "^", "A"},
            new String[]{"<", "v", ">"}
    );

    static final Map<String, Position> DIRPAD_POSITIONS = createKeypadPositions(DIRPAD);


    private static Map<String, Position> createKeypadPositions(List<String[]> keypad) {
        Map<String, Position> positions = new HashMap<>();
        for (int y = 0; y < keypad.size(); y++) {
            String[] row = keypad.get(y);
            for (int x = 0; x < row.length; x++) {
                positions.put(row[x], new Position(x, y));
            }
        }
        return positions;
    }


    private static final Map<String, Map<String, Integer>> shortestSequenceCache = new HashMap<>();


    static int findShortestSequence(String code, int proxies, KeypadType keypadType) {
        String cacheKey = code + "-" + proxies + "-" + keypadType;
        if(shortestSequenceCache.containsKey(cacheKey)){
            return shortestSequenceCache.get(cacheKey).computeIfAbsent("", k ->
                    calculateShortestSequence(code, proxies, keypadType));
        } else {
            int result = calculateShortestSequence(code, proxies, keypadType);
            shortestSequenceCache.put(cacheKey, new HashMap<>());
            shortestSequenceCache.get(cacheKey).put("",result);
            return result;
        }
    }

    private static int calculateShortestSequence(String code, int proxies, KeypadType keypadType) {
        List<List<String>> sequences = keypadControlSequences(code, keypadType, "A");
        if (proxies == 0) {
            return sequences.stream()
                    .mapToInt(seq -> seq.stream().mapToInt(String::length).sum())
                    .min()
                    .orElse(0);
        } else {
            return sequences.stream()
                    .mapToInt(sequence -> sequence.stream()
                            .mapToInt(dircode -> findShortestSequence(dircode, proxies - 1, KeypadType.DIRPAD))
                            .sum())
                    .min()
                    .orElse(0);
        }
    }

    static List<List<String>> keypadControlSequences(String code, KeypadType keypadType, String start) {
        if (code.isEmpty()) {
            return List.of(new ArrayList<>());
        }
        Map<String, Position> keypadPositions = keypadType.keyPositions();
        Position position = keypadPositions.get(start);
        Position nextPosition = keypadPositions.get(code.substring(0, 1));
        Position gap = keypadPositions.get(" ");

        List<List<String>> sequences = new ArrayList<>();
        for (List<String> sequence : keypadControlSequences(code.substring(1), keypadType, code.substring(0, 1))) {
            for (String option : getMoveOptions(position, nextPosition, gap)) {
                List<String> newSequence = new ArrayList<>();
                newSequence.add(option);
                newSequence.addAll(sequence);
                sequences.add(newSequence);
            }
        }

        return sequences;
    }


    static List<String> getMoveOptions(Position position, Position nextPosition, Position gap) {
        String horizontalArrow = nextPosition.x < position.x ? "<" : ">";
        String verticalArrow = nextPosition.y < position.y ? "^" : "v";
        int horizontalDistance = Math.abs(position.x - nextPosition.x);
        int verticalDistance = Math.abs(position.y - nextPosition.y);
        List<String> options = new ArrayList<>();

        if (position.equals(nextPosition)) {
            options.add("A");
        } else if (position.x == nextPosition.x) {
            options.add(verticalArrow.repeat(verticalDistance) + "A");
        } else if (position.y == nextPosition.y) {
            options.add(horizontalArrow.repeat(horizontalDistance) + "A");
        } else {
            boolean firstOptionValid = !((gap.x == nextPosition.x && nonemptyRange(position.y, nextPosition.y).contains(gap.y))
                    || (gap.y == position.y && nonemptyRange(nextPosition.x, position.x).contains(gap.x)));
            boolean secondOptionValid = !((gap.x == position.x && nonemptyRange(nextPosition.y, position.y).contains(gap.y))
                    || (gap.y == nextPosition.y && nonemptyRange(position.x, nextPosition.x).contains(gap.x)));
            if(firstOptionValid){
                options.add(horizontalArrow.repeat(horizontalDistance) + verticalArrow.repeat(verticalDistance) + "A");
            }
            if(secondOptionValid){
                options.add(verticalArrow.repeat(verticalDistance) + horizontalArrow.repeat(horizontalDistance) + "A");
            }
        }

        return options;
    }


    static Set<Integer> nonemptyRange(int start, int end) {
        if (start == end) {
            return Collections.emptySet();
        }
        Set<Integer> range = new HashSet<>();
        if (start < end) {
            for (int i = start; i < end; i++) {
                range.add(i);
            }
        } else {
            for (int i = start-1; i >= end; i--) {
                range.add(i);
            }
        }
        return range;
    }


    private static List<String> openFileSafely(String fileName) {
        Path scriptDir = Paths.get("").toAbsolutePath();
        Path filePath = scriptDir.resolve(fileName);

        List<String> content = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty()) {
                    content.add(trimmedLine);
                }
            }
        } catch (IOException e) {
            System.err.println("The file '" + fileName + "' not found");
            return null;
        }

        return content;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        List<String> inputTxt = openFileSafely("input.txt");

        if (inputTxt != null) {
            int sum1 = inputTxt.stream()
                    .mapToInt(code -> Integer.parseInt(code.substring(0, code.length() - 1)) * findShortestSequence(code, 2, KeypadType.NUMPAD))
                    .sum();
            System.out.println(sum1);

            int sum2 = inputTxt.stream()
                    .mapToInt(code -> Integer.parseInt(code.substring(0, code.length() - 1)) * findShortestSequence(code, 25, KeypadType.NUMPAD))
                    .sum();
            System.out.println(sum2);
        }
        Instant end = Instant.now();


    }
}