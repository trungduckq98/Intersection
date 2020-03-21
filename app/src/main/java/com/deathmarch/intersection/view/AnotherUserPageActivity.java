package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnotherUserPageActivity extends AppCompatActivity {
    private int NUMBER_SETUP;
    int CURRENT_REQUEST = 1;
    int ANOTHER_REQUEST = 2;
    int NOT_FRIEND = 3;
    private LinearLayout ln_Current_Request;
    private LinearLayout ln_Another_Request;
    private TextView btn_Chapnhanketban;
    private TextView btn_Tuchoiketban;
    private LinearLayout ln_Not_Friend;
    private LinearLayout ln_Friend;
    private View view_kengang;
    private ImageView btn_Friend;
    private ImageView btn_NhanTin;
    String currenUserId;
    String anotherUserId;
    Toolbar toolbar;
    DatabaseReference friendsReference;
    DatabaseReference usersReference;


    CircleImageView img_Thump;
    TextView txt_Displayname;
    TextView txt_Fullname, txt_Address, txt_DateBirth, txt_Sex, txt_Email, txtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_page);
        Intent intent = getIntent();
        anotherUserId = intent.getStringExtra("anotherUserId");
        init();
        setup_page(anotherUserId);
        getAnotherUser();
        eventHandler();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar_another_user_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        currenUserId = FirebaseAuth.getInstance().getUid();
        img_Thump = findViewById(R.id.img_thump3);
        txt_Displayname=findViewById(R.id.txt_diplayname3);
        ln_Current_Request = findViewById(R.id.ln_current_request);
        ln_Another_Request = findViewById(R.id.ln_another_request);
        btn_Chapnhanketban = findViewById(R.id.btn_chapnhanketban);
        btn_Tuchoiketban = findViewById(R.id.btn_tuchoiketban);
        btn_Friend=findViewById(R.id.btn_friends);
        btn_NhanTin = findViewById(R.id.btn_nhantin);
        ln_Not_Friend = findViewById(R.id.ln_no_friend);
        ln_Friend = findViewById(R.id.ln_friend);
        view_kengang = findViewById(R.id.kengang_another_page);
        txt_Fullname = findViewById(R.id.txt_fullname3);
        txt_Address = findViewById(R.id.txt_address3);
        txt_DateBirth = findViewById(R.id.txt_datebirth3);
        txt_Sex = findViewById(R.id.txt_sex3);
        txt_Email = findViewById(R.id.txt_email3);
        txtDescription = findViewById(R.id.txt_description3);
        currenUserId = FirebaseAuth.getInstance().getUid();
        friendsReference = FirebaseDatabase.getInstance().getReference("Friends");


    }

    private void eventHandler(){
        ln_Not_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(getApplicationContext())){
                    GuiYeuCauKetBan();
                }
            }
        });

        ln_Current_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(getApplicationContext())){
                    showDialogDeleteRequest();
                }
            }
        });

        btn_Chapnhanketban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(getApplicationContext())){
                    Chapnhanketban();
                }
            }
        });

        btn_Tuchoiketban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(getApplicationContext())){
                    DeleteStateFriend();
                }
            }
        });

        btn_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(getApplicationContext())){
                    showDialogDeletefriend();
                }

            }
        });

        btn_NhanTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("another_user_id", anotherUserId );
                startActivity(intent);
            }
        });
    }

    private void getAnotherUser(){
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        usersReference.child(anotherUserId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserMain userMain = dataSnapshot.child("UserMain").getValue(UserMain.class);
                        UserInfo userInfo = dataSnapshot.child("UserInfo").getValue(UserInfo.class);
                        User user = new User(userMain, userInfo);
                        Glide.with(getApplicationContext())
                                .load(userMain.getUserImage())
                                .placeholder(R.drawable.image_user_defalse)
                                .error(R.drawable.image_user_defalse)
                                .into(img_Thump);
                        txt_Displayname.setText(userMain.getUserDisplayName());
                        txt_Email.append(userMain.getUserEmail());
                        if (userInfo!=null){
                            txt_Fullname.append(userInfo.getUserFullName());
                            txt_Address.append(userInfo.getUserAddress());
                            txt_DateBirth.append(userInfo.getUserDateOfbirth());
                            txt_Sex.append(userInfo.getUserSex());
                            txtDescription.append(userInfo.getUserDescription());
                        }else {
                            txt_Fullname.append("<Chưa cập nhập>");
                            txt_Address.append("<Chưa cập nhập>");
                            txt_DateBirth.append("<Chưa cập nhập>");
                            txt_Sex.append("<Chưa cập nhập>");
                            txtDescription.append("<Chưa cập nhập>");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }





    private void setup_page(final String anotherUserId) {
        final DatabaseReference stateFriendReference =friendsReference.child(currenUserId).child(anotherUserId);
        stateFriendReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()==null){
                    ln_Not_Friend.setVisibility(View.VISIBLE);
                    ln_Current_Request.setVisibility(View.GONE);
                    ln_Another_Request.setVisibility(View.GONE);
                    ln_Friend.setVisibility(View.GONE);
                    view_kengang.setVisibility(View.GONE);
                }else {
                    String state = dataSnapshot.child("type").getValue().toString();
                    Log.d("trungduc", state);
                    if (state.equals("friend")){
                        ln_Another_Request.setVisibility(View.GONE);
                        ln_Current_Request.setVisibility(View.GONE);
                        ln_Not_Friend.setVisibility(View.GONE);
                        ln_Friend.setVisibility(View.VISIBLE);
                        view_kengang.setVisibility(View.VISIBLE);
                    }
                    if (state.equals("send")){
                        ln_Current_Request.setVisibility(View.VISIBLE);
                        ln_Another_Request.setVisibility(View.GONE);
                        ln_Not_Friend.setVisibility(View.GONE);
                        ln_Friend.setVisibility(View.GONE);
                        view_kengang.setVisibility(View.GONE);
                    }
                    if (state.equals("received")){
                        ln_Another_Request.setVisibility(View.VISIBLE);
                        ln_Current_Request.setVisibility(View.GONE);
                        ln_Not_Friend.setVisibility(View.GONE);
                        ln_Friend.setVisibility(View.GONE);
                        view_kengang.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void showDialogDeletefriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String displayname = txt_Displayname.getText().toString();
        builder.setTitle("Bạn có muốn hủy kết bạn với "+displayname+"?");
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckNetwork.check(getApplicationContext())){
                    DeleteStateFriend();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogDeleteRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String displayname = txt_Displayname.getText().toString();
        builder.setTitle("Bạn có muốn rút lại yêu cầu kết bạn với "+displayname+"?");
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckNetwork.check(getApplicationContext())){
                    DeleteStateFriend();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void GuiYeuCauKetBan(){
        final HashMap<String, Object> sendMap = new HashMap<>();
        sendMap.put("type", "send");
        final HashMap<String, Object> receivedMap = new HashMap<>();
        receivedMap.put("type", "received");

        friendsReference.child(currenUserId).child(anotherUserId).updateChildren(sendMap).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendsReference.child(anotherUserId).child(currenUserId).updateChildren(receivedMap);
                        }
                    }
                });
    }

    private void Chapnhanketban(){
        final HashMap<String, Object> friendMap = new HashMap<>();
        friendMap.put("type", "friend");
        friendsReference.child(currenUserId).child(anotherUserId).updateChildren(friendMap).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendsReference.child(anotherUserId).child(currenUserId).updateChildren(friendMap);
                        }
                    }
                });
    }

    private void DeleteStateFriend(){
        friendsReference.child(currenUserId).child(anotherUserId).removeValue().addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            friendsReference.child(anotherUserId).child(currenUserId).removeValue();
                        }
                    }
                }
        );
    }


}
