package com.deathmarch.intersection.repository;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageRepository {
    private static ImageRepository instance;
    private MutableLiveData<ArrayList<String>> liveDataImage;
    private ArrayList<String> arrUrlImage = new ArrayList<>();
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("Post");
    Query query;
    public static ImageRepository getInstance(){
        if (instance==null){
            instance = new ImageRepository();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<String>> getLiveDataImage(String userId){
        liveDataImage = new MutableLiveData<>();
        query = postReference.child(userId).orderByChild("hasImage").equalTo("true");
        getArrUrlImage(query);
        return liveDataImage;
    }
    private void getArrUrlImage(Query query){
        query.addValueEventListener(valueEventListener);
    }

    public void deleteListener(){
        if (query!=null){
            query.removeEventListener(valueEventListener);
            query = null;
        }

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrUrlImage.clear();
            for (DataSnapshot d :dataSnapshot.getChildren()){
                String url = d.child("postImage").getValue().toString();
                arrUrlImage.add(url);
            }
            liveDataImage.setValue(arrUrlImage);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
