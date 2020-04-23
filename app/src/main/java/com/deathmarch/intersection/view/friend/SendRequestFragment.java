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
import com.deathmarch.intersection.adapter.SentRequestFriendAdapter;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.viewmodel.FriendViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SendRequestFragment extends Fragment {
    View view;
    FriendViewModel viewModel;
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
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView(){
        recyclerView = view.findViewById(R.id.recycler_sent_request);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        arrayList = new ArrayList<>();

        adapter = new SentRequestFriendAdapter(getContext());
        recyclerView.setAdapter(adapter);

        //view model
        viewModel = ViewModelProviders.of(getActivity()).get(FriendViewModel.class);
        viewModel.getLiveDataSend(FirebaseAuth.getInstance().getUid()).observe(getActivity(), new Observer<ArrayList<UserMain>>() {
            @Override
            public void onChanged(ArrayList<UserMain> userMains) {
                adapter.updateList(userMains);

            }
        });

    }


}
