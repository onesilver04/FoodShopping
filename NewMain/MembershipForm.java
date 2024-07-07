// 회원가입
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.*;


public class MembershipForm {
    private JTabbedPane tabbedPane;

    public JPanel getMembershipPanel() {
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

        JButton registerButton = new JButton("가입하기");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(registerButton, gbc);

        registerButton.addActionListener(new ActionListener() { // 가입하기 버튼 이벤트 처리
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();
                String birthdate = birthField.getText();
                String phoneNumber = pNumberField.getText();

                if (!isValidInput(name, id, password, address, birthdate, phoneNumber)) {
                    //JOptionPane.showMessageDialog(panel, "입력이 유효하지 않습니다!");
                    return;
                }
				
				if (isUnderage(birthdate)) { // 미성년자 확인
                    JOptionPane.showMessageDialog(panel, "미성년자는 가입할 수 없습니다!");
                    return;
                }

                if (isIdDuplicated(id, name)) { // 중복된 아이디일 경우
                    JOptionPane.showMessageDialog(panel, "이미 존재하는 아이디입니다!");
                    return;
                }

                String hashedPassword = hashPassword(password);
                Member member = new Member(name, id, hashedPassword, address, birthdate, phoneNumber);
                saveMemberToFile(member);
                JOptionPane.showMessageDialog(panel, "가입 완료!");
                nameField.setText("");
                idField.setText("");
                passwordField.setText("");
                addressField.setText("");
                birthField.setText("");
                pNumberField.setText("");
                openLoginTab();
            }
        });

        return panel;
    }

    private boolean isValidInput(String name, String id, String password, String address, String birthdate, String phoneNumber) {
        if (name.isEmpty() || id.isEmpty() || password.isEmpty() || address.isEmpty() || birthdate.isEmpty() || phoneNumber.isEmpty()) {
            return false;
        }

        if (!phoneNumber.matches("^010-\\d{4}-\\d{4}$")) {
            JOptionPane.showMessageDialog(null, "전화번호는 010-xxxx-xxxx 형식이어야 합니다.");
            return false;
        }

        if (!birthdate.matches("^\\d{4}\\.\\d{2}\\.\\d{2}$")) {
            JOptionPane.showMessageDialog(null, "생년월일은 yyyy.mm.dd 형식이어야 합니다.");
            return false;
        }

        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) { // 최소 8자, 하나 이상의 문자 및 숫자
            JOptionPane.showMessageDialog(null, "비밀번호는 영어와 숫자를 포함해야 하며 최소 8자 이상이어야 합니다.");
            return false;
        }

        return true;
    }
	
	private boolean isUnderage(String birthdate) { // 미성년자임을 확인
        String[] parts = birthdate.split("\\.");
        int year = Integer.parseInt(parts[0]);
        return year >= 2006;
    }

    private boolean isIdDuplicated(String id, String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length > 1 && userInfo[1].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hashPassword(String password) { // 비밀번호 해쉬화해서 저장
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
            return null;
        }
    }

    private void saveMemberToFile(Member member) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("members.txt", true))) {
            bw.write(member.getName() + "," + member.getId() + "," + member.getPassword() + "," + member.getAddress() + "," + member.getBirthdate() + "," + member.getPhoneNumber());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openLoginTab() {
        if (tabbedPane != null) {
            int index = tabbedPane.indexOfTab("로그인");
            if (index != -1) {
                tabbedPane.setSelectedIndex(index);
            }
        }
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
}
