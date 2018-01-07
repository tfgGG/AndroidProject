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

import java.util.Calendar;

/**
 * Created by jessicahuang on 2017/12/31.
 */

public class GarbageDetailAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private JSONArray mJsonArray;

    private static class ViewHolder {
        TextView RemainText;
        TextView AddressText;

        /*public void setData(Product item) {
            mProduct = item;
            tvProduct.setText(item.name);
            updateTimeRemaining(System.currentTimeMillis());
        }*/

    }
    void updateData(JSONArray jsonArray) {
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
    GarbageDetailAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mJsonArray = new JSONArray();
    }
   /* private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };*/

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
            convertView = mInflater.inflate(R.layout.garbage_detail_list, parent, false);
            holder = new ViewHolder();
            holder.AddressText = (TextView) convertView.findViewById(R.id.Addresstxt);
            holder.RemainText = (TextView) convertView.findViewById(R.id.RemainTime);
            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = (JSONObject) getItem(position);

        try {
            holder.AddressText.setText(jsonObject.getString("Village")+jsonObject.getString("Name"));
            holder.RemainText.setText(jsonObject.getString("Time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  convertView;
    }
}
