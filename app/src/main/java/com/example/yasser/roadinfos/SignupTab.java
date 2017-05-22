package com.example.yasser.roadinfos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
public class SignupTab extends Fragment {

    EditText emailET, pwET, pw2ET;
    Button signupButton;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup_tab, container, false);
        this.signupButton = (Button) rootView.findViewById(R.id.signupButton);
        this.emailET = (TextInputEditText) rootView.findViewById(R.id.emailET);
        this.pwET = (EditText) rootView.findViewById(R.id.pwET);
        this.pw2ET = (EditText) rootView.findViewById(R.id.pw2ET);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        this.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), Signup2.class));
            }
        });
        return rootView;
    }
}
