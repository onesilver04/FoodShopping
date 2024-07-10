import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MembershipForm {

    public static void showMembershipForm() {
        JFrame r = new JFrame("회원가입 창");
        r.setLayout(new GridBagLayout());
        r.setSize(800, 600); // 적절한 크기로 설정
        r.setLocationRelativeTo(null); // 화면 중앙에 위치
        r.getContentPane().setBackground(Color.WHITE); // 외부 패널 배경색 흰색으로 설정

        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.insets = new Insets(10, 10, 10, 10); // 외부 패널의 간격 설정
        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerGbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬

        RoundedPanel panel = new RoundedPanel(30, new Color(234, 234, 234)); // 둥근 모서리 패널 생성
        panel.setPreferredSize(new Dimension(400, 450)); // 내부 패널 크기 설정
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); // 패널 내부 여백 설정
        panel.setLayout(new GridBagLayout()); // GridBagLayout 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 각 컴포넌트 사이의 간격 설정

        // "회원가입" 레이블을 맨 위에 배치
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTH; // 맨 위에 위치하도록 설정
        JLabel titleLabel = new JLabel("Membership");
        titleLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 22)); // 폰트 설정
        gbc.insets = new Insets(20, 5, 20, 5); // 간격 설정
        panel.add(titleLabel, gbc);

        // 이름 레이블과 텍스트 필드
        gbc.insets = new Insets(5, 5, 5, 5); // 기본 간격 설정
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // 기본으로 돌아감
        gbc.anchor = GridBagConstraints.WEST; // 왼쪽 정렬
        JLabel l0 = new JLabel("name:");
        panel.add(l0, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        JTextField nameField = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        panel.add(nameField, gbc);

        // ID 레이블과 텍스트 필드
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel l1 = new JLabel("ID:");
        panel.add(l1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField idField = new JTextField(10);
        panel.add(idField, gbc);

        // 비밀번호 레이블과 패스워드 필드
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel l2 = new JLabel("비밀번호:");
        panel.add(l2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('\u25CF'); // 입력된 비밀번호를 유니코드 동그라미 기호로 표시
        panel.add(passwordField, gbc);

        // 주소 레이블과 텍스트 필드
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel la = new JLabel("주소:");
        panel.add(la, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField addressField = new JTextField(10);
        panel.add(addressField, gbc);

        // 생년월일 레이블과 콤보박스
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel lb = new JLabel("생년월일:");
        panel.add(lb, gbc);

        // 연도 콤보박스
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JComboBox<String> yearComboBox = new JComboBox<>();
        for (int year = 1960; year <= 2023; year++) {
            yearComboBox.addItem(String.valueOf(year));
        }
        panel.add(yearComboBox, gbc);

        // 월 콤보박스
        gbc.gridx = 2;
        gbc.gridy = 5;
        JComboBox<String> monthComboBox = new JComboBox<>();
        for (int month = 1; month <= 12; month++) {
            monthComboBox.addItem(String.format("%02d", month)); // 숫자를 두 자리 형식으로 추가
        }
        panel.add(monthComboBox, gbc);

        // 일 콤보박스
        gbc.gridx = 3;
        gbc.gridy = 5;
        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(String.format("%02d", day)); // 숫자를 두 자리 형식으로 추가
        }
        panel.add(dayComboBox, gbc);

        // 전화번호 레이블과 텍스트 필드
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        JLabel lp = new JLabel("전화번호:");
        panel.add(lp, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField pNumberField = new JTextField(10);
        panel.add(pNumberField, gbc);

        // 빈 패널을 추가하여 간격을 조절
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        JPanel spacer1 = new JPanel();
        spacer1.setPreferredSize(new Dimension(0, 20)); // 간격 크기 설정
        panel.add(spacer1, gbc);

        // 버튼 크기 설정
        Dimension buttonSize = new Dimension(150, 30);

        // 회원가입 버튼
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 4; // 4개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        gbc.fill = GridBagConstraints.NONE;
        JButton registerButton = new JButton("가입하기");
        registerButton.setPreferredSize(buttonSize); // 크기 설정
        registerButton.setBackground(new Color(47, 157, 39));
        panel.add(registerButton, gbc);

        // 로그인 창으로 버튼
        gbc.gridy = 9; // 로그인 창으로 버튼은 회원가입 버튼 아래에 위치
        gbc.insets = new Insets(10, 0, 0, 0); // 위쪽 여백 추가
        JButton loginButton = new JButton("로그인 창으로");
        loginButton.setPreferredSize(buttonSize); // 크기 설정
        loginButton.setBackground(Color.LIGHT_GRAY);
        panel.add(loginButton, gbc);

        // "로그인 창으로" 버튼 누르면 현재 창을 닫는 event
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.dispose();
            }
        });

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

        // 패널을 중앙에 배치하기 위해 GridBagConstraints 사용
        r.add(panel, outerGbc); 
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

    // 둥근 모서리 패널을 만드는 클래스
    private static class RoundedPanel extends JPanel {
        private final int radius;
        private Color borderColor;

        RoundedPanel(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
            setOpaque(false); // 패널을 투명하게 설정
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(370, 450); // 패널 크기 조정
        }
    }
}
