package com.example.jessicahuang.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        /*EditText ParkingRoad = (EditText)findViewById(R.id.ParkingRoad_Num);
        EditText ParkingLots = (EditText)findViewById(R.id.ParkingLots_Num);
        Spinner  MeterSpinner = (Spinner)findViewById(R.id.MeterSpinner);*/
        ImageButton parking = (ImageButton) MainActivity.this.findViewById(R.id.parking);
        ImageButton garbage = (ImageButton) MainActivity.this.findViewById(R.id.garbage);
        ImageButton setting = (ImageButton) MainActivity.this.findViewById(R.id.setting);

        /*Integer[] items = new Integer[]{100,300,500,1000,1500};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        MeterSpinner.setAdapter(adapter);
        MeterSpinner.setOnItemSelectedListener();*/
        parking.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent( MainActivity.this,ParkingActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        garbage.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent( MainActivity.this,GarbageActiviy.class);
                MainActivity.this.startActivity(intent);
            }
        });
        setting.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent( MainActivity.this,SettingActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
}

