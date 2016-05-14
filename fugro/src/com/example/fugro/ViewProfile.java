package com.example.fugro;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class ViewProfile extends ActionBarActivity {

    RoundImage img;
    TextView tvemail, tvmob, tvexpiry;
    EditText tvname, edtage;

    ImageButton back;

    String regdid, mob, ename, fname, age, expirydate, statuscode, msg;
    String mUserImage = "";

    int REQUEST_CAMERA = 1;
    int SELECT_FILE = 2;

    private Bitmap bitmap;
    InputStream is;
    BitmapFactory.Options bfo;
    Bitmap bitmapOrg;
    ByteArrayOutputStream bao;
    byte[] ba;

    Calendar c;
    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fname = getIntent().getStringExtra("username");
        mob = getIntent().getStringExtra("contactno");
        ename = getIntent().getStringExtra("email");
        regdid = getIntent().getStringExtra("UserId");

        img = (RoundImage) findViewById(R.id.roundimg);
        tvname = (EditText) findViewById(R.id.nametxt);
        tvemail = (TextView) findViewById(R.id.emailtxt);
        tvmob = (TextView) findViewById(R.id.mobile_number);
        tvexpiry = (TextView) findViewById(R.id.exptxt);

        edtage = (EditText) findViewById(R.id.editage);

        back = (ImageButton) findViewById(R.id.crossbtn);

        new SubscriptionDate().execute();

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectImage();
            }
        });

        tvname.setText(fname);
        tvemail.setText(ename);
        tvmob.setText(mob);

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        edtage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        tvname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                tvname.setFocusableInTouchMode(true);
                tvname.setFocusable(true);
                tvname.setClickable(true);
                tvname.setCursorVisible(true);
            }
        });

    }

    public int getAge(int _year, int _month, int _day) {

        int y, m, d, a;

        y = c.get(Calendar.YEAR);
        m = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);
        c.set(_year, _month, _day);
        a = y - c.get(Calendar.YEAR);
        if ((m < c.get(Calendar.MONTH))
                || ((m == c.get(Calendar.MONTH)) && (d < c
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            age = String.valueOf(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(" "));

            // Show selected date
           /* edtage.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
            edtage.setText(age);

        }
    };

    private void selectImage() {
        // TODO Auto-generated method stub
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 25, baos);
        byte[] arr = baos.toByteArray();
        String result = Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");

                img.setImageBitmap(bitmap);
                mUserImage = BitMapToString(bitmap);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                // Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                img.setImageBitmap(bitmap);
                mUserImage = BitMapToString(bitmap);
            }
        }
    }

    private class SubscriptionDate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ViewProfile.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            Jsonparser sh = new Jsonparser();
            // Making a request to url and getting response
            JSONObject jsonStr = sh.makeServiceCall(Utility.App_URL_SubscriptionExpiry + regdid, Jsonparser.GET, nameValuePair);

            Log.d("Response: ", "> " + jsonStr.toString());

            if (jsonStr != null) {
                try {


                    JSONObject jsonObj = new JSONObject(jsonStr.toString());
                    // Getting JSON Array node
                    // contacts = jsonObj.getJSONObject("Data");

                    statuscode = jsonObj.getString("error");
                  //  msg = jsonObj.getString("message");
                    if (statuscode.equals("false")) {
                        expirydate = jsonObj.optString("days");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
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

            if (statuscode.equals("false")) {

                tvexpiry.setText(expirydate +" Days");

            } /*else if (statuscode.trim().equals("true")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                        ViewProfile.this);
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
            }*/
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }
}

