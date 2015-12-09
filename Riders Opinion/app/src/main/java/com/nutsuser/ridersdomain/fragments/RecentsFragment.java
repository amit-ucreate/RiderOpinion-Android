package com.nutsuser.ridersdomain.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.RecentAdapter;

/**
 * Created by user on 11/6/2015.
 */
public class RecentsFragment extends Fragment {

    View view;
    ListView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent, container, false);
        list=(ListView)view.findViewById(R.id.list);
        list.setAdapter(new RecentAdapter(getActivity()));
        return view;
    }
}
