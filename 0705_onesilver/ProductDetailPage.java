// 상품 상세 정보 페이지
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDetailPage {
    private JFrame f;
    private Product product;
    private int quantity = 0;
    private JTextField quantityField;
    private JLabel totalLabel;

    public ProductDetailPage(Product product) {
        this.product = product;
        initialize();
    }

    private void initialize() {
        f = new JFrame(product.getName() + " 상세 정보");
        f.setLayout(new BorderLayout());
        f.setSize(800, 600);
		f.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel(new BorderLayout());
        f.add(contentPanel, BorderLayout.CENTER);

        ImageIcon imageIcon = new ImageIcon(product.getImagePath());
        Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(imageLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridLayout(7, 1));

        JLabel nameLabel = new JLabel("상품명: " + product.getName());
        infoPanel.add(nameLabel);

        JLabel stockLabel = new JLabel("재고: " + product.getStock());
        infoPanel.add(stockLabel);

        JLabel priceLabel = new JLabel("상품 가격: " + product.getPrice() + "원");
        infoPanel.add(priceLabel);

        JPanel quantityPanel = new JPanel(new FlowLayout());

        Font buttonFont = new Font("Malgun Gothic", Font.BOLD, 20);

        JButton decreaseButton = new JButton("-");
        decreaseButton.setFont(buttonFont);
        decreaseButton.addActionListener(e -> {
            if (quantity > 0) {
                quantity--;
                quantityField.setText(String.valueOf(quantity));
                updateTotalLabel();
            }
        });
        quantityPanel.add(decreaseButton);

        quantityField = new JTextField(String.valueOf(quantity), 5);
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setEditable(false); // 사용자가 입력하지 못하도록 비활성화
        quantityPanel.add(quantityField);

        JButton increaseButton = new JButton("+");
        increaseButton.setFont(buttonFont);
        increaseButton.addActionListener(e -> {
             if (quantity < product.getStock()) {
                quantity++;
                quantityField.setText(String.valueOf(quantity));
                updateTotalLabel();
            } else {
                JOptionPane.showMessageDialog(f, "재고가 부족합니다.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });
        quantityPanel.add(increaseButton);

        infoPanel.add(quantityPanel);

        JLabel salePriceLabel = new JLabel("할인 가격: " + product.getSalePrice() + "원");
        infoPanel.add(salePriceLabel);

        totalLabel = new JLabel("합계: " + product.getSalePrice() * quantity + "원");
        infoPanel.add(totalLabel);

        JPanel buttonPanel = new JPanel();
        JButton addToCartButton = new JButton("장바구니 담기");
        addToCartButton.setBackground(Color.BLACK);
        addToCartButton.setForeground(Color.WHITE);
        JButton buyNowButton = new JButton("바로 구매");
        buyNowButton.setBackground(Color.RED);
        buyNowButton.setForeground(Color.WHITE);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(buyNowButton);
	//버튼 이벤트 추가하기
		
	// 바로 구매 버튼 이벤트 추가
        buyNowButton.addActionListener(e -> {
            if (quantity > 0) {
                String pw = JOptionPane.showInputDialog(f, "비밀번호를 입력하세요:", "비밀번호 확인", JOptionPane.PLAIN_MESSAGE);
                if (pw != null && !pw.isEmpty()) {
                    new PaymentPage(product, quantity, pw);
                    f.dispose(); // 현재 창 닫기
                }
            } else {
                JOptionPane.showMessageDialog(f, "수량을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });

        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    private void updateTotalLabel() {
        int totalPrice = product.getSalePrice() * quantity;
        totalLabel.setText("합계: " + totalPrice + "원");
    }
}
