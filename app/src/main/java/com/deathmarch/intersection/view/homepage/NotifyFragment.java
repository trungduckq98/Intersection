package com.deathmarch.intersection.view.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.MyNotifyAdapter;
import com.deathmarch.intersection.model.MyNotification;
import com.deathmarch.intersection.view.SearchActivity;
import com.deathmarch.intersection.viewmodel.MyNotifyViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotifyFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    MyNotifyAdapter adapter;
    ImageView img_search;

    public NotifyFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_notify, container, false);
        recyclerView = view.findViewById(R.id.recycler24);
        img_search = view.findViewById(R.id.img_search_24);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new MyNotifyAdapter(getContext());
        recyclerView.setAdapter(adapter);
        MyNotifyViewModel viewModel = ViewModelProviders.of(getActivity()).get(MyNotifyViewModel.class);
        viewModel.getLiveDataNotify(FirebaseAuth.getInstance().getUid()).observe(getActivity(), new Observer<ArrayList<MyNotification>>() {
            @Override
            public void onChanged(ArrayList<MyNotification> myNotifications) {
                sortNotifyByTime(myNotifications);
            }
        });
        Log.d("ddddddddddddddd", "notify Frag");
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        return view;
    }
    private void sortNotifyByTime(ArrayList<MyNotification> notifications){
        for (int i = 0; i < notifications.size() - 1; i++) {
            for (int j = i + 1; j < notifications.size(); j++) {
                if (notifications.get(i).getTime() > notifications.get(j).getTime()) {
                    MyNotification temp = notifications.get(i);
                    notifications.set(i, notifications.get(j));
                    notifications.set(j, temp);
                }
            }
        }
        adapter.updateList(notifications);

    }



}
