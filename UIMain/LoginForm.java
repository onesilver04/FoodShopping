import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginForm {
    private JTabbedPane tabbedPane;

    public JPanel getLoginPanel() {
        JPanel outerPanel = new JPanel(new GridBagLayout()); // 외부 패널, 중앙 정렬
        outerPanel.setBackground(Color.WHITE); // 외부 패널 배경색 흰색으로 설정
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.insets = new Insets(10, 10, 10, 10); // 외부 패널의 간격 설정

        RoundedPanel panel = new RoundedPanel(15, new Color(234, 234, 234)); // 둥근 모서리 패널 생성 (테두리 색상 지정)
        panel.setPreferredSize(new Dimension(200, 250)); // 내부 패널 크기 설정
        panel.setBackground(new Color(234, 234, 234)); // 내부 패널 배경색 설정
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); // 패널 내부 여백 설정
        panel.setLayout(new GridBagLayout()); // GridBagLayout 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 각 컴포넌트 사이의 간격 설정

        // "Log-in" 레이블을 맨 위에 배치
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH; // 맨 위에 위치하도록 설정
        JLabel titleLabel = new JLabel("Log-in");
        titleLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 18)); // 폰트 설정
        panel.add(titleLabel, gbc);

        // ID 레이블과 텍스트 필드
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // 기본으로 돌아감
        gbc.anchor = GridBagConstraints.WEST; // 왼쪽 정렬
        JLabel l1 = new JLabel("ID:");
        panel.add(l1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        JTextField text = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        panel.add(text, gbc);

        // 비밀번호 레이블과 패스워드 필드
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel l2 = new JLabel("비밀번호:");
        panel.add(l2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        JPasswordField value = new JPasswordField(10); // 패스워드 필드 크기를 줄이기 위해 열 수를 10으로 설정
        value.setEchoChar('\u25CF'); // 입력된 비밀번호를 유니코드 동그라미 기호로 표시
        panel.add(value, gbc);

        // 로그인 버튼
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        gbc.fill = GridBagConstraints.NONE;
        JButton b = new JButton("로그인");
        b.setBackground(new Color(47, 157, 39));
        panel.add(b, gbc);

        // 회원가입 버튼
        gbc.gridy = 4; // 회원가입 버튼은 로그인 버튼 아래에 위치
        JButton rB = new JButton("회원가입");
        rB.setBackground(Color.LIGHT_GRAY);
        panel.add(rB, gbc);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SessionManager.getInstance().isLoggedIn()) {
                    JOptionPane.showMessageDialog(panel, "이미 로그인 되어 있습니다.");
					text.setText("");
                    value.setText("");
                    return;
                }
                
                String id = text.getText();
                String password = new String(value.getPassword());

                if (CheckMember.validateLogin(id, password)) {
                    JOptionPane.showMessageDialog(panel, "로그인 성공!");
                    text.setText("");
                    value.setText("");

                    // 로그인 성공 시 SessionManager를 통해 사용자 정보를 저장
                    Member loggedInUser = CheckMember.getMemberById(id);
                    SessionManager.getInstance().login(loggedInUser);

                    openMainTab();
                } else {
                    JOptionPane.showMessageDialog(panel, "로그인 실패! 아이디 또는 비밀번호를 확인해주세요.");
                }
            }
        });

        rB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMembershipWindow(); // 회원가입 창을 보여줍니다.
            }
        });

        addMouseHoverEffect(b, new Color(183, 240, 177));
        addMouseHoverEffect(rB, new Color(183, 240, 177));

        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerPanel.add(panel, outerGbc); // 내부 패널을 외부 패널에 추가

        return outerPanel;
    }

    private static void showMembershipWindow() {
        MembershipForm.showMembershipForm();
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    private void openMainTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(0); // 메인 탭으로 이동
        }
    }

    // 회원가입 후 로그인 페이지로 전환 메소드
    public void openMembershipFormTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(2);
        }
    }

    private void addMouseHoverEffect(JButton button, Color hoverColor) {
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
            return new Dimension(280, 320); // 패널 크기 조정
        }
    }
}
