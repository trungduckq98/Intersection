package com.deathmarch.intersection.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.deathmarch.intersection.model.MyNotification;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MyNotificationRepo {
    private static MyNotificationRepo instane;
    DatabaseReference notifyReference = FirebaseDatabase.getInstance().getReference()
            .child("Notify");


    private MutableLiveData<ArrayList<MyNotification>> liveData;
    ArrayList<MyNotification> arrayList = new ArrayList<>();

    public static MyNotificationRepo getInstane() {
        instane = new MyNotificationRepo();
        return instane;
    }

    public MutableLiveData<ArrayList<MyNotification>> getLiveData(String currentUserId) {
        liveData = new MutableLiveData<>();
        Query query = notifyReference.child(currentUserId);
        getListNotification(query);
        return liveData;
    }

    private void getListNotification(Query query){
        arrayList.clear();
        query.addChildEventListener(childEventListener);
    }
    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String postId = dataSnapshot.child("postId").getValue().toString();
            String postType = dataSnapshot.child("postType").getValue().toString();
              String postText ="";
                String postImage ="";
            if (postType.equals("text")){
                 postText = dataSnapshot.child("postText").getValue().toString();
            }
            if (postType.equals("image")){

                postImage = dataSnapshot.child("postImage").getValue().toString();
            }
            if (postType.equals("double")){
                 postText = dataSnapshot.child("postText").getValue().toString();
                 postImage = dataSnapshot.child("postImage").getValue().toString();
            }

            if (dataSnapshot.hasChild("cmtNotify/userId")){
                String notifyType = "cmt";
                String seen = dataSnapshot.child("cmtNotify/seen").getValue().toString();
                String userId = dataSnapshot.child("cmtNotify/userId").getValue().toString();
                long time =  dataSnapshot.child("cmtNotify/time").getValue(Long.class) ;
                MyNotification notification = new MyNotification(
                        postId, postImage, postText, postType, notifyType, seen, time, userId);
                arrayList.add(notification);


            }

            if (dataSnapshot.hasChild("likeNotify/userId")){
                String notifyType = "like";
                String seen = dataSnapshot.child("likeNotify/seen").getValue().toString();
                String userId = dataSnapshot.child("likeNotify/userId").getValue().toString();
                long time =  dataSnapshot.child("likeNotify/time").getValue(Long.class) ;
                MyNotification notification = new MyNotification(
                        postId, postImage, postText, postType, notifyType, seen, time, userId);
                arrayList.add(notification);
            }
            Log.d("qqqqqqqq", "child add size: "+arrayList.size());
            liveData.setValue(arrayList);

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String postId = dataSnapshot.child("postId").getValue().toString();
            for (int i = arrayList.size()-1; i>=0;i--){
                if (arrayList.get(i).getPostId().equals(postId)){
                    Log.d("qqqqqqqq", "i remove: "+i);
                    arrayList.remove(i);
                }
            }


            String postType = dataSnapshot.child("postType").getValue().toString();
            String postText ="";
            String postImage ="";
            if (postType.equals("text")){
                postText = dataSnapshot.child("postText").getValue().toString();
            }
            if (postType.equals("image")){

                postImage = dataSnapshot.child("postImage").getValue().toString();
            }
            if (postType.equals("double")){
                postText = dataSnapshot.child("postText").getValue().toString();
                postImage = dataSnapshot.child("postImage").getValue().toString();
            }

            if (dataSnapshot.hasChild("cmtNotify/userId")){
                String notifyType = "cmt";
                String seen = dataSnapshot.child("cmtNotify/seen").getValue().toString();
                String userId = dataSnapshot.child("cmtNotify/userId").getValue().toString();
                long time =  dataSnapshot.child("cmtNotify/time").getValue(Long.class) ;
                MyNotification notification = new MyNotification(
                        postId, postImage, postText, postType, notifyType, seen, time, userId);
                arrayList.add(notification);
                Log.d("qqqqqqqq", "re add size: "+arrayList.size());

            }

            if (dataSnapshot.hasChild("likeNotify/userId")){
                String notifyType = "like";
                String seen = dataSnapshot.child("likeNotify/seen").getValue().toString();
                String userId = dataSnapshot.child("likeNotify/userId").getValue().toString();
                long time =  dataSnapshot.child("likeNotify/time").getValue(Long.class) ;
                MyNotification notification = new MyNotification(
                        postId, postImage, postText, postType, notifyType, seen, time, userId);
                arrayList.add(notification);
                Log.d("qqqqqqqq", "re add size: "+arrayList.size());
            }
            Log.d("qqqqqqqq", " size: "+arrayList.size());
            liveData.setValue(arrayList);

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String postId = dataSnapshot.child("postId").getValue().toString();
            for (int i = 0; i<arrayList.size();i++){
                if (arrayList.get(i).getPostId().equals(postId)){
                    arrayList.remove(i);
                    liveData.setValue(arrayList);
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
