import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MainTabContent {
    private JPanel mainPanel;
    private JPanel productPanel, topPanel, searchPanel, bPanel;
    private JLabel banner;
    private JTextField searchField;
    private JLabel searchClick, imgsearchClick;
	private JScrollPane scrollPane;
    private ProductDatabase productDB;
    
    public MainTabContent() {
        productDB = new ProductDatabase();
        initComponents();
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600));
        
        topPanel = new JPanel(new BorderLayout());
        
        searchPanel = createSearchPanel();
        topPanel.add(searchPanel, BorderLayout.NORTH);
        
        bPanel = createBannerPanel();
        topPanel.add(bPanel, BorderLayout.CENTER);
        
        productPanel = createProductPanel();
		
		scrollPane = new JScrollPane(productPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(780, 400));
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel projectName = new JLabel("프로젝트 이름");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(projectName, gbc);
        
        searchField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.ipadx = 70;
        panel.add(searchField, gbc);
        
        ImageIcon searchIcon = new ImageIcon("C:/Users/채리/바탕 화면/OOPTeamProject/images/search.png");
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
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(searchClick, gbc);
        
        ImageIcon imgSearchIcon = new ImageIcon("C:/Users/채리/바탕 화면/OOPTeamProject/images/imagesearch.png");
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
        gbc.gridy = 0;
        panel.add(imgsearchClick, gbc);
        
        return panel;
    }
	
    private JPanel createBannerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        banner = new JLabel("회원가입 시 10% 할인 쿠폰 증정");
        banner.setForeground(Color.RED);
        banner.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        banner.setHorizontalAlignment(SwingConstants.CENTER);
        banner.setPreferredSize(new Dimension(800, 200));
        panel.add(banner, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }
    
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        Product[] products = productDB.getProducts();
        for (Product product : products) {
            JPanel itemPanel = createProductItemPanel(product);
            panel.add(itemPanel);
        }
        return panel;
    }
    
    private JPanel createProductItemPanel(Product product) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        ImageIcon imageIcon = new ImageIcon(product.getImagePath());
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(nameLabel);
        
        JLabel priceLabel = new JLabel("가격: " + product.getPrice());
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(priceLabel);
        
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showProductDetail(product);
            }
        });
        
        panel.setPreferredSize(new Dimension(200, 200));
        
        return panel;
    }
    
	private void showProductDetail(Product product) {
        SwingUtilities.invokeLater(() -> {
            ProductDetailPage detailPage = new ProductDetailPage(product);
        });
    }
	
    // 메인 패널을 반환하는 메서드
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
