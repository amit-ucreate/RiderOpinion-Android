package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.YouTubeVideoPlay;
import com.nutsuser.ridersdomain.web.pojos.ModifyBikeListData;

import java.util.ArrayList;

/**
 * Created by user on 9/30/2015.
 */
public class AdapterModifier extends BaseAdapter {

    private final Activity context;
    ViewHolder holder;
    ArrayList<ModifyBikeListData> modifyBikeListDatas=new ArrayList<>();
    public AdapterModifier(Activity context,ArrayList<ModifyBikeListData> modifyBikeListDatas) {

        this.context = context;
        Fresco.initialize(context);
        this.modifyBikeListDatas=modifyBikeListDatas;
        //prefsManager = new PrefsManager(context);

    }


    @Override
    public int getCount() {
        return modifyBikeListDatas.size();

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
            rowView = inflater.inflate(R.layout.item_modifier, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.rllayout=(RelativeLayout)rowView.findViewById(R.id.rllayout);
            viewHolder.tvDealerName=(TextView)rowView.findViewById(R.id.tvDealerName);
            viewHolder.tvLocation=(TextView)rowView.findViewById(R.id.tvLocation);
            viewHolder.tvOffers=(TextView)rowView.findViewById(R.id.tvOffers);
            viewHolder.tvdes=(TextView)rowView.findViewById(R.id.tvdes);
            viewHolder.sdv=(SimpleDraweeView)rowView.findViewById(R.id.sdv);
            viewHolder.ivVideo= (ImageView)rowView.findViewById(R.id.ivVideo);

            rowView.setTag(viewHolder);
        }

        holder = (ViewHolder) rowView.getTag();
        if (position % 2 == 0) {
            holder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            holder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        String jsonInString = modifyBikeListDatas.get(position).getImage().toString();
        jsonInString = jsonInString.replace("\\\"", "\"");
        jsonInString = jsonInString.replace("\"{", "{");
        jsonInString = jsonInString.replace("}\"", "}");
        Log.e("jsonInString: ", "" + jsonInString);
        Uri imageUri = Uri.parse(jsonInString);
        holder.sdv.setImageURI(imageUri);
        if (modifyBikeListDatas.get(position).getCompany() != null) {
            holder.tvDealerName.setText(modifyBikeListDatas.get(position).getCompany());
        } else {
            holder.tvDealerName.setText(modifyBikeListDatas.get(position).getCompany());
        }
        if (modifyBikeListDatas.get(position).getAddress() != null) {
            holder.tvLocation.setText(modifyBikeListDatas.get(position).getAddress());
        } else {
            holder.tvLocation.setText("No Address");
        }


        holder.tvOffers.setText(""+modifyBikeListDatas.get(position).getProductCount());
        holder.tvdes.setText(modifyBikeListDatas.get(position).getDescription());


            holder.ivVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(modifyBikeListDatas.get(position).getVideoCode().isEmpty()||modifyBikeListDatas.get(position).getVideoCode()==null){
                        Toast.makeText(context,"Video not available",Toast.LENGTH_SHORT).show();
                        holder.ivVideo.setClickable(false);
                        holder.ivVideo.setClickable(false);
                        holder.ivVideo.setFocusable(false);
                        holder.ivVideo.setEnabled(false);
                    }else {
                        Intent youTubeIntent = new Intent(context, YouTubeVideoPlay.class);
                        youTubeIntent.putExtra("VIDEOURL", modifyBikeListDatas.get(position).getVideoCode());
                        context.startActivity(youTubeIntent);
                    }
                }
            });



        return rowView;
    }

    static class ViewHolder {
        RelativeLayout rllayout;
        TextView tvDealerName,tvLocation,tvOffers,tvdes;
        SimpleDraweeView sdv;
        ImageView ivVideo;

    }


}

