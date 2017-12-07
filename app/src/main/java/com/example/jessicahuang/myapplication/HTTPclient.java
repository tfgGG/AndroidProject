package com.example.jessicahuang.myapplication;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by jessicahuang on 2017/12/4.
 */

public class HTTPclient {

    private RequestQueue mQueue;
    private StringRequest getRequest;
    private String url;
    private String msgAns;

    public void HTTPclient(String url)
    {
        getRequest = new StringRequest(url, new Response.Listener<String>() {
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
    }

    public void clickButton()
    {
        mQueue.add(getRequest);
    }

    public String getMeg()
    {
        return msgAns;
    }

}
