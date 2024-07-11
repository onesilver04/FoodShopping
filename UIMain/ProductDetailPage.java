import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
        contentPanel.setBackground(Color.WHITE); // 전체 배경 흰색으로 설정
        f.add(contentPanel, BorderLayout.CENTER);

        ImageIcon imageIcon = new ImageIcon(product.getImagePath());
        Image image = imageIcon.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(imageLabel, BorderLayout.WEST);


        RoundedPanel infoPanel = new RoundedPanel(20, new Color(240, 240, 240));
        infoPanel.setPreferredSize(new Dimension(300, 100)); // RoundedPanel의 크기 직접 설정
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패널 내부 여백을 조정
		
		
        JPanel infoPanelContainer = new JPanel(new BorderLayout());
        infoPanelContainer.setBackground(Color.WHITE); // 컨테이너 패널 배경 흰색으로 설정
        infoPanelContainer.setBorder(BorderFactory.createEmptyBorder(100, 20, 100, 20)); // 상단과 하단 여백을 20으로 설정

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간의 간격을 설정합니다.
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 20));
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
        buttonPanel.setBackground(new Color(240, 240, 240)); // 버튼 패널 배경 설정
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

        infoPanelContainer.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanelContainer, BorderLayout.CENTER);

        f.setVisible(true);
    }

    private void updateTotalLabel() {
        int totalPrice = product.getSalePrice() * quantity;
        totalLabel.setText("합계: " + totalPrice + "원");
    }

    private void handleCartButton() {
        if (SessionManager.getInstance().getCurrentUser() != null) {
			if (quantity == 0) {
				JOptionPane.showMessageDialog(f, "수량을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
			} else {
				addToCart();
			}
        } else {
            int result = JOptionPane.showConfirmDialog(f, "로그인이 필요합니다. 로그인하시겠습니까?", "로그인 필요", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                showLoginDialog();
                if (SessionManager.getInstance().getCurrentUser() != null) {
					JOptionPane.showMessageDialog(f, "수량을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                } else{
					addToCart();
                }
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
            int response = JOptionPane.showConfirmDialog(f, "장바구니에 상품이 추가되었습니다.", "알림", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (response == JOptionPane.OK_OPTION) {
                f.dispose(); // OK 버튼을 누르면 창 닫기
            }
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
    passwordField.setEchoChar('\u25CF'); // 입력된 비밀번호를 유니코드 동그라미 기호로 표시
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


    // RoundedPanel 클래스 정의
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int cornerRadius, Color backgroundColor) {
            this.cornerRadius = cornerRadius;
            this.backgroundColor = backgroundColor;
            setOpaque(false); // 투명하게 설정하여 배경을 직접 그리도록 합니다.
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }
    }
}
