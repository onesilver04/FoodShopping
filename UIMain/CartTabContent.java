import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartTabContent {
    private JPanel mainPanel;
    private JPanel cartPanel, summaryPanel;
    private JLabel orderAmountLabel;
    private JLabel discountLabel;
    private JLabel totalAmountLabel;
    private JLabel finalAmountLabel; // 최종 금액 라벨 추가
    private JButton orderButton;
    private ProductDatabase productDB;
    private List<JCheckBox> checkBoxes;

    public CartTabContent() {
        productDB = new ProductDatabase();
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(800, 400)); // 메인 패널 크기 조정

        cartPanel = new JPanel();
        cartPanel.setBackground(Color.WHITE); // 카트 패널 배경색 설정
		cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // 가로 스크롤바 비활성화
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryPanel.setPreferredSize(new Dimension(200, 400)); // 요약 패널 크기 조정
        summaryPanel.setBackground(Color.WHITE);

		mainPanel.add(summaryPanel, BorderLayout.EAST);

        orderAmountLabel = new JLabel("주문금액: 0원");
        orderAmountLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.PLAIN, 14));
        discountLabel = new JLabel("상품할인: 0원");
        discountLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.PLAIN, 14));
        totalAmountLabel = new JLabel("결제예정금액: 0원");
        totalAmountLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.PLAIN, 14));
        finalAmountLabel = new JLabel("최종금액: 0원"); // 최종 금액 라벨 초기화
        finalAmountLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.BOLD, 16));

        summaryPanel.add(orderAmountLabel);
        summaryPanel.add(Box.createVerticalStrut(10)); // 주문금액과 상품할인 사이의 간격 추가
        summaryPanel.add(discountLabel);
        summaryPanel.add(Box.createVerticalStrut(10)); // 상품할인과 결제예정금액 사이의 간격 추가
        summaryPanel.add(totalAmountLabel);
        summaryPanel.add(Box.createVerticalStrut(20)); // 결제예정금액과 최종금액 사이의 간격 추가
        summaryPanel.add(finalAmountLabel); // 최종 금액 라벨 추가
        summaryPanel.setBackground(Color.WHITE);

        orderButton = new JButton("주문하기");
        orderButton.setBackground(Color.RED);
        orderButton.setForeground(Color.WHITE);
        orderButton.setPreferredSize(new Dimension(200, 35));
        summaryPanel.add(Box.createVerticalGlue()); // 버튼을 아래로 밀기 위해 추가
        summaryPanel.add(orderButton);

        loadCartItems();

        orderButton.addActionListener(e -> handleOrder());

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private void handleOrder() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            List<String> cartItems = currentUser.getCartItems();
            List<String> selectedItems = cartItems.stream()
                    .filter(item -> {
                        String productName = item.split(",")[0];
                        for (JCheckBox checkBox : checkBoxes) {
                            if (checkBox.isSelected() && checkBox.getText().equals(productName)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "결제할 상품을 선택해주세요.");
                return;
            }
			
			List<Product> selectedProducts = new ArrayList<>();
			List<Integer> selectedQuantities = new ArrayList<>();

            int totalOrderAmount = 0;
            int totalDiscount = 0;
            int totalAmount = 0;
            int finalAmount = 0;

//            Product selectedProduct = null;
//            int selectedQuantity = 0;

            for (String item : selectedItems) {
                String[] parts = item.split(",");
                String productName = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                Product product = productDB.searchProductByName(productName);
                if (product != null) {
                    totalOrderAmount += product.getPrice() * quantity;
                    totalDiscount += (product.getPrice() - product.getSalePrice()) * quantity;
                    totalAmount += product.getSalePrice() * quantity;
                    finalAmount += product.getSalePrice() * quantity;

                    selectedProducts.add(product);
					selectedQuantities.add(quantity);
                }
            }

            if (!selectedProducts.isEmpty()) {
            PaymentPage paymentPage = new PaymentPage(selectedProducts, selectedQuantities);
            paymentPage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            paymentPage.setVisible(true);
			}

            // 주문 완료 후 선택된 항목만 장바구니에서 제거
            currentUser.getCartItems().removeAll(selectedItems);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentUser.getId() + "_cart.txt"))) {
                for (String item : currentUser.getCartItems()) {
                    bw.write(item);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            loadCartItems();
        }
    }

    public void loadCartItems() {
        cartPanel.removeAll(); // 기존 패널 제거
        checkBoxes = new ArrayList<>(); // 체크박스 목록 초기화
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

                    JPanel productPanel = createProductPanel(product, quantity);
                    cartPanel.add(productPanel);
                }
            }

            updateSummaryPanel(totalOrderAmount, totalDiscount, totalAmount);
            updateFinalAmount(); // 최종 금액 업데이트
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
        checkBox.setText(product.getName()); // 체크박스에 상품명 설정
        checkBox.addItemListener(e -> updateFinalAmount()); // 체크박스 상태 변경 리스너 추가
        checkBoxes.add(checkBox); // 체크박스 목록에 추가
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
        nameLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.BOLD, 12));
        centerPanel.add(nameLabel, new GridBagConstraints());

        panel.add(centerPanel, BorderLayout.CENTER);

        // 오른쪽 패널 (가격, 수량, 삭제 버튼)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(180, 100)); // 오른쪽 패널 크기 조정

        JPanel priceQuantityPanel = new JPanel(new GridLayout(2, 1));
        priceQuantityPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel priceLabel = new JLabel(product.getSalePrice() + "원");
        priceLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.BOLD, 12));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel quantityPanel = new JPanel(new FlowLayout());
        final int[] quantity = {initialQuantity}; // quantity를 배열로 감싸서 effectively final로 만듭니다.
        JTextField quantityField = new JTextField(String.valueOf(quantity[0]), 4);
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setEditable(false); // 사용자가 입력하지 못하도록 비활성화

        Border border = quantityField.getBorder();

        JLabel minusLabel = new JLabel("-");
        minusLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.BOLD, 12));
        minusLabel.setBorder(border);
        minusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (quantity[0] > 0) {
                    quantity[0]--;
                    quantityField.setText(String.valueOf(quantity[0]));
                    updateCartItem(product.getName(), quantity[0]);
                    updateCartSummary();
                    updateFinalAmount(); // 수량 변경 시 최종 금액 업데이트
                }
            }
        });

        JLabel plusLabel = new JLabel("+");
        plusLabel.setFont(new Font("G마켓 산스 TTF Medium", Font.BOLD, 12));
        plusLabel.setBorder(border);
        plusLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (quantity[0] < product.getStock()) {
                    quantity[0]++;
                    quantityField.setText(String.valueOf(quantity[0]));
                    updateCartItem(product.getName(), quantity[0]);
                    updateCartSummary();
                    updateFinalAmount(); // 수량 변경 시 최종 금액 업데이트
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
        deleteButton.addActionListener(e -> {
            removeItemFromCart(product, panel);
            updateFinalAmount(); // 삭제 시 최종 금액 업데이트
        }); // 삭제 버튼 클릭 시 동작

        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 중앙으로 조정
        bottomRightPanel.add(deleteButton);

        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void updateCartItem(String productName, int newQuantity) {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            List<String> cartItems = currentUser.getCartItems();
            for (int i = 0; i < cartItems.size(); i++) {
                String[] parts = cartItems.get(i).split(",");
                if (parts[0].equals(productName)) {
                    cartItems.set(i, productName + "," + newQuantity);
                    break;
                }
            }

            // 파일 업데이트
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentUser.getId() + "_cart.txt"))) {
                for (String item : cartItems) {
                    bw.write(item);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeItemFromCart(Product product, JPanel panel) {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            String itemToRemove = product.getName();

            // 세션에서 장바구니 항목 삭제
            List<String> updatedCartItems = currentUser.getCartItems().stream()
                    .filter(item -> !item.startsWith(itemToRemove + ","))
                    .collect(Collectors.toList());
            currentUser.getCartItems().clear();
            currentUser.getCartItems().addAll(updatedCartItems);

            // 파일에서 장바구니 항목 삭제
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentUser.getId() + "_cart.txt"))) {
                for (String item : updatedCartItems) {
                    bw.write(item);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // UI에서 패널 삭제
            cartPanel.remove(panel);
            cartPanel.revalidate();
            cartPanel.repaint();

            // 합계 업데이트
            updateCartSummary();
        }
    }

    private void updateCartSummary() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            int totalOrderAmount = 0;
            int totalDiscount = 0;
            int totalAmount = 0;

            for (String item : currentUser.getCartItems()) {
                String[] parts = item.split(",");
                String productName = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                Product product = productDB.searchProductByName(productName);
                if (product != null) {
                    int productTotal = product.getSalePrice() * quantity;
                    totalOrderAmount += product.getPrice() * quantity;
                    totalDiscount += (product.getPrice() - product.getSalePrice()) * quantity;
                    totalAmount += productTotal;
                }
            }

            updateSummaryPanel(totalOrderAmount, totalDiscount, totalAmount);
        }
    }

    private void updateFinalAmount() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            int finalAmount = 0;

            for (String item : currentUser.getCartItems()) {
                String[] parts = item.split(",");
                String productName = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                for (JCheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected() && checkBox.getText().equals(productName)) {
                        Product product = productDB.searchProductByName(productName);
                        if (product != null) {
                            finalAmount += product.getSalePrice() * quantity;
                        }
                    }
                }
            }

            finalAmountLabel.setText("최종금액: " + finalAmount + "원");
        }
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
