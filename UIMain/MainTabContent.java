import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.plaf.FontUIResource;
import java.util.Enumeration;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class MainTabContent {
    private JPanel mainPanel;
    private JPanel productPanel, topPanel, searchPanel, bPanel;
    private JLabel banner;
    private JTextField searchField;
    private JLabel searchClick, imgsearchClick;
    private JScrollPane scrollPane;
    private ProductDatabase productDB;
    private JTabbedPane tabbedPane;
    private Font customFont;
    private Font defaultFont;
    private Font boldFont;
    private Timer bannerTimer;
    private int currentBannerIndex = 0;
    private final String[] bannerImages = {
        "/images/banner1.jpg",
        "/images/banner2.jpg"
    }; // 배너 이미지 파일 경로
	

    public MainTabContent() {
        productDB = new ProductDatabase();
        applyCustomFont(); // 폰트 설정을 생성자에서 호출
        initComponents();
        startBannerRotation(); // 배너 이미지 교체 시작
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600));
        mainPanel.setBackground(Color.WHITE);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        searchPanel = createSearchPanel();
        topPanel.add(searchPanel, BorderLayout.NORTH);

        bPanel = createBannerPanel();
        topPanel.add(bPanel, BorderLayout.CENTER);

        productPanel = createProductPanel();
        productPanel.setBackground(Color.WHITE); // 제품 패널 배경색 설정

        scrollPane = new JScrollPane(productPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(780, 400));
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void applyCustomFont() {
        try {
            InputStream fontStream = getClass().getResourceAsStream("title.ttf");
            if (fontStream == null) {
                throw new IOException("Font file not found in resources");
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 24);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.err.println("Custom font loading failed. Falling back to default font.");
            customFont = new Font("Malgun Gothic", Font.BOLD, 24); // fallback 폰트
        }

        // 기본 폰트 설정
        try {
            InputStream fontStream = getClass().getResourceAsStream("customFont.ttf");
            if (fontStream == null) {
                throw new IOException("Default font file not found in resources");
            }
            defaultFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, 14);
            boldFont = defaultFont.deriveFont(Font.BOLD);
            setUIFont(new FontUIResource(defaultFont));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.err.println("Default font loading failed. Using system default font.");
            defaultFont = new Font("Malgun Gothic", Font.PLAIN, 14); // system default font
            boldFont = defaultFont.deriveFont(Font.BOLD);
            setUIFont(new FontUIResource(defaultFont));
        }
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel projectName = new JLabel("검색");
        projectName.setFont(defaultFont); // 폰트 설정
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(projectName, gbc);

        searchField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.ipadx = 70;
        panel.add(searchField, gbc);

        ImageIcon searchIcon = loadImageIcon("/images/search.png", 20, 20, false);
        searchClick = new JLabel(searchIcon);
        searchClick.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchClick.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = searchField.getText();
                Product foundProduct = productDB.searchProductByName(searchText);
                if (foundProduct != null) {
                    showProductDetail(foundProduct);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "상품을 찾을 수 없습니다: " + searchText);
                }
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(searchClick, gbc);

        ImageIcon imgSearchIcon = loadImageIcon("/images/imagesearch.png", 20, 20, false);
        imgsearchClick = new JLabel(imgSearchIcon);
        imgsearchClick.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        imgsearchClick.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(mainPanel);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String selectedFilePath = selectedFile.getAbsolutePath();

                    Product matchingProduct = productDB.searchProductByFilePath(selectedFilePath);

                    if (matchingProduct != null) {
                        showProductDetail(matchingProduct);
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "상품을 찾을 수 없습니다");
                    }
                }
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 1;
        panel.add(imgsearchClick, gbc);

        return panel;
    }

    private JPanel createBannerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        banner = new JLabel();
        banner.setHorizontalAlignment(SwingConstants.CENTER);
        banner.setPreferredSize(new Dimension(800, 150)); // 배너 크기 설정
        banner.setSize(new Dimension(800, 150)); // 배너 크기 설정 추가
        panel.add(banner, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        panel.setBackground(Color.WHITE); // 제품 패널 배경색 설정
        Product[] products = productDB.getProducts();
        for (Product product : products) {
            JPanel itemPanel = createProductItemPanel(product);
            panel.add(itemPanel);
        }
        return panel;
    }

