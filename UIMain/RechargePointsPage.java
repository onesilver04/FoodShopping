import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RechargePointsPage extends JFrame {
    JButton btnBack;
    JButton btnRecharge;
    JTextField tfRechargePoints;
    JLabel lblCurrentPoints;
    JPanel panel, bottomPanel;
    int currentPoints;
    Member currentUser;

    public RechargePointsPage(MyPage mainPage) {
        currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "로그인이 필요합니다.");
            mainPage.setVisible(true);
            dispose(); // 현재 창 닫기
            return;
        }

        currentPoints = PointsManager.loadPoints(currentUser.getId()); // 파일에서 포인트를 불러오기

        setTitle("포인트 충전 페이지");
        setSize(800, 600); // 창 사이즈 통일
        setLocationRelativeTo(null);

        // 외부 패널 설정
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(Color.WHITE);
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.insets = new Insets(10, 10, 10, 10);

        // 내부 둥근 모서리 패널 설정
        RoundedPanel panel = new RoundedPanel(30, new Color(234, 234, 234));
        panel.setPreferredSize(new Dimension(400, 300));
        panel.setBackground(new Color(234, 234, 234));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        Font koreanFont = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 16); // 한글 폰트 설정
        Font titleFont = new Font("한컴 말랑말랑 Bold", Font.BOLD, 25); // 타이틀 폰트 설정

        // 타이틀 레이블
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("POINT");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        panel.add(titleLabel, gbc);

        // 현재 포인트 레이블
        gbc.gridy = 1;
        lblCurrentPoints = new JLabel("현재 내 포인트: " + currentPoints);
        lblCurrentPoints.setHorizontalAlignment(SwingConstants.CENTER);
        lblCurrentPoints.setFont(koreanFont);
        panel.add(lblCurrentPoints, gbc);

        // 포인트 입력 필드
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tfRechargePoints = new JTextField();
        tfRechargePoints.setHorizontalAlignment(JTextField.CENTER);
        tfRechargePoints.setFont(koreanFont);
        panel.add(tfRechargePoints, gbc);
		
		Dimension buttonSize = new Dimension(110, 30); // 버튼의 크기를 동일하게 설정

        // 충전 버튼
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        btnRecharge = new JButton("충전");
        btnRecharge.setFont(koreanFont);
		btnRecharge.setPreferredSize(buttonSize); // 크기 설정
        btnRecharge.setBackground(new Color(47, 157, 39));
        addMouseHoverEffect(btnRecharge, new Color(183, 240, 177));
        panel.add(btnRecharge, gbc);

        // 돌아가기 버튼
        gbc.gridy = 4;
        btnBack = new JButton("돌아가기");
        btnBack.setFont(koreanFont);
        btnBack.setBackground(Color.LIGHT_GRAY);
		btnBack.setPreferredSize(buttonSize); // 크기 설정
        addMouseHoverEffect(btnBack, new Color(183, 240, 177));
        panel.add(btnBack, gbc);

        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerPanel.add(panel, outerGbc);

        add(outerPanel);

        // 돌아가기 버튼 클릭 이벤트
        btnBack.addActionListener(e -> {
			mainPage.updatePoints(); // 포인트 업데이트
            mainPage.setVisible(true);
            dispose(); // 현재 창 닫기
        });

        // 충전 버튼 클릭 이벤트
        btnRecharge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rechargePoints = Integer.parseInt(tfRechargePoints.getText());
                    if (rechargePoints < 0) {
                        JOptionPane.showMessageDialog(null, "포인트는 0보다 작을 수 없습니다.");
                    } else {
                        currentPoints += rechargePoints;
                        lblCurrentPoints.setText("현재 내 포인트: " + currentPoints);
                        tfRechargePoints.setText(""); // 입력 필드 초기화
                        PointsManager.savePoints(currentUser.getId(), currentPoints); // 포인트를 파일에 저장
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "유효한 숫자를 입력해주세요.");
                }
            }
        });

        setVisible(true);
    }

    private void addMouseHoverEffect(JButton button, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = button.getBackground();

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor); // 마우스를 가져다 댔을 때 색상 변경
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
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
            return new Dimension(400, 300); // 패널 크기 조정
        }
    }
}
