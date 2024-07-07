import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MainTab {
    JFrame jf;
    JTabbedPane tabbedPane;

    public MainTab(String msg) {
        jf = new JFrame(msg);
        jf.setLayout(new BorderLayout());

        initComponents();

        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // 각 탭의 내용을 별도의 클래스에서 가져와 추가
        MainTabContent mainTabContent = new MainTabContent();
        tabbedPane.addTab("메인", mainTabContent.getMainPanel());

        LoginForm loginForm = new LoginForm();
        loginForm.setTabbedPane(tabbedPane); // TabbedPane을 LoginForm에 전달
        tabbedPane.addTab("로그인", loginForm.getLoginPanel());

        MembershipForm membershipForm = new MembershipForm();
        membershipForm.setTabbedPane(tabbedPane); // TabbedPane을 MembershipForm에 전달
        tabbedPane.addTab("회원가입", membershipForm.getMembershipPanel());
      
        MyPage myPage = new MyPage();
        myPage.setTabbedPane(tabbedPane); // TabbedPane을 MyPage에 전달
        tabbedPane.addTab("마이페이지", myPage);

        // 장바구니 탭은 빈 패널로 시작하고 선택 시 초기화
        JPanel emptyCartPanel = new JPanel();
        tabbedPane.addTab("장바구니", emptyCartPanel);

        tabbedPane.addTab("배송 조회", new JPanel());
		
		JPanel eventPagePanel = EventPage.createEventPage();
        tabbedPane.addTab("이벤트", eventPagePanel);
		
		LogoutPage logoutPage = new LogoutPage(tabbedPane);
        tabbedPane.addTab("로그아웃", logoutPage);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 4) { // 장바구니 탭이 선택된 경우
                    if (SessionManager.getInstance().getCurrentUser() != null) {
                        CartTabContent cartTabContent = new CartTabContent();
                        tabbedPane.setComponentAt(4, cartTabContent.getMainPanel());
                    } else {
                        JOptionPane.showMessageDialog(jf, "로그인이 필요합니다.");
                        tabbedPane.setSelectedIndex(1); // 로그인 탭으로 이동
                    }
                }
            }
        });

        jf.add(tabbedPane, BorderLayout.CENTER);
    }
}
