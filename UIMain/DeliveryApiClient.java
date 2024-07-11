import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DeliveryApiClient {
    private static final String API_URL = "https://trace.cjlogistics.com/web/detail.jsp?slipno=";

    public static DeliveryStatus getDeliveryStatus(String trackingNumber) throws IOException {
        String encodedTrackingNumber = URLEncoder.encode(trackingNumber, StandardCharsets.UTF_8.toString());
        String fullUrl = API_URL + encodedTrackingNumber;
        URI uri = URI.create(fullUrl);
        URL url = uri.toURL();

        System.out.println("Request URL: " + url.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // HTML에서 운송장 번호 옆의 괄호 안에 있는 배송 상태를 추출합니다.
            String responseStr = response.toString();
            String status = extractStatus(responseStr);

            DeliveryStatus deliveryStatus = new DeliveryStatus();
            deliveryStatus.setTrackingNumber(trackingNumber);
            deliveryStatus.setStatus(status);
            deliveryStatus.setLocation(""); // location은 비워둡니다
            deliveryStatus.setEstimatedDeliveryDate(""); // estimatedDeliveryDate는 비워둡니다

            return deliveryStatus;
        } else {
            throw new IOException("API 호출 실패: HTTP 응답 코드 " + responseCode);
        }
    }

    private static String extractStatus(String html) {
        String searchPattern = "운송장 번호 : ";
        int startIndex = html.indexOf(searchPattern);
        if (startIndex == -1) {
            return "배송 상태를 찾을 수 없음";
        }
        startIndex += searchPattern.length();
        int openParenIndex = html.indexOf("(", startIndex);
        int closeParenIndex = html.indexOf(")", openParenIndex);
        if (openParenIndex == -1 || closeParenIndex == -1) {
            return "배송 상태를 찾을 수 없음";
        }
        String status = html.substring(openParenIndex + 1, closeParenIndex).trim();

        // 여기서 상태가 미등록운송장일 경우와 다른 상태인 경우를 처리
        if (status.contains("미등록운송장")) {
            return "미등록운송장";
        } else {
            // 운송장 번호와 상태가 나타난 후 다른 정보를 파싱하는 경우
            String statusPattern = "</td><td>";
            int statusStartIndex = html.indexOf(statusPattern, closeParenIndex);
            if (statusStartIndex == -1) {
                return "배송 상태를 찾을 수 없음";
            }
            statusStartIndex += statusPattern.length();
            int statusEndIndex = html.indexOf("</td>", statusStartIndex);
            if (statusEndIndex == -1) {
                return "배송 상태를 찾을 수 없음";
            }
            return html.substring(statusStartIndex, statusEndIndex).trim();
        }
    }
}
