package com.deathmarch.intersection.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StateFriendViewModel extends ViewModel {
    private MutableLiveData<DataSnapshot> liveDataStateFriend;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Friends");
    private Query query;
    public MutableLiveData<DataSnapshot> getLiveDataStateFriend(String currentUserId, String anotherUserId){
        Log.d("ninhgiang2020", "vao view model lay data");
        if (liveDataStateFriend==null){
            Log.d("ninhgiang2020", "view model chua co data");
            liveDataStateFriend = new MutableLiveData<>();
            query = databaseReference.child(currentUserId).child(anotherUserId);
            loadStateFriend(query);
        }
        Log.d("ninhgiang2020", "view model co data");
        return liveDataStateFriend;
    }
    private void loadStateFriend(Query query){
        Log.d("ninhgiang2020", "thuc hien query");
        query.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (query!=null){
            Log.d("ninhgiang2020", "view model bi huy");
            query.removeEventListener(valueEventListener);
            query = null;
        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("ninhgiang2020", " vao firebase lay data");
            liveDataStateFriend.setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
