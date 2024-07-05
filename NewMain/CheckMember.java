import java.io.*;
import java.security.*;

public class CheckMember {
    public static boolean validateLogin(String id, String password) {
        // 여기에서 실제 유효성 검사를 수행
        String hashedPassword = hashPassword(password);
        try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // 쉼표로 구분
                if (parts.length >= 3) {
                    String fileId = parts[1].trim();
                    String filePassword = parts[2].trim();
                    if (fileId.equals(id) && filePassword.equals(hashedPassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Member getMemberById(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[1].equals(id)) {
                    return new Member(parts[1], parts[2], parts[0], parts[3], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
