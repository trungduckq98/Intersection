package com.deathmarch.intersection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.Messenger;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerAdapter extends RecyclerView.Adapter {
    private String anotherUserId;
    private Context context;
    private ArrayList<Messenger> arrayList = new ArrayList<>();
    private String urlThump="";

    public MessengerAdapter(Context context, String anotherUserId) {
        this.context = context;
        this.anotherUserId = anotherUserId;
    }

    public void updateList(ArrayList<Messenger> newList, String urlThump) {
        this.urlThump = urlThump;
        MessengerDiffUtilCallback callback = new MessengerDiffUtilCallback(arrayList, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.arrayList.clear();
        this.arrayList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getFrom().equals(anotherUserId)){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType==0){
            view = layoutInflater.inflate(R.layout.item_messenger_left, parent, false);
            return new MessengerLeftViewHolder(view);
        }
        view = layoutInflater.inflate(R.layout.item_messenger_right, parent, false);
        return new MessengerRightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messenger messenger = arrayList.get(position);
        holder.setIsRecyclable(false);
        if (messenger.getFrom().equals(anotherUserId)){
            MessengerLeftViewHolder leftViewHolder = (MessengerLeftViewHolder) holder;
            Glide.with(context)
                    .load(urlThump)
                    .placeholder(R.drawable.image_user_defalse)
                    .error(R.drawable.image_user_defalse)
                    .into(leftViewHolder.img_Thump);
            if (messenger.getType().equals("text")){
                    leftViewHolder.txt_messenger_left.setVisibility(View.VISIBLE);
                    leftViewHolder.txt_messenger_left.setText(messenger.getContent());
            }else {
                leftViewHolder.img_messenger_left.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(messenger.getContent())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(leftViewHolder.img_messenger_left);
            }

        }else {
            MessengerRightViewHolder rightViewHolder = (MessengerRightViewHolder) holder;
            if (messenger.getType().equals("text")){
                rightViewHolder.txt_messenger_right.setVisibility(View.VISIBLE);
                rightViewHolder.txt_messenger_right.setText(messenger.getContent());
            }else {
                rightViewHolder.img_messenger_right.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(messenger.getContent())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(rightViewHolder.img_messenger_right);
            }
        }




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    private class MessengerLeftViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView img_Thump;
        private TextView txt_messenger_left;
        private ImageView img_messenger_left;
        public MessengerLeftViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Thump = itemView.findViewById(R.id.img_thump8);
            txt_messenger_left = itemView.findViewById(R.id.message_text__left);
            img_messenger_left = itemView.findViewById(R.id.message_img_left);
        }
    }

    private class MessengerRightViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_messenger_right;
        private ImageView img_messenger_right;
        public MessengerRightViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_messenger_right = itemView.findViewById(R.id.message_text_right);
            img_messenger_right = itemView.findViewById(R.id.message_img_right);
        }
    }


}
