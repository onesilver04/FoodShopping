package LoginMembership;


import java.io.*;
import java.security.*;

public class CheckMember {
	 
	        public static boolean validateLogin(String id, String password) {
	            // 여기에서 실제 유효성 검사를 수행
	        	String hashedPassword = hashPassword(password);
	            try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
	                String line;
	                while ((line = br.readLine()) != null) {
	                    if (line.contains("아이디: " + id + ",") && line.contains("비밀번호: " + hashedPassword)) {
	                        return true;
	                    }
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return false;
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

