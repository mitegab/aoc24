import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class Part1 {

    private static final Logger logger = Logger.getLogger(Part1.class.getName());

    private static final List<List<IVec2>> DIRECTIONS = Arrays.asList(
            Arrays.asList(new IVec2(0, 1), new IVec2(0, 2), new IVec2(0, 3)),
            Arrays.asList(new IVec2(0, -1), new IVec2(0, -2), new IVec2(0, -3)),
            Arrays.asList(new IVec2(1, 1), new IVec2(2, 2), new IVec2(3, 3)),
            Arrays.asList(new IVec2(1, -1), new IVec2(2, -2), new IVec2(3, -3)),
            Arrays.asList(new IVec2(-1, 1), new IVec2(-2, 2), new IVec2(-3, 3)),
            Arrays.asList(new IVec2(-1, -1), new IVec2(-2, -2), new IVec2(-3, -3)),
            Arrays.asList(new IVec2(1, 0), new IVec2(2, 0), new IVec2(3, 0)),
            Arrays.asList(new IVec2(-1, 0), new IVec2(-2, 0), new IVec2(-3, 0))
    );

    public static void main(String[] args) {
        try {
            String input = Files.readString(Path.of("input.txt"));
            String result = process(input);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String process(String input) {
        Map<IVec2, Character> positions = new HashMap<>();
        String[] lines = input.split("\n");

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                positions.put(new IVec2(x, y), line.charAt(x));
            }
        }

        char[] mas = {'M', 'A', 'S'};
        int result = positions.entrySet().stream()
                .filter(entry -> entry.getValue() == 'X')
                .mapToInt(entry -> {
                    IVec2 position = entry.getKey();
                    long count = DIRECTIONS.stream()
                            .filter(direction -> {
                                for (int index = 0; index < direction.size(); index++) {
                                    IVec2 offset = direction.get(index);
                                    Character value = positions.get(position.add(offset));
                                    if (index >= mas.length || value == null || value != mas[index]) {
                                        return false;
                                    }
                                }
                                return true;
                            }).count();

                    logger.info(() -> String.format("Position: %s, Value: %c, Count: %d", position, entry.getValue(), count));
                    return (int) count;
                })
                .sum();

        return String.valueOf(result);
    }


    static class IVec2 {
        int x, y;

        public IVec2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public IVec2 add(IVec2 other) {
            return new IVec2(this.x + other.x, this.y + other.y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IVec2 iVec2 = (IVec2) o;
            return x == iVec2.x && y == iVec2.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
