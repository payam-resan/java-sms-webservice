import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SmsApi {

    private static final String API_KEY = "e883424d-d70f-4e58-8ee3-4e21ea390ff1";
    private static final String URL_STATUS_BY_USER_TRACE_ID = "http://api.sms-webservice.com/api/V3/StatusByUserTraceId";

    public static String statusByUserTraceId(List<String> userTraceIds) throws Exception {
        // ساخت داده JSON
        Map<String, Object> data = new HashMap<>();
        data.put("ApiKey", API_KEY);
        data.put("UserTraceIds", userTraceIds);

        Gson gson = new Gson();
        String jsonInputString = gson.toJson(data);

        URL url = new URL(URL_STATUS_BY_USER_TRACE_ID);
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
            // نمونه: لیستی از UserTraceId ها
            List<String> userTraceIds = List.of("traceId1", "traceId2", "traceId3");
            String response = statusByUserTraceId(userTraceIds);
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
