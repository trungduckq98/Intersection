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
import com.deathmarch.intersection.model.GetTimeAgo;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.model.UserState;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiveRequestAdapter extends RecyclerView.Adapter<ReceiveRequestAdapter.ReceiveRequestViewHolder>{
    private DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
    private String currentUserId;
    private DatabaseReference  friendsReference;
    Context context;
    ArrayList<UserMain> arrayList = new ArrayList<>();
    private AcceptRequestFriend acceptRequestFriend;

    public ReceiveRequestAdapter(Context context, AcceptRequestFriend acceptRequestFriend) {
        this.context = context;
        this.acceptRequestFriend = acceptRequestFriend;
    }

    public void updateList(ArrayList<UserMain> newList) {
        FriendDiffUtilCallback callback = new FriendDiffUtilCallback(arrayList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiveRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receive_request, parent, false);
        return new ReceiveRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  ReceiveRequestViewHolder holder, int position) {
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

        holder.txt_Chapnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(context))    Chapnhanketban(userMain.getUserId(), userMain.getUserDisplayName());

            }
        });

        holder.txt_Tuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(context))  Tuchoiketban(userMain.getUserId());

            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ReceiveRequestViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img_Thump;
        TextView txt_Displayname;
        TextView txt_Chapnhan, txt_Tuchoi;

        public ReceiveRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            img_Thump = itemView.findViewById(R.id.img_thump6);
            txt_Displayname = itemView.findViewById(R.id.txt_diplayname6);
            txt_Chapnhan = itemView.findViewById(R.id.btn_chapnhan6);
            txt_Tuchoi = itemView.findViewById(R.id.btn_tuchoi6);
        }
    }

    private void goAnotherUserPageActivity(String anotherUserId)
    {
        Intent intent = new Intent(context, AnotherUserPageActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void Chapnhanketban(final String anotherUserId, final String displayName){
        if (arrayList.size()==1){
            arrayList.clear();
            notifyDataSetChanged();
        }
        currentUserId = FirebaseAuth.getInstance().getUid();
        friendsReference = FirebaseDatabase.getInstance().getReference("Friends");
        final HashMap<String, Object> friendMap = new HashMap<>();
        friendMap.put("type", "friend");
        friendsReference.child(currentUserId).child(anotherUserId).updateChildren(friendMap).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendsReference.child(anotherUserId).child(currentUserId).updateChildren(friendMap).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                               acceptRequestFriend.isComplete(anotherUserId, displayName);

                                            }
                                        }
                                    }
                            );
                        }
                    }
                });


    }

    private void Tuchoiketban(final String anotherUserId){
        if (arrayList.size()==1){
            arrayList.clear();
            notifyDataSetChanged();
        }
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


