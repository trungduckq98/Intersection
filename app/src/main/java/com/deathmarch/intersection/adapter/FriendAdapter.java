package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
    private ArrayList<UserMain> arrayList = new ArrayList<>();
    private Context context;

    public FriendAdapter(Context context) {
        this.context = context;
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
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendViewHolder holder, final int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
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
        holder.img_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Show menu popup", Toast.LENGTH_SHORT).show();
            }
        });

        usersReference.child(userMain.getUserId()).child("UserState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserState userState = dataSnapshot.getValue(UserState.class);
                if (userState.getUserState().equals("online")){
                    holder.img_State.setImageResource(R.drawable.iconsonline);
                    holder.txt_State.setText("Đang hoạt động");
                }else {
                    holder.img_State.setImageResource(R.drawable.iconsoffline);
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String stateFriend = getTimeAgo.getTimeAgo(userState.getUserTimeState());
                    holder.txt_State.setText(stateFriend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_Thump;
        ImageView img_State;
        TextView txt_Displayname, txt_State;
        ImageView img_option;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Thump = itemView.findViewById(R.id.img_thump_5);
            img_State = itemView.findViewById(R.id.img_friend_state5);
            txt_Displayname = itemView.findViewById(R.id.txt_diplayname5);
            txt_State = itemView.findViewById(R.id.txt_state5);
            img_option = itemView.findViewById(R.id.img_option5);
        }
    }

    private void goAnotherUserPageActivity(String anotherUserId) {
        Intent intent = new Intent(context, AnotherUserPageActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

