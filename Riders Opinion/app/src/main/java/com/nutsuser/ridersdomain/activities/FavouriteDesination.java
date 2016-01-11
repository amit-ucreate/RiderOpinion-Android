package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.daimajia.swipe.util.Attributes;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.FavouriteDestinationAdapter;
import com.nutsuser.ridersdomain.adapter.FreindsCurrentSwipeAdapter;
import com.nutsuser.ridersdomain.adapter.FreindsRequestSwipeAdapter;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.Student;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 1/6/2016.
 */
public class FavouriteDesination extends BaseActivity {
    private Activity activity;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
     RecyclerView mRecyclerView;
    // FreindsRequestSwipeAdapter mUpcomingSwipeViewAdapter;
    //FreindsRequestSwipeAdapter mAdapter;
    private ArrayList<Student> mDataSet;
    FavouriteDestinationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favouritedestination);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());


        mDataSet = new ArrayList<Student>();
        loadData();

        // Creating Adapter object
        mAdapter = new FavouriteDestinationAdapter(this, mDataSet);


        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((FavouriteDestinationAdapter) mAdapter).setMode(Attributes.Mode.Single);

        mRecyclerView.setAdapter(mAdapter);

        /* Scroll Listeners */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    // load initial data
    public void loadData() {

        for (int i = 0; i <= 20; i++) {
            mDataSet.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));
        }
    }
}
