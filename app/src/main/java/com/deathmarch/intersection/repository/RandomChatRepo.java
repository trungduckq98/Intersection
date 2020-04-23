package com.deathmarch.intersection.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.Messenger;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class RandomChatRepo {
    private static  RandomChatRepo instance;


    public static RandomChatRepo getInstance() {
        instance= new RandomChatRepo();
        return instance;
    }

    Query query;
    private DatabaseReference messengerReference = FirebaseDatabase.getInstance().getReference().child("Messenger");
    private MutableLiveData<ArrayList<Messenger>> livedataRandomChat;
    private ArrayList<Messenger> arrMessenger = new ArrayList<>();

    public MutableLiveData<ArrayList<Messenger>> getLivedataRandomChat(String currentUserId){
        livedataRandomChat = new MutableLiveData<>();
        query = messengerReference.child(currentUserId).child("RandomChat");
        getArrMessenger();
        return livedataRandomChat;
    }

    private void getArrMessenger(){
        arrMessenger.clear();
        query.addChildEventListener(childEventListener);

    }

    public void deleteListener(){
        if (query!=null){
            query.removeEventListener(childEventListener);
            query = null;
        }
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Messenger messenger = dataSnapshot.getValue(Messenger.class);
            arrMessenger.add(messenger);
            livedataRandomChat.setValue(arrMessenger);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
