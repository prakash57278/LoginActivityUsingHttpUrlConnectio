package activity.login.loginactivityusinghttpurlconnectio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by prakash on 10-07-2016.
 */
public class JSONParser {
    HttpURLConnection connection;
    BufferedReader reader=null;
    static JSONObject jObj = null;
    static String json = "";
    StringBuffer buffer=null;
    public JSONParser(){

    }
    public JSONObject getJSONFromUrl(String url, JSONObject params) {
        try {
            URL loginUrl=new URL(url);
            connection=(HttpURLConnection)loginUrl.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.connect();

            OutputStreamWriter details=new OutputStreamWriter(connection.getOutputStream());
            details.write(params.toString());
            details.flush();

            reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            buffer=new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            json=buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null)
            {
                connection.disconnect();
            }
        }
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {

        }
        return jObj;

    }
}


