import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SmsService {

    private static final String API_KEY = "e883424d-d70f-4e58-8ee3-4e21ea390ff1";
    private static final String SENDER = "30007546464646";
    private static final String BASE_URL = "http://api.sms-webservice.com/api/V3/Send";

    public static String send(String recipients, String text) throws IOException {
        // url encode the text
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        // ساخت query string
        String query = String.format(
            "ApiKey=%s&Text=%s&Sender=%s&Recipients=%s",
            URLEncoder.encode(API_KEY, StandardCharsets.UTF_8),
            encodedText,
            URLEncoder.encode(SENDER, StandardCharsets.UTF_8),
            URLEncoder.encode(recipients, StandardCharsets.UTF_8)
        );

        // کامل کردن URL
        String fullUrl = BASE_URL + "?" + query;

        // ساخت connection
        URL url = new URL(fullUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Accept-Encoding", ""); // برای مطابقت با curl

        int responseCode = conn.getResponseCode();

        BufferedReader reader;
        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    // تست تابع
    public static void main(String[] args) {
        try {
            String response = send("09120000000", "سلام، تست پیامک!");
            System.out.println("Response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
