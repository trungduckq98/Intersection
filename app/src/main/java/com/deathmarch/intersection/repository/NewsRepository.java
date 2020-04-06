package com.deathmarch.intersection.repository;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewsRepository {
    private static NewsRepository instance;

    public static NewsRepository getInstance() {
        instance = new NewsRepository();
        return instance;
    }

    private DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference().child("Post");
    private DatabaseReference friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends");
    private MutableLiveData<ArrayList<Post>> liveDataPost;
    private Query query;
    private ArrayList<String> arrKeyFriend = new ArrayList<>();
    private ArrayList<Post> arrPost = new ArrayList<>();

    public MutableLiveData<ArrayList<Post>> getLiveDataPost(String currentUserId) {
        liveDataPost = new MutableLiveData<>();
        query = friendsReference.child(currentUserId).orderByChild("type").equalTo("friend");
        loadListKeyFriend();
        return liveDataPost;
    }

    ValueEventListener valueEventListenerFriend = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrKeyFriend.clear();
            arrPost.clear();
            for (DataSnapshot d: dataSnapshot.getChildren()){
                String friendId = d.getKey();
                arrKeyFriend.add(friendId);
            }
            loadListPost(arrKeyFriend);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void loadListKeyFriend() {
        query.addValueEventListener(valueEventListenerFriend);
    }

    private void loadListPost(ArrayList<String> arrKeyFriend) {
        for (String key: arrKeyFriend){
            postsReference.child(key).limitToLast(4).addChildEventListener(childEventListener);
        }
    }






    ChildEventListener childEventListener = new ChildEventListener() {
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
    };
}
