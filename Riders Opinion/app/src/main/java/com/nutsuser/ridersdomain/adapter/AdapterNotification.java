package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.PostRide;

import java.util.ArrayList;

/**
 * Created by User on 1/12/2016.
 */
public class AdapterNotification extends BaseAdapter {
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;
    Context mContext;
    ViewHolder holder;
    private ArrayList<Object> personArray;
    private LayoutInflater inflater;

    public AdapterNotification(Context context, ArrayList<Object> personArray) {
        this.personArray = personArray;
        mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return personArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return personArray.get(position);
    }

    @Override
    public int getViewTypeCount() {
        // TYPE_PERSON and TYPE_DIVIDER
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof PostRide) {
            return TYPE_PERSON;
        }

        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        View rowView = convertView;

        holder = new ViewHolder();
        if (rowView == null) {
            //ViewHolder viewHolder = new ViewHolder();
            switch (type) {
                case TYPE_PERSON:
                    rowView = inflater.inflate(R.layout.adapter_layout_notificationrow, parent, false);

                    holder.frame = (FrameLayout) rowView.findViewById(R.id.frame);
                    holder.name = (TextView) rowView.findViewById(R.id.nameLabel);
                    holder.address = (TextView) rowView.findViewById(R.id.nameDate);
                    holder.mImageView = (ImageView) rowView.findViewById(R.id.imageSet);

                    break;
                case TYPE_DIVIDER:
                    rowView = inflater.inflate(R.layout.postride_header, parent, false);

                    holder.title = (TextView) rowView.findViewById(R.id.headerTitle);

                    break;
            }
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }


        switch (type) {
            case TYPE_PERSON:
                final PostRide person = (PostRide) getItem(position);
                holder.name.setText(person.getType());
                //holder.name.setText(personArray.get(position));
                break;
            case TYPE_DIVIDER:

                String titleString = (String) getItem(position);
                holder.title.setText(titleString);
                break;
        }


        return rowView;
    }

    static class ViewHolder {
        TextView name, address, title;
        FrameLayout frame;
        ImageView mImageView;

    }
}