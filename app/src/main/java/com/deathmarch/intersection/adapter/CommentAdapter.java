package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.Comment;
import com.deathmarch.intersection.model.GetTimeAgo;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.model.UserMain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>  {
    Context context;
    ArrayList<Comment> arrayList = new ArrayList<>();
    DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

    public CommentAdapter(Context context) {
        this.context = context;
    }

    public void updateList(ArrayList<Comment> newList) {
        CommentDiffUtilCallback callback = new CommentDiffUtilCallback(arrayList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        Comment comment = arrayList.get(position);
        usersReference.child(comment.getCmtUserId()).child("UserMain")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMain userMain = dataSnapshot.getValue(UserMain.class);
                Glide.with(context)
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(holder.img_Thump);
                holder.txt_Displayname.setText(userMain.getUserDisplayName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.txt_ConetenCmt.setText(comment.getCmtContent());
        GetTimeAgo getTimeAgo = new GetTimeAgo();
        final String cmtTime = getTimeAgo.getTimeAgo(comment.getCmtTime());
        holder.txt_TimeCmt.setText(cmtTime);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img_Thump;
        TextView txt_Displayname, txt_TimeCmt, txt_ConetenCmt;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Thump = itemView.findViewById(R.id.img_thump15);
            txt_Displayname= itemView.findViewById(R.id.txt_displayname15);
            txt_TimeCmt = itemView.findViewById(R.id.txt_time_comment15);
            txt_ConetenCmt = itemView.findViewById(R.id.txt_content_cmt15);
        }
    }
}
