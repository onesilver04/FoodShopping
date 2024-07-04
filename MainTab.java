import javax.swing.*;
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

        tabbedPane.addTab("마이페이지", new JPanel());

        CartTabContent cartTabContent = new CartTabContent();
        tabbedPane.addTab("장바구니", cartTabContent.getMainPanel());
        tabbedPane.addTab("배송 조회", new JPanel());
        tabbedPane.addTab("이벤트", new JPanel());
        tabbedPane.addTab("로그아웃", new JPanel());

        jf.add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new MainTab("메인 화면");
    }
}

