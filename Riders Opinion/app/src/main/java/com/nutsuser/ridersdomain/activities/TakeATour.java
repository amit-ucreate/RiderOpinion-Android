package com.nutsuser.ridersdomain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

/**
 * Created by admin on 19-07-2016.
 */
public class TakeATour extends FragmentActivity {

    String string_open;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    TextView tvDONE, tvMenu;
    static final int NUM_ITEMS_RIDER_DESTINATION = 3;
    static final int NUM_ITEMS_MEET_AN_PLAN = 2;
    static final int NUM_ITEMS_Riding_Events = 3;
    static final int NUM_ITEMS_Healthly = 2;
    static final int NUM_ITEMS_GetDirection = 1;
    static final int NUM_ITEMS_Modify = 4;
    static final int NUM_ITEMS_My_Profile = 10;
    static final int NUM_ITEMS_LIVE = 1;
    Handler handler, handler1;
    Runnable finalR, run1;

    static int NUM_ITEMS = 0;
    public static String[] IMAGE_NAME = {};
    String lastPosition = "-1";
    public static int count = 0;

    public static final String[] IMAGE_NAME_RIDER_DESTINATION = {"ic_ridingdestination_one", "ic_ridingdestination_two", "ic_ridingdestination_three"};
    public static final String[] IMAGE_NAME_MEET_AN_PLAN = {"ic_meetplan_one", "ic_meetplan_two", "ic_meetplan_three"};
    public static final String[] IMAGE_NAME_Riding_Events = {"ic_ridingevent_one", "ic_ridingevent_two", "ic_ridingevent_three"};
    public static final String[] IMAGE_NAME_Healthly = {"ic_healthy_one", "ic_healthy_two"};
    public static final String[] IMAGE_NAME_GetDirection = {"ic_getdirection_one"};
    public static final String[] IMAGE_NAME_live = {"ic_live_one"};
    public static final String[] IMAGE_NAME_Modify = {"ic_modify_one", "ic_modify_two", "ic_modify_three", "ic_modify_four"};
    public static final String[] IMAGE_NAME_My_Profile = {"ic_myprofile_one", "ic_myprofile_two", "ic_myprofile_three", "ic_myprofile_four", "ic_myprofile_five", "ic_myprofile_six", "ic_myprofile_seven", "ic_myprofile_eight", "ic_myprofile_nine", "ic_myprofile_ten"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_a_tour_fragment_pager);
        count=-1;
        tvDONE = (TextView) findViewById(R.id.tvDONE);
        tvMenu = (TextView) findViewById(R.id.tvMenu);
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeATour.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();

            }
        });
        tvDONE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        string_open = getIntent().getStringExtra("TAKEATOUR");
        if (string_open.matches("DESTINATION")) {
            NUM_ITEMS = NUM_ITEMS_RIDER_DESTINATION;
            IMAGE_NAME = IMAGE_NAME_RIDER_DESTINATION;
        } else if (string_open.matches("PLANARIDE")) {
            NUM_ITEMS = NUM_ITEMS_MEET_AN_PLAN;
            IMAGE_NAME = IMAGE_NAME_MEET_AN_PLAN;
        } else if (string_open.matches("DIRECTION")) {
            NUM_ITEMS = NUM_ITEMS_GetDirection;
            IMAGE_NAME = IMAGE_NAME_GetDirection;
        } else if (string_open.matches("RIDINGEVENT")) {
            NUM_ITEMS = NUM_ITEMS_Riding_Events;
            IMAGE_NAME = IMAGE_NAME_Riding_Events;
        } else if (string_open.matches("MODIFY")) {
            NUM_ITEMS = NUM_ITEMS_Modify;
            IMAGE_NAME = IMAGE_NAME_Modify;
        } else if (string_open.matches("HELATHY")) {
            NUM_ITEMS = NUM_ITEMS_Healthly;
            IMAGE_NAME = IMAGE_NAME_Healthly;
        } else if (string_open.matches("LIVE")) {
            NUM_ITEMS = NUM_ITEMS_LIVE;
            IMAGE_NAME = IMAGE_NAME_live;
        } else if (string_open.matches("PROFILE")) {
            NUM_ITEMS = NUM_ITEMS_My_Profile;
            IMAGE_NAME = IMAGE_NAME_My_Profile;
        }
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled"+lastPosition, "----" + position);

                handler = new Handler();
                finalR = new Runnable() {
                    public void run() {

                        if (!lastPosition.equals("" + position)&&Integer.parseInt(lastPosition)<position) {
                            Log.e("if","if");
                            viewPager.setCurrentItem(position + 1);
                            lastPosition = "" + position;
                            if(count==1){
                                handler1 = new Handler();
                                run1 = new Runnable() {
                                    public void run() {

                                        finish();

                                    }
                                };

                                handler1.postDelayed(run1, 500);
                            }
                        } else {
                            handler1 = new Handler();
                            run1 = new Runnable() {
                                public void run() {
                                    if((count-2)==position)
                                        finish();

                                }
                            };

                            handler1.postDelayed(run1, 500);
                        }
                    }
                };

                handler.postDelayed(finalR, 5000);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            Log.e("NUM_ITEMS:", "" + NUM_ITEMS);
            TakeATour.count=NUM_ITEMS;
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }

        @Override
        public int getItemPosition(Object object) {

            return POSITION_NONE;
        }
    }

    public static class SwipeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.take_a_tour_swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            String imageFileName = IMAGE_NAME[position];
            int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.nutsuser.ridersdomain");
            imageView.setImageResource(imgResId);
            return swipeView;
        }

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);

            return swipeFragment;
        }
    }
}