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
		MainTabContent mainTabContent = new MainTabContent();
        tabbedPane.addTab("메인", mainTabContent.getMainPanel());
        tabbedPane.addTab("로그인/회원가입", new JPanel());
        tabbedPane.addTab("마이페이지", new JPanel());
		CartTabContent cartTabContent = new CartTabContent();
        tabbedPane.addTab("장바구니", cartTabContent.getMainPanel());
        tabbedPane.addTab("배송 조회", new JPanel());
        tabbedPane.addTab("이벤트", new JPanel());
		tabbedPane.addTab("로그아웃", new JPanel());
        
        jf.add(tabbedPane, BorderLayout.CENTER);
    }
}
