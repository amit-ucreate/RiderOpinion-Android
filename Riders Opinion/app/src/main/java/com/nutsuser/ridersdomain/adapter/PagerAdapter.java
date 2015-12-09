package com.nutsuser.ridersdomain.adapter;

/**
 * Created by user on 11/10/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nutsuser.ridersdomain.fragments.ChatFragment;
import com.nutsuser.ridersdomain.fragments.ContactsFragment;
import com.nutsuser.ridersdomain.fragments.RecentsFragment;
import com.nutsuser.ridersdomain.fragments.SettingFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RecentsFragment tab1 = new RecentsFragment();
                return tab1;
            case 1:
                ContactsFragment tab2 = new ContactsFragment();
                return tab2;
            case 2:
                ChatFragment tab3 = new ChatFragment();
                return tab3;
            case 3:
                SettingFragment tab4 = new SettingFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}