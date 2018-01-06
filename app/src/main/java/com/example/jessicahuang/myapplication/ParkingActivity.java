package com.example.jessicahuang.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ParkingActivity extends AppCompatActivity implements OnMapReadyCallback {

    public GoogleMap mgooglemap;
    public  GoolgeTool g;
    public List<Address> addresses;
    String Ans="";
    TextView retrievetxt;
    ListView listView;
    ParkingAdapter parkingAdapter;
    JSONObject requestObj;

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

        listView = (ListView) findViewById(R.id.ParkingList);
        parkingAdapter = new ParkingAdapter(this);
        SetJSONObject();
        parkingnum.setText("可停停車位:"+parkingAdapter.getYesPark()+" 個停車位");
        listView.setAdapter(parkingAdapter);

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

    public  String SetJSONObject(){
        String url = "http://140.136.148.203/Android_PHP/ReturnParkingSpace.php";
        //String requestBody = "?Lat="+String.valueOf(g.getLat())+"&Lon="+String.valueOf(g.getLon());
        String JsonOb = "{\"Lat\":\""+g.getLat()+"\",\"Lon\":\""+ g.getLon()+"\"}";
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
        return  Ans;

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

}
