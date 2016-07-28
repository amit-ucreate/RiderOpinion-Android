package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.PublicProfileScreen;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.web.pojos.RecentFriend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucreateuser on 6/20/2016.
 */
public class AdapterRecentFriends extends BaseAdapter implements CONSTANTS{
    Context context;
    ViewHolder holder;

    List<RecentFriend> mPublicProfileData;


    public AdapterRecentFriends(PublicProfileScreen publicProfileScreen, List<RecentFriend> publicProfileData) {
        context = publicProfileScreen;
        Fresco.initialize(context);
        mPublicProfileData = publicProfileData;

    }

    @Override
    public int getCount() {
        return mPublicProfileData.size();
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
            rowView = inflater.inflate(R.layout.item_recent_friends, null);
            holder = new ViewHolder();
            holder.ivFriends = (SimpleDraweeView) rowView.findViewById(R.id.ivFriends);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();

        }
        Log.e("getRecentFriendImage", "" + mPublicProfileData.get(position).getRecentFriendImage());
        Uri imageUri = Uri.parse(mPublicProfileData.get(position).getRecentFriendImage());
        holder.ivFriends.setImageURI(imageUri);
        holder.ivFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, PublicProfileScreen.class);
//                intent.putExtra(PROFILE, true);
//                intent.putExtra(PROFILE_IMAGE, mPublicProfileData.get(position).getRecentFriendImage());
//                intent.putExtra(USER_ID, mPublicProfileData.get(position).getFriendId());
//                context.startActivity(intent);
            }
        });
        return rowView;
    }

    public class ViewHolder {

        SimpleDraweeView ivFriends;

    }
}
