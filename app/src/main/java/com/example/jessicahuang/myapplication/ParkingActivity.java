package com.example.jessicahuang.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ParkingActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {

    public GoogleMap mgooglemap;
    private GoolgeTool g;
    ListView listView;
    ParkingAdapter parkingAdapter;
    JSONObject requestObj;
    GoogleApiClient mGoogleApiClient;
    private int markerflag = 0;
    private Marker marker;
    private Marker searchmarker,Nowmarker;
    Place plcaceset;
    int countspace = 0 ;
    TextView parkingnum;
    TextView addresstxt;
    LocationManager locationManager;
    double lat,lon;
    Geocoder geocoder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_parking);

        geocoder= new Geocoder(this,Locale.TAIWAN);
        g = getIntent().getParcelableExtra("goolgetool");
        lat = g.getLat();
        lon = g.getLon();
        parkingnum = (TextView)findViewById(R.id.ParkingNum);
        addresstxt = (TextView)findViewById(R.id.Address);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        addresstxt.setText(g.getAddress());
        addresstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plcaceset  = null;
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lon))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();
                mgooglemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                SetJSONObject(lat,lon);
            }
        });

        ImageButton home = (ImageButton)findViewById(R.id.home);
        home.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        /*ImageButton refresh = (ImageButton)findViewById(R.id.refresh);
        refresh.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                RefreshData();
            }
        });*/

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listView = (ListView) findViewById(R.id.ParkingList);
        parkingAdapter = new ParkingAdapter(this);
        SetJSONObject(lat,lon);
        listView.setAdapter(parkingAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                JSONObject obj = (JSONObject)adapter.getItemAtPosition(position);
                try {
                    SetSpaceMarker(obj.getDouble("Lat"),obj.getDouble("Lon"),obj.getString("Name"));
                   // Toast toast = Toast.makeText(ParkingActivity.this,obj.getString("Id")+"  "+obj.getString("CellStatus")+" "+position, Toast.LENGTH_SHORT);
                    //toast.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                plcaceset=place;
                String messege = place.getName() + "地址" + place.getAddress()+ place.getLatLng();
                //Toast toast = Toast.makeText(ParkingActivity.this,messege,Toast.LENGTH_LONG);
                //toast.show();
                markerflag = 2;
                SetJSONObject(place.getLatLng().latitude,place.getLatLng().longitude);
                SetSpaceMarker(place.getLatLng().latitude,place.getLatLng().longitude,place.getName().toString());
                Log.i("Success", "Place: " + place.getName());
            }
            @Override
            public void onError(Status status) {
                String messege = status.getStatusMessage();
                Toast toast = Toast.makeText(ParkingActivity.this,messege, Toast.LENGTH_LONG);
                toast.show();
                Log.i("Failed", "An error occurred: " + status);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                getNowAdress();
                AddNowMarker(lat,lon);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
                Toast toast = Toast.makeText(ParkingActivity.this,"無法更新位置", Toast.LENGTH_LONG);
                toast.show();
            }
        };
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        locationManager.requestLocationUpdates("gps", 6000, 0, listener);

    }
    public void getNowAdress() {
        String msg = " ";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat,lon,1);
            Address address = addresses.get(0);

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
                msg=msg+address.getAddressLine(i).toString();

        } catch (Exception e) {
            e.printStackTrace();
            msg="沒有位置資訊 "+ e.getMessage().toString();
        }
        addresstxt.setText(msg);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mgooglemap = map;
        //TODO:Get map road and detail
        mgooglemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat,lon))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();
        AddNowMarker(g.getLat(), g.getLon());
        mgooglemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void AddNowMarker(double lat,double lon){

        if(Nowmarker!=null)
        Nowmarker.remove();
        Nowmarker = mgooglemap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lon))
                .title("現在位置"));
    }
    public void SetSpaceMarker(double Lat,double Lon,String name) {

        if(markerflag==0) {
           marker = mgooglemap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lon)).title(name)
                   .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            markerflag = 1;
        }else if(markerflag==1){
            marker.remove();
            marker = mgooglemap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lon)).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }else {
            if(marker != null)
                marker.remove();
            if(searchmarker!=null)
                searchmarker.remove();
            searchmarker = mgooglemap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lon)).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            markerflag = 0;
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Lat, Lon))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();

        mgooglemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    public  void DrawCircle(){
        try {
            JSONObject obj;
            BitmapDescriptor iconNo = BitmapDescriptorFactory.fromResource(R.drawable.redpoint);
            BitmapDescriptor iconYes = BitmapDescriptorFactory.fromResource(R.drawable.greenpot);
            for (int i=0;i<parkingAdapter.getCount();i++)
            {
                obj = (JSONObject) parkingAdapter.getItem(i);

                Marker marker = mgooglemap.addMarker(new MarkerOptions()
                        .position(new LatLng(obj.getDouble("Lat"),obj.getDouble("Lon"))));

                if (obj.getString("CellStatus").compareTo("Y")==0){
                    marker.setIcon(iconYes);
                    countspace++;
                }
                else
                    marker.setIcon(iconNo);
        }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("!!Success_1!!","MyCheck DrawCircle");
    }


    public  void SetJSONObject(Double Lat,Double Lon){
        String url = "http://140.136.148.203/Android_PHP/ReturnParkingSpace.php";
        String JsonOb = "{\"Lat\":\"" +Lat+ "\",\"Lon\":\"" + Lon +"\"}";
        try {
            requestObj = new JSONObject(JsonOb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, requestObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        JSONArray responseArr;
                        try {
                            responseArr = response.getJSONArray("ParkingResult");
                            parkingAdapter.updateData(responseArr);
                            DrawCircle();
                            parkingnum.setText("可停停車位:"+countspace+" 個停車位");
                            countspace = 0;
                            Log.d("!!Success_1!!","MyCheck"+response.toString());
                        }
                        catch (Exception e) {
                            Log.d("!!Fail_1!!!",e.getMessage().toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("!!!!!Error!!!!","JSON_Response_Error"+error.getMessage().toString());
                    }
                });

        QueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        Log.d("!!Sucess!!","MyCheck Finish getData");
        //return  Ans;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast toast = Toast.makeText(ParkingActivity.this,connectionResult.toString(), Toast.LENGTH_LONG);
        toast.show();

    }

    public void RefreshData(){



    }
}
