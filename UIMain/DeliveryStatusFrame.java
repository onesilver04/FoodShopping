

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DeliveryStatusFrame extends JFrame {
    private JLabel statusLabel;
    private JLabel locationLabel;
    private JLabel estimatedDeliveryDateLabel;

    public DeliveryStatusFrame(String trackingNumber) {
        setTitle("배송 조회");
        setSize(400, 300);
        setLayout(new GridLayout(3, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        statusLabel = new JLabel("배송 상태: ");
        locationLabel = new JLabel("위치: ");
        estimatedDeliveryDateLabel = new JLabel("예상 도착일: ");

        add(statusLabel);
        add(locationLabel);
        add(estimatedDeliveryDateLabel);

        fetchDeliveryStatus(trackingNumber);
    }

    private void fetchDeliveryStatus(String trackingNumber) {
        try {
            DeliveryStatus deliveryStatus = DeliveryApiClient.getDeliveryStatus(trackingNumber);
            if (deliveryStatus != null) {
                statusLabel.setText("배송 상태: " + deliveryStatus.getStatus());
                locationLabel.setText("위치: " + deliveryStatus.getLocation());
                estimatedDeliveryDateLabel.setText("예상 도착일: " + deliveryStatus.getEstimatedDeliveryDate());
            } else {
                JOptionPane.showMessageDialog(this, "배송 상태를 가져올 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "배송 상태를 가져오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
