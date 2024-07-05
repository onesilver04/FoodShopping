import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CartTabContent {
    private JPanel mainPanel;
    private JPanel cartPanel, summaryPanel;
    private JLabel orderAmountLabel;
    private JLabel discountLabel;
    private JLabel totalAmountLabel;
    private JButton orderButton;
    private ProductDatabase productDB;

    public CartTabContent() {
        productDB = new ProductDatabase();
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(800, 400)); // 메인 패널 크기 조정

        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // 가로 스크롤바 비활성화
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryPanel.setPreferredSize(new Dimension(200, 400)); // 요약 패널 크기 조정
        mainPanel.add(summaryPanel, BorderLayout.EAST);

        orderAmountLabel = new JLabel("주문금액: 0원");
        orderAmountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        discountLabel = new JLabel("상품할인: 0원");
        discountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalAmountLabel = new JLabel("결제예정금액: 0원");
        totalAmountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        summaryPanel.add(orderAmountLabel);
        summaryPanel.add(discountLabel);
        summaryPanel.add(totalAmountLabel);

        orderButton = new JButton("주문하기");
        orderButton.setBackground(Color.RED);
        orderButton.setForeground(Color.WHITE);
        summaryPanel.add(Box.createVerticalGlue()); // 버튼을 아래로 밀기 위해 추가
        summaryPanel.add(orderButton);

        loadCartItems();

        orderButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "주문이 완료되었습니다.");
            clearCart();
            // 주문 완료 후 장바구니 비우기
            cartPanel.removeAll();
            updateSummaryPanel(0, 0, 0);
            cartPanel.revalidate();
            cartPanel.repaint();
        });
    }

    public void loadCartItems() {
        cartPanel.removeAll(); // 기존 패널 제거
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            List<String> cartItems = currentUser.getCartItems();
            int totalOrderAmount = 0;
            int totalDiscount = 0;
            int totalAmount = 0;

            for (String item : cartItems) {
                String[] parts = item.split(",");
                String productName = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                Product product = productDB.searchProductByName(productName);
                if (product != null) {
                    int productTotal = product.getSalePrice() * quantity;
                    totalOrderAmount += product.getPrice() * quantity;
                    totalDiscount += (product.getPrice() - product.getSalePrice()) * quantity;
                    totalAmount += productTotal;

                    cartPanel.add(createProductPanel(product, quantity));
                }
            }

            updateSummaryPanel(totalOrderAmount, totalDiscount, totalAmount);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "로그인이 필요합니다.");
        }
        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private JPanel createProductPanel(Product product, int initialQuantity) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setMaximumSize(new Dimension(500, 100)); // 패널 크기 조정
        panel.setPreferredSize(new Dimension(500, 100)); // 패널 크기 조정
        panel.setMinimumSize(new Dimension(500, 100)); // 패널 크기 조정

        // 왼쪽 패널 (이미지와 체크박스)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(120, 100)); // 이미지 패널 크기 조정

        JCheckBox checkBox = new JCheckBox();
        leftPanel.add(checkBox, BorderLayout.NORTH);

        ImageIcon imageIcon = new ImageIcon(product.getImagePath());
        Image image = imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        panel.add(leftPanel, BorderLayout.WEST);

        // 중앙 패널 (상품명)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        centerPanel.add(nameLabel, new GridBagConstraints());

        panel.add(centerPanel, BorderLayout.CENTER);

        // 오른쪽 패널 (가격, 수량, 삭제 버튼)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(180, 100)); // 오른쪽 패널 크기 조정

        JPanel priceQuantityPanel = new JPanel(new GridLayout(2, 1));
        priceQuantityPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel priceLabel = new JLabel(product.getSalePrice() + "원");
        priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel quantityPanel = new JPanel(new FlowLayout());
        final int[] quantity = {initialQuantity}; // quantity를 배열로 감싸서 effectively final로 만듭니다.
        JTextField quantityField = new JTextField(String.valueOf(quantity[0]), 4);
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setEditable(false); // 사용자가 입력하지 못하도록 비활성화

        Border border = quantityField.getBorder();

        JLabel minusLabel = new JLabel("-");
        minusLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        minusLabel.setBorder(border);
        minusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (quantity[0] > 0) {
                    quantity[0]--;
                    quantityField.setText(String.valueOf(quantity[0]));
                }
            }
        });

        JLabel plusLabel = new JLabel("+");
        plusLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        plusLabel.setBorder(border);
        plusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (quantity[0] < product.getStock()) {
                    quantity[0]++;
                    quantityField.setText(String.valueOf(quantity[0]));
                } else {
                    JOptionPane.showMessageDialog(panel, "재고가 부족합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        quantityPanel.add(minusLabel);
        quantityPanel.add(quantityField);
        quantityPanel.add(plusLabel);

        priceQuantityPanel.add(priceLabel);
        priceQuantityPanel.add(quantityPanel);

        rightPanel.add(priceQuantityPanel, BorderLayout.CENTER);

        ImageIcon trashImageIcon = new ImageIcon("images/trash.png");
        Image trashImage = trashImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon tscaledImageIcon = new ImageIcon(trashImage);
        JButton deleteButton = new JButton(tscaledImageIcon);
        deleteButton.setPreferredSize(new Dimension(20, 20));

        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 중앙으로 조정
        bottomRightPanel.add(deleteButton);

        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void updateSummaryPanel(int orderAmount, int discount, int totalAmount) {
        orderAmountLabel.setText("주문금액: " + orderAmount + "원");
        discountLabel.setText("상품할인: " + discount + "원");
        totalAmountLabel.setText("결제예정금액: " + totalAmount + "원");
    }

    private void clearCart() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentUser.getId() + "_cart.txt"))) {
                bw.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentUser.getCartItems().clear();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}

