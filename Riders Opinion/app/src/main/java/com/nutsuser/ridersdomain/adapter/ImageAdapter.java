package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.SubcribesGridImages;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<SubcribesGridImages> arrayList;
    private static LayoutInflater inflater = null;

    // Constructor
    public ImageAdapter(Context c, ArrayList<SubcribesGridImages> arrayList) {
        mContext = c;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_grid_row, null);
        holder.ivBackgroundColor = (SimpleDraweeView) rowView.findViewById(R.id.ivBackgroundColor);

        if (arrayList.get(position).getDestImage() != null) {
            String milestonesJsonInString = arrayList.get(position).getDestImage().toString();
            milestonesJsonInString = milestonesJsonInString.replace("\\\"", "\"");
            milestonesJsonInString = milestonesJsonInString.replace("\"{", "{");
            milestonesJsonInString = milestonesJsonInString.replace("}\"", "}");
            Log.e("URI:",""+Uri.parse(milestonesJsonInString));
            holder.ivBackgroundColor.setImageURI(Uri.parse(milestonesJsonInString));
        }

        return rowView;

      /*  SimpleDraweeView simpleDraweeView = new SimpleDraweeView(mContext);

        if (arrayList.get(position).getDestImage() != null) {
            String milestonesJsonInString = arrayList.get(position).getDestImage().toString();
            milestonesJsonInString = milestonesJsonInString.replace("\\\"", "\"");
            milestonesJsonInString = milestonesJsonInString.replace("\"{", "{");
            milestonesJsonInString = milestonesJsonInString.replace("}\"", "}");
            simpleDraweeView.setImageURI(Uri.parse(milestonesJsonInString));
        }
        simpleDraweeView.setScaleType(SimpleDraweeView.ScaleType.FIT_XY);
        simpleDraweeView.setLayoutParams(new GridView.LayoutParams(200, 200));
        return simpleDraweeView;*/
    }
    /**
     * Declares a view holder class to
     * contain all resources.
     */
    public class Holder {
        SimpleDraweeView ivBackgroundColor;

    }

}
