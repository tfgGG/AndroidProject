package com.example.jessicahuang.myapplication;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jessicahuang on 2017/11/17.
 */

public class GoolgeTool extends FragmentActivity implements Parcelable
{
    public   Location location = new Location("test");
    private double lat;
    private double lon;

    public void setLocation(Location location){
        this.location = location;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    public static final Parcelable.Creator<GoolgeTool> CREATOR = new Parcelable.Creator<GoolgeTool>(){

        @Override
        public GoolgeTool createFromParcel(Parcel parcel) {

            GoolgeTool g =new GoolgeTool();
            g.setLat(parcel.readDouble());
            g.setLon(parcel.readDouble());
            return g;
        }

        @Override
        public GoolgeTool[] newArray(int size) {
            return new GoolgeTool[0];
        }
    };

    public void setLat(double lat){
        this.lat = lat;
    }
    public void setLon(double lon){
        this.lon = lon;
    }
    public double getLat(){
        return this.lat;
    }
    public double getLon(){
        return  this.lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

