package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.deathmarch.intersection.adapter.PostAdapter;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.viewmodel.PostViewModel;
import com.deathmarch.intersection.viewmodel.StateFriendViewModel;
import com.deathmarch.intersection.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    LinearLayout ln_isFriend;
    LinearLayout ln_ViewImage;
    RecyclerView recyclerView;
    PostAdapter adapter;
    PostViewModel viewModel;
    UserViewModel currentUserViewModel;

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
        setUpToolbar();
        setup_page(anotherUserId);
        setUpInfoUser();
        eventHandler();

    }

    private void init(){
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
        ln_ViewImage = findViewById(R.id.ln_viewimage3);
        view_kengang = findViewById(R.id.kengang_another_page);
        txt_Fullname = findViewById(R.id.txt_fullname3);
        txt_Address = findViewById(R.id.txt_address3);
        txt_DateBirth = findViewById(R.id.txt_datebirth3);
        txt_Sex = findViewById(R.id.txt_sex3);
        txt_Email = findViewById(R.id.txt_email3);
        txtDescription = findViewById(R.id.txt_description3);
        currenUserId = FirebaseAuth.getInstance().getUid();
        friendsReference = FirebaseDatabase.getInstance().getReference("Friends");
        ln_isFriend = findViewById(R.id.ln_isfriend3);
        recyclerView = findViewById(R.id.recycler3);

    }

    private void setUpToolbar(){
        toolbar = findViewById(R.id.toolbar_another_user_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_devider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ArrayList<Post> arrayList = new ArrayList<>();
        adapter = new PostAdapter(this, arrayList, recyclerView);
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(PostViewModel.class);

        viewModel.getLiveDataPost(anotherUserId).observe(this, new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                adapter.updateList(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpInfoUser(){
        currentUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        currentUserViewModel.getLiveDataUser(anotherUserId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d("haiduong2020", "activity co data");
                UserMain userMain = user.getUserMain();
                UserInfo userInfo = user.getUserInfo();
                Glide.with(getApplicationContext())
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(img_Thump);
                txt_Displayname.setText(userMain.getUserDisplayName());
                txt_Email.setText("Email: "+userMain.getUserEmail());
                if (userInfo!=null){
                    txt_Fullname.setText("Họ tên: "+userInfo.getUserFullName());
                    txt_Address.setText("Địa chỉ: "+userInfo.getUserAddress());
                    txt_DateBirth.setText("Ngày sinh: "+userInfo.getUserDateOfbirth());
                    txt_Sex.setText("Giới tính: "+userInfo.getUserSex());
                    txtDescription.setText("Mô tả: "+userInfo.getUserDescription());
                }else {
                    txt_Fullname.setText("Họ tên: "+"<Chưa cập nhập>");
                    txt_Address.setText("Địa chỉ: "+"<Chưa cập nhập>");
                    txt_DateBirth.setText("Ngày sinh: "+"<Chưa cập nhập>");
                    txt_Sex.setText("Giới tính: "+"<Chưa cập nhập>");
                    txtDescription.setText("Mô tả: "+"<Chưa cập nhập>");
                }
            }
        });
    }


    private void setup_page(final String anotherUserId) {
        StateFriendViewModel viewModel = ViewModelProviders.of(this).get(StateFriendViewModel.class);
        viewModel.getLiveDataStateFriend(currenUserId, anotherUserId).observe(this,
                new Observer<DataSnapshot>() {
                    @Override
                    public void onChanged(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()==null){
                            ln_Not_Friend.setVisibility(View.VISIBLE);
                            ln_Current_Request.setVisibility(View.GONE);
                            ln_Another_Request.setVisibility(View.GONE);
                            ln_Friend.setVisibility(View.GONE);
                            view_kengang.setVisibility(View.GONE);
                            ln_isFriend.setVisibility(View.GONE);
                        }else {
                            String state = dataSnapshot.child("type").getValue().toString();
                            if (state.equals("friend")){
                                ln_Another_Request.setVisibility(View.GONE);
                                ln_Current_Request.setVisibility(View.GONE);
                                ln_Not_Friend.setVisibility(View.GONE);
                                ln_Friend.setVisibility(View.VISIBLE);
                                view_kengang.setVisibility(View.VISIBLE);
                                ln_isFriend.setVisibility(View.VISIBLE);
                                setUpRecyclerView();
                            }
                            if (state.equals("send")){
                                ln_Current_Request.setVisibility(View.VISIBLE);
                                ln_Another_Request.setVisibility(View.GONE);
                                ln_Not_Friend.setVisibility(View.GONE);
                                ln_Friend.setVisibility(View.GONE);
                                view_kengang.setVisibility(View.GONE);
                                ln_isFriend.setVisibility(View.GONE);
                            }
                            if (state.equals("received")){
                                ln_Another_Request.setVisibility(View.VISIBLE);
                                ln_Current_Request.setVisibility(View.GONE);
                                ln_Not_Friend.setVisibility(View.GONE);
                                ln_Friend.setVisibility(View.GONE);
                                view_kengang.setVisibility(View.VISIBLE);
                                ln_isFriend.setVisibility(View.GONE);
                            }
                        }
                    }
                }
        );
    }


    private void eventHandler(){
        ln_ViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                intent.putExtra("userId", anotherUserId);
                startActivity(intent);
            }
        });

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
