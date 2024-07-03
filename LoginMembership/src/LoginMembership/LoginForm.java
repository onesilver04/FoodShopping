package LoginMembership;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm {
    public static void main(String[] args) {
        JFrame f = new JFrame("로그인 창");
        f.setLayout(new BorderLayout());
        f.setSize(800, 600); // 적절한 크기로 설정
        f.setLocationRelativeTo(null); // 화면 중앙에 위치
        
        

        JPanel panel = new JPanel(new GridBagLayout()); // GridBagLayout 사용
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 각 컴포넌트 사이의 간격 설정

        JLabel l1 = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(l1, gbc);

        JTextField text = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        panel.add(text, gbc);

        JLabel l2 = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(l2, gbc);

        JPasswordField value = new JPasswordField(10); // 패스워드 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        panel.add(value, gbc);

        JButton b = new JButton("로그인");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(b, gbc);
        
        JButton rB = new JButton("회원가입");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(rB, gbc);
        
        
        f.add(panel, BorderLayout.CENTER);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = text.getText();
                String password = new String(value.getPassword());

                if (CheckMember.validateLogin(id, password)) {
                    JOptionPane.showMessageDialog(f, "로그인 성공!");
                    f.dispose();  // 로그인 성공 시 로그인 창 닫기
                } else {
                    JOptionPane.showMessageDialog(f, "로그인 실패! 아이디 또는 비밀번호를 확인해주세요.");
                }
            }
        });
        
     
  
        
        rB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMembershipWindow(); // 회원가입 창을 보여줍니다.
            }
        });
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
        
        private static void showMembershipWindow() {
    	        MembershipForm.showMembershipForm();
        }
        
            
    
    
}
         
            
        
        
        // 패널을 프레임의 중앙에 추가
       /* f.add(panel, BorderLayout.CENTER);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = f.getText();
                String password = new String(l2.getPassword());
                
                if (validateLogin(id, password)) {
                    JOptionPane.showMessageDialog(f, "로그인 성공!");
                    f.dispose();  // 로그인 성공 시 로그인 창 닫기
                } else {
                    JOptionPane.showMessageDialog(f, "로그인 실패! 아이디 또는 비밀번호를 확인해주세요.");
                }
            }
        });
        
        f.setVisible(true);
 
    
}
    
    private static boolean validateLogin(String id, String password) {
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
    
    
    private static void saveMemberToFile(Member member) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("members.txt", true))) {
            bw.write(member.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // BufferedWriter: 텍스트 파일에 문자를 쓰기 위한 클래스입니다.
        // FileWriter("members.txt", true): members.txt 파일에 쓰기를 위한 객체를 생성합니다. true 옵션은 파일 끝에 추가(append)하는 모드를 나타냅니다.
        // bw.write(...): 파일에 문자열을 씁니다.
        // bw.newLine(): 새 줄을 추가합니다.
    }
    
    private static boolean isIdDuplicated(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("아이디: " + id + ",")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static boolean isValidInput(String name, String id, String password) {
        if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
            return false;  // 필드가 비어 있는지 확인
        }
     
        if (!id.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            return false;  // ID는 알파벳으로 시작하고, 알파벳 및 숫자만 포함
        }
        return true;
    }
}  */



