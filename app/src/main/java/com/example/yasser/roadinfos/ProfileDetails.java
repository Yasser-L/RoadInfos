package com.example.yasser.roadinfos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class ProfileDetails extends Fragment {

    TextView f_name, l_name, phone_number, DoB;
    RadioButton maleRB, femaleRB, clientRB, serviceRB;
    User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_profile_details, container, false);

        f_name = (TextView) rootView.findViewById(R.id.profileDetailFName);
        l_name = (TextView) rootView.findViewById(R.id.profileDetailLName);
        phone_number = (TextView) rootView.findViewById(R.id.profileDetailPhone);
        DoB = (TextView) rootView.findViewById(R.id.profileDetailDOB);
        maleRB = (RadioButton) rootView.findViewById(R.id.profileDetailMale);
        femaleRB = (RadioButton) rootView.findViewById(R.id.profileDetailFemale);
        clientRB = (RadioButton) rootView.findViewById(R.id.profileDetailClient);
        serviceRB = (RadioButton) rootView.findViewById(R.id.profileDetailService);


        GetProfile(1);

        if (user != null) {
            Toast.makeText(getActivity(), "User found!", Toast.LENGTH_LONG).show();
            f_name.setText(user.getFirstName());
            l_name.setText(user.getLastName());
            phone_number.setText(user.getPhoneNumber());
            if (user.getSex().equals("M")) {
                femaleRB.setChecked(false);
                maleRB.setChecked(true);
            }
            else if (user.getSex().equals("F")){
                maleRB.setChecked(false);
                femaleRB.setChecked(true);
            }
            if (!user.getDoB().equals("")){
                DoB.setText(user.getDoB());
            }
            if (user.getUserType().equals("C")){
                serviceRB.setChecked(false);
                clientRB.setChecked(true);
            }
            else if (user.getUserType().equals("S")){
                clientRB.setChecked(false);
                serviceRB.setChecked(true);
            }

        }
        else
            Toast.makeText(getActivity(), "User not found!", Toast.LENGTH_LONG).show();


        return rootView;
    }


    ////////////////////////
    //// HELPER METHODS ////
    ////////////////////////



    private void GetProfile (final int userId){

//        final ProgressDialog progressDialog = ProgressDialog.show(ServiceDetailActivity.this, "Fetching the event's images",
//                "Please wait...", false, false);

//        final User[] user = new User[1];
        final String[] res = new String[1];


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_USER_BY_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        //Showing toast message of the response
//                        Toast.makeText(getApplicationContext(), "Success: " +s, Toast.LENGTH_LONG).show();
                        Log.d("messg",s);

                        res[0] = s;
                        Log.d("rrresponse", res[0]);
                        try {
//                            JSONObject object = new JSONObject();
                            JSONArray array = new JSONArray(res[0]);
                            for (int i = 0; i < array.length(); i++) {
                                Log.d("Inside jsonarray", array.length()+"");
//                                Toast.makeText(getApplicationContext(), "Array length: " + array.length()+"", Toast.LENGTH_LONG).show();

                                JSONObject responseObject = array.getJSONObject(i);


                                String userEmail = responseObject.getString("Email");
                                String userPassword = responseObject.getString("Password");
                                String userFName = responseObject.getString("FirstName");
                                String userLName = responseObject.getString("LastName");
                                String DoB = responseObject.getString("DoB");
                                String userSex = responseObject.getString("Sex");
                                String userPhone = responseObject.getString("PhoneNumber");
                                String userType = responseObject.getString("UserType");
                                boolean userValidated = !responseObject.getString("Validated").equals("0");

                                user = new User(userEmail, userPassword, userFName, userLName, DoB, userSex, userPhone, userType, userValidated);
//                                progressDialog.dismiss();

                            }}catch (JSONException e){
//                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("JSONObject_error:", e.getMessage());
                        }



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

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

//        adapter.notifyDataSetChanged();

//        return user[0];

    }


}
