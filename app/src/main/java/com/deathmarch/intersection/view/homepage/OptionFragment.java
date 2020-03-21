package com.deathmarch.intersection.view.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.deathmarch.intersection.MainActivity;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.view.friend.FriendManagerActivity;
import com.google.firebase.auth.FirebaseAuth;

public class OptionFragment extends Fragment {
    private CardView cardView_Friend;
    private CardView cardView_Logout;

    public OptionFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option, container, false);
        cardView_Friend = view.findViewById(R.id.cardview_friend);
        cardView_Logout = view.findViewById(R.id.cardview_logout);


        cardView_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FriendManagerActivity.class));
            }
        });

        cardView_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        return view;


    }
}
