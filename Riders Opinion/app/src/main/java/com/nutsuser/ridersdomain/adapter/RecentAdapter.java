package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

/**
 * Created by user on 12/9/2015.
 */
public class RecentAdapter extends BaseAdapter {
    private static LayoutInflater inflater=null;
    Context context;
    public RecentAdapter(Activity mainActivity) {
        // TODO Auto-generated constructor stub

        context=mainActivity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.item_recent_adapter, null);



        return rowView;
    }
    public class Holder
    {
        TextView tv;
        ImageView img;
    }
}
