package activity.login.loginactivityusinghttpurlconnectio;

import java.util.ArrayList;
import java.util.List;
import static activity.login.loginactivityusinghttpurlconnectio.AppConfig.URL_LOGIN;
import static activity.login.loginactivityusinghttpurlconnectio.AppConfig.URL_ACCOUNT_INFO;
import static activity.login.loginactivityusinghttpurlconnectio.AppConfig.URL_GET_TRANSACTION_BY_DATE;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.widget.EditText;

/**
 * Created by prakash on 10-07-2016.
 */
public class UserFunctions {
    private JSONParser jsonParser;

    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    public JSONObject loginUser(String email, String password) throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userid",email);
        jsonObject.put("password",password);
        JSONObject json = jsonParser.getJSONFromUrl(URL_LOGIN,jsonObject);
        return json;
    }
    public JSONObject afterLogin(String SessionId, String ClientSSN) throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("SessionId",SessionId);
        jsonObject.put("ClientSSN",ClientSSN);
        JSONObject json = jsonParser.getJSONFromUrl(URL_ACCOUNT_INFO,jsonObject);
        return json;
    }
    public JSONObject getTransactionByDate(String sessionid, String accountid, String accounttype,String extend, String sDate, String eDate) throws JSONException{
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("SessionId",sessionid);
        jsonObject.put("AccountID",accountid);
        jsonObject.put("AccountType",accounttype);
        jsonObject.put("ExtendedAccountType",extend);
        jsonObject.put("StartDate",sDate);
        jsonObject.put("EndDate",eDate);
        JSONObject json=jsonParser.getJSONFromUrl(URL_GET_TRANSACTION_BY_DATE,jsonObject);
        return json;

    }

}
