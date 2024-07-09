// 마이페이지 구성
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPage extends JPanel {
    private JTabbedPane tabbedPane;

    // 버튼 생성 및 추가
    JButton btnRechargePoints = new JButton("포인트 충전하러 가기~");
    JButton btnOrderHistory = new JButton("주문 내역 확인");

    JPanel mainPanel = new JPanel();

    Font koreanFont = new Font("Malgun Gothic", Font.PLAIN, 24); // 한글 폰트 설정

    public MyPage() {
        // 메인 패널 생성
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        // 버튼 크기 조정
        Dimension buttonSize = new Dimension(200, 50);
        btnRechargePoints.setPreferredSize(buttonSize);
        btnOrderHistory.setPreferredSize(buttonSize);

        mainPanel.add(btnRechargePoints, gbc);
        mainPanel.add(btnOrderHistory, gbc);

        btnRechargePoints.addActionListener(new ActionListener() { // 포인트 충전하러 가기
            @Override
            public void actionPerformed(ActionEvent e) {
                openRechargePointsPage();
            }
        });

        btnOrderHistory.addActionListener(new ActionListener() { // 주문 내역 확인
            @Override
            public void actionPerformed(ActionEvent e) {
                openOrderHistoryPage();
            }
        });

        // 메인 패널을 현재 JPanel에 추가
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public void openRechargePointsPage() {
        this.setVisible(false); // 현재 창 숨기기
        new RechargePointsPage(this); // 포인트 충전 페이지 열기
    }

    public void openOrderHistoryPage() {
        this.setVisible(false); // 현재 창 숨기기
        new OrderHistoryPage(this); // 주문 내역 페이지 열기
    }
}
