package com.deathmarch.intersection.view.homepage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.view.ChatActivity;
import com.deathmarch.intersection.view.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    //  listenerMess(currentUserId);

    }

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

//        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
//            updateUserStatus("online");
//        }


    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
//            updateUserStatus("offline");
//        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void listenerMess(final String currentUserId){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("LastMess").child(currentUserId);
        Query query = databaseReference.orderByChild("time").limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){;
                    for (DataSnapshot d :dataSnapshot.getChildren()) {
                        Messenger messenger = d.getValue(Messenger.class);
                        if (!messenger.getFrom().equals(currentUserId)){
                            if (!messenger.getSeen().equals("true")){
                                String contentShow;
                                if (messenger.getType().equals("text")){
                                    contentShow= messenger.getContent();
                                }else {
                                    contentShow= "Đã gửi một ảnh";
                                }

                                String anotherUserId = messenger.getFrom();


                                createNotification(anotherUserId, contentShow);
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNotification(String anotherUserId,  String content){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("another_user_id",anotherUserId );
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_message_black_24dp)
                        //  .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle("Bạn có tin nhắn mới")
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }






}
