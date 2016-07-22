package activity.login.loginactivityusinghttpurlconnectio;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ProgressDialog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View;


public class AfterLogin extends AppCompatActivity  implements ListView.OnItemClickListener{
    public static final String KEY_ACCOUNT_ID = "account_id";
    public static final String KEY_ACCOUNT_TYPE = "account_type";
    public static final String KEY_EXTENDED_ACCOUNT_TYPE = "extended_account_type";
    String SessionId = null;
    String ClientSSN = null;
    ListView listView;
    String loginResponse;
    TextView responseAfterLogin;
    SessionMangement sessionMangement;
    List<AccountModel> accountModelsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        listView= (ListView) findViewById(R.id.lvAccounts);
        listView.setOnItemClickListener(this);
        Intent intentExtras = getIntent();
        loginResponse = intentExtras.getStringExtra("JsonResponse");
        try {
            JSONObject jsonObject = new JSONObject(loginResponse);
            SessionId = jsonObject.optString("SessionId");
            ClientSSN = jsonObject.optString("SSN");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ResponseAfterLoginActivity().execute();
    }
    public class ResponseAfterLoginActivity extends AsyncTask<String, String,  List<AccountModel>> {
         private ProgressDialog pDialog;
         protected void onPreExecute() {
             super.onPreExecute();
             pDialog = new ProgressDialog(AfterLogin.this);
             pDialog.setTitle("Contacting Servers");
             pDialog.setMessage("Reponse Coming ...");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
             pDialog.setCanceledOnTouchOutside(false);
         }
        @Override
        protected  List<AccountModel> doInBackground(String... strings) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject afterLogin = null;
            try {
                afterLogin = userFunction.afterLogin(SessionId, ClientSSN);
                JSONObject parentObject=new JSONObject(afterLogin.toString());
                JSONArray parentArray=parentObject.getJSONArray("Accounts");
                accountModelsList=new ArrayList<>();
                for(int i=0;i<parentArray.length();i++)
                {
                    JSONObject finalObject =parentArray.getJSONObject(i);
                    AccountModel accountModel=new AccountModel();
                    accountModel.setFIId(finalObject.getInt("FIId"));
                    accountModel.setFIName(finalObject.getString("FIName"));
                    accountModel.setAcctId(finalObject.getInt("AcctId"));
                    accountModel.setAccountType(finalObject.getString("AccountType"));
                    accountModel.setExtendedAccountType(finalObject.getString("ExtendedAccountType"));
                    accountModelsList.add(accountModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return accountModelsList;
        }

        @Override
        protected void onPostExecute( List<AccountModel> s) {
            super.onPostExecute(s);
            pDialog.setMessage("Loading User Space");
            pDialog.setTitle("Getting Data");
            pDialog.dismiss();
            AccountAdapter adapter=new AccountAdapter(getApplicationContext(),R.layout.account_row,s);
            listView.setAdapter(adapter);


        }
    }
    public class AccountAdapter extends ArrayAdapter{
        private List<AccountModel> accountModel;
        private int resource;
        private LayoutInflater inflater;
        public AccountAdapter(Context context, int resource, List<AccountModel> objects) {
            super(context, resource, objects);
            accountModel=objects;
            this.resource=resource;
            inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null) {
                convertView = inflater.inflate(resource, null);
            }
                TextView fiid;
                TextView finame;
                TextView acctid;
                TextView accounttype,extendedaccounttype;

                fiid= (TextView)convertView.findViewById(R.id.textView);
                finame= (TextView)convertView.findViewById(R.id.textView2);
                acctid= (TextView)convertView.findViewById(R.id.textView3);
                accounttype=(TextView)convertView.findViewById(R.id.textView4);
                extendedaccounttype= (TextView) convertView.findViewById(R.id.textVie);
                fiid.setText(Integer.toString(accountModel.get(position).getFIId()));
                finame.setText(accountModel.get(position).getFIName());
                acctid.setText(Integer.toString(accountModel.get(position).getAcctId()));
                accounttype.setText((accountModel.get(position).getAccountType()));
                extendedaccounttype.setText(accountModel.get(position).getExtendedAccountType());

            return convertView;
        }

    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent=new Intent(this,GetTransactionByDate.class);
        AccountModel accountList=accountModelsList.get(i);
        intent.putExtra("SessionId",SessionId);
        intent.putExtra(KEY_ACCOUNT_ID,Integer.toString(accountList.getAcctId()));
        intent.putExtra(KEY_ACCOUNT_TYPE,accountList.getAccountType());
        intent.putExtra(KEY_EXTENDED_ACCOUNT_TYPE,accountList.getExtendedAccountType());
        startActivity(intent);
    }

}
