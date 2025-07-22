import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SmsApi {

    private static final String API_KEY = "e883424d-d70f-4e58-8ee3-4e21ea390ff1";
    private static final String SENDER = "30007546464646";
    private static final String URL_STRING = "http://api.sms-webservice.com/api/V3/SendBulk";

    // کلاس برای ساختار JSON Recipients
    static class Recipient {
        @SerializedName("Destination")
        String destination;

        @SerializedName("UserTraceId")
        String userTraceId;

        Recipient(String destination, String userTraceId) {
            this.destination = destination;
            this.userTraceId = userTraceId;
        }
    }

    // کلاس برای داده کلی ارسال
    static class RequestData {
        @SerializedName("ApiKey")
        String apiKey;

        @SerializedName("Text")
        String text;

        @SerializedName("Sender")
        String sender;

        @SerializedName("Recipients")
        Recipient[] recipients;

        RequestData(String apiKey, String text, String sender, Recipient[] recipients) {
            this.apiKey = apiKey;
            this.text = text;
            this.sender = sender;
            this.recipients = recipients;
        }
    }

    public static String sendBulk(String destination, String userTraceId, String text) throws Exception {
        Recipient recipient = new Recipient(destination, userTraceId);
        Recipient[] recipients = {recipient};

        RequestData requestData = new RequestData(API_KEY, text, SENDER, recipients);

        // تبدیل آبجکت به JSON
        Gson gson = new Gson();
        String jsonInputString = gson.toJson(requestData);

        URL url = new URL(URL_STRING);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();

        InputStream stream;
        if (code >= 200 && code < 300) {
            stream = con.getInputStream();
        } else {
            stream = con.getErrorStream();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;

        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        return response.toString();
    }

    // تست تابع
    public static void main(String[] args) {
        try {
            String response = sendBulk("09120000000", "trace123", "متن پیامک تستی");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
