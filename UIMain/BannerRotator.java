// thread 이용_runnable
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BannerRotator implements Runnable {
    private JLabel bannerLabel;
    private String[] bannerImages;
    private int bannerWidth;
    private int bannerHeight;
    private int currentBannerIndex = 0;
    private volatile boolean running = true;

    public BannerRotator(JLabel bannerLabel, String[] bannerImages, int bannerWidth, int bannerHeight) {
        this.bannerLabel = bannerLabel;
        this.bannerImages = bannerImages;
        this.bannerWidth = bannerWidth;
        this.bannerHeight = bannerHeight;
    }

    @Override
    public void run() {
        while (running) {
            currentBannerIndex = (currentBannerIndex + 1) % bannerImages.length;
            ImageIcon bannerIcon = loadImageIcon(bannerImages[currentBannerIndex], bannerWidth, bannerHeight, false);
            SwingUtilities.invokeLater(() -> bannerLabel.setIcon(bannerIcon));
            try {
                Thread.sleep(5000); // 5초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
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
}
