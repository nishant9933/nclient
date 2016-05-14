package com.example.fugro;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;



/**
 * Created by Sneha on 29-04-2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener{

    private ViewPager viewPager, viewPager2, viewPager3;
    LinearLayout viewPager4;
    SlidingTabLayout slidingTabLayout;
    private ProgressDialog pDialog;
    private String[] mSetting, msettingtitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private RoundImage img;
    private Button menubtn;
    private TextView usertv;
    public static String username,contactImg, emailid,contactno,noofusers,lat,lng,userId;
    private SessionManager session;
    private Boolean valuemenu = false;
    private Boolean value = true;
    public static ServicesData completeData;
    /*Button currentloc;
    RadioButton btnsearch,btnchat,btnnotification,btncontact,btnbotlist;*/
    SharedPreferences preferences;
    String UserID;
    String userProfileImage = "NotFound";

    String[] titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	FileLogging.Log("Launch main", "main activity started");
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_home);

        preferences = getSharedPreferences("LOGIN_AUTHENTICATION", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = preferences.edit();
        UserID=preferences.getString("UserID", null);

        titles = new String[]{"All Alerts", "Urgent Alert", "Happening Alerts", "Alerts After 6 Hours"};

        if (Utility.isNetworkAvailable(getApplicationContext())) {
            new UserServiceTab().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_LONG).show();
        }

        userId = getIntent().getStringExtra("UserId");
        username = getIntent().getStringExtra("username");
        emailid = getIntent().getStringExtra("email");
        contactno = getIntent().getStringExtra("contactno");
        noofusers = getIntent().getStringExtra("NoofUsers");
        contactImg = getIntent().getStringExtra("ProPic");
        lat = "28.401064827220896"; //getIntent().getStringExtra("Lat");
        lng = "53.2177734375"; //getIntent().getStringExtra("Lng");

        session = new SessionManager(getApplicationContext());
        usertv = (TextView) findViewById(R.id.textView);
        if(username != null)
         usertv.setText(username);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
           // setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.menue_btn);
            toolbar.setTitle("");
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager2 = (ViewPager) findViewById(R.id.viewpager2);
        viewPager3 = (ViewPager) findViewById(R.id.viewpager3);
        viewPager4 = (LinearLayout) findViewById(R.id.viewpager4);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);


        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.parseColor("#37A4CE");
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    //drawer is open
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else {
                    //noinspection WrongConstant
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        mSetting = new String[]{"View Profile", "Settings", "Logout"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.drawer_list_item, R.id.textView, mSetting);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        //menubtn = (Button)findViewById(R.id.menubtn);
        mTitle = "test";

       /* ll = (LinearLayout) findViewById(R.id.llayout);
        lv = (ListView) findViewById(R.id.nleft_drawer);
        ll.setVisibility(View.GONE);*/

        img = (RoundImage) findViewById(R.id.roundimg);
        // byte[] encodeByte=Base64.(this.getResources(),R.drawable.proimg);
        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.proimg);
        img.setImageBitmap(Utility.getRoundedBitmap(bm));

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ViewProfile.class));
            }
        });
        }
        catch( Exception ex){
        	FileLogging.LogError("Homeactivity", ex.getMessage());
        }
        
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private class UserServiceTab extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            /*Jsonparser sh = new Jsonparser();
            // Making a request to url and getting response
            JSONObject jsonStr = sh.makeServiceCall(Utility.App_URL_GetServiceList.toString().trim() + "/" + value, Jsonparser.GET);
            Log.d("Response: ", "> " + jsonStr.toString());
            try {

                JSONObject jsonObj = new JSONObject(jsonStr.toString());
                // Getting JSON Array node
                contacts = jsonObj.getJSONArray("Data");
                Log.d("Service Data:", ">" + contacts.toString());
                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    mainid = c.getString("ServiceID");
                    mainname = c.getString("ServiceName");
                    stringArrayListID.add(mainid);
                    stringArrayListName.add(mainname);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            if (pDialog.isShowing())
                pDialog.dismiss();

           /* for (int i = 0; i < stringArrayListID.size(); i++) {

                contactID = new String[i];
                contactID = stringArrayListID.toArray(new String[stringArrayListID.size()]);
                contactName = new String[i];
                contactName = stringArrayListName.toArray(new String[stringArrayListName.size()]);

            }*/
            viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager(),titles));
            slidingTabLayout.setViewPager(viewPager);

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //   getSupportActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(@SuppressWarnings("rawtypes") AdapterView parent, View view, int position, long id) {

            switch (position) {
                case 0:
                    Intent i = new Intent(getApplicationContext(), ViewProfile.class);
                    i.putExtra("UserId",userId);
                    i.putExtra("username", username);
                    i.putExtra("email", emailid);
                    i.putExtra("contactno",contactno);
                    i.putExtra("NoofUsers",noofusers);
                    i.putExtra("Lng",lng);
                    i.putExtra("Lat",lat);
                    startActivity(i);


                    break;
                case 1:

                    Intent iinent1 = new Intent(HomeActivity.this, SettingList.class);
                    startActivity(iinent1);

                    break;
                case 2:

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            HomeActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("Log Out");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click OK to logout")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    session.logoutUser();
                                }
                            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alertDialogBuilder.show();

                    break;

            }

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mSetting[position]);
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
