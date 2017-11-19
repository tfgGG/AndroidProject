package com.example.jessicahuang.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class TestActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoolgeTool googletool = new GoolgeTool();
    public  Location location_Active = new Location("");
    public  GoogleMap mgooglemap;
    private FusedLocationProviderClient mFusedLocationClient;
    public double lat=23;
    public double lon=21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);


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
                        // Got last known location. In some rare situations this can be null
                        location_Active.setLatitude(location.getLatitude());
                        location_Active.setLongitude(location.getLongitude());
                        if (location == null) {
                            Toast toastfail = Toast.makeText(TestActivity.this,"沒有位置資訊", Toast.LENGTH_LONG);
                            toastfail.show();
                        }
                        else
                        {
                            String msg = "經度: "+location_Active.getLatitude()+"緯度: "+location_Active.getLongitude();
                            Log.d("debug",msg);
                            Toast toast = Toast.makeText(TestActivity.this,msg, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mgooglemap = map;
        mgooglemap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .title("現在位置"));
        //TODO:
        //location_Active.getLongitude(), location_Active.getLatitude())
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText(location_Active.getLatitude()+"  "+location_Active.getLongitude());
    }
}
