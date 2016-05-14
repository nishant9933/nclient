package com.example.fugro;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class FugroActivity extends AppCompatActivity {

    RelativeLayout rl;

    private static int SPLASH_TIME_OUT = 5000;

    EditText editText1, editText2;
    String mob, pass, ename;

    Button Verfybtn;

    String name, email, statuscode, msg, userID, contactno, userno, deviceid, area, username, lat, lng;
    SessionManager session;

    ProgressDialog pDialog;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fugro);
        
        try{

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceid = manager.getDeviceId();

        Log.d("Device Id:", deviceid);

        rl = (RelativeLayout)findViewById(R.id.logrelative);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                rl.setVisibility(View.VISIBLE);

                // close this activity
                // finish();
            }
        }, SPLASH_TIME_OUT);


        session = new SessionManager(getApplicationContext());

        editText2 = (EditText) findViewById(R.id.editText2);

        editText1 = (EditText) findViewById(R.id.editText3);


        Verfybtn = (Button) findViewById(R.id.Verfybtn);
        Verfybtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //startActivity(new Intent(getApplicationContext(),HomeActivity.class));


                ename = editText2.getText().toString().trim();
                mob = editText1.getText().toString().trim();
                //   pass = editText3.getText().toString().trim();

                if (mob.equals("")) {
                    editText1.setError("Field is required!!");
               } else if (ename.equals("")) {
                    editText2.setError("Field is required!!");
                } else if (Utility.isNetworkAvailable(getApplicationContext())) {
                    new UserLogin().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_LONG).show();
                }
            }


        });
        }
        catch(  Exception ex){
        	FileLogging.LogError("FugroActivity", ex.getMessage());
        }

    }

    private class UserLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FugroActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("contact_number", mob));
            nameValuePair.add(new BasicNameValuePair("username", ename));
            nameValuePair.add(new BasicNameValuePair("device_id", deviceid));

try{
            Jsonparser sh = new Jsonparser();
            // Making a request to url and getting response
            JSONObject jsonStr = sh.makeServiceCall(Utility.App_URL_Login, Jsonparser.POST, nameValuePair);

         

            if (jsonStr != null) {
                try {


                    JSONObject jsonObj = new JSONObject(jsonStr.toString());
                    // Getting JSON Array node
                    // contacts = jsonObj.getJSONObject("Data");

                    statuscode = jsonObj.getString("error");
                    msg = jsonObj.getString("message");
                    if (statuscode.equals("false")) {
                        name = jsonObj.getString("fullName");
                        email = jsonObj.getString("email");
                        contactno = jsonObj.getString("contract_number");
                        userno = jsonObj.getString("users");
                        userID = jsonObj.getString("userID");
                        username = jsonObj.getString("username");

                        JSONArray areaarr = jsonObj.getJSONArray("area");
                        for (int i=0; i<areaarr.length(); i++){
                            JSONObject areaobj = areaarr.getJSONObject(i);
                            area = areaobj.getString("area");

                            String[] latlong = area.split(",");
                            lat = latlong[i];
                            lng = latlong[i+1];

                            Log.d("Lat",lat);
                            Log.d("Lng",lng);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
}
catch (Exception e) {
    FileLogging.LogError("LOGIN", e.getMessage());
}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

          //  startActivity(new Intent(getApplicationContext(),HomeActivity.class));
try{
            if (statuscode != null && statuscode.equals("false")) {
                session.createLoginSession("Fugro", editText2.getText().toString());

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("UserId",userID);
                i.putExtra("username", name);
                i.putExtra("email", email);
                i.putExtra("contactno",contactno);
                i.putExtra("NoofUsers",userno);
                i.putExtra("Area",area);
                i.putExtra("Lng",lng);
                i.putExtra("Lat",lat);
                startActivity(i);
                finish();
            } else if (statuscode != null && statuscode.trim().equals("true")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                        FugroActivity.this);
                // set title
                alertDialogBuilder.setTitle("Status");

                // set dialog message
                alertDialogBuilder
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                dialog.cancel();
                            }
                        });
//
                alertDialogBuilder.show();
            }else{
            	Toast.makeText(getApplicationContext(), "Could not fatch data please check network connectivity", Toast.LENGTH_SHORT).show();
            }
        
        }
        catch(  Exception ex){
        	FileLogging.LogError("FugroActivity", ex.getMessage());
        }
        }
    }
        @Override
        public void onDestroy() {
            super.onDestroy();
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.cancel();
            }
        }

}