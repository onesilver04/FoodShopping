// 주문 내역 확인 코드
import javax.swing.*;
import java.awt.*;

public class OrderHistoryPage extends JFrame {
    public OrderHistoryPage(MyPage mainPage) {
        setTitle("Order History");
        setSize(800, 600);
        setLocationRelativeTo(null); // 화면 중앙에 위치

        // 패널 설정
        JPanel panel = new JPanel(new BorderLayout());

        // 표 데이터 생성
        String[] columnNames = {"주문 내역"};
        Object[][] data = {
            {"주문 1"},
            {"주문 2"},
            {"주문 3"}
        };

        // JTable 생성
        JTable table = new JTable(data, columnNames) {
            // 모든 셀을 수정 불가능하도록 설정
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 테이블 크기 설정
        table.setPreferredScrollableViewportSize(new Dimension(700, 400));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // 테이블을 패널의 중앙에 추가
        panel.add(scrollPane, BorderLayout.CENTER);

        // 돌아가기 버튼 생성 및 하단 중앙에 추가
        JButton btnBack = new JButton("돌아가기");
        btnBack.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnBack);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 돌아가기 버튼 클릭 이벤트
        btnBack.addActionListener(e -> {
            mainPage.setVisible(true);
            dispose(); // 현재 창 닫기
        });

        add(panel);
        setVisible(true);
    }
}
