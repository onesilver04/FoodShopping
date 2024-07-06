// 주문 내역 확인 코드
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class OrderHistoryPage extends JFrame {
    private static final String ORDERS_FILE = "orders.txt";

    public OrderHistoryPage(MyPage mainPage) {
        setTitle("Order History_Check Order");
        setSize(800, 600);
        setLocationRelativeTo(null); // 화면 중앙에 위치

        // 패널 설정
        JPanel panel = new JPanel(new BorderLayout());

        // 표 데이터 생성
        String[] columnNames = {"대표 상품 이미지", "주문 내역"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };

        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            List<String[]> orderHistory = loadOrderHistory(currentUser.getId());
            for (String[] order : orderHistory) {
                model.addRow(order);
            }
        } else {
            JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }

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

    private List<String[]> loadOrderHistory(String memberId) {
        List<String[]> orderHistory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(memberId + ",")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String imagePath = "path/to/default/image.jpg"; // 기본 이미지 경로 설정
                        String orderDetails = String.format("상품명: %s\n수량: %s\n가격: %s\n총 결제 금액: %s",
                                parts[1], parts[2], parts[3], parts[4]);
                        orderHistory.add(new String[]{imagePath, orderDetails});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderHistory;
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
