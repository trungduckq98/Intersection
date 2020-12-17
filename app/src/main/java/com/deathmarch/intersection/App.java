package com.deathmarch.intersection;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class App extends Application implements  LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
              ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            String currentUserId = FirebaseAuth.getInstance().getUid();
            updateUserStatus(currentUserId, "offline");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            String currentUserId = FirebaseAuth.getInstance().getUid();
            updateUserStatus(currentUserId, "online");
        }
    }

    private  void updateUserStatus(String currentUserId, String state) {
        DatabaseReference stateCurrentUserReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(currentUserId)
                .child("UserState");
        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("userState", state);
        onlineStateMap.put("userTimeState", ServerValue.TIMESTAMP);
        stateCurrentUserReference.setValue(onlineStateMap);
    }





}
