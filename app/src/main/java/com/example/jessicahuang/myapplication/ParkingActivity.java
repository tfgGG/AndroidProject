package com.example.jessicahuang.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    public List<Address> addresses;
    String Ans="";
    ListView listView;
    ParkingAdapter parkingAdapter;
    JSONObject requestObj;
    GoogleApiClient mGoogleApiClient;
    private int markerflag = 0;
    private Marker marker;
    private Marker searchmarker;
    ArrayList<Circle> circlearray ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_parking);

        g = getIntent().getParcelableExtra("goolgetool");

        TextView parkingnum = (TextView)findViewById(R.id.ParkingNum);
        TextView addresstxt = (TextView)findViewById(R.id.Address);
        addresstxt.setText(g.getAddress());
        addresstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(g.getLat(), g.getLon()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();
                mgooglemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                SetJSONObject(g.getLat(),g.getLon());
            }
        });

        ImageButton home = (ImageButton)findViewById(R.id.home);
        home.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        ImageButton refresh = (ImageButton)findViewById(R.id.refresh);
        refresh.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                RefreshData();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listView = (ListView) findViewById(R.id.ParkingList);
        parkingAdapter = new ParkingAdapter(this);
        SetJSONObject(g.getLat(),g.getLon());
        parkingnum.setText("可停停車位:"+parkingAdapter.getYesPark()+" 個停車位");
        listView.setAdapter(parkingAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                JSONObject obj = (JSONObject)adapter.getItemAtPosition(position);
                try {
                    SetSpaceMarker(obj.getDouble("Lat"),obj.getDouble("Lon"),obj.getString("Name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(ParkingActivity.this,obj.toString(), Toast.LENGTH_LONG);
                toast.show();

            }
        });


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

                String messege = place.getName() + "地址" + place.getAddress()+ place.getLatLng();
                Toast toast = Toast.makeText(ParkingActivity.this,messege,Toast.LENGTH_LONG);
                toast.show();
                markerflag = 2;
                SetJSONObject(place.getLatLng().latitude,place.getLatLng().longitude);
                SetSpaceMarker(place.getLatLng().latitude,place.getLatLng().longitude,place.getName().toString());
                Log.i("Success", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                String messege = status.getStatusMessage();
                Toast toast = Toast.makeText(ParkingActivity.this,messege, Toast.LENGTH_LONG);
                toast.show();
                Log.i("Failed", "An error occurred: " + status);
            }
        });
       /* AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();
        autocompleteFragment.setFilter(typeFilter);*/


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
        JSONObject obj;
        for (int i=0;i<parkingAdapter.getCount();i++)
        {
            obj = (JSONObject) parkingAdapter.getItem(i);
            CircleOptions circleOptions = null;
            try {
                circleOptions = new CircleOptions()
                        .center(new LatLng(obj.getDouble("Lat"), obj.getDouble("Lon")))
                        .radius(5000);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Circle circle = mgooglemap.addCircle(circleOptions);
            circlearray.add(circle);
        }
    }

    public void RefreshData(){


    //TODO:


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
                            //Ans = response.toString();
                            Log.d("!!Success_1!!",response.toString());
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
        //return  Ans;

    }

    public String getParking() {

        //String requestBody = "Lat="+String.valueOf(g.getLat())+"&Lon="+String.valueOf(g.getLon());
        //String url ="http://140.136.148.203/Android_PHP/mes.php";
        String url = "http://140.136.148.203/Android_PHP/ReturnParkingSpace.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Ans= response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Ans = error.getMessage().toString();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Lat", String.valueOf(g.getLat()) );
                map.put("Lon", String.valueOf(g.getLon()) );
                return map;
            }
        };
        QueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
        return  Ans;
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast toast = Toast.makeText(ParkingActivity.this,connectionResult.toString(), Toast.LENGTH_LONG);
        toast.show();

    }

}
