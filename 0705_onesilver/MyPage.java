// '마이페이지' 구성하는 실제 코드
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPage extends JPanel {
	private JTabbedPane tabbedPane;
	
    // 버튼 생성 및 추가
    JButton btnEditInfo = new JButton("회원정보 수정하기");
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
        btnEditInfo.setPreferredSize(buttonSize);
        btnRechargePoints.setPreferredSize(buttonSize);
        btnOrderHistory.setPreferredSize(buttonSize);

        mainPanel.add(btnEditInfo, gbc);
        mainPanel.add(btnRechargePoints, gbc);
        mainPanel.add(btnOrderHistory, gbc);

        // 각 버튼에 이벤트 처리
        btnEditInfo.addActionListener(new ActionListener() { // 회원정보 수정하기
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditInfoPage();
            }
        });

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

	// 페이지 전환 메소드
    public void openEditInfoPage() {
        JOptionPane.showMessageDialog(this, "회원정보 수정 페이지로 이동합니다.");
        // 실제 페이지 전환 로직을 여기에 추가
    }

    public void openRechargePointsPage() {
        // ok버튼 눌러서 이동하고 싶다면 아래 주석 추가
        //JOptionPane.showMessageDialog(this, "포인트 충전 페이지로 이동합니다.");
        this.setVisible(false); // 현재 창 숨기기
        new RechargePointsPage(this); // 포인트 충전 페이지 열기
    }

    public void openOrderHistoryPage() {
        //JOptionPane.showMessageDialog(this, "주문 내역 확인 페이지로 이동합니다.");
        // 실제 페이지 전환 로직을 여기에 추가
        this.setVisible(false); // 현재 창 숨기기
        new OrderHistoryPage(this); // 포인트 충전 페이지 열기
	}
}