package com.example.moham.testrealtimedb.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    FirebaseAuth auth;

    @BindView(R2.id.usrname_edittext)
    public EditText usrNameEditText;
    @BindView(R2.id.psswrd_edittext)
    public EditText passwdNameEditText;
    @BindView(R2.id.register)
    public Button register;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);
        auth = FirebaseAuth.getInstance();

        return v;
    }

    @OnClick(R2.id.register)
    public void signup() {

        if (TextUtils.isEmpty(passwdNameEditText.getText()) || TextUtils.isEmpty(usrNameEditText.getText())) {
            Toast.makeText(getActivity(), "Check your inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = passwdNameEditText.getText().toString();
        String email = usrNameEditText.getText().toString() + "@chat.com";
        if (password.trim().length() < 6) {
            Toast.makeText(getActivity(), "password must have 6 or more cells", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Register ...");
        dialog.show();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
