import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class SmsApi {

    private static final String API_KEY = "e883424d-d70f-4e58-8ee3-4e21ea390ff1";
    private static final String URL_ACCOUNT_INFO = "http://api.sms-webservice.com/api/V3/AccountInfo";

    public static String accountInfo() throws Exception {
        // ساخت داده JSON
        Map<String, String> data = new HashMap<>();
        data.put("ApiKey", API_KEY);

        Gson gson = new Gson();
        String jsonInputString = gson.toJson(data);

        URL url = new URL(URL_ACCOUNT_INFO);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        // ارسال JSON
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
            String response = accountInfo();
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
