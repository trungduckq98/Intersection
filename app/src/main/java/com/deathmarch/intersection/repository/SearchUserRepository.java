package com.deathmarch.intersection.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchUserRepository {
    private static SearchUserRepository instance;
    public static SearchUserRepository getInstance() {
        if (instance==null){
            instance = new SearchUserRepository();
        }
        return instance;
    }

    private String currentUserId = FirebaseAuth.getInstance().getUid();
    DatabaseReference  usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
    Query queryDisplayName, queryFullName;
    ArrayList<User> arrayList = new ArrayList<>();
    ArrayList<User> newsList = new ArrayList<>();


    private MutableLiveData<ArrayList<User>> liveDataListUser;

    public MutableLiveData<ArrayList<User>> getLiveDataListUser(String text) {
        Log.d("AnCuc2020", "vao repository lay data");
        queryDisplayName = usersReference.orderByChild("UserMain/userDisplayName").startAt(text).endAt(text+"\uf8ff");
        queryFullName = usersReference.orderByChild("UserInfo/userFullName").startAt(text).endAt(text+"\uf8ff");
        liveDataListUser = new MutableLiveData<>();
        getListUser();
        return liveDataListUser;
    }

    private void getListUser(){
        Log.d("AnCuc2020", "query lay data");
        arrayList.clear();
        newsList.clear();
        queryDisplayName.addListenerForSingleValueEvent(valueEventListener);
        queryFullName.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("AnCuc2020", "lay data tu firebase");
            for (DataSnapshot d :dataSnapshot.getChildren()) {
                UserMain userMain = d.child("UserMain").getValue(UserMain.class);
                UserInfo userInfo = d.child("UserInfo").getValue(UserInfo.class);
                User user = new User(userMain, userInfo);
                if (!userMain.getUserId().equals(currentUserId)){
                    arrayList.add(user);
                }
            }
            newsList.addAll(new HashSet<>(arrayList));
            liveDataListUser.setValue(newsList);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
