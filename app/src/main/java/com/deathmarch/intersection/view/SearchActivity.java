package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.AnotherUserAdapter;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchActivity extends AppCompatActivity {
    String currentUserId;
    ArrayList<User> newList;
    AnotherUserAdapter adapter;
    Toolbar toolbar_search;
    SearchView searchView;
    RecyclerView recyclerView;

    DatabaseReference usersReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        eventHandler();
    }

    private void eventHandler() {

        setSupportActionBar(toolbar_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_search.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")){
                    if (CheckNetwork.check(getApplicationContext())){
                        search(newText);
                    }

                }else {
                    newList.clear();
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void init(){

        currentUserId = FirebaseAuth.getInstance().getUid();
        toolbar_search = findViewById(R.id.toolbar_search);
        searchView = findViewById(R.id.search_main);
        searchView.onActionViewExpanded();


        recyclerView = findViewById(R.id.recycler_another_user);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        newList =new ArrayList<>();
        adapter = new AnotherUserAdapter(getApplicationContext(), newList);
        recyclerView.setAdapter(adapter);





    }

    private void search(String text){
        final ArrayList<User> arrayList = new ArrayList<>();
        newList.clear();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        Query queryDisplayName = usersReference.orderByChild("UserMain/userDisplayName").startAt(text).endAt(text+"\uf8ff");
        final Query queryFullName = usersReference.orderByChild("UserInfo/userFullName").startAt(text).endAt(text+"\uf8ff");
        queryDisplayName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d :dataSnapshot.getChildren())
                {
                    UserMain userMain = d.child("UserMain").getValue(UserMain.class);
                    UserInfo userInfo = d.child("UserInfo").getValue(UserInfo.class);
                    User user = new User(userMain, userInfo);
                    if (!userMain.getUserId().equals(currentUserId)){
                        arrayList.add(user);
                    }

                }
                queryFullName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d :dataSnapshot.getChildren())
                        {
                            UserMain userMain = d.child("UserMain").getValue(UserMain.class);
                            UserInfo userInfo = d.child("UserInfo").getValue(UserInfo.class);
                            User user = new User(userMain, userInfo);
                            if (!userMain.getUserId().equals(currentUserId)){
                                arrayList.add(user);
                            }
                        }
                        newList.addAll(new HashSet<>(arrayList));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
