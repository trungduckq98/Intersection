package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.GetTimeAgo;
import com.deathmarch.intersection.model.MyNotification;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.view.PostDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyNotifyAdapter extends RecyclerView.Adapter<MyNotifyAdapter.MyNotifyViewHolder> {
    private ArrayList<MyNotification> arrayList = new ArrayList<>();
    private Context context;
    private DatabaseReference usersReference;
    private DatabaseReference postReference;

    public MyNotifyAdapter(Context context) {
        this.context = context;
        this.usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        this.postReference = FirebaseDatabase.getInstance().getReference().child("Post");
    }

    public void updateList(ArrayList<MyNotification> newList) {

        MyNotifyDiffUtillCallback callback = new MyNotifyDiffUtillCallback(arrayList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public MyNotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new MyNotifyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyNotifyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final MyNotification notification = arrayList.get(position);
        if (notification.getSeen().equals("true")) {
            holder.relativeLayout.setBackgroundColor(Color.WHITE);
        }
        if (notification.getSeen().equals("false")) {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorXXX));
        }

        GetTimeAgo getTimeAgo = new GetTimeAgo();
        final String postTime = getTimeAgo.getTimeAgo(notification.getTime());
        holder.txt_time.setText(postTime);





        usersReference.child(notification.getUserId()).child("UserMain").child("userDisplayName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.getValue().toString();
                        String notifyType=notification.getNotifyType();
                        String notifyAction ="";
                        if (notifyType.equals("like")){
                            notifyAction = " đã thích ";
                        }
                        if (notifyType.equals("cmt")){
                            notifyAction = " đã bình luận về ";
                        }

                        String contentPost ="";
                        String postType = notification.getPostType();
                        if (postType.equals("text")){
                            contentPost = "bài viết "+ notification.getPostText()+" của bạn";
                        }
                        if (postType.equals("image")){
                            holder.imageView.setVisibility(View.VISIBLE);
                            contentPost = "hình ảnh của bạn";
                            Glide.with(context)
                                    .load(notification.getPostImage())
                                    .placeholder(R.drawable.image_user_defalse)
                                    .error(R.drawable.image_user_defalse)
                                    .into(holder.imageView);
                        }
                        if (postType.equals("double")){
                            holder.imageView.setVisibility(View.VISIBLE);
                            contentPost = "bài viết "+ notification.getPostText()+" của bạn";
                            Glide.with(context)
                                    .load(notification.getPostImage())
                                    .placeholder(R.drawable.image_user_defalse)
                                    .error(R.drawable.image_user_defalse)
                                    .into(holder.imageView);

                        }

                        holder.textView.setText(username+notifyAction+contentPost);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showPost(notification.getPostId());

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyNotifyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView, txt_time;
        RelativeLayout relativeLayout;
        public MyNotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout =itemView.findViewById(R.id.relative30);
            imageView = itemView.findViewById(R.id.img_30);
            textView = itemView.findViewById(R.id.txt_30);
            txt_time = itemView.findViewById(R.id.txt_time30);
        }
    }

    private void showPost(String postId){

        postReference.child(FirebaseAuth.getInstance().getUid()).child(postId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);
                        FragmentActivity fragmentActivity = (FragmentActivity) context;
                        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                        PostDialogFragment postDialog  = PostDialogFragment.newInstane();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("post", post);
                        postDialog.setArguments(bundle);
                        Log.d("kkkkkkkkkkk", "Bundle");
                        postDialog.show(fragmentManager, "duc");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );


    }
}
