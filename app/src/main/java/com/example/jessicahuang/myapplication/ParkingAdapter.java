package com.example.jessicahuang.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jessicahuang on 2017/12/31.
 */

public class ParkingAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private JSONArray mJsonArray;

    private static class ViewHolder {
        ImageView ImageView;
        TextView AddressText;
        TextView PayText;
    }
    void updateData(JSONArray jsonArray) {
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
    ParkingAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mJsonArray = new JSONArray();
    }



    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // 檢查view是否已存在，如果已存在就不用再取一次id
        if (convertView == null) {
            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.parking_list, parent, false);
            holder = new ViewHolder();
            holder.ImageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.AddressText = (TextView) convertView.findViewById(R.id.AddressText);
            holder.PayText = (TextView) convertView.findViewById(R.id.PayText);
            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = (JSONObject) getItem(position);
        try{
            String name =  jsonObject.getString("Name");
            if(name.compareTo("汽車停車位")==0)
                holder.ImageView.setImageResource(R.mipmap.ic_launcher);
            else
                holder.ImageView.setImageResource(R.mipmap.ic_launcher_round);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            holder.PayText.setText(jsonObject.getString("PayCash"));
            holder.AddressText.setText(jsonObject.getString("Name")+jsonObject.getString("Distance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  convertView;
    }
}
