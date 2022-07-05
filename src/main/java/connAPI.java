import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class API {

    public API() {
    }

    public JsonObject getBuilder(String path, String searchquery) throws Exception {
        HttpGet httpGet;

        if (searchquery == null) {
            httpGet = new HttpGet("https://swapi.dev/");
        } else {
            httpGet = new HttpGet("https://swapi.dev/api/" + path + "/?search=" + searchquery);
        }
        return getRequest(httpGet);
    }



    public JsonObject getRequest(HttpGet getRequest) throws IOException {
        JsonObject jsonObject = new JsonObject();
        try {

            URL url = new URL(getRequest.getURI().toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }

            jsonObject = deserialize(stringBuilder.toString());
            br.close();


            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return jsonObject;
    }

    public JsonObject deserialize(String json) {
        Gson gson = new Gson();
        JsonObject jsonClass = gson.fromJson(json, JsonObject.class);
        return jsonClass;
    }

    public JsonObject innerRequest(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        return getRequest(httpGet);
    }

}
