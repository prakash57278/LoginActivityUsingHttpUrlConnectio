package activity.login.loginactivityusinghttpurlconnectio;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GetTransactionByDate extends AppCompatActivity {
    String Sessionid=null;
    String Accountid=null;
    String Accounttype=null;
    String ExtendedType=null;
    EditText editText1,editText2;
    ListView listViewTransaction;
    Button submit;
    TextView textView;
    List<GetTransactionModel> getTransactionModelsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_transaction_by_date);
        Intent intent=getIntent();
        Sessionid=intent.getStringExtra("SessionId");
        editText1= (EditText) findViewById(R.id.editText4);
        editText2= (EditText) findViewById(R.id.editText5);
        Accountid=intent.getStringExtra(AfterLogin.KEY_ACCOUNT_ID);
        Accounttype=intent.getStringExtra(AfterLogin.KEY_ACCOUNT_TYPE);
        ExtendedType=intent.getStringExtra(AfterLogin.KEY_EXTENDED_ACCOUNT_TYPE);
        listViewTransaction= (ListView)findViewById(R.id.listTransaction);
        submit= (Button) findViewById(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetTransaction().execute();
            }
        });


    }
    public class GetTransaction extends AsyncTask<String,String,List<GetTransactionModel>>
    {
        private ProgressDialog pDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GetTransactionByDate.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected List<GetTransactionModel> doInBackground(String... strings) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = null;
            try {
                String sDate,eDate;
                sDate=editText1.getText().toString();
                eDate=editText2.getText().toString();
                json = userFunction.getTransactionByDate(Sessionid,Accountid,Accounttype,ExtendedType,sDate,eDate);
                JSONObject parentObject=new JSONObject(json.toString());
                JSONArray parentArray=parentObject.getJSONArray("Transactions");
                getTransactionModelsList=new ArrayList<>();
                for(int i=0;i<parentArray.length();i++)
                {
                    JSONObject finalObject =parentArray.getJSONObject(i);
                    GetTransactionModel transactionModel= new GetTransactionModel();
                    transactionModel.setTransactionDate(finalObject.getString("TransactionDate"));
                    transactionModel.setAmount(finalObject.getInt("Amount"));
                    transactionModel.setDescription(finalObject.getString("Description"));
                    getTransactionModelsList.add(transactionModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getTransactionModelsList;
        }

        @Override
        protected void onPostExecute(List<GetTransactionModel> s){
            super.onPostExecute(s);
            pDialog.setMessage("Loading User Space");
            pDialog.setTitle("Getting Data");
            pDialog.dismiss();
            GetTransactionAdapter adapter=new GetTransactionAdapter(getApplicationContext(),R.layout.get_transaction_row,s);
            listViewTransaction.setAdapter(adapter);

        }
    }
    public class GetTransactionAdapter extends ArrayAdapter {

        private List<GetTransactionModel> transactionModel;
        private int resource;
        private LayoutInflater inflater;
        public GetTransactionAdapter(Context context, int resource,List<GetTransactionModel> objects) {
            super(context, resource, objects);
            transactionModel=objects;
            this.resource=resource;
            inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null) {
                convertView = inflater.inflate(resource, null);
            }
            TextView date;
            TextView credit;
            TextView debit;
            TextView description;
            int amount;
            String filterdater,finaldate;
            date= (TextView)convertView.findViewById(R.id.textView7);
            credit= (TextView)convertView.findViewById(R.id.textView8);
            debit= (TextView)convertView.findViewById(R.id.textView9);
            description=(TextView)convertView.findViewById(R.id.textView10);
            amount=transactionModel.get(position).getAmount();
            filterdater=transactionModel.get(position).getTransactionDate();
            finaldate=filterdater.substring(0,10);
            date.setText(finaldate);
            if(amount>0)
                credit.setText(Integer.toString(transactionModel.get(position).getAmount()));
            else
                debit.setText(Integer.toString(transactionModel.get(position).getAmount()));

            description.setText(transactionModel.get(position).getDescription());


            return convertView;
        }

    }
}
