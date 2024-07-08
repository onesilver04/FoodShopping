
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.table.DefaultTableModel;

public class Delivery extends JPanel {
    private JTabbedPane tabbedPane;
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;

    public Delivery() {
        setLayout(new BorderLayout());

        // 테이블 모델 생성
        String[] columnNames = {"주문 번호", "고객 이름", "운송장 번호", "예상 도착일"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // 테이블 생성
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200)); // 테이블의 크기를 설정

        // 테이블 패널 생성
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 버튼 패널 (위쪽)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // 버튼 추가
        JButton addButton = new JButton("추가");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        JButton editButton = new JButton("수정");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRow();
            }
        });

        JButton trackButton = new JButton("추적");
        trackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTrackingURL();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(trackButton);

        tablePanel.add(buttonPanel, BorderLayout.NORTH);

        // 아이콘 패널 (아래쪽)
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 가운데 정렬, 수평 간격 20, 수직 간격 10

        // 사진 4개 추가
        iconPanel.add(createImageLabel("ordered.png", "배송 준비 중"));
        iconPanel.add(createImageLabel("out_for_delivery.png", "배송 중"));
        iconPanel.add(createImageLabel("in_transit.png", "배송 완료"));
        iconPanel.add(createImageLabel("delivered.png", "배송 실패"));

        // 메인 패널에 테이블 패널과 아이콘 패널 추가
        add(tablePanel, BorderLayout.CENTER);
        add(iconPanel, BorderLayout.SOUTH);
    }

 // ImageIcon을 생성하기 위한 예제 코드
    private JLabel createImageLabel(String relativePath, String text) {
        // 파일 경로를 상대 경로로 지정
        ImageIcon icon = new ImageIcon(relativePath);
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            JLabel label = new JLabel(text, icon, JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            return label;
        } else {
            System.err.println("Couldn't load file: " + relativePath);
            return new JLabel(text);
        }
    }


    private void addRow() {
        tableModel.addRow(new Object[]{"", "", "", ""});
    }

    private void editRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String orderNumber = JOptionPane.showInputDialog("주문 번호:", tableModel.getValueAt(selectedRow, 0));
            String customerName = JOptionPane.showInputDialog("고객 이름:", tableModel.getValueAt(selectedRow, 1));
            String trackingNumber = JOptionPane.showInputDialog("운송장 번호:", tableModel.getValueAt(selectedRow, 2));
            String estimatedDeliveryDate = JOptionPane.showInputDialog("예상 도착일:", tableModel.getValueAt(selectedRow, 3));
            tableModel.setValueAt(orderNumber, selectedRow, 0);
            tableModel.setValueAt(customerName, selectedRow, 1);
            tableModel.setValueAt(trackingNumber, selectedRow, 2);
            tableModel.setValueAt(estimatedDeliveryDate, selectedRow, 3);
        }
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    private void openTrackingURL() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String trackingNumber = (String) tableModel.getValueAt(selectedRow, 2); // 운송장 번호 열에서 값을 가져옴
            String url = "https://trace.cjlogistics.com/web/detail.jsp?trackingNumber=" + trackingNumber;
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "URL을 여는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
