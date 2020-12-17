package com.deathmarch.intersection.view.homepage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.FriendOnlineAdapter;
import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.model.UserState;
import com.deathmarch.intersection.view.ChatActivity;
import com.deathmarch.intersection.view.RandomChatActivity;
import com.deathmarch.intersection.viewmodel.FriendOnlineViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerFragment extends Fragment {
    View view;
    RecyclerView recyclerView, rv_Online;
    LinearLayout ln_GoRandomchat;
    String currenUserId;
    private DatabaseReference databaseReference;
    private DatabaseReference usersReference;

    public MessengerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messenger, container, false);
        Log.d("ddddddddddddddd", "Mess Frag");
        init();
        firebaseConnect();
        getListUserChat();
        setUpRecyclerViewFriendOnline();
        ln_GoRandomchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRandomchat();
            }
        });


        return view;
    }

    private void init(){
        recyclerView = view.findViewById(R.id.recycler_fragment_messenger);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        ln_GoRandomchat = view.findViewById(R.id.ln_gorandomchat11);

        rv_Online = view.findViewById(R.id.recycler_online);


    }

    private void setUpRecyclerViewFriendOnline(){
        final FriendOnlineAdapter adapter = new FriendOnlineAdapter(getContext());
        rv_Online.setAdapter(adapter);
        FriendOnlineViewModel viewModel = ViewModelProviders.of(getActivity()).get(FriendOnlineViewModel.class);
        viewModel.getLiveDataFriendOnline(FirebaseAuth.getInstance().getUid()).observe(getActivity(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                ArrayList<User> arrOnline = new ArrayList<>();
                for (int i = 0; i<users.size();i++){
                    if (users.get(i).getUserState().getUserState().equals("online")){
                        arrOnline.add(users.get(i));
                    }
                    adapter.updateList(arrOnline);
                }


            }
        });
    }

    public void firebaseConnect() {
        currenUserId = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void getListUserChat() {
        Query query = databaseReference.child("LastMess").child(currenUserId).orderByChild("time");
        FirebaseRecyclerOptions<Messenger> options =
                new FirebaseRecyclerOptions.Builder<Messenger>()
                        .setQuery(query, Messenger.class)
                        .build();
        FirebaseRecyclerAdapter<Messenger, UserChatViewHolder> adapter =new FirebaseRecyclerAdapter<Messenger, UserChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UserChatViewHolder holder, int i, @NonNull Messenger messenger) {

                if (messenger.getFrom().equals(currenUserId)){
                    holder.txt_Last_Messenger.setTextColor(Color.GREEN);
                    holder.ln_item_chat.setBackgroundColor(Color.WHITE);
                    if (messenger.getType().equals("text")){
                        holder.txt_Last_Messenger.setText(messenger.getContent());
                    }else {
                        holder.txt_Last_Messenger.setText("Bạn đã gửi một ảnh");
                    }

                }else {
                    holder.txt_Last_Messenger.setTextColor(Color.RED);
                    if (messenger.getType().equals("text")){
                        holder.txt_Last_Messenger.setText(messenger.getContent());
                    }else {
                        holder.txt_Last_Messenger.setText("Bạn nhận được một ảnh");
                    }

                    if (messenger.getSeen().equals("true")){
                        holder.ln_item_chat.setBackgroundColor(Color.WHITE);
                    }else {
                        holder.ln_item_chat.setBackgroundColor(getResources().getColor(R.color.colorXXX));
                    }
                }

                final String another_user_id = getRef(i).getKey().toString();

                usersReference.child(another_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserMain userMain = dataSnapshot.child("UserMain").getValue(UserMain.class);
                        UserState userState = dataSnapshot.child("UserState").getValue(UserState.class);
                        Glide.with(getActivity())
                                .load(userMain.getUserImage())
                                .placeholder(R.drawable.image_user_defalse)
                                .error(R.drawable.image_user_defalse)
                                .into(holder.img_Thump);
                        holder.txt_Name_UserChat.setText(userMain.getUserDisplayName());
                        if (userState.getUserState().equals("online")){
                            holder.img_State_UserChat.setImageResource(R.drawable.iconsonline);

                        }else {
                            holder.img_State_UserChat.setImageResource(R.drawable.iconsoffline);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("another_user_id", another_user_id );
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public UserChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_chat, parent, false);
                UserChatViewHolder viewHolder = new UserChatViewHolder(view);
                return viewHolder;
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.startListening();



    }

    private void goRandomchat(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid()).child("UserInfo").child("userSex");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gioitinh = dataSnapshot.getValue().toString();
                Intent intent = new Intent(getContext(), RandomChatActivity.class);
                intent.putExtra("mySexxx", gioitinh);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public static class UserChatViewHolder extends RecyclerView.ViewHolder {
        TextView txt_Name_UserChat;
        TextView txt_Last_Messenger;
        CircleImageView img_Thump;
        ImageView img_State_UserChat;
        LinearLayout ln_item_chat;

        public UserChatViewHolder(View itemView) {
            super(itemView);
            txt_Name_UserChat = itemView.findViewById(R.id.txt_name_userchat9);
            txt_Last_Messenger = itemView.findViewById(R.id.txt_last_mess9);
            img_Thump = itemView.findViewById(R.id.img_thump9);
            img_State_UserChat = itemView.findViewById(R.id.img_state_userchat9);
            ln_item_chat = itemView.findViewById(R.id.ln_item_userchat);
        }
    }
}