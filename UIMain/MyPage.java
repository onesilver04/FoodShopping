import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPage extends JPanel {
    private JTabbedPane tabbedPane;
    private JLabel pointsLabel; // 포인트 라벨을 클래스 레벨로 선언

    // 버튼 생성 및 추가
    JButton btnRechargePoints = new JButton("포인트 충전");
    JButton btnOrderHistory = new JButton("주문 내역 확인");

    JPanel mainPanel = new JPanel();

    Font koreanFont = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 24); // 한글 폰트 설정

    public MyPage() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return; // 패널을 생성하지 않고 종료
        }

        int currentPoints = PointsManager.loadPoints(currentUser.getId()); // 파일에서 포인트를 불러오기

        // 메인 패널 생성
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE); // 메인 패널 배경 흰색으로 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER; // 중앙 정렬

        // 버튼 크기 및 스타일 조정
        Dimension buttonSize = new Dimension(200, 50);

        btnRechargePoints.setPreferredSize(buttonSize);
        btnRechargePoints.setBackground(new Color(183, 240, 177)); // 포인트 충전 버튼 배경색
        btnRechargePoints.setBorder(new LineBorder(Color.LIGHT_GRAY));

        btnOrderHistory.setPreferredSize(buttonSize);
        btnOrderHistory.setBackground(new Color(183, 240, 177)); // 주문 내역 확인 버튼 배경색
        btnOrderHistory.setBorder(new LineBorder(Color.LIGHT_GRAY));

        // 각 버튼에 이벤트 처리
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

        // 프로필 패널 생성
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setBackground(Color.WHITE); // 배경 흰색으로 설정

        // 프로필 사진
        JLabel profileImageLabel = new JLabel();
        ImageIcon profileImage = new ImageIcon("images/profile.jpg"); // 상대 경로를 사용하여 이미지 로드
        if (profileImage.getIconWidth() == -1) {
            System.err.println("이미지를 로드할 수 없습니다: images/profile.jpg");
        } else {
            Image image = profileImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            profileImage.setImage(image);
            profileImageLabel.setIcon(profileImage);
        }
        profileImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(profileImageLabel);

        // 아이디 라벨
        JLabel idLabel = new JLabel(currentUser.getId());
        idLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 20));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        idLabel.setBackground(Color.WHITE); // 배경 흰색으로 설정
        profilePanel.add(idLabel);
		
		// empty 라벨
        JLabel emptyLabel = new JLabel(" ");
        emptyLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 20));
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyLabel.setBackground(Color.WHITE); // 배경 흰색으로 설정
        profilePanel.add(emptyLabel);


        // 현재 포인트 라벨 추가
        pointsLabel = new JLabel("현재 포인트: " + currentPoints + "점");
        pointsLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.PLAIN, 16));
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pointsLabel.setBackground(Color.WHITE); // 배경 흰색으로 설정
        profilePanel.add(pointsLabel);

        // 프로필 패널을 메인 패널에 추가
        gbc.gridy = 0;
        mainPanel.add(profilePanel, gbc); // 프로필 패널을 맨 위에 추가

        // 버튼 패널 생성 및 추가
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE); // 배경 흰색으로 설정
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 10, 10, 10);
        buttonGbc.gridx = 0;
        buttonGbc.gridy = GridBagConstraints.RELATIVE;
        buttonGbc.anchor = GridBagConstraints.CENTER;

        buttonPanel.add(btnRechargePoints, buttonGbc);
        buttonPanel.add(btnOrderHistory, buttonGbc);

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc); // 버튼 패널을 아래에 추가

        // 메인 패널을 현재 JPanel에 추가
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.setBackground(Color.WHITE); // MyPage 패널 배경 흰색으로 설정
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    // 포인트 업데이트 메서드 추가
    public void updatePoints() {
        int currentPoints = PointsManager.loadPoints(SessionManager.getInstance().getCurrentUser().getId());
        pointsLabel.setText("현재 포인트: " + currentPoints + "점");
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
        new OrderHistoryPage(this); // 주문 내역 페이지 열기
    }
}
