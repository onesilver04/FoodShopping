// '로그아웃' 구성하는 실제 코드
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutPage extends JPanel {
    private JButton logoutButton;
	JPasswordField passwordField;
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

    private void handleLogout() { // 로그아웃 처리하는 함수
        passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
            this,
            passwordField,
            "비밀번호를 입력하세요:",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            // 비밀번호 검증 로직 (여기서는 "password"가 맞는 비밀번호라고 가정합니다)
            if (password.equals("password")) {
                JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
                // 실제 로그아웃 로직을 여기에 추가: 메인 페이지로 이동
                openMainTab();
            } else {
                JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.");
            }
        }
    }
	
	// 페이지 전환 메소드
	public void openMainTab(){
		tabbedPane.setSelectedIndex(0);	
	}
}