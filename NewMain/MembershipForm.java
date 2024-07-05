import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();
                String birthdate = birthField.getText();
                String phoneNumber = pNumberField.getText();

                if (!isValidInput(name, id, password, address, birthdate, phoneNumber)) {
                    JOptionPane.showMessageDialog(panel, "입력이 유효하지 않습니다!");
                    return;
                }

                if (isIdDuplicated(id, name)) {
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

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    private void openLoginTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(1); // 로그인 탭으로 이동
        }
    }

    private static boolean isValidInput(String name, String id, String password, String address, String birthdate, String phoneNumber) {
        return name != null && !name.isEmpty() && id != null && !id.isEmpty() && password != null && !password.isEmpty()
                && address != null && !address.isEmpty() && birthdate != null && !birthdate.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty();
    }

    private static boolean isIdDuplicated(String id, String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && (parts[1].equals(id) || parts[0].equals(name))) {
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

    private static void saveMemberToFile(Member member) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("members.txt", true))) {
            bw.write(member.toCsvString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

