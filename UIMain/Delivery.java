import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import javax.swing.table.JTableHeader;
import javax.swing.border.LineBorder;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class Delivery extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane;

    public Delivery() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 테이블 모델 생성
        String[] columnNames = {"주문 물품", "수량", "운송장 번호", "배송 상태", "배송조회"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 4; // 운송장 번호와 배송조회 버튼만 편집 가능
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
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
        profilePanel.setBackground(Color.WHITE);

        // 프로필 사진
        JLabel profileImageLabel = new JLabel();
        ImageIcon profileImage = new ImageIcon("@@images/profile.jpg");
        if (profileImage.getIconWidth() == -1) {
            System.err.println("이미지를 로드할 수 없습니다: images/profile.jpg");
            // 기본 이미지로 대체
            profileImage = new ImageIcon("images/default_profile.jpg");
            if (profileImage.getIconWidth() == -1) {
                System.err.println("기본 이미지를 로드할 수 없습니다: images/default_profile.jpg");
            } else {
                Image image = profileImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                profileImage.setImage(image);
                profileImageLabel.setIcon(profileImage);
            }
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
        idLabel.setBackground(Color.WHITE);
        profilePanel.add(idLabel);

        add(profilePanel, BorderLayout.NORTH);

        // 테이블 생성
        table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 4) {
                    return new ButtonRenderer();
                }
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 4) {
                    return new ButtonEditor(new JCheckBox());
                }
                return super.getCellEditor(row, column);
            }
        };
        table.setBackground(Color.WHITE);
        table.setBorder(new LineBorder(Color.LIGHT_GRAY));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setPreferredScrollableViewportSize(new Dimension(300, 100));

        // 헤더 색상 변경
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(183, 240, 177));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 120));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // 테이블 패널 생성
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setPreferredSize(new Dimension(300, 120));

   
        
        // 메인 패널에 테이블 패널과 아이콘 패널 추가
        add(tablePanel, BorderLayout.CENTER);

        loadOrderData(currentUser.getId());
    }

    private JLabel createImageLabel(String relativePath, String text, int width, int height) {
        ImageIcon icon = new ImageIcon(relativePath);
        if (icon.getIconWidth() == -1) {
            System.err.println("이미지를 로드할 수 없습니다: " + relativePath);
            JLabel label = new JLabel(text);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setBackground(Color.WHITE);
            return label;
        } else {
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            icon.setImage(image);
            JLabel label = new JLabel(text, icon, JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setBackground(Color.WHITE);
            return label;
        }
    }

    private void loadOrderData(String memberId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(memberId)) {
                    String trackingNumber = parts[5];
                    tableModel.addRow(new Object[]{parts[1], parts[2], trackingNumber, "조회 필요", "배송조회"});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fetchDeliveryStatus(String trackingNumber) {
        try {
            DeliveryStatus status = DeliveryApiClient.getDeliveryStatus(trackingNumber);
            return status.getStatus();
        } catch (IOException e) {
            e.printStackTrace();
            return "조회 실패";
        }
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    private void updateDeliveryStatus(String trackingNumber, int rowIndex) {
        SwingUtilities.invokeLater(() -> {
            String updatedStatus = fetchDeliveryStatus(trackingNumber);
            tableModel.setValueAt(updatedStatus, rowIndex, 3);
        });
    }

    private void openTrackingURL(String trackingNumber, int rowIndex) {
        String url = "https://trace.cjlogistics.com/web/detail.jsp?slipno=" + trackingNumber;
        try {
            Desktop.getDesktop().browse(new URI(url));
            // 배송 상태 업데이트
            updateDeliveryStatus(trackingNumber, rowIndex);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "URL을 여는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.WHITE);
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
        private int rowIndex;

        public ButtonEditor(JCheckBox checkBox) {
            button = new JButton("배송조회");
            button.addActionListener(this);
            button.setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            trackingNumber = (String) table.getValueAt(row, 2);
            rowIndex = row;
            button.setText((value == null) ? "배송조회" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateDeliveryStatus(trackingNumber, rowIndex); // 배송 상태 업데이트
            openTrackingURL(trackingNumber, rowIndex); // URL 열기
            fireEditingStopped();
        }
    }
}
