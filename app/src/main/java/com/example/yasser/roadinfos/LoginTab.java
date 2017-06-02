package com.example.yasser.roadinfos;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Yasser on 11/03/2017.
 */
public class LoginTab extends Fragment {

    EditText emailET, pwET;
    Button loginButton;
    TextView guestLogin;
    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_tab, container, false);

        // set an exit transition
        getActivity().getWindow().setExitTransition(new Slide());

        emailET = (EditText) rootView.findViewById(R.id.emailET);
        pwET = (EditText) rootView.findViewById(R.id.pwET);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        guestLogin = (TextView) rootView.findViewById(R.id.guestLogin);
        context = getActivity();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginTab.this.getContext(), WelcomeActivity.class);
                i.putExtra("userType", "service");
                getActivity().startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        guestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginTab.this.getContext(), WelcomeActivity.class);
                i.putExtra("userType", "guest");
                getActivity().startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

        return rootView;
    }
}
