package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.adapter.PostAdapter;
import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.viewmodel.UserViewModel;
import com.deathmarch.intersection.viewmodel.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageActivity extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView img_Thump, img_ChooseImage;
    TextView txt_Displayname;

    LinearLayout ln_CreatePost;
    LinearLayout ln_ViewImage;
    TextView txt_ControlInfo;
    LinearLayout ln_ViewUser;
    TextView txt_Fullname, txt_Address, txt_DateBirth, txt_Sex, txt_Email, txtDescription, txtEditPage;
    String currentUserId;
    DatabaseReference usersReference;
    RecyclerView recyclerView;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        init();
        setUpToobar();
        setUpInfoUser();
        setUpRecyclerViewPost();
        evenHandler();



    }
    private void init(){
        currentUserId = FirebaseAuth.getInstance().getUid();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        img_Thump = findViewById(R.id.img_thump12);
        img_ChooseImage = findViewById(R.id.img_choose12);
        txt_Displayname=findViewById(R.id.txt_diplayname12);
        ln_CreatePost = findViewById(R.id.ln_create_post12);
        ln_ViewUser = findViewById(R.id.ln_viewInfo);
        ln_ViewImage = findViewById(R.id.ln_viewimage12);
        txt_ControlInfo = findViewById(R.id.txt_control_info);
        txt_Fullname = findViewById(R.id.txt_fullname12);
        txt_Address = findViewById(R.id.txt_address12);
        txt_DateBirth = findViewById(R.id.txt_datebirth12);
        txt_Sex = findViewById(R.id.txt_sex12);
        txt_Email = findViewById(R.id.txt_email12);
        txtDescription = findViewById(R.id.txt_description12);
        txtEditPage = findViewById(R.id.txt_edit_mypage12);
    }

    private void setUpToobar(){
        toolbar = findViewById(R.id.toolbar_my_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpInfoUser() {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getLiveDataUser(FirebaseAuth.getInstance().getUid()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
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

    private void setUpRecyclerViewPost(){
        recyclerView = findViewById(R.id.recycler12);
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

        PostViewModel postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);

        postViewModel.getLiveDataPost(currentUserId).observe(this, new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                adapter.updateList(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }



    private void evenHandler(){
        txt_ControlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ln_ViewUser.getVisibility()==View.GONE){
                    ln_ViewUser.setVisibility(View.VISIBLE);
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_arrow_up_black_24dp);
                    txt_ControlInfo.setCompoundDrawablesWithIntrinsicBounds(null, null, myDrawable, null);
                }else {
                    ln_ViewUser.setVisibility(View.GONE);
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_down_black_24dp);
                    txt_ControlInfo.setCompoundDrawablesWithIntrinsicBounds(null, null, myDrawable, null);
                }
            }
        });

        ln_ViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                intent.putExtra("userId", currentUserId);
                startActivity(intent);
            }
        });


        txtEditPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateCurrentUserInfoActivity.class);
                startActivity(intent);
            }
        });

        ln_CreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
                startActivity(intent);
            }
        });


    }
}
