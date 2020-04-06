package com.deathmarch.intersection.view.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.CountFriend;
import com.deathmarch.intersection.view.SearchActivity;
import com.google.android.material.tabs.TabLayout;

public class FriendManagerActivity extends AppCompatActivity implements CountFriend {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FriendTabsAccessorAdapter friendTabsAccessorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);
        init();
        setUpViewPager();
        setUpToolbar();
    }
    private void init(){
        toolbar =findViewById(R.id.toolbar_friend_manage);
        tabLayout = findViewById(R.id.tablayout_friend_manager);
        viewPager = findViewById(R.id.viewpager_friend_manager);
        friendTabsAccessorAdapter = new FriendTabsAccessorAdapter(getSupportFragmentManager(), 1);
        viewPager.setAdapter(friendTabsAccessorAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Bạn bè");
        tabLayout.getTabAt(1).setText("Lời mời kết bạn");
        tabLayout.getTabAt(2).setText("Đã gửi yêu cầu");

    }
    private void setUpToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()== R.id.tb_search){
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                }
                return false;
            }
        });
    }
    private void setUpViewPager(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        toolbar.setTitle("Bạn bè");
                        break;
                    case 1:
                        toolbar.setTitle("Lời mời kết bạn");
                        break;
                    case 2:
                        toolbar.setTitle("Đã gửi yêu cầu");
                        break;

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void countFriend(int count) {
        if (count!=0){
            tabLayout.getTabAt(0).setText("Bạn bè"+"("+count+")");
        }else {
            tabLayout.getTabAt(0).setText("Bạn bè");
        }
    }




}
