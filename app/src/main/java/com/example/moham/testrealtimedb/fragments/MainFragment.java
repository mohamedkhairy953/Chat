package com.example.moham.testrealtimedb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moham.testrealtimedb.R;
import com.example.moham.testrealtimedb.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.content;

public class MainFragment extends Fragment {

    @BindView(R2.id.loginbtn)
    Button loginbtn;
    @BindView(R2.id.regbtn)
    Button regbtn;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,v);
        return v;
    }
    @OnClick(R2.id.loginbtn)
    public void goToSignIn(){
       getActivity().getSupportFragmentManager().beginTransaction()
                .replace(content, new LoginFragment()).commit();
    }
    @OnClick(R2.id.regbtn)
    public void goToSignUp(){
       getActivity().getSupportFragmentManager().beginTransaction()
                .replace(content, new RegisterFragment()).commit();
    }
}
