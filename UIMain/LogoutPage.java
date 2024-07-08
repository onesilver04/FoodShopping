import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutPage extends JPanel {
    private JButton logoutButton;
    private JTabbedPane tabbedPane;

    public LogoutPage(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        logoutButton = new JButton("로그아웃을 하려면 이 버튼을 눌러주세요");
        logoutButton.setPreferredSize(new Dimension(300, 50));

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(logoutButton, gbc);
    }

    public void handleLogout() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "로그인이 되어 있지 않습니다.");
            return;
        }

        // 현재 사용자 로그아웃
        SessionManager.getInstance().logout();
        JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
        openMainTab(); // 로그아웃 후 메인 페이지로 이동
    }

    // 페이지 전환 메소드
    public void openMainTab() {
        tabbedPane.setSelectedIndex(0);
    }
}
