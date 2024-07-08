import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDetailPage {
    private JFrame f;
    private Product product;
    private int quantity = 0;
    private JTextField quantityField;
    private JLabel totalLabel;
    private CartTabContent cartTabContent; // CartTabContent 인스턴스 추가

    // 기본 생성자 추가
    public ProductDetailPage(Product product) {
        this(product, null);
    }

    public ProductDetailPage(Product product, CartTabContent cartTabContent) {
        this.product = product;
        this.cartTabContent = cartTabContent; // CartTabContent 인스턴스 초기화
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
        Image image = imageIcon.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(imageLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간의 간격을 설정합니다.
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        infoPanel.add(nameLabel, gbc);

        JLabel stockLabel = new JLabel("재고: " + product.getStock());
        gbc.gridy++;
        infoPanel.add(stockLabel, gbc);

        JLabel priceLabel = new JLabel("상품 가격: " + product.getPrice() + "원");
        gbc.gridy++;
        infoPanel.add(priceLabel, gbc);

        JLabel salePriceLabel = new JLabel("할인 가격: " + product.getSalePrice() + "원");
        gbc.gridy++;
        infoPanel.add(salePriceLabel, gbc);

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel decreaseButton = new JLabel("-");
        decreaseButton.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        decreaseButton.setPreferredSize(new Dimension(25, 25));
        decreaseButton.setHorizontalAlignment(SwingConstants.CENTER);
        decreaseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (quantity > 0) {
                    quantity--;
                    quantityField.setText(String.valueOf(quantity));
                    updateTotalLabel();
                }
            }
        });
        quantityPanel.add(decreaseButton);

        quantityField = new JTextField(String.valueOf(quantity), 5);
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setEditable(false); // 사용자가 입력하지 못하도록 비활성화
        quantityPanel.add(quantityField);

        JLabel increaseButton = new JLabel("+");
        increaseButton.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        increaseButton.setPreferredSize(new Dimension(25, 25));
        increaseButton.setHorizontalAlignment(SwingConstants.CENTER);
        increaseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (quantity < product.getStock()) {
                    quantity++;
                    quantityField.setText(String.valueOf(quantity));
                    updateTotalLabel();
                } else {
                    JOptionPane.showMessageDialog(f, "재고가 부족합니다.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        quantityPanel.add(increaseButton);

        gbc.gridy++;
        infoPanel.add(quantityPanel, gbc);

        totalLabel = new JLabel("합계: " + product.getSalePrice() * quantity + "원");
        gbc.gridy++;
        infoPanel.add(totalLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addToCartButton = new JButton("장바구니 담기");
        addToCartButton.setBackground(Color.BLACK);
        addToCartButton.setForeground(Color.WHITE);
        JButton buyNowButton = new JButton("바로 구매");
        buyNowButton.setBackground(Color.RED);
        buyNowButton.setForeground(Color.WHITE);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(buyNowButton);
		
		// 바로 구매 버튼 이벤트 추가
        buyNowButton.addActionListener(e -> {
            if (SessionManager.getInstance().getCurrentUser() != null) {
                if (quantity > 0) {
					JFrame paymentFrame = new PaymentPage(product, quantity);
					paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					paymentFrame.setVisible(true);
                    f.dispose(); // 현재 창 닫기
                } else {
                    JOptionPane.showMessageDialog(f, "수량을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                 handleLoginAndPurchase(); // 로그인되지 않은 상태라면 handleLoginAndPurchase() 호출
            }
        });

        // 장바구니 버튼 이벤트 추가
        addToCartButton.addActionListener(e -> handleCartButton());

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        infoPanel.add(buttonPanel, gbc);

        contentPanel.add(infoPanel, BorderLayout.CENTER);

        f.setVisible(true);
    }

    private void updateTotalLabel() {
        int totalPrice = product.getSalePrice() * quantity;
        totalLabel.setText("합계: " + totalPrice + "원");
    }

    private void handleCartButton() {
        if (SessionManager.getInstance().getCurrentUser() != null) {
            addToCart();
        } else {
            int result = JOptionPane.showConfirmDialog(f, "로그인이 필요합니다. 로그인하시겠습니까?", "로그인 필요", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                showLoginDialog();
				addToCart();
            }
        }
    }
	
	private void handleLoginAndPurchase() {
        int result = JOptionPane.showConfirmDialog(f, "로그인이 필요합니다. 로그인하시겠습니까?", "로그인 필요", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            showLoginDialog();
            if (SessionManager.getInstance().getCurrentUser() != null) { // 로그인 성공 시
                if (quantity > 0) {
                    new PaymentPage(product, quantity); // 비번 없이 PaymentPage 생성
                    f.dispose(); // 현재 창 닫기
                } else {
                    JOptionPane.showMessageDialog(f, "수량을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private void addToCart() {
        Member currentUser = SessionManager.getInstance().getCurrentUser();
        String cartEntry = product.getName() + "," + quantity;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentUser.getId() + "_cart.txt", true))) {
            bw.write(cartEntry);
            bw.newLine();
            currentUser.getCartItems().add(cartEntry); // 세션의 장바구니 정보 업데이트
            JOptionPane.showMessageDialog(f, "장바구니에 상품이 추가되었습니다.");
            if (cartTabContent != null) {
                cartTabContent.loadCartItems(); // 장바구니 페이지 업데이트
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(f, "로그인", true);
        loginDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel idLabel = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginDialog.add(idLabel, gbc);

        JTextField idField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginDialog.add(idField, gbc);

        JLabel passwordLabel = new JLabel("비밀번호:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginDialog.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginDialog.add(passwordField, gbc);

        JButton loginButton = new JButton("로그인");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginDialog.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());
            if (CheckMember.validateLogin(id, password)) {
                JOptionPane.showMessageDialog(loginDialog, "로그인 성공!");
                Member loggedInUser = CheckMember.getMemberById(id);
                SessionManager.getInstance().login(loggedInUser);
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "로그인 실패! 아이디 또는 비밀번호를 확인해주세요.");
            }
        });

        loginDialog.pack();
        loginDialog.setLocationRelativeTo(f);
        loginDialog.setVisible(true);
    }
}
