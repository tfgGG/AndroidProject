package com.example.jessicahuang.myapplication;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GarbageActiviy extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    ListView listView;
    TextView addresstxt;
    GarbageAdapter GarbageAdapter;
    private GoolgeTool g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage);
        g = getIntent().getParcelableExtra("goolgetool");


        addresstxt = (TextView)findViewById(R.id.Address);
        addresstxt.setText(g.getAddress());

        TextView addresstxt = (TextView)findViewById(R.id.Address);
        addresstxt.setText(g.getAddress());

        ImageButton home = (ImageButton)findViewById(R.id.home);
        home.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.GarbageList);
        GarbageAdapter = new GarbageAdapter(this);
        GetTrashLine("http://140.136.148.203/Android_PHP/ReturnTrash.php");


        listView.setAdapter(GarbageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                JSONObject obj = (JSONObject)adapter.getItemAtPosition(position);

                Toast toast = Toast.makeText(GarbageActiviy.this,obj.toString(), Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent( GarbageActiviy.this,GarbageDetailActiviy.class);
                try {
                    intent.putExtra("Id", obj.getString("LineId"));
                    intent.putExtra("googletool",g);
                    intent.putExtra("Name",obj.getString("City")+obj.getString("LineName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

    }


    public  void GetTrashLine(String url){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        JSONArray responseArr;
                        try {
                            responseArr = response.getJSONArray("ParkingResult");
                            GarbageAdapter.updateData(responseArr);
                            Log.d("!!Success_1!!",response.toString());
                        }
                        catch (Exception e) {
                            Log.d("!!Fail_1!!!",e.getMessage().toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //Log.d("!!!!!Error!!!!","JSON_Response_Error"+error.getMessage().toString());
                    }
                });

        QueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }


    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast toast = Toast.makeText(GarbageActiviy.this,connectionResult.toString(), Toast.LENGTH_LONG);
        toast.show();

    }
}
