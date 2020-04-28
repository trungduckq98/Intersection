package com.deathmarch.intersection.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.deathmarch.intersection.R;
import com.deathmarch.intersection.view.homepage.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
@SuppressWarnings("deprecation")
public class LauncherActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private BroadcastReceiver broadcastReceiver;
    private LinearLayout ln_NoInternet;
    private ImageView img_Intro;
    private TextView txt_Intro;
    private Animation top_Anim, bot_Anim;
    private IntentFilter intentFilter;
    private int timeOut = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        init();
        createBroadcast();
    }

    private void init() {
        ln_NoInternet = findViewById(R.id.ln_connect);
        txt_Intro = findViewById(R.id.txt_intro);
        img_Intro = findViewById(R.id.img_intro1);
        top_Anim = AnimationUtils.loadAnimation(LauncherActivity.this, R.anim.top_animation);
        bot_Anim = AnimationUtils.loadAnimation(LauncherActivity.this, R.anim.bot_animation);
        img_Intro.setAnimation(top_Anim);
        txt_Intro.setAnimation(bot_Anim);
    }

    private void createBroadcast() {
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    int check = getNumberCheck();
                    goActivity(check);
                } else {
                    timeOut = 1000;
                    notInternet();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void goActivity(final int check) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (check) {
                    case 1:
                        goLoginActivity();
                        break;
                    case 2:
                        goAddEmailActivity();
                        break;
                    case 3:
                        goUserProfileActivity();
                        break;
                    case 4:
                        goHomeActivity();
                        break;
                    default:
                        // code block
                }
            }
        }, timeOut);
    }

    private int getNumberCheck() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return 1;
        } else {
            if (firebaseUser.getEmail() == null) {
                return 2;
            } else {
                if (firebaseUser.getDisplayName() == null
                        || firebaseUser.getDisplayName().equals("null")
                        || firebaseUser.getDisplayName().equals("")) {
                    return 3;
                } else {
                    return 4;
                }
            }
        }
    }

    private void notInternet() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txt_Intro.setVisibility(View.GONE);
                ln_NoInternet.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

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
