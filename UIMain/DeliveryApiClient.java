

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class DeliveryApiClient {
    private static final String API_URL = "https://trace.cjlogistics.com/web/detail.jsp?trackingNumber=";

    public static DeliveryStatus getDeliveryStatus(String trackingNumber) throws IOException {
        @SuppressWarnings("deprecation")
		URL url = new URL(API_URL + trackingNumber);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // JSoup을 사용하여 HTML 파싱
            Document doc = Jsoup.parse(response.toString());

            // 필요한 정보 추출 (예: 배송 상태, 위치, 예상 도착일)
            String status = doc.select("div.status").text(); // 예시로 div.status 클래스를 가진 요소에서 텍스트 추출
            String location = doc.select("div.location").text(); // 예시로 div.location 클래스를 가진 요소에서 텍스트 추출
            String estimatedDeliveryDate = doc.select("div.estimatedDeliveryDate").text(); // 예시로 div.estimatedDeliveryDate 클래스를 가진 요소에서 텍스트 추출

            DeliveryStatus deliveryStatus = new DeliveryStatus();
            deliveryStatus.setTrackingNumber(trackingNumber);
            deliveryStatus.setStatus(status);
            deliveryStatus.setLocation(location);
            deliveryStatus.setEstimatedDeliveryDate(estimatedDeliveryDate);

            return deliveryStatus;
        } else {
            throw new IOException("API 호출 실패: HTTP 응답 코드 " + responseCode);
        }
    }
}
