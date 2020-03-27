package com.deathmarch.intersection.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.model.UserMain;
import com.firebase.ui.database.FirebaseArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MessengerRepository {

    private static MessengerRepository instance;
    private int itemPos = 0;
    private static final int TOTAL_ITEMS_TO_LOAD = 20;
    private int mCurrentPage = 1;
    private String mLastKey = "";
    private String mPrevKey = "";

    private String currentUserId = FirebaseAuth.getInstance().getUid();
    private DatabaseReference messengerReference =FirebaseDatabase.getInstance().getReference().child("Messenger");
    Query query;

    private MutableLiveData<ArrayList<Messenger>> livedataMessenger;
    private ArrayList<Messenger> arrMessenger = new ArrayList<>();

    public static MessengerRepository getInstance() {
        if (instance==null){
            instance = new MessengerRepository();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Messenger>> getLivedataMessenger(String anotherUserId){
        livedataMessenger = new MutableLiveData<>();
        query = messengerReference.child(currentUserId).child(anotherUserId);
        getArrMessenger();
        return livedataMessenger;
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
            livedataMessenger.setValue(arrMessenger);
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
