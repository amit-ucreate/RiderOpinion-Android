package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.ProductMultipleSelect;
import com.nutsuser.ridersdomain.web.pojos.ProductMultipleSelectAssistance;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;

import java.util.ArrayList;

/**
 * Created by user on 1/7/2016.
 */
public class PlanRidePostAdapterAssistance extends BaseAdapter {

    Context mContext;
    ViewHolder holder;
    private LayoutInflater inflater;
    ArrayList<ProductMultipleSelectAssistance> objects;

    public PlanRidePostAdapterAssistance(Context context, ArrayList<ProductMultipleSelectAssistance> objects) {

        mContext = context;
        this.objects=objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }
    public ProductMultipleSelectAssistance getProduct(int position) {
        return ((ProductMultipleSelectAssistance) getItem(position));
    }

    public ArrayList<ProductMultipleSelectAssistance> getBox() {
        ArrayList<ProductMultipleSelectAssistance> box = new ArrayList<>();
        for (ProductMultipleSelectAssistance p : objects) {
            if (p.box)
                box.add(p);
        }
        return box;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        holder = new ViewHolder();
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.row_item_post_ride, parent, false);
            holder.frame = (FrameLayout) rowView.findViewById(R.id.frame);
            holder.name = (TextView) rowView.findViewById(R.id.nameLabel);
            holder.address = (TextView) rowView.findViewById(R.id.nameDate);
            holder.cbBox = (CheckBox) rowView.findViewById(R.id.cbBox);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        ProductMultipleSelectAssistance p = getProduct(position);
        holder.name.setText(p._name);
        holder.cbBox.setOnCheckedChangeListener(myCheckChangList);
        holder.cbBox.setTag(position);
        Log.e("NMAE:", "" + p._name);
        Log.e("String:",""+p.toString());

        return rowView;
    }
    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        getProduct((Integer) buttonView.getTag()).box = isChecked;
    }
};
    static class ViewHolder {
        TextView name, address, title;
        FrameLayout frame;
        CheckBox cbBox;

    }
}