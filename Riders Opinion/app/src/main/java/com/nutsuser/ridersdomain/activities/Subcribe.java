package com.nutsuser.ridersdomain.activities;

import android.os.Bundle;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.fragments.FragmentStatePagerAdapterFragment;

import java.util.ArrayList;

/**
 * Created by admin on 11-02-2016.
 */
public class Subcribe extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new FragmentStatePagerAdapterFragment()).commit();
        }
    }


}
