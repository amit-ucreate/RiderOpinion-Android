package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.ModiflyBikeData;

import java.util.ArrayList;

/**
 * Created by user on 9/25/2015.
 */
public class AdapterModify extends BaseAdapter {

    private final Activity context;
    ViewHolder holder;
    ArrayList<ModiflyBikeData> modiflyBikeDatas=new ArrayList<>();
    public AdapterModify(Activity context,ArrayList<ModiflyBikeData> modiflyBikeDatas) {

        this.context = context;
        Fresco.initialize(context);
        this.modiflyBikeDatas=modiflyBikeDatas;
        //prefsManager = new PrefsManager(context);

    }


    @Override
    public int getCount() {
        return modiflyBikeDatas.size();

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_modify, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ivgrid=(SimpleDraweeView)rowView.findViewById(R.id.ivgrid);
            viewHolder.tvTitle=(TextView)rowView.findViewById(R.id.tvTitle);
            viewHolder.tvPrice=(TextView)rowView.findViewById(R.id.tvPrice);
            rowView.setTag(viewHolder);
        }

        holder = (ViewHolder) rowView.getTag();
        String jsonInString = modiflyBikeDatas.get(position).getImage().toString();
        jsonInString = jsonInString.replace("\\\"", "\"");
        jsonInString = jsonInString.replace("\"{", "{");
        jsonInString = jsonInString.replace("}\"", "}");
        Log.e("jsonInString: ", "" + jsonInString);
        Uri imageUri = Uri.parse(jsonInString);
        holder.ivgrid.setImageURI(imageUri);
        holder.tvTitle.setText(modiflyBikeDatas.get(position).getCategory());
        holder.tvPrice.setText(modiflyBikeDatas.get(position).getVender());


        return rowView;
    }

    static class ViewHolder {
        SimpleDraweeView ivgrid;
        TextView tvTitle,tvPrice;

    }


}