private JPanel createProductItemPanel(Product product) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE); // 개별 제품 패널 배경색 설정
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패널 사이 공간을 흰색으로 만듦

    // 이미지 로드 시 디버깅 메시지 추가
    System.out.println("Loading image for product: " + product.getName() + " from path: " + product.getImagePath());
    ImageIcon imageIcon = loadImageIcon(product.getImagePath(), 150, 150, true);
    if (imageIcon == null) {
        System.out.println("Failed to load image for product: " + product.getName());
    }
    JLabel imageLabel = new JLabel(imageIcon);
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(imageLabel, BorderLayout.CENTER);

    JPanel infoPanel = new JPanel(new GridLayout(3, 1)); // 3 rows to include the sale price
    infoPanel.setBackground(Color.WHITE); // 정보 패널 배경색 설정

    JLabel nameLabel = new JLabel(product.getName());
    nameLabel.setFont(defaultFont); // 폰트 설정
    nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
    nameLabel.setBackground(Color.WHITE);
    nameLabel.setOpaque(true); // 배경색을 설정하기 위해 불투명하게 만듦
    infoPanel.add(nameLabel);

    // 가격에 취소선 추가
    String originalPriceStr = String.format("<html><strike>%s</strike></html>", "가격: " + product.getPrice());
    JLabel priceLabel = new JLabel(originalPriceStr);
    priceLabel.setFont(defaultFont); // 폰트 설정
    priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
    priceLabel.setBackground(Color.WHITE);
    priceLabel.setOpaque(true);
    infoPanel.add(priceLabel);

    // 움직이는 GIF 아이콘 추가
    ImageIcon discountIcon = loadImageIcon("/images/boom.gif", 10, 10, false);
    String salePriceStr = String.format("<html><span style='color:red; font-weight:bold;'>할인가: %s</span></html>", product.getSalePrice());
    JLabel salePriceLabel = new JLabel(salePriceStr, discountIcon, JLabel.HORIZONTAL);
    salePriceLabel.setFont(boldFont); // 폰트 설정
    salePriceLabel.setHorizontalTextPosition(JLabel.LEFT);
    salePriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
    salePriceLabel.setBackground(Color.WHITE);
    salePriceLabel.setOpaque(true);
    infoPanel.add(salePriceLabel);

    panel.add(infoPanel, BorderLayout.SOUTH);

    panel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (product.getName().contains("맥주")) {
                Member loggedInUser = SessionManager.getInstance().getCurrentUser();
                if (loggedInUser != null) {
                    String birthdate = loggedInUser.getBirthdate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                    LocalDate birthDate = LocalDate.parse(birthdate, formatter);
                    LocalDate cutoffDate = LocalDate.of(2006, 1, 1);
                    if (birthDate.isAfter(cutoffDate)) {
                        JOptionPane.showMessageDialog(mainPanel, "미성년자는 술을 구매할 수 없습니다.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "로그인이 필요합니다.");
                    return;
                }
            }
            showProductDetail(product);
        }
    });

    panel.setPreferredSize(new Dimension(200, 200));

    return panel;
}


    private void showProductDetail(Product product) {
        SwingUtilities.invokeLater(() -> {
            new ProductDetailPage(product);
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private ImageIcon loadImageIcon(String path, int width, int height, boolean isAbsolutePath) {
    try {
        URL imgUrl;
        if (isAbsolutePath) {
            imgUrl = new File(path).toURI().toURL();
        } else {
            imgUrl = getClass().getResource(path);
        }

        if (imgUrl == null) {
            System.out.println("이미지를 로드할 수 없습니다: " + path);
            return null;
        } else {
            ImageIcon icon = new ImageIcon(imgUrl);
            // 파일 확장자 확인하여 GIF인 경우 크기 조정을 하지 않음
            if (path.endsWith(".gif")) {
                return icon; // GIF 파일은 크기 조정 없이 반환
            } else if (width > 0 && height > 0) {
                Image image = icon.getImage();
                Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(resizedImage);
            }
            return icon;
        }
    } catch (MalformedURLException e) {
        System.out.println("잘못된 이미지 경로: " + path);
        e.printStackTrace();
        return null;
    }
}


    private void startBannerRotation() {
        bannerTimer = new Timer(5000, new ActionListener() { // 5초마다 이미지 교체
            @Override
            public void actionPerformed(ActionEvent e) {
                currentBannerIndex = (currentBannerIndex + 1) % bannerImages.length;
                ImageIcon bannerIcon = loadImageIcon(bannerImages[currentBannerIndex], banner.getWidth(), banner.getHeight(), false);
                banner.setIcon(bannerIcon);
            }
        });
        bannerTimer.start();
        // 초기 이미지 설정
        ImageIcon initialBannerIcon = loadImageIcon(bannerImages[currentBannerIndex], banner.getWidth(), banner.getHeight(), false);
        banner.setIcon(initialBannerIcon);
    }

    public static void setUIFont(FontUIResource font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
