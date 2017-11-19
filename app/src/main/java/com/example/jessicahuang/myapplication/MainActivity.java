package com.example.jessicahuang.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity  {

    private FusedLocationProviderClient mFusedLocationClient;
    private  Location location_Active = new Location("");
    public GoolgeTool goolgeTool = new GoolgeTool();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(v);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location) {

                        goolgeTool.setLon(location.getLongitude());
                        goolgeTool.setLat(location.getLatitude());
                        if (location == null) {
                            Toast toastfail = Toast.makeText(MainActivity.this,"沒有位置資訊", Toast.LENGTH_LONG);
                            toastfail.show();
                        }
                        else
                        {
                            String msg = "經度: "+location.getLatitude()+"緯度: "+location.getLongitude();
                            Log.d("debug",msg);
                            Toast toast = Toast.makeText(MainActivity.this,msg, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void sendMessage(View view) {

        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("goolgetool", goolgeTool);
        startActivity(intent);
    }


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
