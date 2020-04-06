package com.deathmarch.intersection.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.ImageAdapter;
import com.deathmarch.intersection.viewmodel.ImageViewModel;

import java.util.ArrayList;

public class ViewImageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageAdapter adapter;
    ImageViewModel viewModel;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        setUpToolbar();
        recyclerView = findViewById(R.id.recycler16);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String displayname = intent.getStringExtra("userDisplayname");
        toolbar.setTitle(displayname+" Image");

        adapter = new ImageAdapter(this);
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        viewModel.getLiveDataImage(userId).observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> arrayList) {
                adapter.updateList(arrayList);
            }
        });


    }

    private void setUpToolbar(){
        toolbar = findViewById(R.id.toolbar_16);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
