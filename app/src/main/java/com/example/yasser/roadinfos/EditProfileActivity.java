package com.example.yasser.roadinfos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;

public class EditProfileActivity extends Fragment {

    View scrollView;
    EditText f_name, l_name, phone_number;
    RadioButton clientRB, serviceRB;
    Button cancelEdit, saveProfile;
    int userId = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);

        scrollView = rootView.findViewById(R.id.profile_root);

        f_name = (EditText) rootView.findViewById(R.id.profileFName);
        l_name = (EditText) rootView.findViewById(R.id.profileLName);
        phone_number = (EditText) rootView.findViewById(R.id.profilePhone);
        clientRB = (RadioButton) rootView.findViewById(R.id.profileClient);
        serviceRB = (RadioButton) rootView.findViewById(R.id.profileService);
        cancelEdit = (Button) rootView.findViewById(R.id.profileCancelEdit);
        saveProfile = (Button) rootView.findViewById(R.id.profileSave);

        scrollView.clearFocus();

        clientRB.setChecked(true);

        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checked;
                if (clientRB.isChecked())
                    checked = "C";
                else
                    checked = "S";

                if (UpdateProfile(userId, f_name.getText().toString(), l_name.getText().toString(),
                        phone_number.getText().toString(), checked)){
                    Toast.makeText(getActivity(), "Successfully updated!\n The app will restart now", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Successfully updated!\n The app will restart now")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Runtime.getRuntime().exit(0);
                                        }
                                    }, 1000);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                    Toast.makeText(getActivity(), "Problem updating the user!", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;

    }


    ////////////////////////
    //// HELPER METHODS ////
    ////////////////////////



    private boolean UpdateProfile (final int userId, final String fname, final String lname,
                                   final String phone, final String type){

//        final ProgressDialog progressDialog = ProgressDialog.show(ServiceDetailActivity.this, "Fetching the event's images",
//                "Please wait...", false, false);

        final boolean[] success = {false};
        final String[] res = new String[1];


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Showing toast message of the response
//                        Toast.makeText(getApplicationContext(), "Success: " +s, Toast.LENGTH_LONG).show();
                        Log.d("messg",s);
//                        if (s.equals("success"))
                            success[0] = true;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {


                        //Showing toast
                        Toast.makeText(getActivity(), "eeee: " +volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("userId", Integer.toString(userId));
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("phone", phone);
                params.put("type", type);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

//        adapter.notifyDataSetChanged();

        return success[0];

    }

}
