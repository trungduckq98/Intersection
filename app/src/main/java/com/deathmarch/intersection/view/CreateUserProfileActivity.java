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
import android.widget.ImageView;
import android.widget.Toast;

import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.UserMain;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateUserProfileActivity extends AppCompatActivity {
    private CircleImageView img_profile;
    private ImageView img_choose_Image;
    private EditText edt_profile_displayname;
    private Button btn_addProfile;
    private String downloadUrl;

    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    StorageReference profileImageStorage;

    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);
        init();
        eventHandler();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                img_profile.setImageURI(uri);
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void init(){
        img_profile = findViewById(R.id.img_profile_image);
        edt_profile_displayname = findViewById(R.id.edt_profile_displayname);
        btn_addProfile = findViewById(R.id.btn_add_profile);
        img_choose_Image = findViewById(R.id.img_profile_choose);
    }
    private void eventHandler(){
        img_choose_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(CreateUserProfileActivity.this);

            }
        });




        btn_addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()){
                    if (CheckNetwork.check(getApplicationContext()))
                    {
                        uploadImageToStorage();
                    }
                    else
                    {
                        Toast.makeText(CreateUserProfileActivity.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }



    private void uploadImageToStorage(){
        profileImageStorage = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Account Settings");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final StorageReference filepath = profileImageStorage.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final UploadTask uploadTask = filepath.putFile(uri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    downloadUrl = filepath.getDownloadUrl().toString();
                    return filepath.getDownloadUrl();

                } else {
                    throw task.getException();
                }

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().toString();
                    updateDisplayName();


                } else {

                }
            }
        });
    }

    private void updateDisplayName(){
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(edt_profile_displayname.getText().toString())
                .build();
        firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            updateUserMain();


                        }
                    }
                }
        );
    }

    private void updateUserMain(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        String userPhoneNumber;
        if (firebaseAuth.getCurrentUser().getPhoneNumber()!=null){
            userPhoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        }else {
            Random random = new Random();
           userPhoneNumber = "0"+random.nextInt(999999999);

        }
        UserMain userMain = new UserMain(
                firebaseAuth.getCurrentUser().getUid(),
                firebaseAuth.getCurrentUser().getEmail(),
                userPhoneNumber,
                edt_profile_displayname.getText().toString().trim(),
                downloadUrl);
        DatabaseReference myRef = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).child("UserMain");
        myRef.setValue(userMain).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    goCurrentUserInfo();
                }
            }
        });

    }

    private boolean isValidate(){
        if (uri == null){
            Toast.makeText(this, "Hãy chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edt_profile_displayname.getText().toString()))
        {
            Toast.makeText(this, "Hãy nhập tên hiển thị", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void goCurrentUserInfo(){
        Intent intent = new Intent(getApplicationContext(), UpdateCurrentUserInfoActivity.class);
        intent.putExtra("number_setup",1);
        startActivity(intent);
        finish();
    }
}
