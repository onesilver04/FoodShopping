import javax.swing.*;

public class LogoutPage {
    private JTabbedPane tabbedPane;
    private JFrame mainFrame;

    public LogoutPage(JTabbedPane tabbedPane, JFrame mainFrame) {
        this.tabbedPane = tabbedPane;
        this.mainFrame = mainFrame;
    }

    public void handleLogout() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(mainFrame, "로그인이 되어 있지 않습니다.");
            return;
        }

        // 현재 사용자 로그아웃
        SessionManager.getInstance().logout();
        JOptionPane.showMessageDialog(mainFrame, "로그아웃 되었습니다.");
        openMainTab(); // 로그아웃 후 메인 페이지로 이동
    }

    // 페이지 전환 메소드
    private void openMainTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(0);
        }
    }
}
