import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import javax.swing.table.JTableHeader;
import javax.swing.border.LineBorder;

public class Delivery extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane;

    public Delivery() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return; // 패널을 생성하지 않고 종료
        }

        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // 배경 흰색으로 설정

        // 테이블 모델 생성
        String[] columnNames = {"주문 물품", "수량", "운송장 번호", "배송조회"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // 배송조회 버튼만 편집 가능
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return JButton.class;
                }
                return String.class;
            }
        };

        // 프로필 패널 생성
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setBackground(Color.WHITE); // 배경 흰색으로 설정

        // 프로필 사진
        JLabel profileImageLabel = new JLabel();
        ImageIcon profileImage = new ImageIcon("images/profile.jpg"); // 상대 경로를 사용하여 이미지 로드
        if (profileImage.getIconWidth() == -1) {
            System.err.println("이미지를 로드할 수 없습니다: images/profile.jpg");
        } else {
            Image image = profileImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            profileImage.setImage(image);
            profileImageLabel.setIcon(profileImage);
        }
        profileImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(profileImageLabel);

        // 아이디 라벨
        JLabel idLabel = new JLabel(currentUser.getId());
        idLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 20));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        idLabel.setBackground(Color.WHITE); // 배경 흰색으로 설정
        profilePanel.add(idLabel);

        add(profilePanel, BorderLayout.NORTH);

        // 테이블 생성
        table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 3) {
                    return new ButtonRenderer();
                }
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 3) {
                    return new ButtonEditor(new JCheckBox());
                }
                return super.getCellEditor(row, column);
            }
        };
        table.setBackground(Color.WHITE); // 테이블 배경 흰색으로 설정
        table.setBorder(new LineBorder(Color.LIGHT_GRAY)); // 테두리 색 설정
        table.setGridColor(Color.LIGHT_GRAY); // 그리드 색 설정
        table.setPreferredScrollableViewportSize(new Dimension(300, 100)); // 테이블의 선호 크기 설정

        // 헤더 색상 변경
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(183, 240, 177)); // 헤더 배경 색 설정
        header.setForeground(Color.BLACK); // 헤더 텍스트 색 설정

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 120)); // 스크롤 팬 크기 설정
        scrollPane.getViewport().setBackground(Color.WHITE); // 테이블 뷰포트 배경 흰색으로 설정

        // 테이블 패널 생성
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBackground(Color.WHITE); // 테이블 패널 배경 흰색으로 설정
        tablePanel.setPreferredSize(new Dimension(300, 120)); // 테이블 패널 크기 설정

        // 아이콘 패널 (아래쪽)
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 가운데 정렬, 수평 간격 20, 수직 간격 10
        iconPanel.setBackground(Color.WHITE); // 배경 흰색으로 설정

        // 사진 4개 추가
        iconPanel.add(createImageLabel("images/order.jpg", "주문 완료", 45, 45));
        iconPanel.add(createImageLabel("images/out_for_delivery.jpg", "배송지 출발", 50, 50));
        iconPanel.add(createImageLabel("images/in_transit.png", "배송 중", 50, 50));
        iconPanel.add(createImageLabel("images/delivered.png", "배송 완료", 50, 50));

        // 메인 패널에 테이블 패널과 아이콘 패널 추가
        add(tablePanel, BorderLayout.CENTER);
        add(iconPanel, BorderLayout.SOUTH);

        loadOrderData(currentUser.getId()); // 로그인된 사용자의 주문 내역 불러오기
    }

    // ImageIcon을 생성하기 위한 예제 코드
    private JLabel createImageLabel(String relativePath, String text, int width, int height) {
        ImageIcon icon = new ImageIcon(relativePath);
        if (icon.getIconWidth() == -1) {
            System.err.println("이미지를 로드할 수 없습니다: " + relativePath);
            return new JLabel(text);
        } else {
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon.setImage(image);
            JLabel label = new JLabel(text, icon, JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setBackground(Color.WHITE); // 배경 흰색으로 설정
            return label;
        }
    }

    private void loadOrderData(String memberId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(memberId)) {
                    tableModel.addRow(new Object[]{parts[1], parts[2], parts[5], "배송조회"});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    private void openTrackingURL(String trackingNumber) {
        String url = "https://trace.cjlogistics.com/web/detail.jsp?trackingNumber=" + trackingNumber;
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "URL을 여는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.WHITE); // 버튼 배경 흰색으로 설정
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "배송조회" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String trackingNumber;

        public ButtonEditor(JCheckBox checkBox) {
            button = new JButton("배송조회");
            button.addActionListener(this);
            button.setBackground(Color.WHITE); // 버튼 배경 흰색으로 설정
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            trackingNumber = (String) table.getValueAt(row, 2); // 운송장 번호 열에서 값을 가져옴
            button.setText((value == null) ? "배송조회" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            openTrackingURL(trackingNumber);
            fireEditingStopped();
        }
    }
}
