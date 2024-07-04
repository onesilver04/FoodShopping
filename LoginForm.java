import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
	private JTabbedPane tabbedPane;
		
    public JPanel getLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); // GridBagLayout 사용
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 각 컴포넌트 사이의 간격 설정

        JLabel l1 = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(l1, gbc);

        JTextField text = new JTextField(10); // 텍스트 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 텍스트 필드가 수평으로 확장되도록 설정
        panel.add(text, gbc);

        JLabel l2 = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(l2, gbc);

        JPasswordField value = new JPasswordField(10); // 패스워드 필드 크기를 줄이기 위해 열 수를 10으로 설정
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 패스워드 필드가 수평으로 확장되도록 설정
        panel.add(value, gbc);

        JButton b = new JButton("로그인");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(b, gbc);
        
        JButton rB = new JButton("회원가입");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // 2개의 컬럼에 걸쳐서 버튼 추가
        gbc.anchor = GridBagConstraints.CENTER; // 버튼을 중앙 정렬
        panel.add(rB, gbc);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = text.getText();
                String password = new String(value.getPassword());

                if (CheckMember.validateLogin(id, password)) {
                    JOptionPane.showMessageDialog(panel, "로그인 성공!");
					openMainTab();
                    // 로그인 성공 시 추가 로직: 메인 페이지로 이동+로그인 정보 유지
					openMainTab();
                } else {
                    JOptionPane.showMessageDialog(panel, "로그인 실패! 아이디 또는 비밀번호를 확인해주세요.");
                }
            }
        });

        rB.addActionListener(new ActionListener() {
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
	
	// 회원가입 후 로그인 페이지로 전환 메소드
	public void openMembershipFormTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(2);
        }
    }
	
}