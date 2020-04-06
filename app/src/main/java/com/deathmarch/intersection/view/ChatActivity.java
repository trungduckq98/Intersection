package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.MessengerAdapter;
import com.deathmarch.intersection.model.GetTimeAgo;
import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.model.UserState;
import com.deathmarch.intersection.viewmodel.MessengerViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    String currentUserId;
    private DatabaseReference usersReference;
    private DatabaseReference databaseReference;
    private DatabaseReference messengerReference;
    private String anotherUserId;
    private CircleImageView img_Thump;
    private TextView txt_DisplayName;
    private ImageView img_State;
    private TextView txt_State;
    private TextView txt_Daxem;
    private RecyclerView recyclerView;
    private MessengerAdapter adapter;
    private ArrayList<Messenger> arrayList;
    private String urlImageAnother;

    Toolbar toolbar;
    ImageView img_ExitChat;
    ImageView img_sent_mess;
    ImageView img_sent_img;
    EditText edt_Content_Mess;



    private MessengerViewModel viewModel;

    String anotherUserInChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        checkSeen();



    }

    private void init(){
        Intent intent = getIntent();
        anotherUserId = intent.getStringExtra("another_user_id");
        currentUserId = FirebaseAuth.getInstance().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        messengerReference = FirebaseDatabase.getInstance().getReference().child("Messenger");
        img_Thump = findViewById(R.id.img_thump_chat_activity);
        txt_DisplayName = findViewById(R.id.txt_name_friend_chat_activity);
        txt_State = findViewById(R.id.txt_friend_state_chat_activity);
        img_State = findViewById(R.id.img_friend_state_chat_activity);
        txt_Daxem=findViewById(R.id.txt_daxem);
        loadAnotherUser();
        getAnotherInChat();


        recyclerView = findViewById(R.id.recycler_chat_activity);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayout);
        arrayList = new ArrayList<>();
        adapter = new MessengerAdapter(this, arrayList, anotherUserId);
        recyclerView.setAdapter(adapter);


        viewModel = ViewModelProviders.of(this).get(MessengerViewModel.class);

        viewModel.getLiveDataMessenger(FirebaseAuth.getInstance().getUid(), anotherUserId).observe(this, new Observer<ArrayList<Messenger>>() {
            @Override
            public void onChanged(ArrayList<Messenger> messengers) {
                Log.d("trungduc", "size:" +messengers.size());
                adapter.updateList(messengers, urlImageAnother);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messengers.size() - 1);



            }
        });




        toolbar =findViewById(R.id.toolbar_chat_activity);
        setSupportActionBar(toolbar);
        img_ExitChat = findViewById(R.id.img_exit_chat);
        img_ExitChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_Content_Mess = findViewById(R.id.edt_content_messenger);
        img_sent_mess  = findViewById(R.id.btn_sent_messenger_chat_activity);
        img_sent_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edt_Content_Mess.getText().toString().trim())){
                    if (CheckNetwork.check(getApplicationContext())) createMessenger("text", edt_Content_Mess.getText().toString().trim());
                }
            }
        });

        img_sent_img = findViewById(R.id.btn_sent_img);
        img_sent_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ChatActivity.this);
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {
                    recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if ( bottom < oldBottom) {
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int last = recyclerView.getAdapter().getItemCount();
                                        if (last>2){
                                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                        }

                                    }
                                }, 100);

                            }
                        }
                    });
            }
        });



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


    private void createMessenger(String type,String content){
        Map messageMap = new HashMap();
        messageMap.put("content", content);
        messageMap.put("type", type);
        messageMap.put("time", ServerValue.TIMESTAMP);
        messageMap.put("from", currentUserId);
        if (anotherUserInChat.equals("online")){
            messageMap.put("seen", "true");
        }else {
            messageMap.put("seen", "false");
        }
        sentMessenger(messageMap);
        edt_Content_Mess.setText("");
    }



    private void sentMessenger(Map messageMap){
        String currenUserRef = "Messenger/" + currentUserId + "/" + anotherUserId;
        String anotherUserRef = "Messenger/" + anotherUserId + "/" + currentUserId;

        String currentLastMess = "LastMess/"+currentUserId+"/"+anotherUserId;
        String anotherLastMess = "LastMess/"+anotherUserId+"/"+currentUserId;

        DatabaseReference message_push = messengerReference.child(currenUserRef).child(anotherUserRef).push();
        String push_id = message_push.getKey();
        Map databaseMap = new HashMap();
        databaseMap.put(currenUserRef + "/" + push_id, messageMap);
        databaseMap.put(anotherUserRef + "/" + push_id, messageMap);
        databaseMap.put(anotherLastMess, messageMap);
        databaseMap.put(currentLastMess, messageMap);
        databaseReference.updateChildren(databaseMap);
    }


    private void loadAnotherUser(){
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        usersReference.child(anotherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMain userMain = dataSnapshot.child("UserMain").getValue(UserMain.class);
                UserState userState = dataSnapshot.child("UserState").getValue(UserState.class);
                urlImageAnother =userMain.getUserImage();
                Glide.with(getApplicationContext())
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(img_Thump);
                txt_DisplayName.setText(userMain.getUserDisplayName());

                if (userState.getUserState().equals("online")) {
                    img_State.setImageResource(R.drawable.iconsonline);
                    txt_State.setText("Đang hoạt động");
                } else {
                    img_State.setImageResource(R.drawable.iconsoffline);
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String stateFriend = getTimeAgo.getTimeAgo(userState.getUserTimeState());
                    txt_State.setText(stateFriend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateStateInChat(String text) {
        Map stateInChat = new HashMap();
        stateInChat.put("state", text);
        databaseReference.child("StateInChat").child(currentUserId).child(anotherUserId)
                .updateChildren(stateInChat);
    }

    private void getAnotherInChat(){
        databaseReference.child("StateInChat").child(anotherUserId).child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null){
                            anotherUserInChat = dataSnapshot.getValue().toString();
                        }else {
                            anotherUserInChat = "false";
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateSennLastMess(){
        final Map lastMessMap = new HashMap();
        lastMessMap.put("seen", "true");
        databaseReference.child("LastMess").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentUserId+"/"+anotherUserId)){
                    databaseReference.child("LastMess").child(currentUserId).child(anotherUserId)
                            .updateChildren(lastMessMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                databaseReference.child("LastMess").child(anotherUserId).child(currentUserId)
                                        .updateChildren(lastMessMap);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void checkSeen(){
        databaseReference.child("LastMess").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentUserId+"/"+anotherUserId)){
                    Messenger messenger = dataSnapshot.child(currentUserId+"/"+anotherUserId).getValue(Messenger.class);
                    String from = messenger.getFrom();
                    String seen = messenger.getSeen();
                    if (from.equals(currentUserId)){
                        if (seen.equals("true")){
                            txt_Daxem.setVisibility(View.VISIBLE);
                        }else {
                            txt_Daxem.setVisibility(View.GONE);
                        }
                    }else {
                        txt_Daxem.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        databaseReference.child("LastMess").child(currentUserId).child(anotherUserId)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Messenger messenger = dataSnapshot.getValue(Messenger.class);
//                        String from = messenger.getFrom();
//                        String seen = messenger.getSeen();
//                        if (from.equals(currentUserId)){
//                            if (seen.equals("true")){
//                                txt_Daxem.setVisibility(View.VISIBLE);
//                            }else {
//                                txt_Daxem.setVisibility(View.GONE);
//                            }
//                        }else {
//                            txt_Daxem.setVisibility(View.GONE);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateStateInChat("online");
        updateSennLastMess();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateStateInChat("offline");
    }
}
