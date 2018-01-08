package com.example.jessicahuang.myapplication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GarbageDetailActiviy extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {

    String ID;
    ListView listView;
    TextView garbagename ;
    TextView addresstxt;
    GarbageDetailAdapter GarbageDetailAdapter;
    ArrayList<CountDownTimer> countDownTimers;
    CountDownTimer CurrentClock;
    private GoolgeTool g;
    public GoogleMap mgooglemap;
    JSONObject requestObj;
    JSONArray responseArr;
    int markerflag = 0;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_detail);

        ID = getIntent().getStringExtra("Id");
        g = getIntent().getParcelableExtra("googletool");
        String name= getIntent().getStringExtra("Name");

        Toast toast = Toast.makeText(GarbageDetailActiviy.this,ID, Toast.LENGTH_LONG);
        toast.show();

        garbagename = (TextView)findViewById(R.id.GarbageName);
        addresstxt = (TextView)findViewById(R.id.Address);
        garbagename.setText(name);

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

        listView = (ListView) findViewById(R.id.GarbageDetailList);
        GarbageDetailAdapter = new GarbageDetailAdapter(this);
        SetJSONObject(ID);
        listView.setAdapter(GarbageDetailAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                JSONObject obj = (JSONObject)adapter.getItemAtPosition(position);
                try {
                    SetSpaceMarker(obj.getDouble("lat"),obj.getDouble("lon"),obj.getString("Name"));
                    /*CurrentClock.onFinish();
                    SetSpaceMarker(obj.getDouble("Lat"),obj.getDouble("Lon"),obj.getString("Name"));
                    String time1 = obj.getString("Time");
                    String time2 = String.valueOf(Calendar.getInstance(Locale.TAIWAN).getTime());
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    Date date1 = format.parse(time1);
                    Date date2 = format.parse(time2);
                    long difference = date2.getTime() - date1.getTime();
                    NewClock(difference);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast toast = Toast.makeText(GarbageDetailActiviy.this,obj.toString(), Toast.LENGTH_LONG);
                //toast.show();
            }
        });


    }

   /* public void  NewClock(long counttime ){

        CurrentClock = new CountDownTimer(counttime,60000){
            @Override
            public void onFinish() {
               clocktxt.setText("垃圾車已過站");
            }
            @Override
            public void onTick(long millisUntil){
                long hour = millisUntil/3600000;
                long min =  millisUntil%3600000;

                if(millisUntil>=3600000)
                    clocktxt.setText(hour+"時"+min+"分");
                else
                    clocktxt.setText(min+"分");
            }
        }.start();
    }*/
    @Override
    public void onMapReady(GoogleMap map) {

        mgooglemap = map;
        mgooglemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //TODO:
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
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Lat, Lon))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();

        mgooglemap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public  void SetJSONObject(String Id){
        String url = "http://140.136.148.203/Android_PHP/ReturnTrashDetail.php";
       // String JsonOb = "{\"Lat\":\"" +Lat+ "\",\"Lon\":\"" + Lon +"\",\"type\":\"trash\"}";
        String JsonOb = "{\"ID\":\""+Id+"\"}";
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
                        try {
                            responseArr = response.getJSONArray("ParkingResult");
                            GarbageDetailAdapter.updateData(responseArr);
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
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast toast = Toast.makeText(GarbageDetailActiviy.this,connectionResult.toString(), Toast.LENGTH_LONG);
        toast.show();

    }
}
