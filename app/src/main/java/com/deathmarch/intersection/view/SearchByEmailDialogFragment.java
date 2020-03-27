package com.deathmarch.intersection.view;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import com.deathmarch.intersection.CheckNetwork;
import com.deathmarch.intersection.R;
import com.deathmarch.intersection.model.UserMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchByEmailDialogFragment extends DialogFragment {
    View view;
    private EditText edt_EmailSearch;
    private Button btn_EmailSearch;
    private TextView txt_EmailSearch;
    private LinearLayout ln_EmailSearch;
    private CircleImageView img_Thump2;
    private TextView txt_Displayname2;

    DatabaseReference usersReference;
    String currentUserId;
    String anotherUserId;


    public static SearchByEmailDialogFragment newInstance() {
        return new SearchByEmailDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_search_by_gmail, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        edt_EmailSearch = view.findViewById(R.id.edt_emailsearch);
        btn_EmailSearch = view.findViewById(R.id.btn_emailsearch);
        ln_EmailSearch = view.findViewById(R.id.ln_emailsearch);
        txt_EmailSearch = view.findViewById(R.id.txt_emailsearch);
        img_Thump2 = view.findViewById(R.id.img_thump2);
        txt_Displayname2 = view.findViewById(R.id.txt_diplayname2);


        btn_EmailSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_EmailSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(email)){
                    if (isValidEmail(email) ){
                        if (CheckNetwork.check(getContext())){
                            searchByEmail(email);
                        }

                    }else {
                        Toast.makeText(getContext(), "Bạn nhập sai định dạng email", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Hãy nhập email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ln_EmailSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetwork.check(getContext())){
                    goAnotherUserPageActivity(anotherUserId);
                }

            }
        });




        return view;
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }

    private void searchByEmail(String text){
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        Query queryDisplayName = usersReference.orderByChild("UserMain/userEmail").equalTo(text);

        queryDisplayName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMain userMain = new UserMain();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    userMain = d.child("UserMain").getValue(UserMain.class);
                }
                if (userMain!=null){
                    if (userMain.getUserEmail()==null
                            || userMain.getUserEmail().equals("")
                            || userMain.getUserEmail().equals("null")
                            || userMain.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        txt_EmailSearch.setVisibility(View.VISIBLE);
                        ln_EmailSearch.setVisibility(View.GONE);
                    }else {
                        txt_EmailSearch.setVisibility(View.GONE);
                        ln_EmailSearch.setVisibility(View.VISIBLE);
                        anotherUserId = userMain.getUserId();
                        txt_Displayname2.setText(userMain.getUserDisplayName());
                        Glide.with(getContext())
                                .load(userMain.getUserImage())
                                .placeholder(R.drawable.image_user_defalse)
                                .error(R.drawable.image_user_defalse)
                                .into(img_Thump2);

                    }
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void goAnotherUserPageActivity( String anotherUserId)
    {
        Intent intent = new Intent(getContext(), AnotherUserPageActivity.class);
        intent.putExtra("anotherUserId", anotherUserId);
        getContext().startActivity(intent);
        dismiss();
    }

}
