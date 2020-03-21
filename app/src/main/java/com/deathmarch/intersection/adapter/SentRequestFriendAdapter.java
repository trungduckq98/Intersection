package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.view.AnotherUserPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SentRequestFriendAdapter extends RecyclerView.Adapter<SentRequestFriendAdapter.SentRequestFriendViewHolder> {
    private DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
    private String currentUserId;
    private DatabaseReference  friendsReference;
    Context context;
    ArrayList<UserMain> arrayList;

    public SentRequestFriendAdapter(Context context, ArrayList<UserMain> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void updateList(ArrayList<UserMain> newList) {
        FriendDiffUtilCallback callback = new FriendDiffUtilCallback(arrayList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public SentRequestFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sent_request, parent, false);
        return new SentRequestFriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  final SentRequestFriendViewHolder holder, final int position) {
       final UserMain userMain = arrayList.get(position);

        holder.txt_Displayname.setText(userMain.getUserDisplayName());
        Glide.with(context)
                .load(userMain.getUserImage())
                .placeholder(R.drawable.image_user_defalse)
                .error(R.drawable.image_user_defalse)
                .into(holder.img_Thump);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(context)) goAnotherUserPageActivity(userMain.getUserId());
            }
        });
        holder.txt_DeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(context)) HuyYeuCauKetBan(userMain.getUserId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SentRequestFriendViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img_Thump;
        TextView txt_Displayname;
        TextView txt_DeleteRequest;
        public SentRequestFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Thump = itemView.findViewById(R.id.img_thump7);
            txt_Displayname = itemView.findViewById(R.id.txt_diplayname7);
            txt_DeleteRequest = itemView.findViewById(R.id.txt_delete_requet7);
        }
    }
    private void goAnotherUserPageActivity(String anotherUserId)
    {
        Intent intent = new Intent(context, AnotherUserPageActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void HuyYeuCauKetBan(final String anotherUserId){
        currentUserId = FirebaseAuth.getInstance().getUid();
        friendsReference = FirebaseDatabase.getInstance().getReference("Friends");
        friendsReference.child(currentUserId).child(anotherUserId).removeValue().addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendsReference.child(anotherUserId).child(currentUserId).removeValue();
                        }
                    }
                }
        );
    }

}

