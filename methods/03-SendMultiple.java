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
    private static final String URL_STRING = "http://api.sms-webservice.com/api/V3/SendMultiple";

    // کلاس مدل Recipient مشابه PHP
    static class Recipient {
        @SerializedName("Sender")
        String sender;

        @SerializedName("Text")
        String text;

        @SerializedName("Destination")
        String destination;

        @SerializedName("UserTraceId")
        String userTraceId;

        Recipient(String sender, String text, String destination, String userTraceId) {
            this.sender = sender;
            this.text = text;
            this.destination = destination;
            this.userTraceId = userTraceId;
        }
    }

    // مدل داده کلی برای ارسال
    static class RequestData {
        @SerializedName("ApiKey")
        String apiKey;

        @SerializedName("Recipients")
        Recipient[] recipients;

        RequestData(String apiKey, Recipient[] recipients) {
            this.apiKey = apiKey;
            this.recipients = recipients;
        }
    }

    public static String sendMultiple(String destination, String userTraceId, String text) throws Exception {
        String sender = "30007546464646";  // اگر عدد بود، رشته در نظر بگیرید

        Recipient recipient = new Recipient(sender, text, destination, userTraceId);
        Recipient[] recipients = { recipient };

        RequestData requestData = new RequestData(API_KEY, recipients);

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

        InputStream stream = (code >= 200 && code < 300) ? con.getInputStream() : con.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;

        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        return response.toString();
    }

    public static void main(String[] args) {
        try {
            String response = sendMultiple("09121234567", "trace123", "پیام تستی");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
