package com.example.fugro;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Sneha on 08-05-2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class SettingList extends ActionBarActivity {

    ListView listView;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_list);
        //  getSupportActionBar().hide();

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        back = (ImageButton) findViewById(R.id.crosbtn);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        String[] values = new String[]{
            "Notification Tune",
            "Notification Color"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                switch( position ) {
                    case 0:
                      //  startActivity(new Intent(getApplicationContext(),About.class));
                        break;
                    case 1:

                      //shareIt();

                        break;

                }
            }

        });
    }
}
