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
import com.nutsuser.ridersdomain.database.DatabaseHelper;
import com.nutsuser.ridersdomain.database.DatasourceHandler;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.pojos.FilterMultiple;
import com.nutsuser.ridersdomain.web.pojos.ProductMultipleSelectAssistance;

import java.util.ArrayList;

/**
 * Created by user on 1/7/2016.
 */
public class FilterMultipleCheck extends BaseAdapter {

    Context mContext;
    // database work
    DatabaseHelper mDatabaseHelper;
    DatasourceHandler mDatasourceHandler;
    ViewHolder holder;
    ArrayList<FilterMultiple> objects;
    String distance;
    PrefsManager prefsManager;
    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if(isChecked==true){
                Log.e("Int:",""+(Integer) buttonView.getTag());

                if((Integer) buttonView.getTag()==0){
                    boolean check=mDatasourceHandler.updateTypeFilter("Beach", "1",distance,prefsManager.getCaseId());
                    Log.e("Beach:", "" + check);
                }
                else if((Integer) buttonView.getTag()==1){
                    boolean check=mDatasourceHandler.updateTypeFilter("Hills", "1",distance,prefsManager.getCaseId());
                    Log.e("Hills:", "" + check);
                }
                else if((Integer) buttonView.getTag()==2){
                    boolean check=mDatasourceHandler.updateTypeFilter("Wildlife", "1",distance,prefsManager.getCaseId());
                    Log.e("Wildlife:", "" + check);
                }
                else if((Integer) buttonView.getTag()==3){
                    boolean check=mDatasourceHandler.updateTypeFilter("Adventure", "1",distance,prefsManager.getCaseId());
                    Log.e("Adventure:", "" + check);
                }
            }
            else{
                if((Integer) buttonView.getTag()==0){
                    boolean check=mDatasourceHandler.updateTypeFilter("Beach", "0",distance,prefsManager.getCaseId());
                    Log.e("else Beach:", "" + check);
                }
                else if((Integer) buttonView.getTag()==1){
                    boolean check=mDatasourceHandler.updateTypeFilter("Hills", "0",distance,prefsManager.getCaseId());
                    Log.e("else Hills:", "" + check);
                }
                else if((Integer) buttonView.getTag()==2){
                    boolean check=mDatasourceHandler.updateTypeFilter("Wildlife", "0",distance,prefsManager.getCaseId());
                    Log.e("else Wildlife:", "" + check);
                }
                else if((Integer) buttonView.getTag()==3){
                    boolean check=mDatasourceHandler.updateTypeFilter("Adventure", "0",distance,prefsManager.getCaseId());
                    Log.e("else Adventure:", "" + check);
                }
            }

            getProduct((Integer) buttonView.getTag()).box = isChecked;
        }
    };
    private LayoutInflater inflater;

    public FilterMultipleCheck(Context context, ArrayList<FilterMultiple> objects,String dis) {

        mContext = context;
        this.objects = objects;
        mDatabaseHelper = new DatabaseHelper(mContext);
        mDatasourceHandler = new DatasourceHandler(mContext);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        distance= dis;
        prefsManager= new PrefsManager(context);
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

    public FilterMultiple getProduct(int position) {

        return ((FilterMultiple) getItem(position));
    }

    public ArrayList<FilterMultiple> getBox() {
        ArrayList<FilterMultiple> box = new ArrayList<>();
        for (FilterMultiple p : objects) {
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
        FilterMultiple p = getProduct(position);
        holder.name.setText(p._name);
        holder.cbBox.setOnCheckedChangeListener(myCheckChangList);
        holder.cbBox.setTag(position);
        Log.e("NMAE:", "" + p._name);
        Log.e("String:", "" + p.toString());
        if(objects.get(position).box){
            holder.cbBox.setChecked(true);
        }
        return rowView;
    }

    public static class ViewHolder {
        TextView name, address, title;
        FrameLayout frame;
        public static CheckBox cbBox;

    }
}