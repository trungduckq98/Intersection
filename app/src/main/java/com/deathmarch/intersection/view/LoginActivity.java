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
import com.deathmarch.intersection.view.homepage.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {
    private Button btn_Go_Register;
    private Button btn_Login;
    private Button btn_Login_PhoneNumber;
    private TextView txt_State_Login;
    private TextInputEditText edt_User_Login, edt_Pass_Login;
    private ProgressBar progressBarLogin;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        eventHandler();
    }

    private void init() {
        progressBarLogin = findViewById(R.id.progress_login);
        txt_State_Login = findViewById(R.id.txt_state_login);
        btn_Go_Register = findViewById(R.id.btn_go_register);
        btn_Login = findViewById(R.id.btn_login);
        btn_Login_PhoneNumber = findViewById(R.id.btn_login_phoneNumber);
        edt_User_Login = findViewById(R.id.edt_user_login);
        edt_Pass_Login = findViewById(R.id.edt_password_login);

    }

    private void eventHandler() {
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_User_Login.getText().toString().trim();
                String pass = edt_Pass_Login.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    txt_State_Login.setText("Hãy nhập email và mật khẩu");
                    txt_State_Login.setVisibility(View.VISIBLE);
                } else {
                    if (isValidEmail(email) == false) {
                        txt_State_Login.setText("Bạn nhập sai định dạng email");
                        txt_State_Login.setVisibility(View.VISIBLE);

                    } else {
                        if (CheckNetwork.check(getApplicationContext())) {
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            btn_Login.setVisibility(View.GONE);
                            progressBarLogin.setVisibility(View.VISIBLE);
                            txt_State_Login.setText("Đang đăng nhập...");
                            txt_State_Login.setVisibility(View.VISIBLE);
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    btn_Login.setVisibility(View.VISIBLE);
                                    progressBarLogin.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        txt_State_Login.setVisibility(View.GONE);
                                        goActivity();
                                    } else {
                                        txt_State_Login.setText("Email or mật khẩu không chính xác");
                                    }
                                }
                            });
                        } else {
                            txt_State_Login.setText("Kiểm tra kết nối internet và thử lại");
                            txt_State_Login.setVisibility(View.VISIBLE);
                        }

                    }
                }

            }
        });

        btn_Go_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneNumberOTPActivity.class);
                intent.putExtra("title_activity", 1 );
                startActivity(intent);
            }
        });

        btn_Login_PhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneNumberOTPActivity.class);
                intent.putExtra("title_activity", 2 );
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }


    private void goActivity() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.getEmail() == null) {
            goAddEmailActivity();
        } else {
            if (firebaseUser.getDisplayName() == null
                    || firebaseUser.getDisplayName().equals("null")
                    || firebaseUser.getDisplayName().equals("")) {
                goUserProfileActivity();
            } else {
                goHomeActivity();
            }
        }

    }


    private void goAddEmailActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateEmailAndPassActivity.class);
        startActivity(intent);
        finish();
    }

    private void goUserProfileActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateUserProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void goHomeActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
