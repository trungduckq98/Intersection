package com.deathmarch.intersection.view.friend;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.CountFriend;
import com.deathmarch.intersection.adapter.SentRequestFriendAdapter;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.viewmodel.FriendViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SendRequestFragment extends Fragment {
    View view;
    FriendViewModel viewModel;
    private String currentUserId;
    DatabaseReference friendsReference;
    DatabaseReference usersReference;
    RecyclerView recyclerView;

    private ArrayList<UserMain> arrayList;
    private SentRequestFriendAdapter adapter;




    public SendRequestFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_request, container, false);
        init();

        return view;
    }

    private void init(){
        currentUserId = FirebaseAuth.getInstance().getUid();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = view.findViewById(R.id.recycler_sent_request);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        arrayList = new ArrayList<>();

        adapter = new SentRequestFriendAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        viewModel.initSend();
        viewModel.getLiveDataSend().observe(this, new Observer<ArrayList<UserMain>>() {
            @Override
            public void onChanged(ArrayList<UserMain> userMains) {
                adapter.updateList(userMains);
                adapter.notifyDataSetChanged();


            }
        });

    }


}
