import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PaymentPage extends JFrame {
    private JLabel addressLabel;
    private JTextField addressField;
    private JLabel productLabel;
    private JLabel couponLabel;
    private JLabel totalAmountLabel;
    private JLabel totalAmountValue;
    private JButton pointButton;
    private JButton cardButton;
    private JButton kakaoPayButton;
    private JButton bankTransferButton;
    private JComboBox<String> couponComboBox;
    private JScrollPane productScrollPane;

    public PaymentPage() {
        setTitle("결제 페이지");
        setSize(700, 500); // 창 사이즈
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메인 패널 설정
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // 주소 패널
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBorder(BorderFactory.createTitledBorder("배송지"));

        addressLabel = new JLabel("주소");
        addressPanel.add(addressLabel, BorderLayout.NORTH);

        addressField = new JTextField(20);
        addressPanel.add(addressField, BorderLayout.CENTER);

        mainPanel.add(addressPanel);

        // 주문상품 패널
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createTitledBorder("주문상품"));

        // 이미지 추가
        JLabel productImageLabel = new JLabel();
        ImageIcon productImage = new ImageIcon("/mnt/data/product_image.jpg");
        productImageLabel.setIcon(productImage);

        JPanel productInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productInfoPanel.add(productImageLabel);

        productLabel = new JLabel("   상품명1   수량    가격");
        productInfoPanel.add(productLabel);

        productPanel.add(productInfoPanel, BorderLayout.NORTH);

        productScrollPane = new JScrollPane(productPanel);
        mainPanel.add(productScrollPane);

        // 쿠폰 & 할인 패널
        JPanel couponPanel = new JPanel(new BorderLayout());
        couponPanel.setBorder(BorderFactory.createTitledBorder("쿠폰 & 할인"));

        couponLabel = new JLabel("사용 가능한 쿠폰");
        couponLabel.setPreferredSize(new Dimension(100, 20)); // 라벨 크기 조정
        couponPanel.add(couponLabel, BorderLayout.NORTH);

        // JComboBox 사용
        couponComboBox = new JComboBox<>();
        couponComboBox.addItem("신규가입쿠폰");
        couponComboBox.addItem("여름맞이 쿠폰");
        couponPanel.add(couponComboBox, BorderLayout.CENTER);

        mainPanel.add(couponPanel);

        // 총 주문 금액 및 결제 수단
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));

        JPanel totalAmountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalAmountLabel = new JLabel("총 주문 금액:");
        totalAmountValue = new JLabel("nnn ₩");
        totalAmountPanel.add(totalAmountLabel);
        totalAmountPanel.add(totalAmountValue);
        paymentPanel.add(totalAmountPanel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        pointButton = new JButton("포인트");
        cardButton = new JButton("카드");
        kakaoPayButton = new JButton("카카오페이");
        bankTransferButton = new JButton("무통장입금");

        buttonPanel.add(pointButton);
        buttonPanel.add(cardButton);
        buttonPanel.add(kakaoPayButton);
        buttonPanel.add(bankTransferButton);

        paymentPanel.add(buttonPanel);

        mainPanel.add(paymentPanel);

        // 텍스트 파일에서 주소 읽기
        String address = readAddress("/mnt/data/address.txt");
        addressField.setText(address);

        // 포인트 버튼 클릭 시 입력 창
        pointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String point = JOptionPane.showInputDialog(PaymentPage.this, "차감할 포인트를 입력하세요:", "포인트 차감", JOptionPane.PLAIN_MESSAGE);
                if (point != null && !point.isEmpty()) {
                    try {
                        int pointValue = Integer.parseInt(point);
                        // 포인트 차감 로직 추가 (예: 총 금액에서 포인트 차감 등)
                        JOptionPane.showMessageDialog(PaymentPage.this, "포인트 " + pointValue + "가 차감되었습니다.", "포인트 차감", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(PaymentPage.this, "유효한 숫자를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private String readAddress(String filePath) {
        StringBuilder address = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                address.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaymentPage paymentPage = new PaymentPage();
            paymentPage.setVisible(true);
        });
    }
}
