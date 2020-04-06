package com.deathmarch.intersection.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.UserMain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRepository {
    private DatabaseReference friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends");
    private DatabaseReference usersReference;
    private Query queryFriend;
    private Query queryReceive;
    private Query querySend;
    private static FriendRepository instance;

    private MutableLiveData<ArrayList<UserMain>> livedataUserFriend = new MutableLiveData<>();
    private ArrayList<UserMain> arrUserFriend = new ArrayList<>();
    private ArrayList<String> arrKeyFriend = new ArrayList<>();

    private MutableLiveData<ArrayList<UserMain>> livedataUserReceive = new MutableLiveData<>();
    private ArrayList<UserMain> arrUserReceive = new ArrayList<>();
    private ArrayList<String> arrKeyReceive = new ArrayList<>();

    private MutableLiveData<ArrayList<UserMain>> livedataUserSend = new MutableLiveData<>();
    private ArrayList<UserMain> arrUserSend = new ArrayList<>();
    private ArrayList<String> arrKeySend = new ArrayList<>();


    public static FriendRepository getInstance() {
         if (instance == null) {
        instance = new FriendRepository();
         }
        return instance;
    }

    public MutableLiveData<ArrayList<UserMain>> getLivedataUserFriend(String currentUserId){
        queryFriend = friendsReference.child(currentUserId).orderByChild("type").equalTo("friend");
        livedataUserFriend = new MutableLiveData<>();
        loadListKeyFriend();
        return livedataUserFriend;
    }

    private void loadListKeyFriend(){
        queryFriend.addValueEventListener(valueEventListenerFriend);
    }

    public void deleteListener(){
        if (queryFriend!=null){
            queryFriend.removeEventListener(valueEventListenerFriend);
            queryFriend = null;
        }

        if (querySend!=null){
            querySend.removeEventListener(valueEventListenerSend);
            querySend = null;
        }

        if (queryReceive!=null){
            queryReceive.removeEventListener(valueEventListenerReceive);
            queryReceive=null;
        }
    }

    ValueEventListener valueEventListenerFriend = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrKeyFriend.clear();
            arrUserFriend.clear();
            for (DataSnapshot d: dataSnapshot.getChildren()){
                String friendId = d.getKey();
                arrKeyFriend.add(friendId);
            }
            loadListFriend(arrKeyFriend);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void loadListFriend(ArrayList<String> arrKey){
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        for (String key: arrKey){
            usersReference.child(key).child("UserMain").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrUserFriend.add(dataSnapshot.getValue(UserMain.class));
                    Log.d("kienquoc", "FRIEND:  "+dataSnapshot.toString());
                    livedataUserFriend.setValue(arrUserFriend);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public MutableLiveData<ArrayList<UserMain>> getLivedataUserReceive(String currentUserId)
    {
        queryReceive = friendsReference.child(currentUserId).orderByChild("type").equalTo("received");
        livedataUserReceive = new MutableLiveData<>();
        loadListKeyReceive();
        return livedataUserReceive;
    }

    private void loadListKeyReceive(){
        queryReceive.addValueEventListener(valueEventListenerReceive);
    }

    ValueEventListener valueEventListenerReceive = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrKeyReceive.clear();
            arrUserReceive.clear();
            for (DataSnapshot d: dataSnapshot.getChildren()){
                String friendId = d.getKey();
                arrKeyReceive.add(friendId);
            }
            loadListReceive(arrKeyReceive);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void loadListReceive(ArrayList<String> arrKey){
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        for (String key: arrKey){
            usersReference.child(key).child("UserMain").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrUserReceive.add(dataSnapshot.getValue(UserMain.class));
                    livedataUserReceive.setValue(arrUserReceive);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }




    public MutableLiveData<ArrayList<UserMain>> getLivedataUserSend(String currentUserId){
        querySend = friendsReference.child(currentUserId).orderByChild("type").equalTo("send");
        livedataUserSend = new MutableLiveData<>();
        loadListKeySend();
        return livedataUserSend;
    }

    private void loadListKeySend(){
        querySend.addValueEventListener(valueEventListenerSend);
    }

    private void loadListSend(ArrayList<String> arrKey){
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        for (String key: arrKey){
            usersReference.child(key).child("UserMain").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrUserSend.add(dataSnapshot.getValue(UserMain.class));
                    livedataUserSend.setValue(arrUserSend);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    ValueEventListener valueEventListenerSend = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrKeySend.clear();
            arrUserSend.clear();
            for (DataSnapshot d: dataSnapshot.getChildren()){
                String friendId = d.getKey();
                arrKeySend.add(friendId);
            }
            loadListSend(arrKeySend);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };






}