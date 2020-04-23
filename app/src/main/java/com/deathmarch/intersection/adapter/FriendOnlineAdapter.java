package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.view.ChatActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendOnlineAdapter extends RecyclerView.Adapter<FriendOnlineAdapter.FriendOnlineViewHolder>  {
    private Context context;
    private ArrayList<User> arrayList = new ArrayList<>();

    public FriendOnlineAdapter(Context context) {
        this.context = context;
    }

    public void updateList(ArrayList<User> newsList){
        ListUserDiffUtilCallback callback = new ListUserDiffUtilCallback(arrayList, newsList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newsList);
        diffResult.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_online, parent, false);
        return new FriendOnlineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendOnlineViewHolder holder,int position) {

        Glide.with(context)
                .load(arrayList.get(position).getUserMain().getUserImage())
                .placeholder(R.drawable.image_user_defalse)
                .error(R.drawable.image_user_defalse)
                .into(holder.img_Thump);
        final String userId = arrayList.get(position).getUserMain().getUserId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("another_user_id",userId );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class FriendOnlineViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img_Thump;

        public FriendOnlineViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Thump = itemView.findViewById(R.id.img_thum23);
        }
    }
}
