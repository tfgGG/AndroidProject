package com.example.jessicahuang.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


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
                intent.putExtra("goolgetool", goolgeTool);
                MainActivity.this.startActivity(intent);
            }
        });
        setting.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        parking.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent( MainActivity.this, ParkingActivity.class);
                intent.putExtra("goolgetool", goolgeTool);
                MainActivity.this.startActivity(intent);
            }
        });

    }


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

