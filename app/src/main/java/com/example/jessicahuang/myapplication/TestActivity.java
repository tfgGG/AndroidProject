package com.example.jessicahuang.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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

import com.google.android.gms.common.api.GoogleApiClient;
/*import com.google.android.gms.common.api.;*/
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


public class TestActivity extends AppCompatActivity implements OnMapReadyCallback,OnConnectionFailedListener{

    public  GoogleMap mgooglemap;
    public  GoolgeTool g;
    public List<Address> addresses;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        g = getIntent().getParcelableExtra("goolgetool");
        String msg="";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(g.getLat(), g.getLon(),1);
            Address address = addresses.get(0);

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
               msg=msg+address.getAddressLine(i).toString();

        } catch (IOException e) {
            e.printStackTrace();
            msg="沒有位置資訊 "+ e.getMessage().toString();
        }

        TextView textaddress = (TextView)findViewById(R.id.textView2);
        textaddress.setText(msg);
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText("經度:"+ g.getLat() + "緯度:" + g.getLon());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String messege = place.getName() + "地址" + place.getAddress()+ place.getLatLng();
                Toast toast = Toast.makeText(TestActivity.this,messege,Toast.LENGTH_LONG);
                toast.show();
                Log.i("Success", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                String messege = status.getStatusMessage();
                Toast toast = Toast.makeText(TestActivity.this,messege, Toast.LENGTH_LONG);
                toast.show();
                Log.i("Failed", "An error occurred: " + status);
            }
        });
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();
        autocompleteFragment.setFilter(typeFilter);

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast toast = Toast.makeText(TestActivity.this,connectionResult.toString(), Toast.LENGTH_LONG);
        toast.show();

    }
}
