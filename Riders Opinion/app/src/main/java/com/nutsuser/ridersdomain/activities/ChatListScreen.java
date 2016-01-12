package com.nutsuser.ridersdomain.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.PagerAdapter;


/**
 * Created by user on 11/4/2015.
 */
public class ChatListScreen extends AppCompatActivity {

   // ChatFragment mChatFragment;
    ///ContactsFragment mContactsFragment;
    //RecentsFragment mRecentsFragment;
   // SettingFragment mSettingFragment;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        mTextView=(TextView)findViewById(R.id.tvTitleToolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.notification_icon_recents_active));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.notification_icon_contacts));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.notification_icon_chats));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.notification_icon_settings));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        getSupportActionBar().setTitle("RECENT");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.e("pos: ", "" + tab.getPosition());
                if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.notification_icon_contacts_active);
                    mTextView.setText("CONTACTS");
                   // getSupportActionBar().setTitle("CONTACTS");
                }
                else if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.notification_icon_recents_active);
                    mTextView.setText("RECENT");
                    //  getSupportActionBar().setTitle("RECENT");
                }
                else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.notification_icon_chats_active);
                    mTextView.setText("CHATS");
                  //  getSupportActionBar().setTitle("CHATS");
                }
                else if(tab.getPosition()==3){
                    tab.setIcon(R.drawable.notification_icon_settings_active);
                    mTextView.setText("SETTINGS");
                    //getSupportActionBar().setTitle("SETTINGS");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.notification_icon_contacts);
                }
                else if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.notification_icon_recents);
                }
                else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.notification_icon_chats);
                }
                else if(tab.getPosition()==3){
                    tab.setIcon(R.drawable.notification_icon_settings);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
               /* if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.tabbaricontasks);
                }
                else if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.tabbaricontasks);
                }
                else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.tabbaricontasks);
                }*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              //  finish();
                Intent intent = new Intent(ChatListScreen.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreen","OPEN");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
