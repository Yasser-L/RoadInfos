package com.example.yasser.roadinfos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Yasser on 11/03/2017.
 */
public class LoginTab extends Fragment {

    EditText emailET, pwET;
    Button loginButton;
    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_tab, container, false);
        emailET = (EditText) rootView.findViewById(R.id.emailET);
        pwET = (EditText) rootView.findViewById(R.id.pwET);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        context = getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(LoginTab.this.getContext(), WelcomeActivity.class));
//                getActivity().finish();
            }
        });
        return rootView;
    }
}
