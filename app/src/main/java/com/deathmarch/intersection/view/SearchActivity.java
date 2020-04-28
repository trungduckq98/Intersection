package com.deathmarch.intersection.view;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Locale;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    String currentUserId;
   // ArrayList<User> newList = new ArrayList<>();
    Set set = new HashSet();
    AnotherUserAdapter adapter;
    Toolbar toolbar_search;
    SearchView searchView;
    RecyclerView recyclerView;
    ImageView btn_Voice;
    DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
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
                    set.clear();
                    adapter.updateList(set);
                }
                return false;
            }
        });
        btn_Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });


    }

    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");

        try {
            startActivityForResult(intent, 123);
        }catch (Exception e){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123 && resultCode==RESULT_OK &&data!=null){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = result.get(0);
            searchView.setQuery(text, true);


        }
    }

    private void init(){
        currentUserId = FirebaseAuth.getInstance().getUid();
        toolbar_search = findViewById(R.id.toolbar_search);
        searchView = findViewById(R.id.search_main);
        searchView.onActionViewExpanded();
        btn_Voice = findViewById(R.id.btn_void);
        recyclerView = findViewById(R.id.recycler_another_user);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new AnotherUserAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    private void search(String text){
        set.clear();
        Query queryDisplayName = usersReference.orderByChild("UserMain/userDisplayName").startAt(text).endAt(text+"\uf8ff");
        Query queryFullName = usersReference.orderByChild("UserInfo/userFullName").startAt(text).endAt(text+"\uf8ff");
        queryDisplayName.addListenerForSingleValueEvent(valueEventListener);
        queryFullName.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("VietNam2020", "datasnap = "+dataSnapshot.toString());
            for (DataSnapshot d :dataSnapshot.getChildren()) {
                UserMain userMain = d.child("UserMain").getValue(UserMain.class);
                UserInfo userInfo = d.child("UserInfo").getValue(UserInfo.class);
                User user = new User(userMain, userInfo);
                if (!userMain.getUserId().equals(currentUserId)){
                    set.add(user);
                }
            }
            adapter.updateList(set);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

}
