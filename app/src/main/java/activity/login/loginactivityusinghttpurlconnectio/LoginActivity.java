package activity.login.loginactivityusinghttpurlconnectio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static activity.login.loginactivityusinghttpurlconnectio.AppConfig.URL_LOGIN;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import activity.login.loginactivityusinghttpurlconnectio.R;
import activity.login.loginactivityusinghttpurlconnectio.SessionMangement;

public class LoginActivity extends AppCompatActivity {
    EditText etUserName, etPassword;
    SessionMangement session;
    String username,password;
    Button lgnbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = (EditText) findViewById(R.id.editText1);
        etPassword = (EditText) findViewById(R.id.editText2);
        session = new SessionMangement(getApplicationContext());
        lgnbtn = (Button) findViewById(R.id.loginbtn);
        lgnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginRequest().execute();
            }
        });

    }
    public class LoginRequest extends AsyncTask<String,String,String>
    {
       private ProgressDialog pDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.setCanceledOnTouchOutside(false);
            //onDestroy();
        }
        @Override
        protected String doInBackground(String... strings) {
            username=etUserName.getText().toString();
            password=etPassword.getText().toString();
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = null;
            try {
                json = userFunction.loginUser("neeraj333","Test1234_");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject parentObject= null;
            try {
                parentObject = new JSONObject(json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                int Status=parentObject.getInt("Status");
                String ErrorMessage=parentObject.getString("ErrorMessage");
                String ExtendedErrorMessage=parentObject.getString("ExtendedErrorMessage");
                if(Status==200&&(ErrorMessage!=null&&ErrorMessage.isEmpty())&&(ExtendedErrorMessage!=null&&ExtendedErrorMessage.isEmpty()))
                {
                    session.createLoginSession(username,password);
                    return json.toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.setMessage("Loading User Space");
            pDialog.setTitle("Getting Data");
            pDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
            intent.putExtra("JsonResponse", s);
            startActivity(intent);

        }
    }
}
