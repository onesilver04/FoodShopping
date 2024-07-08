// 포인트 충전 페이지(개인정보와 연결 완료)
import javax.swing.*;
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

        // 패널 설정
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 중앙에 빈 공간 추가
        panel.add(Box.createVerticalGlue(), BorderLayout.CENTER);

        // 포인트 관련 컴포넌트 추가
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3행 1열, 세로 간격 10

        Font koreanFont = new Font("Malgun Gothic", Font.PLAIN, 24); // 한글 폰트 설정
	
        lblCurrentPoints = new JLabel("현재 내 포인트: " + currentPoints);
        lblCurrentPoints.setHorizontalAlignment(SwingConstants.CENTER);
        lblCurrentPoints.setFont(koreanFont); // 폰트, 텍스트 크기 설정
        centerPanel.add(lblCurrentPoints);

        tfRechargePoints = new JTextField(); // 충전할 포인트 입력받을 필드
        tfRechargePoints.setHorizontalAlignment(JTextField.CENTER); // 텍스트 중앙 정렬
        tfRechargePoints.setFont(koreanFont);
        centerPanel.add(tfRechargePoints);

        btnRecharge = new JButton("OK");
        btnRecharge.setFont(koreanFont);
        centerPanel.add(btnRecharge);

        panel.add(centerPanel, BorderLayout.CENTER);

        // 돌아가기 버튼 생성 및 하단 중앙에 추가
        btnBack = new JButton("돌아가기");
        btnBack.setFont(koreanFont);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnBack);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 돌아가기 버튼 클릭 이벤트
        btnBack.addActionListener(e -> {
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

        add(panel);
        setVisible(true);
    }
}
