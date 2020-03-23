package com.deathmarch.intersection.repository;

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

import java.util.ArrayList;

public class CommentRepository {
    private static CommentRepository instance;
    private DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference().child("Post");
    private MutableLiveData<ArrayList<Comment>> liveDataCmt;
    private ArrayList<Comment> arrCmt = new ArrayList<>();

    public static CommentRepository getInstance(){
        instance= new CommentRepository();
        return getInstance();
    }

    public MutableLiveData<ArrayList<Comment>> getLiveDataCmt(String postUserId, String postId){
        liveDataCmt = new MutableLiveData<>();
        getArrComment(postUserId, postId);
        return liveDataCmt;
    }

    private void getArrComment(String postUserId, String postId){
        postsReference.child(postUserId).child(postId).child("postComment")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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
                });

    }
}
