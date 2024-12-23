import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day23 {

    public static void main(String[] args) {
        try {
            solve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void solve() throws IOException {
        Map<String, Set<String>> connections = new HashMap<>();
        Set<String> allComputers = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("-");
                String comp1 = parts[0];
                String comp2 = parts[1];
                allComputers.add(comp1);
                allComputers.add(comp2);
                connections.computeIfAbsent(comp1, k -> new HashSet<>()).add(comp2);
                connections.computeIfAbsent(comp2, k -> new HashSet<>()).add(comp1);
            }
        }

        int countAll = 0;
        int countT = 0;
        Set<Set<String>> foundSetsAll = new HashSet<>();
        Set<Set<String>> foundSetsT = new HashSet<>();

        List<String> computersList = new ArrayList<>(allComputers);
        Collections.sort(computersList);

        for (int i = 0; i < computersList.size(); i++) {
            for (int j = i + 1; j < computersList.size(); j++) {
                for (int k = j + 1; k < computersList.size(); k++) {
                    String comp1 = computersList.get(i);
                    String comp2 = computersList.get(j);
                    String comp3 = computersList.get(k);

                    if (connections.getOrDefault(comp1, Collections.emptySet()).contains(comp2) &&
                            connections.getOrDefault(comp1, Collections.emptySet()).contains(comp3) &&
                            connections.getOrDefault(comp2, Collections.emptySet()).contains(comp3)) {

                        Set<String> currentSet = new HashSet<>(Arrays.asList(comp1, comp2, comp3));
                        if (foundSetsAll.add(currentSet)) {
                            countAll++;
                            if (comp1.startsWith("t") || comp2.startsWith("t") || comp3.startsWith("t")) {
                                countT++;
                                foundSetsT.add(currentSet);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Part 1: Total sets of three inter-connected computers: " + countAll);
        System.out.println("Part 1: Sets containing at least one computer starting with 't': " + countT);
        // Part 2: Find the largest clique (more efficient approach)
        Set<String> maxClique = new HashSet<>();
        List<String> sortedComputers = new ArrayList<>(allComputers);
        Collections.sort(sortedComputers);

        findMaxClique(new ArrayList<>(), sortedComputers, connections, maxClique);

        List<String> sortedLargestClique = new ArrayList<>(maxClique);
        Collections.sort(sortedLargestClique);
        String password = String.join(",", sortedLargestClique);
        System.out.println("Part 2: The password to get into the LAN party is " + password);
    }

    private static boolean isConnectedToAll(String computer, List<String> cliqueNodes, Map<String, Set<String>> connections) {
        for (String node : cliqueNodes) {
            if (!connections.getOrDefault(computer, Collections.emptySet()).contains(node)) {
                return false;
            }
        }
        return true;
    }

    private static void findMaxClique(List<String> potentialClique, List<String> remainingNodes, Map<String, Set<String>> connections, Set<String> maxClique) {
        if (remainingNodes.isEmpty()) {
            if (potentialClique.size() > maxClique.size()) {
                maxClique.clear();
                maxClique.addAll(potentialClique);
            }
            return;
        }

        // Pruning optimization
        if (potentialClique.size() + remainingNodes.size() <= maxClique.size()) {
            return;
        }

        String currentNode = remainingNodes.get(0);
        List<String> restOfNodes = remainingNodes.subList(1, remainingNodes.size());


        boolean canInclude = true;
        for (String node : potentialClique) {
            if (!connections.getOrDefault(currentNode, Collections.emptySet()).contains(node)) {
                canInclude = false;
                break;
            }
        }
        if (canInclude) {
            List<String> newPotentialClique = new ArrayList<>(potentialClique);
            newPotentialClique.add(currentNode);
            findMaxClique(newPotentialClique, restOfNodes, connections, maxClique);
        }


        findMaxClique(potentialClique, restOfNodes, connections, maxClique);
    }
}