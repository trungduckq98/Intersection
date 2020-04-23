package com.deathmarch.intersection.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.model.UserState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendOnlineRepository {
    private static FriendOnlineRepository instance;
    private DatabaseReference friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends");
    private DatabaseReference usesReference = FirebaseDatabase.getInstance().getReference().child("Users");
    public static FriendOnlineRepository getInstance() {
        instance= new FriendOnlineRepository();
        return instance;
    }

    private MutableLiveData<ArrayList<User>> liveDataFriendOnline;
    private ArrayList<User> arrFriendOnline = new ArrayList<>();

    public MutableLiveData<ArrayList<User>> getLiveDataFriendOnline(String currentUserId) {
        liveDataFriendOnline = new MutableLiveData<>();
        Query query = friendsReference.child(currentUserId).orderByChild("type").equalTo("friend");
        getArrKeyFriend(query);
        return liveDataFriendOnline;
    }

    private void getArrKeyFriend(Query query){
        final ArrayList<String> arrKey = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    String friendId = d.getKey();
                    arrKey.add(friendId);
                }
                loadListFriendOnline(arrKey);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListFriendOnline(ArrayList<String> arrKey){
        arrFriendOnline.clear();
        for (String key: arrKey){
            usesReference.child(key).addListenerForSingleValueEvent(valueEventListener);
        }

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            UserMain userMain = dataSnapshot.child("UserMain").getValue(UserMain.class);
            UserInfo userInfo = dataSnapshot.child("UserInfo").getValue(UserInfo.class);
            UserState userState = dataSnapshot.child("UserState").getValue(UserState.class);
            User user = new User(userMain, userInfo, userState);
            arrFriendOnline.add(user);
            liveDataFriendOnline.setValue(arrFriendOnline);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


}
