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
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.R.attr.button;


public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    public GoolgeTool goolgeTool = new GoolgeTool();
    private static final int REQUEST_LOCATION = 1;
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

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            Log.d("!!!!REQUEST!!!","PERMISION");
        }else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location == null) {
                                Toast toastfail = Toast.makeText(MainActivity.this, "沒有位置資訊", Toast.LENGTH_LONG);
                                toastfail.show();
                            } else {
                                goolgeTool.setLon(location.getLongitude());
                                goolgeTool.setLat(location.getLatitude());
                                String msg = "經度: " + location.getLatitude() + "緯度: " + location.getLongitude();
                                Log.d("debug", msg);
                                Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });
            Log.d("!!!GETLOCATION!!!","LATITUDE"+goolgeTool.getLat());
        }

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

    public void sendMessage(View view) {

        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("goolgetool", goolgeTool);
        startActivity(intent);
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
<<<<<<< HEAD
*/

    protected void onPause() {
        super.onPause();
        Log.d("debug","MainActivity Onpause");
    }
    protected void onStop(){

        super.onStop();
        Log.d("debug","MainActivity OnStop");
    }
    protected void onDestroy(){

        super.onDestroy();
        Log.d("debug","MainActivity OnDestroy");

    }
    protected void onStart(){

        super.onStart();
        Log.d("debug","MainActivity OnStart");

    }
    protected void onRestart(){

        super.onRestart();
        Log.d("debug","MainActivity OnReStart");

    }
    protected void onResume(){

        super.onResume();
        Log.d("debug","MainActivity OnResume");

    }

}

