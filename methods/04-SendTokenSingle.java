import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SmsApi {

    private static final String API_KEY = "e883424d-d70f-4e58-8ee3-4e21ea390ff1";
    private static final String BASE_URL = "http://api.sms-webservice.com/api/V3/SendTokenSingle";

    public static String sendTokenSingle(String templateKey, String destination, String p1, String p2, String p3) throws Exception {
        // ساخت پارامترهای URL با کدگذاری مناسب
        String params = "ApiKey=" + URLEncoder.encode(API_KEY, StandardCharsets.UTF_8)
                + "&TemplateKey=" + URLEncoder.encode(templateKey, StandardCharsets.UTF_8)
                + "&Destination=" + URLEncoder.encode(destination, StandardCharsets.UTF_8)
                + "&p1=" + URLEncoder.encode(p1, StandardCharsets.UTF_8)
                + "&p2=" + URLEncoder.encode(p2, StandardCharsets.UTF_8)
                + "&p3=" + URLEncoder.encode(p3, StandardCharsets.UTF_8);

        String urlString = BASE_URL + "?" + params;
        URL url = new URL(urlString);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // خواندن پاسخ
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static void main(String[] args) {
        try {
            String response = sendTokenSingle("YourTemplateKey", "09121234567", "param1", "param2", "param3");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
