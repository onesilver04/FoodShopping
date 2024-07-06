import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PointsManager {
    public static final String POINTS_FILE = "members.txt";

    public static int loadPoints(String memberId) {
        int points = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(POINTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(memberId + ",")) {
                    String[] parts = line.split(",");
                    points = Integer.parseInt(parts[parts.length - 1]);
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0; // 파일이 없거나 읽기 오류가 발생한 경우 0으로 초기화
        }
        return points;
    }

    public static void savePoints(String memberId, int points) {
        try {
            File file = new File(POINTS_FILE);
            List<String> lines = new ArrayList<>();
            boolean memberFound = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(memberId + ",")) {
                        String[] parts = line.split(",");
                        parts[parts.length - 1] = String.valueOf(points);
                        lines.add(String.join(",", parts));
                        memberFound = true;
                    } else {
                        lines.add(line);
                    }
                }
            }

            if (!memberFound) {
                lines.add(memberId + "," + points);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
