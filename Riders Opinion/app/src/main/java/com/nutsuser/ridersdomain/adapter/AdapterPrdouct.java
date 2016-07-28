package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.web.pojos.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 10/1/2015.
 */
public class AdapterPrdouct extends RecyclerView.Adapter<AdapterPrdouct.ViewHolder> {

    private Activity activity;
    ArrayList<Product> stringArrayList;
    public AdapterPrdouct(Activity activity,ArrayList<Product> stringArrayList) {
        this.activity = activity;
        this.stringArrayList=stringArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String jsonInString = stringArrayList.get(position).getProductImage().toString();
        jsonInString = jsonInString.replace("\\\"", "\"");
        jsonInString = jsonInString.replace("\"{", "{");
        jsonInString = jsonInString.replace("}\"", "}");
        Log.e("jsonInString: ", "" + jsonInString);
        Uri imageUri = Uri.parse(jsonInString);
        holder.sdvProductimage.setImageURI(imageUri);
        holder.tvProductNamel.setText(stringArrayList.get(position).getProduct());
        if(stringArrayList.get(position).getPrice().isEmpty()||stringArrayList.get(position).getPrice().equals("0")){
            holder.tvProductPrice.setVisibility(View.GONE);
        }else {
            holder.tvProductPrice.setText("₹ "+stringArrayList.get(position).getPrice());
        }
        if(stringArrayList.get(position).getDiscount().isEmpty()||stringArrayList.get(position).getDiscount().equals("0")){
            holder.tvProductOffer.setVisibility(View.GONE);
        }else {
            holder.tvProductOffer.setText(stringArrayList.get(position).getDiscount() + "%");

        }

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.sdvProductimage)
        SimpleDraweeView sdvProductimage;
        @Bind(R.id.tvProductName)
        TextView tvProductNamel;
        @Bind(R.id.tvProductPrice)
        TextView tvProductPrice;
        @Bind(R.id.tvProductOffer)
        CircleButtonText tvProductOffer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
