package com.example.jessicahuang.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ParkingActivity extends AppCompatActivity implements OnMapReadyCallback {

    public GoogleMap mgooglemap;
    public  GoolgeTool g;
    public List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_parking);

        g = getIntent().getParcelableExtra("goolgetool");
        String msg="";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(g.getLat(), g.getLon(),1);
            Address address = addresses.get(0);

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
                msg=msg+address.getAddressLine(i).toString();

        } catch (Exception e) {
            e.printStackTrace();
            msg="沒有位置資訊 "+ e.getMessage().toString();
        }

        TextView parkingnum = (TextView)findViewById(R.id.ParkingNum);
        TextView addresstxt = (TextView)findViewById(R.id.Address);
        addresstxt.setText(msg);
        //TODO:changetxt
       // parkingnum.setText("經度:"+ g.getLat() + "緯度:" + g.getLon());
        ImageButton home = (ImageButton)findViewById(R.id.home);
        home.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {

        mgooglemap = map;
        //TODO:Get map road and detail
        mgooglemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(g.getLat(), g.getLon()))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();

        mgooglemap.addMarker(new MarkerOptions()
                .position(new LatLng(g.getLat(),g.getLon()))
                .title("現在位置"));

        mgooglemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

}
