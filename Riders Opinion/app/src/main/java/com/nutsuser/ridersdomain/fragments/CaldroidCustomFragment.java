package com.nutsuser.ridersdomain.fragments;

import com.nutsuser.ridersdomain.adapter.AdapterDate;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by user on 9/24/2015.
 */
public class CaldroidCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new AdapterDate(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

}
