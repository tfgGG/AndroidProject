package com.example.jessicahuang.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public FusedLocationProviderClient mFusedLocationClient;
    public GoolgeTool goolgeTool = new GoolgeTool();
    private static final int REQUEST_LOCATION = 1;
    public List<Address> addresses;
    String msg="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ImageButton parking = (ImageButton) MainActivity.this.findViewById(R.id.parking);
        ImageButton garbage = (ImageButton) MainActivity.this.findViewById(R.id.garbage);
        ImageButton setting = (ImageButton) MainActivity.this.findViewById(R.id.setting);



        /*mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
        }*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            Log.d("!!!!REQUEST!!!","PERMISION");
        }else {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location l2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location l3 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if(l != null){
                goolgeTool.setLat(l.getLatitude());
                goolgeTool.setLon(l.getLongitude());
            }else if(l2 != null){
                goolgeTool.setLat(l2.getLatitude());
                goolgeTool.setLon(l2.getLongitude());
            }else if(l3 != null){
                goolgeTool.setLat(l3.getLatitude());
                goolgeTool.setLon(l3.getLongitude());
            }else{
                Toast toast = Toast.makeText(MainActivity.this, "無法取得位置", Toast.LENGTH_LONG);
                toast.show();
            }

        }

        Geocoder geocoder = new Geocoder(this, Locale.TAIWAN);
        try {
            addresses = geocoder.getFromLocation(goolgeTool.getLat(),goolgeTool.getLon(),1);
            Address address = addresses.get(0);

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
                msg=msg+address.getAddressLine(i).toString();

        } catch (Exception e) {
            e.printStackTrace();
            msg="沒有位置資訊 "+ e.getMessage().toString();
        }
        goolgeTool.setAddress(msg);

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
        Log.d("debug","MainActivity OnCreate");
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

