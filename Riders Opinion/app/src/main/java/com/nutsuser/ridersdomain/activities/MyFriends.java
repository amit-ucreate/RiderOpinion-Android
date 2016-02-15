package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.FreindsCurrentSwipeAdapter;
import com.nutsuser.ridersdomain.adapter.FreindsRequestSwipeAdapter;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.Student;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 1/6/2016.
 */
public class MyFriends extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bt_upcoming)
    Button bt_upcoming;
    @Bind(R.id.bt_past)
    Button bt_past;
    // FreindsRequestSwipeAdapter mUpcomingSwipeViewAdapter;
    FreindsRequestSwipeAdapter mAdapter;
    FreindsCurrentSwipeAdapter mUpcomingSwipeViewAdapter;
    private Activity activity;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private ArrayList<Student> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        bt_upcoming = (Button) findViewById(R.id.bt_upcoming);
        bt_past = (Button) findViewById(R.id.bt_past);

        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());


        mDataSet = new ArrayList<Student>();
        loadData();

        if (mDataSet.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }


        // Creating Adapter object
        mAdapter = new FreindsRequestSwipeAdapter(this, mDataSet);


        // Setting Mode to Single to reveal bottom View for one item in List
        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
        ((FreindsRequestSwipeAdapter) mAdapter).setMode(Attributes.Mode.Single);

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

    @OnClick({R.id.bt_past, R.id.bt_upcoming})
    void click(View view) {
        switch (view.getId()) {
            case R.id.bt_past:
                bt_upcoming.setBackgroundColor(Color.parseColor("#ffffff"));
                bt_past.setBackgroundColor(Color.parseColor("#C6B9B3"));
                bt_past.setTextColor(Color.parseColor("#ffffff"));
                bt_upcoming.setTextColor(Color.parseColor("#CD411E"));
                mAdapter = new FreindsRequestSwipeAdapter(this, mDataSet);


                // Setting Mode to Single to reveal bottom View for one item in List
                // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                ((FreindsRequestSwipeAdapter) mAdapter).setMode(Attributes.Mode.Single);

                mRecyclerView.setAdapter(mAdapter);
                break;
            case R.id.bt_upcoming:
                bt_upcoming.setBackgroundColor(Color.parseColor("#C6B9B3"));
                bt_past.setBackgroundColor(Color.parseColor("#ffffff"));
                bt_past.setTextColor(Color.parseColor("#CD411E"));
                bt_upcoming.setTextColor(Color.parseColor("#ffffff"));
                // Creating Adapter object
                mUpcomingSwipeViewAdapter = new FreindsCurrentSwipeAdapter(this, mDataSet);


                // Setting Mode to Single to reveal bottom View for one item in List
                // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                ((FreindsCurrentSwipeAdapter) mUpcomingSwipeViewAdapter).setMode(Attributes.Mode.Single);

                mRecyclerView.setAdapter(mUpcomingSwipeViewAdapter);
                break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish();
                Intent intent = new Intent(MyFriends.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreen", "OPEN");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
