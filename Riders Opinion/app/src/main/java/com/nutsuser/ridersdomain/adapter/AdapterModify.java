package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nutsuser.ridersdomain.R;

/**
 * Created by user on 9/25/2015.
 */
public class AdapterModify extends BaseAdapter {

    private final Activity context;
    ViewHolder holder;

    public AdapterModify(Activity context) {

        this.context = context;
        Fresco.initialize(context);
        //prefsManager = new PrefsManager(context);

    }


    @Override
    public int getCount() {
        return 7;

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_modify, null);
            ViewHolder viewHolder = new ViewHolder();

            rowView.setTag(viewHolder);


        }

        holder = (ViewHolder) rowView.getTag();

        return rowView;
    }


}
