package com.example.fugro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;




public class HappeningsAlertFragment extends Fragment {

    public static String ARG_POSITION = "position";

    private int position;

    ListView lv;

    ArrayList<HappeningItems> dataItems;

    FrgmntAdapter dataAdapter;

    ProgressDialog pDialog;

    JSONObject mainAlert = null;

    JSONArray contacts = null;

    String title, condition, detail, from, to;

    public static HappeningsAlertFragment newInstance(int position) {
        HappeningsAlertFragment f = new HappeningsAlertFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        position = getArguments().getInt(ARG_POSITION);

        View rootView = inflater.inflate(R.layout.fragment_pers, container, false);

        lv = (ListView) rootView.findViewById(R.id.listfrag);

        dataItems = new ArrayList<HappeningItems>();

        new Alerts().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getActivity(),DetailActivity.class);
                in.putExtra("Detail", dataItems.get(position).getEventdescription());
                startActivity(in);
            }
        });

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    private class Alerts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance

            Jsonparser sh = new Jsonparser();

            // Making a request to url and getting response
            JSONObject jsonStr = sh.makeServiceCall(Utility.App_URL_GetAllAlerts+"/"+HomeActivity.lat+"/"+HomeActivity.lng , Jsonparser.GET, null);

            HappeningItems happeningItems = null;
            if(jsonStr != null){
            try{

                JSONObject jsonObject = new JSONObject(jsonStr.toString());

                mainAlert = jsonObject.getJSONObject("allerts");

                contacts = mainAlert.getJSONArray("happeningAlerts");

                for (int i=0; i<contacts.length(); i++){

                    JSONObject obj = contacts.getJSONObject(i);

                    title = obj.getString("condition");
                    condition = obj.getString("condition");
                    detail = obj.getString("name");
                    from = obj.getString("from");
                    to = obj.getString("to");

                    happeningItems = new HappeningItems(title,from,to,detail);

                    dataItems.add(happeningItems);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Response: ", "> " + jsonStr.toString());
            }
            else{
            	FileLogging.LogError("ALertFragment", "No data found while fatching all record");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            dataAdapter = new FrgmntAdapter(getActivity(), R.layout.fraglist, dataItems);

            lv.setAdapter(dataAdapter);

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
