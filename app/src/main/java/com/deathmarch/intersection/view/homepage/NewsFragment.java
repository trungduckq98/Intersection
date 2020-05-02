package com.deathmarch.intersection.view.homepage;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.PostAdapter;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.viewmodel.NewsViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    View view;
    NewsViewModel viewModel;
    RecyclerView recyclerView;
    PostAdapter adapter;
    public NewsFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        setUpRecyclerViewPost();
        Log.d("ddddddddddddddd", "new Frag");

        return view;
    }

    private void setUpRecyclerViewPost(){
        recyclerView = view.findViewById(R.id.recylerview23);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.custom_devider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);


        ArrayList<Post> arrayList = new ArrayList<>();
        adapter = new PostAdapter(getActivity(), arrayList, recyclerView);
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        viewModel.getLiveDataPost(FirebaseAuth.getInstance().getUid()).observe(getActivity(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                sortPostByTime(posts);
            }
        });
    }

    private void sortPostByTime(ArrayList<Post> posts){
        for (int i = 0; i < posts.size() - 1; i++) {
            for (int j = i + 1; j < posts.size(); j++) {
                if (posts.get(i).getPostTime() > posts.get(j).getPostTime()) {
                    Post temp = posts.get(i);
                    posts.set(i, posts.get(j));
                    posts.set(j, temp);
                }
            }
        }
        adapter.updateList(posts);
        adapter.notifyDataSetChanged();
    }
}