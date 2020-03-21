package com.deathmarch.intersection.adapter;




import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.view.AnotherUserPageActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnotherUserAdapter extends RecyclerView.Adapter<AnotherUserAdapter.AnotherUserViewHolder> {
    Context context;
    ArrayList<User> arrayList;

    public AnotherUserAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }



    @NonNull
    @Override
    public AnotherUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_another_user, parent, false);

        return new AnotherUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnotherUserViewHolder holder, final int position) {
        //set animation
        holder.imgThump.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_trans_animation));
        holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        Glide.with(context)
                .load(arrayList.get(position).getUserMain().getUserImage())
                .placeholder(R.drawable.image_user_defalse)
                .error(R.drawable.image_user_defalse)
                .into(holder.imgThump);
        holder.txt_DisplayName.setText(arrayList.get(position).getUserMain().getUserDisplayName());
        if (arrayList.get(position).getUserInfo() != null) {
            holder.txt_FullName.setText(arrayList.get(position).getUserInfo().getUserFullName());
        } else {
            holder.txt_FullName.setText("Chưa cập nhật");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(context)){
                    goAnotherUserPageActivity(arrayList.get(position).getUserMain().getUserId());
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AnotherUserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgThump;
        TextView txt_DisplayName;
        TextView txt_FullName;
        LinearLayout linearLayout;

        public AnotherUserViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ln_anotheruser);
            imgThump = itemView.findViewById(R.id.img_thump1);
            txt_DisplayName = itemView.findViewById(R.id.txt_diplayname1);
            txt_FullName = itemView.findViewById(R.id.txt_fullname1);
        }
    }



    private void goAnotherUserPageActivity(String anotherUserId)
    {
        Intent intent = new Intent(context, AnotherUserPageActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}

