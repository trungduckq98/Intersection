package com.deathmarch.intersection.view.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.view.SearchActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsAccessorAdapter mytabsAccessorAdapter;
    String currentUserId;
    private DatabaseReference stateCurrentUserReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        eventHandler();
    }

    private void init(){
        toolbar =findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        mytabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager(), 1);
        viewPager.setAdapter(mytabsAccessorAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_new_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.iconsmessage);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_notifications_active_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_menu_black_24dp);

        currentUserId = FirebaseAuth.getInstance().getUid();
        stateCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("UserState");
    }

    private void eventHandler(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem()!=0){
                    toolbar.setVisibility(View.GONE);
                }else {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setSupportActionBar(toolbar);

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

    private  void updateUserStatus(String state) {
        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("userState", state);
        onlineStateMap.put("userTimeState", ServerValue.TIMESTAMP);
        stateCurrentUserReference.setValue(onlineStateMap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserStatus("online");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }



}