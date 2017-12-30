package com.example.jessicahuang.myapplication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jessicahuang on 2017/12/22.
 */

public class HTTPClient  {



    private String msgAns;
    private StringRequest StringRequest;
    private String url;
    private JSONObject AnsJson;
    private JsonObjectRequest jsObjRequest;

    public HTTPClient(String url){
        this.url = url;
    }
    public void SetJsonRequest(){

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        AnsJson = response;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        try {
                            AnsJson.put("Error",error.getMessage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public StringRequest SetStringRequest(){

        StringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                msgAns = s;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                msgAns = volleyError.getMessage();
            }
        });

        return StringRequest;
    }

    public String getAnsString() {
        return msgAns;
    }

    public StringRequest getStringRequest() {
        return StringRequest;
    }

    public JSONObject getAnsJson() {
        return this.AnsJson;
    }
    public JsonObjectRequest getJsObjRequest(){
        return  this.jsObjRequest;
    }


}
