// 메인 페이지의 '탭'들을 관리하는 클래스
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MainTab {
    JFrame jf;
    JTabbedPane tabbedPane;
    
    public MainTab(String msg) {
        jf = new JFrame(msg);
        jf.setLayout(new BorderLayout());
         
        initComponents();
		
        jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }
    
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        // 각 탭의 내용을 별도의 클래스에서 가져와 추가
		MainTabContent mainTabContent = new MainTabContent(); // 탭 클릭 시 '메인' 패널 나옴
        tabbedPane.addTab("메인", mainTabContent.getMainPanel());
        
		LoginForm loginForm = new LoginForm(); // 탭 클릭 시 '로그인/회원가입' 패널 나옴
		tabbedPane.addTab("로그인/회원가입", loginForm.getLoginPanel());
		
		MyPage myPage = new MyPage(); // 탭 클릭 시 '마이페이지' 패널 나옴
        tabbedPane.addTab("마이페이지", myPage);
		
		//CartTabContent cartTabContent = new CartTabContent();
        tabbedPane.addTab("장바구니", new JPanel());
        tabbedPane.addTab("배송 조회", new JPanel());
        tabbedPane.addTab("이벤트", new JPanel());
		
		LogoutPage logoutPage = new LogoutPage(tabbedPane); // 탭 클릭 시 '로그아웃' 패널 나옴
		tabbedPane.addTab("로그아웃", logoutPage);
        
        jf.add(tabbedPane, BorderLayout.CENTER);
    }
}
