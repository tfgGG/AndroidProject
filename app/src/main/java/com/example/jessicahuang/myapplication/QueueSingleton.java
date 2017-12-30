package com.example.jessicahuang.myapplication;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jessicahuang on 2017/12/22.
 */

public class QueueSingleton extends Application {



    private static QueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private QueueSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }
    public QueueSingleton(){}

    public static synchronized QueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new QueueSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
