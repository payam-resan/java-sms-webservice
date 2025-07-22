import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SmsApi {

    private static final String API_KEY = "e883424d-d70f-4e58-8ee3-4e21ea390ff1";
    private static final String URL_TOKEN_LIST = "http://api.sms-webservice.com/api/V3/TokenList";

    public static String tokenList() throws Exception {
        // ساخت JSON داده ورودی
        String jsonInputString = "{\"ApiKey\":\"" + API_KEY + "\"}";

        URL url = new URL(URL_TOKEN_LIST);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        // ارسال داده JSON به سرور
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
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
            String response = tokenList();
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
