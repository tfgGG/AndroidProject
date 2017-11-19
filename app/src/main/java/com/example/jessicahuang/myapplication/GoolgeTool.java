package com.example.jessicahuang.myapplication;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jessicahuang on 2017/11/17.
 */

public class GoolgeTool extends FragmentActivity
{
    private  GoogleMap mgooglemap;
    public Location location = new Location("test");


    public void setLocation(Location location){
        this.location = location;
    }
}

