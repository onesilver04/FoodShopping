import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPage extends JFrame {
    // 버튼 생성 및 추가
    JButton btnEditInfo = new JButton("회원정보 수정하기");
    JButton btnRechargePoints = new JButton("포인트 충전하러 가기~");
    JButton btnCheckOrder = new JButton("주문 내역 확인");
    JButton btnLogout = new JButton("로그아웃");

    JPanel mainPanel = new JPanel();
	
	Font koreanFont = new Font("Malgun Gothic", Font.PLAIN, 24); // 한글 폰트 설정

    public MyPage() {
        setTitle("마이 페이지");
        setSize(800, 600); // 창 사이즈 통일
        setLocationRelativeTo(null);

        // 버튼 크기 조정
        Dimension buttonSize = new Dimension(200, 50);
        btnEditInfo.setPreferredSize(buttonSize);
        btnRechargePoints.setPreferredSize(buttonSize);
        btnCheckOrder.setPreferredSize(buttonSize);
        btnLogout.setPreferredSize(buttonSize);

        // 메인 패널 생성
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        mainPanel.add(btnEditInfo, gbc);
        mainPanel.add(btnRechargePoints, gbc);
        mainPanel.add(btnCheckOrder, gbc);
        mainPanel.add(btnLogout, gbc);

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

        btnCheckOrder.addActionListener(new ActionListener() { // 주문 내역 확인
            @Override
            public void actionPerformed(ActionEvent e) {
                openCheckOrderPage();
            }
        });

        btnLogout.addActionListener(new ActionListener() { // 로그아웃
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // 메인 패널을 프레임에 추가
        add(mainPanel);

        setVisible(true);
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

    public void openCheckOrderPage() {
        JOptionPane.showMessageDialog(this, "주문 내역 확인 페이지로 이동합니다.");
        // 실제 페이지 전환 로직을 여기에 추가
    }

    public void logout() {
        JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
        // 실제 로그아웃 로직을 여기에 추가
    }

    public static void main(String[] args) {
        new MyPage();
    }
}
