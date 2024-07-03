package LoginMembership;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MembershipForm {

    public static void showMembershipForm() {
        JFrame r = new JFrame("회원가입 창");
        r.setLayout(new BorderLayout());
        r.setSize(800, 600); // 적절한 크기로 설정
        r.setLocationRelativeTo(null); // 화면 중앙에 위치

        JPanel panel = new JPanel(new GridBagLayout()); // GridBagLayout 사용
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 각 컴포넌트 사이의 간격 설정

        JLabel l0 = new JLabel("name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(l0, gbc);

        JTextField nameField = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        panel.add(nameField, gbc);

        JLabel l1 = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(l1, gbc);

        JTextField idField = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        panel.add(idField, gbc);

        JLabel l2 = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(l2, gbc);

        JPasswordField passwordField = new JPasswordField(10); // 패스워드 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        panel.add(passwordField, gbc);
        

        JLabel la = new JLabel("주소:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(la, gbc);

        JTextField addressField = new JTextField(10); // 패스워드 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        panel.add(addressField, gbc);
        
        JLabel lb = new JLabel("생년월일:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lb, gbc);

        JTextField birthField = new JTextField(10); // 패스워드 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        panel.add(birthField, gbc);
        
        
        JLabel lp = new JLabel("전화번호:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lp, gbc);

        JTextField pNumberField = new JTextField(10); // 패스워드 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        panel.add(pNumberField, gbc);
        

        JButton loginButton = new JButton("로그인 창으로");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(loginButton, gbc);

        // "로그인 창으로" 버튼 누르면 showLoginWindow함수 일어나 로그인창으로 이동하는 event
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginWindow(); // 로그인 창을 보여줍니다.
                r.dispose(); // 회원가입 창 닫기
            }
        });

        JButton registerButton = new JButton("가입하기");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(registerButton, gbc);
        
        r.add(panel, BorderLayout.CENTER);
        
        //회원가입 버튼 누른 후 일어나는 event
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();
                String birthdate = birthField.getText();
                String phoneNumber = pNumberField.getText();

                // 회원정보 중 하나라도 입력 안했을 때
                if (!isValidInput(name, id, password, address, birthdate, phoneNumber)) {
                    JOptionPane.showMessageDialog(r, "입력이 유효하지 않습니다!");
                    return;
                }

                if (isIdDuplicated(id, name)) { //isIdDuplicated 함수에 의해 member.txt에 저장된 정보와 비교
                    JOptionPane.showMessageDialog(r, "이미 존재하는 아이디입니다!");
                    return;
                }
                
                // 입력받은 정보에 대해 객체 생성
                String hashedPassword = hashPassword(password);
                Member member = new Member(name, id, hashedPassword, address, birthdate, phoneNumber);
                saveMemberToFile(member); // 입력받은 정보 저장하기
                JOptionPane.showMessageDialog(r, "가입 완료!");
            }
        });

        r.add(panel);
        r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        r.setVisible(true);
    }
 
    //로그인창 뜨게하는 함수 
    private static void showLoginWindow() {
        LoginForm.main(new String[]{});
    }
    
    // 회원가입 창에 입력 시 null 값 없이 다 입력되었는지 확인하는 함수
    private static boolean isValidInput(String name, String id, String password, String address, String birthdate, String phoneNumber) {
        return name != null && !name.isEmpty() && id != null && !id.isEmpty() && password != null && !password.isEmpty()
                && address != null && !address.isEmpty() && birthdate != null && !birthdate.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty();
    }
    
    // 입력받은 회원정보를 member.text에 있는 name(parts[0]), id(parts[1])정보와 비교
    private static boolean isIdDuplicated(String id, String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(id)||parts[0].equals(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //입력받은 비밀번호를 해시화
    private static String hashPassword(String password) {
        try {
        	MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 입력받은 정보를 member.txt에 저장함
    private static void saveMemberToFile(Member member) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("members.txt", true))) {
            bw.write(member.toCsvString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
        
            
  
       
