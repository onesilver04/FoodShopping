import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryPage extends JFrame {
    private static final String ORDERS_FILE = "orders.txt";
    private ProductDatabase productDatabase;

    public OrderHistoryPage(MyPage mainPage) {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return; // 창을 열지 않음
        }

        setTitle("Order History_Check Order");
        setSize(800, 600);
        setLocationRelativeTo(null); // 화면 중앙에 위치

        productDatabase = new ProductDatabase(); // ProductDatabase 초기화

        // 패널 설정
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE); // 배경 흰색으로 설정

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
                if (column == 0) {
                    return ImageIcon.class;
                } else {
                    return String.class;
                }
            }
        };

        List<Object[]> orderHistory = loadOrderHistory(currentUser.getId());
        for (Object[] order : orderHistory) {
            model.addRow(order);
        }

        // 텍스트 셀 높이 중앙 정렬 설정
        table.setDefaultRenderer(String.class, new TextAreaRenderer());

        // 테이블 크기 및 속성 설정
        table.setFont(new Font("G마켓 산스 TTF Medium", Font.PLAIN, 18)); // table 텍스트
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setRowHeight(160); // 각 셀의 높이
        table.setBackground(Color.WHITE); // 테이블 배경 흰색으로 설정
        table.setGridColor(Color.LIGHT_GRAY); // 그리드 색상 설정

        // 테이블 헤더 색상 설정
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(183, 240, 177)); // 헤더 배경색 설정
        header.setForeground(Color.BLACK); // 헤더 텍스트 색상 설정
        header.setFont(new Font("G마켓 산스 TTF Medium", Font.BOLD, 18)); // 헤더 텍스트 폰트 설정

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
        btnBack.setFont(new Font("G마켓 산스 TTF Medium", Font.PLAIN, 16));
        btnBack.setBackground(new Color(47, 157, 39)); // 버튼 배경색
        btnBack.setBorder(new LineBorder(Color.LIGHT_GRAY));
        addMouseHoverEffect(btnBack, new Color(183, 240, 177));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE); // 배경 흰색으로 설정
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

    private List<Object[]> loadOrderHistory(String memberId) {
        List<Object[]> orderHistory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(memberId + ",")) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String productName = parts[1];
                        Product product = productDatabase.searchProductByName(productName);
                        String imagePath = (product != null) ? product.getImagePath().replace("\\", "/") : "images/null.png";  // : 뒤에 널 이미지 경로 넣기
                        ImageIcon imageIcon = resizeImageIcon(new ImageIcon(imagePath), 170, 170); // 이미지 크기 조정
                        String orderDetails = String.format("<html>상품명: <span style='font-family: \"G마켓 산스 TTF Medium\"; font-size: 16px;'>%s</span><br><br>수량: %s<br>가격: %s<br>총 결제 금액: %s</html>",
                                parts[1], parts[2], parts[3], parts[4]);
                        orderHistory.add(new Object[]{imageIcon, orderDetails});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderHistory;
    }

    // 이미지 크기 조정 메서드
    private ImageIcon resizeImageIcon(ImageIcon imageIcon, int width, int height) {
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    // 내부 클래스 TextAreaRenderer
    class TextAreaRenderer extends JLabel implements TableCellRenderer {
        public TextAreaRenderer() {
            setOpaque(true); // 배경을 채우기 위해 필요
            setFont(new Font("G마켓 산스 TTF Medium", Font.PLAIN, 17)); // 기본 table 텍스트
            setVerticalAlignment(SwingConstants.CENTER); // 텍스트를 세로로 중앙 정렬
            setHorizontalAlignment(SwingConstants.LEFT); // 텍스트를 가로로 왼쪽 정렬
            setBackground(Color.WHITE); // 셀 배경 흰색으로 설정
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    private void addMouseHoverEffect(JButton button, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = button.getBackground();

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor); // 마우스를 가져다 댔을 때 색상 변경
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(originalColor); // 마우스를 뗐을 때 원래 색상으로 복구
            }
        });
    }

    public static void main(String[] args) {
        // 이 부분은 테스트를 위해 필요한 클래스와 메서드를 포함합니다.
        new OrderHistoryPage(new MyPage());
    }
}
