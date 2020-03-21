package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.view.homepage.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneNumberOTPActivity extends AppCompatActivity {
    private int number_setup;
    private Button btn_Get_Code, btn_Sent_Code;
    LinearLayout ln_Sent_Code;
    ProgressBar loadding;
    String phoneNumber;
    TextView txt_mess;
    TextView txt_title_activity;
    EditText edt_Phone_number;
    TextInputEditText edt_Code_Number;
    String myOTP;

    private CountryCodePicker ccp;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_o_t_p);
        init();
        eventHandler();
    }

    private void init() {
        ccp = findViewById(R.id.ccp);
        loadding = findViewById(R.id.load);
        txt_mess = findViewById(R.id.txt_messfailed);
        txt_title_activity = findViewById(R.id.txt_title_activity);
        edt_Phone_number = findViewById(R.id.edt_sdt);
        edt_Code_Number = findViewById(R.id.edt_code);
        btn_Get_Code = findViewById(R.id.btn_get_code);
        btn_Sent_Code = findViewById(R.id.btn_sent_code);
        ln_Sent_Code = findViewById(R.id.linner_sent_code);
        ccp.registerCarrierNumberEditText(edt_Phone_number);

        Intent intent = getIntent();
        number_setup = intent.getIntExtra("title_activity", 1);
        if (number_setup==1){
            txt_title_activity.setText("Register....");
        }else {
            txt_title_activity.setText("Login with Phone Number....");
        }
    }

    private void eventHandler() {
        btn_Get_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_Phone_number.getText().toString())) {
                    txt_mess.setText("Vui lòng nhập số điện thoại");
                    txt_mess.setVisibility(View.VISIBLE);
                } else {
                    phoneNumber = ccp.getFullNumberWithPlus();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS,
                            PhoneNumberOTPActivity.this, mCallbacks);
                    btn_Get_Code.setVisibility(View.GONE);
                    loadding.setVisibility(View.VISIBLE);
                }


            }
        });

        btn_Sent_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOTP = edt_Code_Number.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, myOTP);
                signInWithPhoneAuthCredential(credential);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    loadding.setVisibility(View.GONE);
                    btn_Get_Code.setVisibility(View.VISIBLE);
                    txt_mess.setText("Số điện thoại bạn nhập không hợp lệ, vui lòng kiểm tra lại!!!");
                    txt_mess.setVisibility(View.VISIBLE);
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                loadding.setVisibility(View.GONE);
                ln_Sent_Code.setVisibility(View.VISIBLE);
                mVerificationId = verificationId;
                mResendToken = token;


            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            goActivity();
                        } else {
                            Toast.makeText(PhoneNumberOTPActivity.this, "OTP sai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    private void goActivity() {
        firebaseAuth = FirebaseAuth.getInstance();
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
