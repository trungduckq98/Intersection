package com.deathmarch.intersection.view.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.AcceptRequestFriend;
import com.deathmarch.intersection.adapter.CountFriend;
import com.deathmarch.intersection.adapter.ReceiveRequestAdapter;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.view.AnotherUserPageActivity;
import com.deathmarch.intersection.viewmodel.FriendViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ReceiveRequestFragment extends Fragment implements AcceptRequestFriend {
    View view;
    LinearLayout linearLayout;
    Snackbar snackbar;
    FriendViewModel viewModel;
    RecyclerView recyclerView;
    private ReceiveRequestAdapter adapter;
    private AcceptRequestFriend acceptRequestFriend =this;

    public ReceiveRequestFragment(){
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_receive_request, container, false);
        setUpRecyclerView();


        return view;
    }

    private void setUpRecyclerView(){
        linearLayout = view.findViewById(R.id.linearlayout_fragment_receiver);
        recyclerView = view.findViewById(R.id.recycler_receive_request);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new ReceiveRequestAdapter(getContext(), acceptRequestFriend);
        recyclerView.setAdapter(adapter);

        //view model

        viewModel = ViewModelProviders.of(getActivity()).get(FriendViewModel.class);
        viewModel.getLiveDataReceive(FirebaseAuth.getInstance().getUid()).observe(this, new Observer<ArrayList<UserMain>>() {
            @Override
            public void onChanged(ArrayList<UserMain> userMains) {
                adapter.updateList(userMains);
            }
        });


    }

    public void showSnackbar(final String userId, String displayname) {
        String message = "Bạn và "+ displayname+" đã trở thành bạn bè.";
         snackbar = Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG);
         snackbar.setAction("ViewPage", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAnotherUserPageActivity(userId);
            }
        });
         snackbar.show();
    }

    @Override
    public void isComplete(String userId, String displayname) {
        showSnackbar(userId, displayname);
    }

    private void goAnotherUserPageActivity(String anotherUserId)
    {
        Intent intent = new Intent(getContext(), AnotherUserPageActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }
}
