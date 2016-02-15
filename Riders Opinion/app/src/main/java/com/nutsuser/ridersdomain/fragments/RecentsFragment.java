package com.nutsuser.ridersdomain.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.RecentAdapter;

/**
 * Created by user on 11/6/2015.
 */
public class RecentsFragment extends Fragment {

    View view;
    ListView list;
    Button btrides, btmessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent, container, false);
        btrides = (Button) view.findViewById(R.id.btrides);
        btmessage = (Button) view.findViewById(R.id.btmessage);
        list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(new RecentAdapter(getActivity()));

        btrides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btmessage.setBackgroundColor(Color.parseColor("#ffffff"));
                btrides.setBackgroundColor(Color.parseColor("#C6B9B3"));
                btrides.setTextColor(Color.parseColor("#ffffff"));
                btmessage.setTextColor(Color.parseColor("#CD411E"));
            }
        });
        btmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btmessage.setBackgroundColor(Color.parseColor("#C6B9B3"));
                btrides.setBackgroundColor(Color.parseColor("#ffffff"));
                btrides.setTextColor(Color.parseColor("#CD411E"));
                btmessage.setTextColor(Color.parseColor("#ffffff"));

            }
        });

        return view;
    }
}
