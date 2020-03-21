package com.deathmarch.intersection.view.friend;

import android.os.Bundle;
import android.util.Log;
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
import com.deathmarch.intersection.adapter.FriendAdapter;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.viewmodel.FriendViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListFriendFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    FriendViewModel viewModel;
    ArrayList<UserMain> arrayList;
    FriendAdapter adapter;
    public ListFriendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_friend, container, false);
        init();
        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recycler_list_friend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        arrayList = new ArrayList<>();
        adapter = new FriendAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        viewModel.initFriend();
        viewModel.getLiveDataFriend().observe(this, new Observer<ArrayList<UserMain>>() {
            @Override
            public void onChanged(ArrayList<UserMain> userMains) {

                adapter.updateList(userMains);
                adapter.notifyDataSetChanged();
            }
        });

    }


}
