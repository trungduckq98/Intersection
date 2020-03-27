package com.deathmarch.intersection.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.model.UserState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    private static UserRepository instance;
    private MutableLiveData<User> liveDataUser;
    private Query query;
    String test;

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");

    public static UserRepository getInstance(){
        if (instance==null){
            instance = new UserRepository();
        }
        return instance;
    }

    public MutableLiveData<User> getLiveDataUser(String userId){
        test = userId;
        Log.d("haiduong2020", "vao repository lay data");
        liveDataUser = new MutableLiveData<>();
        query = userRef.child(userId);
        getDetailUser();
        return liveDataUser;
    }
    private void getDetailUser(){
        query.addValueEventListener(valueEventListener);
    }
    public void removeListener(){
        if (query!=null){
            Log.d("haiduong2020", "Remove listener");
            query.removeEventListener(valueEventListener);
            query = null;
        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("haiduong2020", "firebase: " +test);
            UserMain userMain = dataSnapshot.child("UserMain").getValue(UserMain.class);
            UserInfo userInfo = dataSnapshot.child("UserInfo").getValue(UserInfo.class);
            UserState userState = dataSnapshot.child("UserState").getValue(UserState.class);
            User user = new User(userMain, userInfo, userState);
            liveDataUser.setValue(user);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
