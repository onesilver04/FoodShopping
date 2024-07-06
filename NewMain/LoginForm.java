import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
    private JTabbedPane tabbedPane;

    public JPanel getLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel l1 = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(l1, gbc);

        JTextField idField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(idField, gbc);

        JLabel l2 = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(l2, gbc);

        JPasswordField passwordField = new JPasswordField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("로그인");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        JButton registerButton = new JButton("회원가입");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(passwordField.getPassword());

                if (CheckMember.validateLogin(id, password)) {
                    JOptionPane.showMessageDialog(panel, "로그인 성공!");
                    idField.setText("");
                    passwordField.setText("");
                    
                    // 로그인 성공 시 SessionManager를 통해 사용자 정보를 저장
                    Member loggedInUser = CheckMember.getMemberById(id);
                    SessionManager.getInstance().login(loggedInUser);

                    openMainTab();
                } else {
                    JOptionPane.showMessageDialog(panel, "로그인 실패! 아이디 또는 비밀번호를 확인해주세요.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMembershipFormTab(); // 회원가입 창을 보여줍니다.
            }
        });

        return panel;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    private void openMainTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(0); // 메인 탭으로 이동
        }
    }

    public void openMembershipFormTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("로그인");
        LoginForm loginForm = new LoginForm();
        frame.setContentPane(loginForm.getLoginPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
