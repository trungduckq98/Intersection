package com.deathmarch.intersection.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.Comment;
import com.deathmarch.intersection.model.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CommentRepository {
    private static CommentRepository instance;
    private DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference().child("Post");
    private Query query;
    private MutableLiveData<ArrayList<Comment>> liveDataCmt;
    private ArrayList<Comment> arrCmt = new ArrayList<>();

    public static CommentRepository getInstance(){
        if (instance==null){
            instance= new CommentRepository();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataCmt(String postUserId, String postId){
        Log.d("vvvvvvvvvv", "vao repository lay data");
        liveDataCmt = new MutableLiveData<>();
        query = postsReference.child(postUserId).child(postId).child("postComment");
        getArrComment();
        return liveDataCmt;
    }

    private void getArrComment(){
        Log.d("vvvvvvvvvv", "query lay data");
        arrCmt.clear();
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
            Log.d("vvvvvvvvvv", "query lay data");
            Comment comment = dataSnapshot.getValue(Comment.class);
            arrCmt.add(comment);
            liveDataCmt.setValue(arrCmt);
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
