import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day19p2 {

    private static List<String> parts;
    private static Map<String, Integer> cache = new HashMap<>();

    public static void main(String[] args) {
        String fn = "input.txt";
        int part = 0;
        String sep = "\n\n";
        List<String> inputFileContent = null;
        try {
            inputFileContent = Files.readAllLines(Paths.get(fn));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }
        String[] blocks = String.join("\n", inputFileContent).split(sep);
        parts = new ArrayList<>(Arrays.asList(blocks[part].split(", ")));
        List<String> wanted = Arrays.asList(blocks[part+1].split("\n"));
        parts.sort(Comparator.reverseOrder());


        int cc = 0;
        for (String s : wanted) {
            cc += possible(s);
        }
        System.out.println(cc);
    }


    static int possible(String s) {
        if (cache.containsKey(s)){
            return cache.get(s);
        }

        if (s.isEmpty()) {
            cache.put(s, 1);
            return 1;
        }

        int count = 0;
        for (String p : parts) {
            if (s.startsWith(p)) {
                count += possible(s.substring(p.length()));
            }
        }
        cache.put(s, count);
        return count;

    }
}