package com.deathmarch.intersection.view.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.MainActivity;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.chatbot.ChatBotActivity;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.view.MyPageActivity;
import com.deathmarch.intersection.view.ViewImageActivity;
import com.deathmarch.intersection.view.friend.FriendManagerActivity;
import com.deathmarch.intersection.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class OptionFragment extends Fragment {
    private View view;
    private CardView cardView_Friend;
    private CardView cardView_Logout;
    private CardView cardView_Go_mypage;
    private CardView cardView_Image;
    private CardView cardView_Setting;

    private ImageView img_Thump;
    private TextView txt_Displayname;


    private String currentUserId;
    private DatabaseReference currentUserReference;
    private DatabaseReference stateCurrentUserReference;
    public OptionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_option, container, false);
        init();
        eventHandler();
        loadInfoCurrentUser();
        Log.d("ddddddddddddddd", "opt Frag");
        return view;


    }

    private void init(){
        cardView_Friend = view.findViewById(R.id.cardview_friend);
        cardView_Logout = view.findViewById(R.id.cardview_logout);
        cardView_Go_mypage= view.findViewById(R.id.go_my_page_10);
        cardView_Image = view.findViewById(R.id.cardview_image);
        cardView_Setting = view.findViewById(R.id.cardview_settings);
        img_Thump = view.findViewById(R.id.img_thump10);
        txt_Displayname = view.findViewById(R.id.txt_displayname10);
        currentUserId = FirebaseAuth.getInstance().getUid();
        currentUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        stateCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("UserState");
    }

    private void eventHandler(){
        cardView_Go_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });
        cardView_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FriendManagerActivity.class));
            }
        });

        cardView_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserStatus("offline");
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        cardView_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra("userId", FirebaseAuth.getInstance().getUid());
                intent.putExtra("userDisplayname", txt_Displayname.getText().toString());
                startActivity(intent);
            }
        });

        cardView_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatBotActivity.class));
            }
        });
    }

    private  void updateUserStatus(String state) {
        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("userState", state);
        onlineStateMap.put("userTimeState", ServerValue.TIMESTAMP);
        stateCurrentUserReference.setValue(onlineStateMap);
    }

    private void loadInfoCurrentUser(){
        UserViewModel viewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        viewModel.getLiveDataUser(FirebaseAuth.getInstance().getUid()).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d("duckq123", "data in fragment option");
                UserMain userMain = user.getUserMain();
                Glide.with(getActivity())
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(img_Thump);
                txt_Displayname.setText(userMain.getUserDisplayName());
            }
        });
    }
}
