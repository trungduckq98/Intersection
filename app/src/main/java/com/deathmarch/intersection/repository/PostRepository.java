package com.deathmarch.intersection.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class PostRepository {
    private static PostRepository instance;
    private DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference().child("Post");
    private MutableLiveData<ArrayList<Post>> liveDataPost;
    private ArrayList<Post> arrPost = new ArrayList<>();

    public static PostRepository getInstance(){
        instance  = new PostRepository();
        return instance;
    }

    public MutableLiveData<ArrayList<Post>> getLiveDataPost(String userId){
        liveDataPost = new MutableLiveData<>();
        getArrPost(userId);
        return liveDataPost;
    }

    private void getArrPost(String userId){
        Query query = postsReference.child(userId).orderByChild("postTime").limitToLast(20);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);
                arrPost.add(post);
                liveDataPost.setValue(arrPost);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                for (int i=0;i<arrPost.size();i++){
                    if (arrPost.get(i).getPostId().equals(post.getPostId())){
                        arrPost.remove(arrPost.get(i));
                        liveDataPost.setValue(arrPost);
                    }
                }
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
