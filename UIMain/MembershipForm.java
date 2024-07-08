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
        panel.setBackground(Color.WHITE); // 배경색을 흰색으로 설정
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
        passwordField.setEchoChar('\u25CF'); // 입력된 비밀번호를 유니코드 동그라미 기호로 표시
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        panel.add(passwordField, gbc);

        JLabel la = new JLabel("주소:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(la, gbc);

        JTextField addressField = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        panel.add(addressField, gbc);

        JLabel lb = new JLabel("생년월일:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lb, gbc);

        // 연도 콤보박스
        JComboBox<String> yearComboBox = new JComboBox<>();
        for (int year = 1900; year <= 2023; year++) {
            yearComboBox.addItem(String.valueOf(year));
        }
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 콤보박스가 수평으로 확장되도록 설정
        panel.add(yearComboBox, gbc);

        // 월 콤보박스
        JComboBox<String> monthComboBox = new JComboBox<>();
        for (int month = 1; month <= 12; month++) {
            monthComboBox.addItem(String.format("%02d", month)); // 숫자를 두 자리 형식으로 추가
        }
        gbc.gridx = 2;
        gbc.gridy = 4;
        panel.add(monthComboBox, gbc);

        // 일 콤보박스
        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(String.format("%02d", day)); // 숫자를 두 자리 형식으로 추가
        }
        gbc.gridx = 3;
        gbc.gridy = 4;
        panel.add(dayComboBox, gbc);

        JLabel lp = new JLabel("전화번호:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lp, gbc);

        JTextField pNumberField = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        panel.add(pNumberField, gbc);

        JButton registerButton = new JButton("가입하기");
        registerButton.setBackground(new Color(47, 157, 39));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(registerButton, gbc);

        JButton loginButton = new JButton("로그인 창으로");
        loginButton.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(loginButton, gbc);

        // "로그인 창으로" 버튼 누르면 현재 창을 닫는 event
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.dispose();
            }
        });

        r.add(panel, BorderLayout.CENTER);

        //회원가입 버튼 누른 후 일어나는 event
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String password = new String(passwordField.getPassword());
                String address = addressField.getText();
                // 콤보박스에서 선택된 아이템을 올바른 형식으로 저장
                String birthdate = yearComboBox.getSelectedItem().toString() + "." +
                                   monthComboBox.getSelectedItem().toString() + "." +
                                   dayComboBox.getSelectedItem().toString();
                String phoneNumber = pNumberField.getText();

                // 회원정보 중 하나라도 입력 안했을 때
                if (!isValidInput(name, id, password, address, birthdate, phoneNumber)) {
                    JOptionPane.showMessageDialog(r, "입력이 유효하지 않습니다!");
                    return;
                }

                if (isUnderage(birthdate)) { // 미성년자 확인
                    JOptionPane.showMessageDialog(panel, "미성년자는 가입할 수 없습니다!");
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

                int response = JOptionPane.showConfirmDialog(r, "가입 완료!", "회원가입 성공", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (response == JOptionPane.OK_OPTION) {
                    r.dispose(); // OK 버튼을 누르면 창 닫기
                }
            }
        });

        addMouseHoverEffect(registerButton, new Color(183, 240, 177));
        addMouseHoverEffect(loginButton, new Color(183, 240, 177));

        r.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        r.setVisible(true);
    }

    // 회원가입 창에 입력 시 입력 기준에 맞게 다 입력되었는지 확인하는 함수
    private static boolean isValidInput(String name, String id, String password, String address, String birthdate, String phoneNumber) {
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

    private static boolean isUnderage(String birthdate) { // 미성년자임을 확인
        String[] parts = birthdate.split("\\."); // '.'으로 분리
        int year = Integer.parseInt(parts[0]);
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return (currentYear - year) < 18; // 18세 미만인 경우
    }

    // 입력받은 회원정보를 member.txt에 있는 name(parts[0]), id(parts[1])정보와 비교
    private static boolean isIdDuplicated(String id, String name) {
        try (BufferedReader br = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo.length >= 2 && (userInfo[1].equals(id) || userInfo[0].equals(name))) {
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
            bw.write(member.getName() + "," + member.getId() + "," + member.getPassword() + "," + member.getAddress() + "," + member.getBirthdate() + "," + member.getPhoneNumber());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addMouseHoverEffect(JButton button, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = button.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor); // 마우스를 가져다 댔을 때 색상 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor); // 마우스를 뗐을 때 원래 색상으로 복구
            }
        });
    }
}
