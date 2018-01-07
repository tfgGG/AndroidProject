package com.example.jessicahuang.myapplication;

import android.content.Context;
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

public class GarbageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private JSONArray mJsonArray;

    private static class ViewHolder {
        ImageView ImageView;
        TextView NameText;
        TextView VillageText;
        TextView NowText;

    }
    void updateData(JSONArray jsonArray) {
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
    GarbageAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.garbage_list, parent, false);
            holder = new ViewHolder();
            holder.ImageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.NameText = (TextView) convertView.findViewById(R.id.Name);
            holder.VillageText = (TextView) convertView.findViewById(R.id.Addresstxt);
            holder.NowText = (TextView) convertView.findViewById(R.id.NowText);
            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ImageView.setImageResource(R.mipmap.ic_launcher_round);
        JSONObject jsonObject = (JSONObject) getItem(position);

        try {
            holder.NameText.setText(jsonObject.getString("City")+jsonObject.getString("LineName"));
            holder.VillageText.setText(jsonObject.getString("Village"));
            holder.NowText.setText("Default");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  convertView;
    }
}
