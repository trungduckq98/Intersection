package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
@SuppressWarnings("deprecation")
public class CreateEmailAndPassActivity extends AppCompatActivity {
    private TextInputEditText edt_Add_Password, edt_Add_Password2;
    private TextInputEditText edt_Add_Email;
    private TextView txt_State;
    private ProgressBar progressBar;
    private Button btn_Add_Password;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email_and_pass);
        init();
        eventHandler();
    }

    private void init(){
        edt_Add_Email = findViewById(R.id.edt_create_email);
        edt_Add_Password = findViewById(R.id.edt_create_pass);
        edt_Add_Password2 = findViewById(R.id.edt_create_pass2);

        txt_State = findViewById(R.id.txt_state_create_email_and_pass);
        progressBar = findViewById(R.id.progress_create_email_and_pass);

        btn_Add_Password = findViewById(R.id.btn_create_email_and_pass);
    }

    private void eventHandler() {
        btn_Add_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate(edt_Add_Email.getText().toString(),
                        edt_Add_Password.getText().toString(),
                        edt_Add_Password2.getText().toString())) {
                    if (CheckNetwork.check(getApplicationContext())){
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        btn_Add_Password.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        txt_State.setText("Please Wait...");
                        txt_State.setVisibility(View.VISIBLE);
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseUser.updateEmail(edt_Add_Email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser.updatePassword(edt_Add_Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            progressBar.setVisibility(View.GONE);
                                            btn_Add_Password.setVisibility(View.VISIBLE);
                                            if (task.isSuccessful()) {
                                                txt_State.setVisibility(View.GONE);
                                                Intent intent = new Intent(CreateEmailAndPassActivity.this, CreateUserProfileActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {

                                                txt_State.setText("Email đã được sử dụng");
                                            }

                                        }
                                    });
                                } else {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    progressBar.setVisibility(View.GONE);
                                    btn_Add_Password.setVisibility(View.VISIBLE);
                                    txt_State.setText("Email đã được sử dụng");

                                }
                            }
                        });
                    }else {
                        txt_State.setText("Kiểm tra kết nối internet và thử lại");
                        txt_State.setVisibility(View.VISIBLE);
                    }


                } else {

                    txt_State.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean isValidate(String email, String pass1, String pass2) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass1) || TextUtils.isEmpty(pass2)) {
            txt_State.setText("Hãy nhập đủ thông tin");
            return false;
        }
        if (isValidEmail(email) == false) {
            txt_State.setText("Định dạng email không chính xác");
            return false;
        }
        if (!pass1.equals(pass2)) {
            txt_State.setText("Mật khẩu không trùng khớp");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }
}
