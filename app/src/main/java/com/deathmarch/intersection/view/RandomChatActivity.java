package com.deathmarch.intersection.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.MessengerAdapter;
import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.viewmodel.RandomChatViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomChatActivity extends AppCompatActivity {
    int setUp;
    String mySexxx;
    Toolbar toolbar;
    Button btn_TimDoi;
    Spinner spinner;
    Query querySetUp;
    LinearLayout linearLayoutTop;
    TextView txt_State;
    RelativeLayout relativeLayoutContent;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    String urlImageAnother = "";

    ImageView img_sent_mess;
    ImageView img_sent_img;
    EditText edt_Content_Mess;
    String anotherUserId;

    String currentUserId;
    DatabaseReference randomChatRef;
    DatabaseReference messengerReference;
    DatabaseReference lastMessRef;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_chat);
        Intent intent = getIntent();
        mySexxx = intent.getStringExtra("mySexxx");
        init();
        setUpToolbar();
        setUpSpinner();
        setUpPage();


        eventHandler();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar33);
        btn_TimDoi = findViewById(R.id.btn_timdoi33);
        linearLayoutTop = findViewById(R.id.ln_top33);
        relativeLayoutContent = findViewById(R.id.relative33);
        spinner = findViewById(R.id.spiner33);
        txt_State = findViewById(R.id.txt_state33);

        edt_Content_Mess = findViewById(R.id.edt_content_messenger33);
        img_sent_mess  = findViewById(R.id.btn_sent_messenger_chat_activity33);
        img_sent_img = findViewById(R.id.btn_sent_img33);



        currentUserId = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        randomChatRef = FirebaseDatabase.getInstance().getReference().child("RandomChat");
        messengerReference = FirebaseDatabase.getInstance().getReference().child("Messenger");
        lastMessRef = FirebaseDatabase.getInstance().getReference().child("LastMess");
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setUpSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Tất cả");
        list.add("Nam");
        list.add("Nữ");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
    }

    private void eventHandler() {
        btn_TimDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(getApplicationContext())){
                    if (setUp==1) {
                        String findSex = spinner.getSelectedItem().toString();
                        timdoi(findSex);
                    }
                    if (setUp==2) {
                        huytimdoi();
                    }
                    if (setUp==3) {
                        showDialogHuydoi();
                    }
                }

            }
        });


        img_sent_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edt_Content_Mess.getText().toString().trim())){
                    if (CheckNetwork.check(getApplicationContext())) createMessenger("text", edt_Content_Mess.getText().toString().trim());
                }
            }
        });

        img_sent_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(RandomChatActivity.this);
            }
        });
    }
    private void createMessenger(String type,String content){
        Map messageMap = new HashMap();
        messageMap.put("content", content);
        messageMap.put("type", type);
        messageMap.put("time", ServerValue.TIMESTAMP);
        messageMap.put("from", currentUserId);
        messageMap.put("seen", "true");
        sentMessenger(messageMap);
        edt_Content_Mess.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();
                Random random = new Random();
                String push =""+ random.nextInt(999999999);

                final StorageReference filepath = FirebaseStorage.getInstance().getReference().
                        child("MessengerImage").child(currentUserId).child(anotherUserId).child(push);
                UploadTask uploadTask = filepath.putFile(uri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (task.isSuccessful()) {

                            return filepath.getDownloadUrl();

                        } else {
                            throw task.getException();
                        }

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String downloadUrl = task.getResult().toString();
                            createMessenger("image", downloadUrl);

                        } else {

                        }
                    }
                });

                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void setUpRecyclerview(){
        recyclerView = findViewById(R.id.recycler_chat_activity33);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayout);
        final MessengerAdapter adapter = new MessengerAdapter(this,anotherUserId );
        recyclerView.setAdapter(adapter);


        RandomChatViewModel viewModel = ViewModelProviders.of(this).get(RandomChatViewModel.class);
        viewModel.getLiveDataMessenger(currentUserId).observe(this, new Observer<ArrayList<Messenger>>() {
            @Override
            public void onChanged(ArrayList<Messenger> messengers) {
                Log.d("hihihihihi", "size: "+messengers.size());
                adapter.updateList(messengers, urlImageAnother);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messengers.size() - 1);
            }
        });
    }

    private void sentMessenger(Map messageMap){
        String currenUserRef = "Messenger/" + currentUserId + "/RandomChat";
        String anotherUserRef = "Messenger/" + anotherUserId + "/RandomChat";

//        String currentLastMess = "LastMess/"+currentUserId+"/RandomChat";
//        String anotherLastMess = "LastMess/"+anotherUserId+"/RandomChat";

        DatabaseReference message_push = messengerReference.child(currenUserRef).child(anotherUserRef).push();
        String push_id = message_push.getKey();
        Map databaseMap = new HashMap();
        databaseMap.put(currenUserRef + "/" + push_id, messageMap);
        databaseMap.put(anotherUserRef + "/" + push_id, messageMap);
//        databaseMap.put(anotherLastMess, messageMap);
//        databaseMap.put(currentLastMess, messageMap);
        databaseReference.updateChildren(databaseMap);
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild("state")) {
                if (dataSnapshot.child("state").hasChild("yes")) {
                    setUp =3;
                    btn_TimDoi.setText("Huỷ đối");
                    txt_State.setText("Hãy trò chuyện vui vẻ!!!");
                    txt_State.setVisibility(View.VISIBLE);
                    linearLayoutTop.setVisibility(View.GONE);
                    relativeLayoutContent.setVisibility(View.VISIBLE);
                    anotherUserId = dataSnapshot.child("state").child("yes").getValue().toString();
                    setUpRecyclerview();

                }
                if (dataSnapshot.child("state").hasChild("findSex")) {
                    setUp=2;
                    btn_TimDoi.setText("Huỷ tìm đối");
                    txt_State.setText("Đang tìm đối, vui lòng đợi");
                    txt_State.setVisibility(View.VISIBLE);
                    linearLayoutTop.setVisibility(View.GONE);
                    relativeLayoutContent.setVisibility(View.INVISIBLE);
                }
            } else {
                setUp=1;
                btn_TimDoi.setText("Tìm đối");
                linearLayoutTop.setVisibility(View.VISIBLE);
                relativeLayoutContent.setVisibility(View.INVISIBLE);
                txt_State.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void setUpPage() {
        querySetUp = randomChatRef.child(currentUserId);
        querySetUp.addValueEventListener(valueEventListener);
    }

    private void timdoi(final String findSex) {
        Query query = randomChatRef;
        if (findSex.equals("Tất cả")) {
            query = randomChatRef.orderByChild("state/mySex").startAt("N");
        }
        if (findSex.equals("Nam")) {
            query = randomChatRef.orderByChild("state/mySex").equalTo("Nam");
        }
        if (findSex.equals("Nữ")) {
            query = randomChatRef.orderByChild("state/mySex").equalTo("Nữ");
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arr = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String findSex = d.child("state").child("findSex").getValue().toString();
                    if (findSex.equals("Tất cả") || findSex.equals(mySexxx)) {
                        String userId = d.getKey().toString();
                        arr.add(userId);
                    }
                }
                if (arr.size() == 0) {
                    dangkitimdoi(findSex);
                } else {
                    String userchoose = arr.get(0);
                    if (arr.size() > 1) {
                        Random random = new Random();
                        int k = random.nextInt(arr.size());
                        userchoose = arr.get(k);
                    }
                    ghepdoi(userchoose);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void dangkitimdoi(final String findSex) {
        randomChatRef.child(currentUserId).child("state").child("mySex").setValue(mySexxx)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        randomChatRef.child(currentUserId).child("state").child("findSex").setValue(findSex);

                    }});
    }

    private void ghepdoi(final String userChooseId){
        randomChatRef.child(userChooseId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                randomChatRef.child(userChooseId).child("state").child("yes").setValue(currentUserId)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    randomChatRef.child(currentUserId).child("state").child("yes").setValue(userChooseId);
                                }
                            }
                        });
            }
        });

    }

    private void huytimdoi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bạn chắc chắn muốn huỷ tìm đối?");
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckNetwork.check(getApplicationContext())) {
                    randomChatRef.child(currentUserId).removeValue();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
    private void showDialogHuydoi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bạn chắc chắn muốn huỷ nhắn tin với đối phương này?");
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckNetwork.check(getApplicationContext())) {
                    huydoi();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void huydoi(){
        randomChatRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("state/yes")){
                    String anotherId = dataSnapshot.child("state/yes").getValue().toString();
                    randomChatRef.child(currentUserId).removeValue();
                    randomChatRef.child(anotherId).removeValue();
                    messengerReference.child(currentUserId).child("RandomChat").removeValue();
                    messengerReference.child(anotherId).child("RandomChat").removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }









}





