import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class SmsApi {

    private static final String API_KEY = "e883424d-d70f-4e58-8ee3-4e21ea390ff1";
    private static final String URL_SEND_TOKEN_MULTI = "http://api.sms-webservice.com/api/V3/SendTokenMulti";

    // کلاس مدل برای Recipient
    static class Recipient {
        String Destination;
        String UserTraceId;
        Map<String, String> Parameters;

        public Recipient(String destination, String userTraceId, Map<String, String> parameters) {
            this.Destination = destination;
            this.UserTraceId = userTraceId;
            this.Parameters = parameters;
        }
    }

    // کلاس مدل درخواست
    static class SendTokenMultiRequest {
        String ApiKey;
        String TemplateKey;
        List<Recipient> Recipients;

        public SendTokenMultiRequest(String apiKey, String templateKey, List<Recipient> recipients) {
            this.ApiKey = apiKey;
            this.TemplateKey = templateKey;
            this.Recipients = recipients;
        }
    }

    public static String sendTokenMulti(String templateKey, String destination, String userTraceId, Map<String, String> parameters) throws Exception {
        // ساخت مدل داده
        Recipient recipient = new Recipient(destination, userTraceId, parameters);
        SendTokenMultiRequest requestData = new SendTokenMultiRequest(API_KEY, templateKey, Collections.singletonList(recipient));

        // تبدیل مدل به JSON
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(requestData);

        URL url = new URL(URL_SEND_TOKEN_MULTI);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        // ارسال داده JSON در بدنه درخواست
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // خواندن پاسخ
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String responseLine;

        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();

        return response.toString();
    }

    public static void main(String[] args) {
        try {
            Map<String, String> parameters = Map.of(
                "param1", "value1",
                "param2", "value2",
                "param3", "value3"
            );
            String response = sendTokenMulti("YourTemplateKey", "09121234567", "trace123", parameters);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
