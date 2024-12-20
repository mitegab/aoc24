import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Day20 {

    private static final int INF = 1_000_000_000;

    public static void main(String[] args) {
        System.out.println(task1("input.txt", 2, 100));
        System.out.println(task1("input.txt", 20, 100));
    }

    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }


    private static class DijkstraResult {
        Map<Point, Integer> dist;
        Map<Point, Point> prev;

        public DijkstraResult(Map<Point, Integer> dist, Map<Point, Point> prev) {
            this.dist = dist;
            this.prev = prev;
        }
    }


    static DijkstraResult dijkstra(Map<Point, Character> area, Point start) {
        Map<Point, Integer> dist = new HashMap<>();
        Map<Point, Point> prev = new HashMap<>();
        PriorityQueue<Pair<Integer, Point>> q = new PriorityQueue<>(Comparator.comparingInt(Pair::getFirst));


        for (Map.Entry<Point, Character> entry : area.entrySet()) {
            Point pos = entry.getKey();
            char charValue = entry.getValue();
            if (charValue == '#') {
                continue;
            }
            dist.put(pos, INF);
            prev.put(pos, null);
        }

        dist.put(start, 0);
        q.offer(new Pair<>(0, start));

        while (!q.isEmpty()) {
            Pair<Integer, Point> current = q.poll();
            int d = current.getFirst();
            Point point = current.getSecond();
            int x = point.x;
            int y = point.y;


            int[] dx = {0, 1, 0, -1};
            int[] dy = {-1, 0, 1, 0};


            for (int i = 0; i < 4; i++){
                int nx = x + dx[i];
                int ny = y + dy[i];

                Point nextPos = new Point(nx, ny);
                if (area.getOrDefault(nextPos, '#') == '#'){
                    continue;
                }
                if (d + 1 < dist.getOrDefault(nextPos, INF)) {
                    dist.put(nextPos, d + 1);
                    prev.put(nextPos, point);
                    q.offer(new Pair<>(d + 1, nextPos));
                }
            }


        }

        return new DijkstraResult(dist, prev);
    }


    static class Pair<K,V>{
        K first;
        V second;
        public Pair(K first, V second){
            this.first = first;
            this.second = second;
        }
        public K getFirst(){return this.first;}
        public V getSecond(){return this.second;}
    }

    private static class ReadInputResult {
        Map<Point, Character> area;
        Point start;
        Point end;
        public ReadInputResult(Map<Point, Character> area, Point start, Point end) {
            this.area = area;
            this.start = start;
            this.end = end;
        }
    }

    static ReadInputResult readInput(String fn) {
        Map<Point, Character> area = new HashMap<>();
        Point start = null;
        Point end = null;


        List<String> inputFileContent = null;
        try {
            inputFileContent = Files.readAllLines(Paths.get(fn));
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return null;
        }


        for (int row = 0; row < inputFileContent.size(); row++) {
            String line = inputFileContent.get(row);
            for (int col = 0; col < line.length(); col++) {
                char charValue = line.charAt(col);
                Point point = new Point(col, row);
                if (charValue == 'S') {
                    start = point;
                    area.put(point, '.');
                    continue;
                } else if (charValue == 'E') {
                    end = point;
                    area.put(point, '.');
                    continue;
                }
                area.put(point, charValue);

            }
        }
        return new ReadInputResult(area, start, end);
    }


    static int task1(String fn, int d, int thresh) {

        ReadInputResult readInputResult = readInput(fn);
        if(readInputResult == null){
            return -1;
        }
        Map<Point, Character> area = readInputResult.area;
        Point start = readInputResult.start;
        Point end = readInputResult.end;


        DijkstraResult dijkstraResult = dijkstra(area, start);
        Map<Point, Integer> dist = dijkstraResult.dist;
        Map<Point, Point> prev = dijkstraResult.prev;


        Point pos = end;
        List<Point> path = new ArrayList<>();
        while (pos != null && !pos.equals(start)) {
            path.add(pos);
            pos = prev.get(pos);
        }
        path.add(start);

        int count = 0;
        for (int i = 0; i < path.size(); i++){
            Point p1 = path.get(i);
            for (int j = i + 1; j < path.size(); j++){
                Point p2 = path.get(j);

                int x = p1.x;
                int y = p1.y;
                int x2 = p2.x;
                int y2 = p2.y;

                if (Math.abs(x - x2) + Math.abs(y - y2) <= d && dist.get(p1) - dist.get(p2) > d) {
                    int dd = dist.get(p1) - dist.get(p2) - (Math.abs(x - x2) + Math.abs(y - y2));
                    if (dd >= thresh) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
}