package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.GetTimeAgo;
import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.model.UserMain;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context context;
    private ArrayList<Post> arrayList;

    String currentUserId = FirebaseAuth.getInstance().getUid();
    DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("Post");


    public PostAdapter(Context context, ArrayList<Post> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void updateList(ArrayList<Post> newList) {
        PostDiffUtilCallback callback = new PostDiffUtilCallback(arrayList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final boolean isLike;
        final Post post = arrayList.get(position);
        usersReference.child(post.getPostUserId()).child("UserMain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMain userMain = dataSnapshot.getValue(UserMain.class);
                holder.txt_Displayname.setText(userMain.getUserDisplayName());
                Glide.with(context)
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(holder.img_Thump);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        GetTimeAgo getTimeAgo = new GetTimeAgo();
        final String postTime = getTimeAgo.getTimeAgo(post.getPostTime());
        holder.txt_Time.setText(postTime);
        final String type = post.getPostType();


        if (type.equals("double")) {
            holder.txt_Type.setText("Đã thêm bài đăng mới");
            holder.txt_PostText.setText("   " + post.getPostText());
            Glide.with(context)
                    .load(post.getPostImage())
                    .placeholder(R.drawable.image_user_defalse)
                    .error(R.drawable.image_user_defalse)
                    .into(holder.img_PostImage);

        } else if (type.equals("text")) {
            holder.txt_Type.setText("Đã thêm bài đăng mới");
            holder.img_PostImage.setVisibility(View.GONE);
            holder.txt_PostText.setText("   " + post.getPostText());
        } else if (type.equals("image")) {
            holder.txt_Type.setText("Đã thêm một ảnh mới");
            holder.txt_PostText.setVisibility(View.GONE);
            Glide.with(context)
                    .load(post.getPostImage())
                    .placeholder(R.drawable.image_user_defalse)
                    .error(R.drawable.image_user_defalse)
                    .into(holder.img_PostImage);
        }

        postReference.child(post.getPostUserId()).child(post.getPostName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("postLike")){
                   long countLike = dataSnapshot.child("postLike").getChildrenCount();
                    holder.txt_CountLike.setText(""+countLike);
                }else {
                    holder.txt_CountLike.setText(""+0);
                }



                if (dataSnapshot.hasChild("postLike/"+currentUserId)){
                    holder.img_Like.setImageResource(R.drawable.ic_heart_pink);
                    holder.isLike = true;
                }else {
                    holder.img_Like.setImageResource(R.drawable.ic_heart_white);
                    holder.isLike=false;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.img_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isLike==true){
                    postReference.child(post.getPostUserId()).child(post.getPostName())
                            .child("postLike").child(currentUserId).removeValue();
                }else {
                    Map likeMap = new HashMap();
                    likeMap.put("likeTime", ServerValue.TIMESTAMP);
                    postReference.child(post.getPostUserId()).child(post.getPostName())
                            .child("postLike").child(currentUserId).updateChildren(likeMap);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        boolean isLike;
        CircleImageView img_Thump;
        TextView txt_Displayname;
        TextView txt_Type;
        TextView txt_Time;
        TextView txt_PostText;
        ImageView img_PostImage;
        TextView txt_CountLike;
        ImageView img_Like;
        TextView txt_Comment;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Thump = itemView.findViewById(R.id.img_thump13);
            txt_Displayname = itemView.findViewById(R.id.txt_diplayname13);
            txt_Type = itemView.findViewById(R.id.txt_type13);
            txt_Time = itemView.findViewById(R.id.txt_time13);
            txt_PostText = itemView.findViewById(R.id.post_text13);
            img_PostImage = itemView.findViewById(R.id.post_image13);
            txt_CountLike = itemView.findViewById(R.id.txt_count_like13);
            img_Like = itemView.findViewById(R.id.img_like13);
            txt_Comment = itemView.findViewById(R.id.txt_comment13);

        }
    }
}
