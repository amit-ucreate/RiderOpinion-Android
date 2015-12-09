package com.nutsuser.ridersdomain.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutsuser.ridersdomain.R;

/**
 * Created by user on 11/6/2015.
 */
public class ChatFragment extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_contacts, container, false);
        return view;
    }
}
