// 결제 페이지 코드
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentPage extends JFrame {
    JLabel couponLabel, totalAmountLabel, totalAmountValue, productImageLabel, productLabel;
    JButton pointButton, cardButton, kakaoPayButton, bankTransferButton, changeAddressButton;
    JComboBox<String> couponComboBox;
    JPanel productPanel, mainPanel, addressPanel, couponPanel, paymentPanel, totalAmountPanel, buttonPanel, productInfoPanel;
    JScrollPane productScrollPane;
    JTextArea addressTextArea;
    int totalAmount; // 할인 적용된 금액 예정
    int originalTotalAmount; // 총 금액 원본 저장(데이터 받아오기)

    // 여러 상품 처리 생성자
    public PaymentPage(List<Product> products, List<Integer> quantities) {
        initialize(products, quantities);
    }

    // 단일 상품 처리 생성자
    public PaymentPage(Product product, int quantity) {
        List<Product> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        products.add(product);
        quantities.add(quantity);
        initialize(products, quantities);
    }

    private void initialize(List<Product> products, List<Integer> quantities) {
        setTitle("결제 페이지");
        setSize(800, 600); // 창 사이즈
        setLocationRelativeTo(null); // 창을 화면 중앙에 위치

        originalTotalAmount = 0;
        totalAmount = 0;
        for (int i = 0; i < products.size(); i++) {
            originalTotalAmount += products.get(i).getSalePrice() * quantities.get(i);
            totalAmount += products.get(i).getSalePrice() * quantities.get(i);
        }

        // 메인 패널 설정
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // 주소 패널
        addressPanel = new JPanel();
        addressPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        addressPanel.setBorder(BorderFactory.createTitledBorder("배송지"));
        addressPanel.setMaximumSize(new Dimension(800, 70));

        // 텍스트 파일에서 주소 읽기
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        String address = currentUser != null ? currentUser.getAddress() : "변경버튼을 눌러서 배송받을 주소를 입력해주세요";
		
        addressTextArea = new JTextArea(address);
        addressTextArea.setEditable(false);
        addressTextArea.setLineWrap(true);
        addressTextArea.setWrapStyleWord(true);
        addressTextArea.setPreferredSize(new Dimension(600, 50));

        JScrollPane scrollPane = new JScrollPane(addressTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 50)); // 스크롤 팬 크기 조정

        // 주소 패널의 상단에 스크롤 추가
        addressPanel.add(scrollPane);

        // 주소 변경 버튼 이벤트 처리
        changeAddressButton = new JButton("변경");
        changeAddressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newAddress = JOptionPane.showInputDialog(PaymentPage.this, "새로운 주소를 입력하세요:", "주소 변경", JOptionPane.PLAIN_MESSAGE);
                if (newAddress != null && !newAddress.isEmpty()) {
                    addressTextArea.setText(newAddress);
                }
            }
        });

        // 버튼을 주소 패널에 추가
        addressPanel.add(changeAddressButton);

        mainPanel.add(addressPanel); // 메인 패널에 주소 패널 추가

        // 주문상품 패널
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBorder(BorderFactory.createTitledBorder("주문상품"));

        // 여러 상품 추가
        for (int i = 0; i < products.size(); i++) {
            addProduct(products.get(i).getImagePath(), products.get(i).getName(), quantities.get(i), products.get(i).getSalePrice());
        }

        productScrollPane = new JScrollPane(productPanel);
        productScrollPane.setPreferredSize(new Dimension(700, 100)); // 스크롤 패널 크기 설정(상품이 몇 개 없을 경우 스크롤이 보이지 않음)
        mainPanel.add(productScrollPane); // 메인 패널에 스크롤 옵션 추가

        // 쿠폰 & 할인 패널
        couponPanel = new JPanel(new BorderLayout());
        couponPanel.setBorder(BorderFactory.createTitledBorder("쿠폰 & 할인"));
        couponPanel.setMaximumSize(new Dimension(800, 80)); // 쿠폰 선택 라벨 크기 조정

        couponLabel = new JLabel("사용 가능한 쿠폰");
        couponLabel.setPreferredSize(new Dimension(10, 40)); // 라벨 크기 조정
        couponPanel.add(couponLabel, BorderLayout.NORTH);

        // 쿠폰 적용 JComboBox
        couponComboBox = new JComboBox<>(); // ItemEvent
        couponComboBox.addItem("신규 가입 쿠폰(10%)");
        couponComboBox.addItem("여름 맞이 쿠폰(5%)");
        couponComboBox.setPreferredSize(new Dimension(200, 20)); // JComboBox 크기 조정
        couponPanel.add(couponComboBox, BorderLayout.CENTER);
        couponPanel.setPreferredSize(new Dimension(400, 100)); // 쿠폰 선택 combobox 패널 사이즈 조정
        mainPanel.add(couponPanel); // 메인 패널에 쿠폰 패널 추가

        // 쿠폰 선택 이벤트 처리
        couponComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCoupon = (String) couponComboBox.getSelectedItem();
                if (selectedCoupon != null) {
                    if (selectedCoupon.contains("10%")) {
                        totalAmount = originalTotalAmount - (originalTotalAmount * 10 / 100);
                    } else if (selectedCoupon.contains("5%")) {
                        totalAmount = originalTotalAmount - (originalTotalAmount * 5 / 100);
                    } else {
                        totalAmount = originalTotalAmount;
                    }
                    totalAmountValue.setText(totalAmount + " ₩");
                }
            }
        });

        // 총 주문 금액 및 결제 수단
        paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));

        totalAmountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalAmountLabel = new JLabel("총 주문 금액:");
        totalAmountValue = new JLabel(totalAmount + " ₩");
        totalAmountPanel.add(totalAmountLabel);
        totalAmountPanel.add(totalAmountValue);
        paymentPanel.add(totalAmountPanel);

        buttonPanel = new JPanel(new GridLayout(1, 4));
        pointButton = new JButton("포인트");
        cardButton = new JButton("카드");
        kakaoPayButton = new JButton("카카오페이");
        bankTransferButton = new JButton("무통장입금");

        buttonPanel.add(pointButton);
        buttonPanel.add(cardButton);
        buttonPanel.add(kakaoPayButton);
        buttonPanel.add(bankTransferButton);

        paymentPanel.add(buttonPanel); // 창 하단에 결제수단 버튼 구현
        mainPanel.add(paymentPanel); // 메인 패널에 결제수단 버튼 패널 추가

        // 포인트 버튼 클릭 시 입력 창
        pointButton.addActionListener(e -> {
            String point = JOptionPane.showInputDialog(PaymentPage.this, "차감할 포인트를 입력하세요:", "포인트 차감", JOptionPane.PLAIN_MESSAGE);
            if (point != null && !point.isEmpty()) {
                try {
                    int pointValue = Integer.parseInt(point);
                    int currentPoints = PointsManager.loadPoints(currentUser.getId());
                    if (currentPoints < pointValue) {
                        JOptionPane.showMessageDialog(PaymentPage.this, "포인트가 부족합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    totalAmount -= pointValue; // 총 금액에서 포인트 차감
                    if (totalAmount < 0) totalAmount = 0; // 총 금액이 0보다 작아지지 않도록 설정
                    totalAmountValue.setText(totalAmount + " ₩");
                    
                    currentPoints -= pointValue; // 사용자의 포인트 차감
                    PointsManager.savePoints(currentUser.getId(), currentPoints); // 파일에 포인트 업데이트
                    JOptionPane.showMessageDialog(PaymentPage.this, "포인트 " + pointValue + "가 차감되었습니다.", "포인트 차감", JOptionPane.INFORMATION_MESSAGE); // 포인트 차감 알림창
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PaymentPage.this, "유효한 숫자를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE); // 오류 알림 창
                }
            }
        });

        setVisible(true);
    }

    public void addProduct(String imagePath, String name, int quantity, int price) { // 상품 추가
        productInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productImageLabel = new JLabel();
        ImageIcon productImage = new ImageIcon(imagePath);
		
		Image image = productImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        productImage = new ImageIcon(image);

        productImageLabel.setIcon(productImage);
        productInfoPanel.add(productImageLabel);

        productLabel = new JLabel(String.format("   %s   %d개   %d원", name, quantity, price));
        productInfoPanel.add(productLabel);
        productPanel.add(productInfoPanel); // 상품패널에 상품 정보 표기
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
