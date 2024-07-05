// 주문 내역 확인 코드
import javax.swing.*;
import java.awt.*;
import javax.swing.table.TableColumn; // 열 너비 조정용
import javax.swing.table.TableCellRenderer; // 텍스트 줄바꿈용

public class OrderHistoryPage extends JFrame {
    public OrderHistoryPage(MyPage mainPage) {
        setTitle("Order History_Check Order");
        setSize(800, 600);
        setLocationRelativeTo(null); // 화면 중앙에 위치

        // 패널 설정
        JPanel panel = new JPanel(new BorderLayout());

        // 표 데이터 생성
        String columnName[] = {"대표 상품 이미지", "주문 내역"};
        Object[][] data = {
            {new ImageIcon("C:/Users/SM-PC/Desktop/OPPcode/Images/sm_logo.png"), "주문 상품 1\n주문 날짜 1\n총액 1"},
            {new ImageIcon("path/to/image2.jpg"), "주문 상품 2\n주문 날짜 2\n총액 2"},
            {new ImageIcon("path/to/image3.jpg"), "주문 상품 3\n주문 날짜 3\n총액 3"},
            {new ImageIcon("path/to/image4.jpg"), "주문 상품 4\n주문 날짜 4\n총액 4"},
			{new ImageIcon("path/to/image5.jpg"), "주문 상품 5\n주문 날짜 5\n총액 5"}
        };

        // JTable 생성
        JTable table = new JTable(data, columnName) {
            // 모든 셀을 수정 불가능하도록 설정
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
			
			// 각 셀에 대한 렌더러 설정(이미지, 문자열을 따로 인식하기 위함)
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
			
        };
        
		// 텍스트 줄바꿈을 위해 셀 렌더러 설정
        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
		
        // 테이블 크기 및 속성 설정
		table.setFont(new Font("Malgun Gothic", Font.PLAIN, 20)); // table 텍스트
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setRowHeight(160); // 각 셀의 높이
		
		// 열 너비 설정
		TableColumn imageColumn = table.getColumnModel().getColumn(0);
        imageColumn.setPreferredWidth(100);
        TableColumn textColumn = table.getColumnModel().getColumn(1);
        textColumn.setPreferredWidth(500);
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
	
	// 내부 클래스 TextAreaRenderer
    class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setFont(new Font("Malgun Gothic", Font.PLAIN, 20)); // table 텍스트
            return this;
        }
	}
}
