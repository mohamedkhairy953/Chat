package com.example.moham.testrealtimedb.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moham.testrealtimedb.MainActivity;
import com.example.moham.testrealtimedb.R;
import com.example.moham.testrealtimedb.R2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    FirebaseAuth auth;

    @BindView(R2.id.login)
    Button login;
    @BindView(R2.id.usrname_login)
    EditText userName_login;
    @BindView(R2.id.psswrd_login)
    EditText passwd_login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);
        auth=FirebaseAuth.getInstance();
        return v;
    }
    @OnClick(R2.id.login)
    public void login(){
        if (TextUtils.isEmpty(passwd_login.getText()) || TextUtils.isEmpty(userName_login.getText())) {
            Toast.makeText(getActivity(), "Check your inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = passwd_login.getText().toString();
        String email = userName_login.getText().toString() + "@chat.com";
        if (password.trim().length() < 6) {
            Toast.makeText(getActivity(), "password must have 6 or more cells", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Signing in ...");
        dialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
