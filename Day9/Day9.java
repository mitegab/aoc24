import java.io.*;
import java.util.*;

public class Day9 {
    public static void main(String[] args) throws IOException {

        String path = "input.txt";
        if (args.length > 0) {
            path = args[0];
        }


        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine().trim();
        br.close();

        List<Integer> files = new ArrayList<>();
        for (char ch : line.toCharArray()) {
            files.add(Character.getNumericValue(ch));
        }

        List<Integer> filesystem = new ArrayList<>();
        boolean isBlock = true;
        for (int i = 0; i < files.size(); i++) {
            if (isBlock) {
                int blockSize = files.get(i);
                for (int j = 0; j < blockSize; j++) {
                    filesystem.add(i / 2);
                }
                isBlock = false;
            } else {
                int freeSpace = files.get(i);
                for (int j = 0; j < freeSpace; j++) {
                    filesystem.add(-1);
                }
                isBlock = true;
            }
        }

        // Part 1: Compact filesystem to minimize gaps
        int left = 0;
        int right = filesystem.size() - 1;
        while (left < right) {
            if (filesystem.get(left) == -1) {
                while (right > left && filesystem.get(right) == -1) {
                    right--;
                }
                if (right <= left) {
                    break;
                }
                filesystem.set(left, filesystem.get(right));
                filesystem.set(right, -1);
            }
            left++;
        }

        int checkSum1 = 0;
        for (int i = 0; i < filesystem.size(); i++) {
            if (filesystem.get(i) == -1) {
                break;
            }
            checkSum1 += filesystem.get(i) * i;
        }

        System.out.println("Solution Part 1:");
        System.out.println(checkSum1);

        // Part 2: Optimize placement of blocks
        System.out.println("Solution Part 2:");


        right = filesystem.size() - 1;
        while (right >= 0) {
            if (filesystem.get(right) != -1) {
                int moveBlockSize = 0;
                int blockId = filesystem.get(right);
                while (right >= 0 && filesystem.get(right) == blockId) {
                    moveBlockSize++;
                    right--;
                }

                boolean found = false;
                int tempLeft = 0, tempRight = 0;
                while (tempLeft < right) {
                    if (filesystem.get(tempLeft) == -1) {
                        tempRight = tempLeft;
                        while (tempRight < filesystem.size() && filesystem.get(tempRight) == -1
                                && (tempRight - tempLeft) < moveBlockSize) {
                            tempRight++;
                        }

                        if (tempRight - tempLeft >= moveBlockSize) {
                            found = true;
                            break;
                        } else {
                            tempLeft = tempRight;
                        }
                    }
                    tempLeft++;
                }

                if (found) {
                    for (int i = 0; i < moveBlockSize; i++) {
                        filesystem.set(tempLeft + i, filesystem.get(right + i + 1));
                        filesystem.set(right + i + 1, -1);
                    }
                }
            } else {
                right--;
            }
        }

        int checkSum2 = 0;
        for (int i = 0; i < filesystem.size(); i++) {
            if (filesystem.get(i) == -1) {
                continue;
            }
            checkSum2 += filesystem.get(i) * i;
        }

        System.out.println("Solution Part 2:");
        System.out.println(checkSum2);
    }
}
