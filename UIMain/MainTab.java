// 탭 구성
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.FontFormatException;

public class MainTab {
    private static JTabbedPane tabbedPane; // 정적 변수로 선언
    JFrame jf;
    LogoutPage logoutPage;
    private Font customFont;

    public MainTab(String msg) {
        jf = new JFrame(msg);
        jf.setLayout(new BorderLayout());

        // UIManager를 사용하여 탭 색상 및 구분선을 설정
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
        UIManager.put("TabbedPane.borderHightlightColor", Color.WHITE);
        UIManager.put("TabbedPane.darkShadow", Color.WHITE);
        UIManager.put("TabbedPane.light", Color.WHITE);
        UIManager.put("TabbedPane.focus", Color.WHITE);
        UIManager.put("TabbedPane.unselectedBackground", Color.WHITE);
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        UIManager.put("TabbedPane.shadow", Color.WHITE);

        // 탭의 글씨체를 설정
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("customFont.ttf")).deriveFont(Font.PLAIN, 14);
            setUIFont(new FontUIResource(customFont));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.err.println("Custom font loading failed. Using default font.");
            customFont = new Font("Arial", Font.PLAIN, 14);
        }

        initComponents();

        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane(); // 정적 변수로 초기화
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setOpaque(true);

        // UI 커스텀 설정
        tabbedPane.setUI(new BasicTabbedPaneUI() {

            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 30; // 탭의 높이를 30 픽셀로 설정
            }

            @Override
            protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
                g.setColor(new Color(47, 157, 39)); // 탭 영역 전체의 배경색 설정
                g.fillRect(0, 0, tabbedPane.getWidth(), tabbedPane.getHeight());
                super.paintTabArea(g, tabPlacement, selectedIndex);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // 탭 경계선을 그리지 않음
            }
        });

        // 탭의 글씨체를 변경하기 위해 탭 컴포넌트 설정
        tabbedPane.setFont(customFont);

        // 각 탭의 내용을 별도의 클래스에서 가져와 추가
        MainTabContent mainTabContent = new MainTabContent();
        tabbedPane.addTab("메인", mainTabContent.getMainPanel());
        setTabColor(tabbedPane, 0, new Color(47, 157, 39));

        LoginForm loginForm = new LoginForm();
        loginForm.setTabbedPane(tabbedPane); // TabbedPane을 LoginForm에 전달
        tabbedPane.addTab("로그인", loginForm.getLoginPanel());
        setTabColor(tabbedPane, 1, new Color(47, 157, 39));

        MyPage myPage = new MyPage();
        myPage.setTabbedPane(tabbedPane); // TabbedPane을 MyPage에 전달
        tabbedPane.addTab("마이페이지", myPage);
        setTabColor(tabbedPane, 2, new Color(47, 157, 39));

        // 장바구니 탭은 빈 패널로 시작하고 선택 시 초기화
        JPanel emptyCartPanel = new JPanel();
        tabbedPane.addTab("장바구니", emptyCartPanel);
        setTabColor(tabbedPane, 3, new Color(47, 157, 39));

        tabbedPane.addTab("배송 조회", new Delivery());
        setTabColor(tabbedPane, 4, new Color(47, 157, 39));

        JPanel eventPagePanel = new JPanel();
        tabbedPane.addTab("이벤트", eventPagePanel);
        setTabColor(tabbedPane, 5, new Color(47, 157, 39));

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == 5) { // 이벤트 탭의 인덱스 = 5
                    JPanel newEventPage = EventPage.createEventPage();
                    tabbedPane.setComponentAt(5, newEventPage);
                }
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 3) { // 장바구니 탭이 선택된 경우
                    if (SessionManager.getInstance().getCurrentUser() != null) {
                        CartTabContent cartTabContent = new CartTabContent();
                        tabbedPane.setComponentAt(3, cartTabContent.getMainPanel());
                    } else {
                        JOptionPane.showMessageDialog(jf, "로그인이 필요합니다.");
                        tabbedPane.setSelectedIndex(1); // 로그인 탭으로 이동
                    }
                }
            }
        });

        // 탭 위쪽에 로고와 텍스트, 로그아웃 버튼 추가
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE); // 전체 패널 배경색을 흰색으로 설정
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Color.WHITE);

        ImageIcon logoIcon = new ImageIcon("Image/logo.png");
        Image logoImage = logoIcon.getImage(); // ImageIcon에서 이미지 가져오기
        Image resizedImage = logoImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH); // 이미지 크기 조정
        logoIcon = new ImageIcon(resizedImage); // 조정된 이미지로 새로운 ImageIcon 생성

        JLabel logoLabel = new JLabel(logoIcon);
        logoPanel.add(logoLabel);

        JLabel shopNameLabel = new JLabel("Grocery Market");
        shopNameLabel.setFont(new Font("한컴 말랑말랑 Bold", Font.BOLD, 24)); // shopNameLabel의 폰트를 다른 폰트로 설정
        logoPanel.add(shopNameLabel);

        topPanel.add(logoPanel, BorderLayout.WEST);

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setFont(customFont.deriveFont(Font.PLAIN, 12)); // 로그아웃 버튼의 글씨 크기 설정
        logoutButton.setPreferredSize(new Dimension(80, 20)); // 버튼 크기 설정
        logoutButton.setMargin(new Insets(0, 0, 0, 0)); // 버튼 여백 설정
        logoutButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 버튼 경계 설정
        logoutButton.setBackground(Color.WHITE); // 버튼 배경색 설정
        logoutButton.setForeground(Color.BLACK); // 버튼 글자색 설정
        logoutPage = new LogoutPage(tabbedPane, jf);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutPage.handleLogout(); // 로그아웃 버튼 클릭 시 LogoutPage의 handleLogout 메소드 호출
            }
        });
        topPanel.add(logoutButton, BorderLayout.EAST);

        // 메인 프레임에 상단 패널과 탭 패널 추가
        jf.add(topPanel, BorderLayout.NORTH);
        jf.add(tabbedPane, BorderLayout.CENTER);
    }

    private void setTabColor(JTabbedPane tabbedPane, int index, Color color) {
        tabbedPane.setBackgroundAt(index, color);
    }

    // UIManager를 사용하여 전체 UI의 폰트를 설정하는 메소드
    public static void setUIFont(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    // 이벤트 탭으로 전환하는 정적 메서드
    public static void switchToEventTab() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(5); // 이벤트 탭의 인덱스 = 5
        }
    }
}
