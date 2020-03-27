package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.UserMain;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreatePostActivity extends AppCompatActivity {
    private ImageButton imageButton_close;
    private TextView txt_post;
    private EditText addpost_Edittext;
    private Button btn_Add_Image;
    private ImageView img_Image_Post;
    private ImageView img_Thump;
    private TextView txt_DisplayName;
    private Button btn_XoaAnh;
    Uri uri;
    private ProgressDialog progressDialog;
    Random random = new Random();

    String currentUserId;
    private DatabaseReference currentUserReference;
    private DatabaseReference myPostReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        init();
        eventHandler();
        loadInfoCurrentUser();

    }

    private void checkInfoPost(){
        String post_text = addpost_Edittext.getText().toString().trim();
        if (TextUtils.isEmpty(post_text) && uri==null){
            Toast.makeText(this, "Chưa có text and Image", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(post_text) && uri!=null){
            progressDialog.show();
            createPostImage();
        }
        if (!TextUtils.isEmpty(post_text) && uri==null ){
            createPostText(post_text);
        }
        if (!TextUtils.isEmpty(post_text) && uri!=null ){
            progressDialog.show();
            createPostDouble(post_text);
        }
    }

    private void createPostText(String text){
        DatabaseReference message_push = myPostReference.push();
        String postId = message_push.getKey();

        Map textMap = new HashMap();
        textMap.put("postUserId", currentUserId);
        textMap.put("postId", postId);
        textMap.put("hasImage", "false");
        textMap.put("postType", "text");
        textMap.put("postText", text);
        textMap.put("postTime", ServerValue.TIMESTAMP);
        myPostReference.child(postId).updateChildren(textMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    addpost_Edittext.setText("");
                    Toast.makeText(CreatePostActivity.this, "create post Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createPostImage(){
        DatabaseReference message_push = myPostReference.push();
        final String postId = message_push.getKey();

        final StorageReference filepath = FirebaseStorage.getInstance().getReference().
                child("Post").child(currentUserId).child(postId);
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
                    Map textMap = new HashMap();
                    textMap.put("postUserId", currentUserId);
                    textMap.put("postId", postId);
                    textMap.put("hasImage", "true");
                    textMap.put("postType", "image");
                    textMap.put("postImage", downloadUrl);
                    textMap.put("postTime", ServerValue.TIMESTAMP);
                    myPostReference.child(postId).updateChildren(textMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                addpost_Edittext.setText("");
                                uri = null;
                                btn_Add_Image.setText("Chọn ảnh");
                                btn_XoaAnh.setVisibility(View.GONE);
                                img_Image_Post.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Toast.makeText(CreatePostActivity.this, "create post image Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    private void createPostDouble( final String text){
        DatabaseReference message_push = myPostReference.push();
        final String postId = message_push.getKey();
        final StorageReference filepath = FirebaseStorage.getInstance().getReference().
                child("Post").child(currentUserId).child(postId);
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
                    Map textMap = new HashMap();
                    textMap.put("postUserId", currentUserId);
                    textMap.put("postId", postId);
                    textMap.put("postType", "double");
                    textMap.put("hasImage", "true");
                    textMap.put("postText", text);
                    textMap.put("postImage", downloadUrl);
                    textMap.put("postTime", ServerValue.TIMESTAMP);
                    myPostReference.child(postId).updateChildren(textMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                addpost_Edittext.setText("");
                                uri = null;
                                btn_Add_Image.setText("Chọn ảnh");
                                btn_XoaAnh.setVisibility(View.GONE);
                                img_Image_Post.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                Toast.makeText(CreatePostActivity.this, "create post image Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }





    private void init(){
        currentUserId = FirebaseAuth.getInstance().getUid();
        currentUserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        myPostReference = FirebaseDatabase.getInstance().getReference().child("Post").child(currentUserId);
        imageButton_close = findViewById(R.id.fullscreen_dialog_close);
        txt_post = findViewById(R.id.fullscreen_dialog_action);
        addpost_Edittext = findViewById(R.id.addpost_edittext);
        btn_Add_Image = findViewById(R.id.btn_add_image_post);
        img_Image_Post = findViewById(R.id.img_add_image_post);
        img_Thump = findViewById(R.id.img_thump11);
        txt_DisplayName=findViewById(R.id.txt_displayname11);
        btn_XoaAnh = findViewById(R.id.btn_delete_image);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create New Post");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void  eventHandler(){
        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        txt_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfoPost();
            }
        });

        btn_Add_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(CreatePostActivity.this);
            }
        });

        btn_XoaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = null;
                btn_XoaAnh.setVisibility(View.GONE);
                img_Image_Post.setVisibility(View.GONE);
                btn_Add_Image.setText("Thêm ảnh");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                img_Image_Post.setVisibility(View.VISIBLE);
                btn_XoaAnh.setVisibility(View.VISIBLE);
                btn_Add_Image.setText("Chọn ảnh khác");
                img_Image_Post.setImageURI(uri);
                super.onActivityResult(requestCode, resultCode, data);

            }
        }
    }

    private void loadInfoCurrentUser(){
        currentUserReference.child(currentUserId).child("UserMain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMain userMain = dataSnapshot.getValue(UserMain.class);
                Glide.with(getApplicationContext())
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(img_Thump);
                txt_DisplayName.setText(userMain.getUserDisplayName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
