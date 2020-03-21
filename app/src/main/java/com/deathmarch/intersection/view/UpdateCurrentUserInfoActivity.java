package com.deathmarch.intersection.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.UserInfo;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.view.homepage.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateCurrentUserInfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int number_setup = 0;
    private CircleImageView img_Thump;
    private TextView txt_DisplayName;
    private EditText edt_fullname_current_user_info;
    private EditText edt_address_current_user_info;
    private Button btn_choose_date_current_user_info;
    private Spinner sp_choose_sex_current_user_info;
    private EditText edt_description;
    private Button btn_Update_current_user_info;
    private String currenUserId;
    private DatabaseReference currentUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_current_user_info);
        Intent intent = getIntent();
        number_setup = intent.getIntExtra("number_setup", 0);
        init();
        firebaseConnect();
        eventHandler();
    }
    private void init() {
        toolbar = findViewById(R.id.toolbar_current_user_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number_setup == 1) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                finish();


            }
        });
        img_Thump = findViewById(R.id.img_thump_current_user_info);
        txt_DisplayName = findViewById(R.id.txt_displayname_current_user_info);
        edt_fullname_current_user_info = findViewById(R.id.edt_fullname_current_user_info);
        edt_address_current_user_info = findViewById(R.id.edt_address_current_user_info);
        btn_choose_date_current_user_info = findViewById(R.id.btn_choose_date_current_user_info);
        sp_choose_sex_current_user_info = findViewById(R.id.sp_choose_sex_current_user_info);
        edt_description = findViewById(R.id.edt_description);
        btn_Update_current_user_info = findViewById(R.id.btn_update_current_user_info);
        ngaysinhgioitinh();




    }
    private void firebaseConnect() {
        currenUserId = FirebaseAuth.getInstance().getUid();
        currentUserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currenUserId);
    }
    private void eventHandler()
    {
        btn_Update_current_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edt_fullname_current_user_info.getText().toString().trim();
                String address = edt_address_current_user_info.getText().toString().trim();
                String dateOfbirth = btn_choose_date_current_user_info.getText().toString().trim();
                String sex = sp_choose_sex_current_user_info.getSelectedItem().toString();
                String description = edt_description.getText().toString().trim();
                if (isValidate(fullname, address, dateOfbirth, sex, description)){
                    UserInfo userInfo = new UserInfo(fullname, description, dateOfbirth, address, sex);
                    if (CheckNetwork.check(getApplicationContext())){
                        updateUserInfo(userInfo);
                    }else {
                        Toast.makeText(UpdateCurrentUserInfoActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserMain();
        if (number_setup != 1) {
            loadUserInfo();
        }
    }

    private void loadUserMain() {
        currentUserReference.child("UserMain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMain userMain = dataSnapshot.getValue(UserMain.class);
                txt_DisplayName.setText(userMain.getUserDisplayName());
                Glide.with(getApplicationContext())
                        .load(userMain.getUserImage())
                        .placeholder(R.drawable.image_user_defalse)
                        .error(R.drawable.image_user_defalse)
                        .into(img_Thump);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo(){
        currentUserReference.child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                edt_fullname_current_user_info.setText(userInfo.getUserFullName());
                edt_address_current_user_info.setText(userInfo.getUserAddress());
                btn_choose_date_current_user_info.setText(userInfo.getUserDateOfbirth());
                if (userInfo.getUserSex().equals("Nam")){
                    sp_choose_sex_current_user_info.setSelection(1);
                }else {
                    sp_choose_sex_current_user_info.setSelection(2);
                }
                edt_description.setText(userInfo.getUserDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUserInfo(UserInfo userInfo) {
        currentUserReference.child("UserInfo").setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }else {
                    Toast.makeText(UpdateCurrentUserInfoActivity.this, "Update thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ngaysinhgioitinh() {
        List<String> list = new ArrayList<>();
        list.add("Chọn giới tính");
        list.add("Nam");
        list.add("Nữ");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = null;
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_choose_sex_current_user_info.setAdapter(adapter);

        btn_choose_date_current_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;

                mDatePicker = new DatePickerDialog(UpdateCurrentUserInfoActivity.this,
                        AlertDialog.THEME_TRADITIONAL, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        btn_choose_date_current_user_info.setText("" + selectedday + "-" + selectedmonth + "-" + selectedyear);
                    }
                }, mYear, mMonth, mDay);

                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

    }

    private boolean isValidate(String fullname, String address, String dateofbirth, String sex, String description) {
        if (TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Hãy nhập họ tên", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Hãy nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(dateofbirth)){
            Toast.makeText(this, "Hãy chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(sex)){
            Toast.makeText(this, "Hãy chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Hãy nhập mô tả", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dateofbirth.equals("Chọn ngày")){
            Toast.makeText(this, "Hãy chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sex.equals("Chọn giới tính")){
            Toast.makeText(this, "Hãy chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
