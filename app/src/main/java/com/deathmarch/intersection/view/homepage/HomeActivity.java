package com.deathmarch.intersection.view.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.view.CreatePostActivity;
import com.deathmarch.intersection.view.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private static String CURRENTUSERNAME;

    public static String getCURRENTUSERNAME() {
        return CURRENTUSERNAME;
    }

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsAccessorAdapter mytabsAccessorAdapter;
    String currentUserId;
    private DatabaseReference stateCurrentUserReference;
    private DatabaseReference tokenReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        getCurrentUserName();
        eventHandler();
        setUpToken();
        viewPager.setOffscreenPageLimit(3);


    }
    //set up token id for FCM
    private void setUpToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("trungduc", "getInstanceId failed", task.getException());
                            return;
                        }
                        final String token = task.getResult().getToken();
                        tokenReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue()!=null){
                                    String myToken = dataSnapshot.getValue().toString();
                                    if (!token.equals(myToken)){
                                        tokenReference.setValue(token);
                                    }
                                }else {
                                    tokenReference.setValue(token);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
    }

    private void getCurrentUserName(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .child("UserMain").child("userDisplayName");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CURRENTUSERNAME = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        tokenReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("userToken");
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
                if (item.getItemId()== R.id.search_home){
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                }
                if (item.getItemId()== R.id.menu_create_post){
                    Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                    startActivity(intent);
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

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            updateUserStatus("online");
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
