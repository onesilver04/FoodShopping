import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RechargePointsPage extends JFrame {
    JButton btnBack;
    JButton btnRecharge;
    JTextField tfRechargePoints;
    JLabel lblCurrentPoints;
    JPanel panel, bottomPanel;
    int currentPoints;
    public static final String POINTS_FILE = "members.txt";

    public RechargePointsPage(MyPage mainPage) {
        currentPoints = loadPoints(); // 파일에서 포인트를 불러오기

        setTitle("포인트 충전 페이지");
        setSize(800, 600); // 창 사이즈 통일
        setLocationRelativeTo(null);

        // 패널 설정
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 중앙에 빈 공간 추가
        panel.add(Box.createVerticalGlue(), BorderLayout.CENTER);

        // 포인트 관련 컴포넌트 추가
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3행 1열, 세로 간격 10

        Font koreanFont = new Font("Malgun Gothic", Font.PLAIN, 24); // 한글 폰트 설정
	
        lblCurrentPoints = new JLabel("현재 내 포인트: " + currentPoints);
        lblCurrentPoints.setHorizontalAlignment(SwingConstants.CENTER);
        lblCurrentPoints.setFont(koreanFont); // 폰트, 텍스트 크기 설정
        centerPanel.add(lblCurrentPoints);

        tfRechargePoints = new JTextField(); // 충전할 포인트 입력받을 필드
        tfRechargePoints.setHorizontalAlignment(JTextField.CENTER); // 텍스트 중앙 정렬
        tfRechargePoints.setFont(koreanFont);
        centerPanel.add(tfRechargePoints);

        btnRecharge = new JButton("OK");
        btnRecharge.setFont(koreanFont);
        centerPanel.add(btnRecharge);

        panel.add(centerPanel, BorderLayout.CENTER);

        // 돌아가기 버튼 생성 및 하단 중앙에 추가
        btnBack = new JButton("돌아가기");
        btnBack.setFont(koreanFont);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnBack);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 돌아가기 버튼 클릭 이벤트
        btnBack.addActionListener(e -> {
            mainPage.setVisible(true);
            dispose(); // 현재 창 닫기
        });

        // 충전 버튼 클릭 이벤트
        btnRecharge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rechargePoints = Integer.parseInt(tfRechargePoints.getText());
                    if (rechargePoints < 0) {
                        JOptionPane.showMessageDialog(null, "포인트는 0보다 작을 수 없습니다.");
                    } else {
                        currentPoints += rechargePoints;
                        lblCurrentPoints.setText("현재 내 포인트: " + currentPoints);
                        tfRechargePoints.setText(""); // 입력 필드 초기화
                        savePoints(currentPoints); // 포인트를 파일에 저장
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "유효한 숫자를 입력해주세요.");
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private void savePoints(int points) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(POINTS_FILE, true))) {
            writer.write("포인트: " + points + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadPoints() {
        int points = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(POINTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("포인트: ")) {
                    points = Integer.parseInt(line.substring(5));
                }
            }
        } catch (IOException | NumberFormatException e) {
            return 0; // 파일이 없거나 읽기 오류가 발생한 경우 0으로 초기화
        }
        return points;
    }
}
