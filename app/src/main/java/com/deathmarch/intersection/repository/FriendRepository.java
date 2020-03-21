package com.deathmarch.intersection.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.UserMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRepository {
    private DatabaseReference friendsReference;
    private DatabaseReference usersReference;
    private String currentUserId = FirebaseAuth.getInstance().getUid();



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
        // if (instance == null) {
        instance = new FriendRepository();
        // }
        return instance;
    }

    public MutableLiveData<ArrayList<UserMain>> getLivedataUserFriend(){
        loadListKeyFriend();
        return livedataUserFriend;
    }

    private void loadListKeyFriend(){
        friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserId);
        Query query = friendsReference.orderByChild("type").equalTo("friend");
        query.addValueEventListener(new ValueEventListener() {
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
        });
    }

    private void loadListFriend(ArrayList<String> arrKey){
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        for (String key: arrKey){
            usersReference.child(key).child("UserMain").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrUserFriend.add(dataSnapshot.getValue(UserMain.class));
                    livedataUserFriend.setValue(arrUserFriend);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    public MutableLiveData<ArrayList<UserMain>> getLivedataUserReceive()
    {
        loadListKeyReceive();
        return livedataUserReceive;
    }

    private void loadListKeyReceive(){
        friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserId);
        Query query = friendsReference.orderByChild("type").equalTo("received");
        query.addValueEventListener(new ValueEventListener() {
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
        });
    }

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

    public MutableLiveData<ArrayList<UserMain>> getLivedataUserSend(){
        loadListKeySend();
        return livedataUserSend;
    }

    private void loadListKeySend(){
        friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserId);
        Query query = friendsReference.orderByChild("type").equalTo("send");
        query.addValueEventListener(new ValueEventListener() {
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
        });
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






}