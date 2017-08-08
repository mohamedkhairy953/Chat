package com.example.moham.testrealtimedb;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.moham.testrealtimedb.fragments.LoginFragment;
import com.example.moham.testrealtimedb.fragments.MainFragment;
import com.example.moham.testrealtimedb.fragments.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.content;

public class AuthenticationActivity extends AppCompatActivity {
    FirebaseAuth auth;

    String MAIN_FRAG_TAG = "xx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(content, new MainFragment(), MAIN_FRAG_TAG).commit();
        else
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
    }


    @Override
    public void onBackPressed() {
        Fragment mainF = getSupportFragmentManager().findFragmentByTag(MAIN_FRAG_TAG);
        if (mainF != null) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new MainFragment(), MAIN_FRAG_TAG).commit();
        }
    }
}
