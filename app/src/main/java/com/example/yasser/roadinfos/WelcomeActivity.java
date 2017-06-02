package com.example.yasser.roadinfos;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.makeText;
import static com.example.yasser.roadinfos.ItemPickerDialogFragment.LOGTAG;

public class WelcomeActivity extends AppCompatActivity {

    private ViewGroup container;
    private DrawerLayout main_drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav_view;
    ProgressBar em_pb;
    private ImageButton sos_button, events_list, my_service, last_events, last_services,
                        search, profile, accident_em, medical_em, assault_em;
    private ArrayList<String> pickerItems;
    private int selectedIndex;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private RadioButton accidentRB, breakdownRB, outOfGasRB;
    private EditText sosMessage;
    private LinearLayout emContactsList, emptyEmContactsList;
    private List<PhoneWithEMCase> emCasesList = new ArrayList<>();
    private Button pickEmContact;
    private TextView emListTitle, accidentSelected, medicalSelected, assaultSelected, progress_text;

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();

        // set an enter transition
        getWindow().setEnterTransition(new Slide());
        // set an exit transition
        getWindow().setExitTransition(new Slide());

        String userType = i.getStringExtra("userType");
        if (userType.equals("guest")) {
            setContentView(R.layout.activity_welcome_guest);
        }
        else {
            setContentView(R.layout.activity_welcome);

            main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);

            toggle = new ActionBarDrawerToggle(this, main_drawer, R.string.openDrawer, R.string.closeDrawer);
            main_drawer.addDrawerListener(toggle);
            toggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            nav_view = (NavigationView) findViewById(R.id.main_nav_view);
            nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.nav_logout:
                            startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                            finish();
                    }
                    return false;
                }
            });

            //
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View emergency_dialog = inflater.inflate(R.layout.emergency_dialog,
                    container, false);
            final View last_dialog = inflater.inflate(R.layout.last_duration,
                    container, false);

            sos_button = (ImageButton) findViewById(R.id.sos_button);
            events_list = (ImageButton) findViewById(R.id.my_events_button);
            my_service = (ImageButton) findViewById(R.id.my_service_button);
            search = (ImageButton) findViewById(R.id.search_button);
            profile = (ImageButton) findViewById(R.id.profile_button);
            last_events = (ImageButton) findViewById(R.id.last_events_button);
            last_services = (ImageButton) findViewById(R.id.last_services);


            sosMessage = (EditText) emergency_dialog.findViewById(R.id.sosMessage);
            emptyEmContactsList = (LinearLayout) emergency_dialog.findViewById(R.id.emEmptyContactsList);
            accident_em = (ImageButton) emergency_dialog.findViewById(R.id.accidentEm);
            medical_em = (ImageButton) emergency_dialog.findViewById(R.id.medicalEm);
            assault_em = (ImageButton) emergency_dialog.findViewById(R.id.assaultEm);
            accidentSelected = (TextView) emergency_dialog.findViewById(R.id.accidentSelected);
            medicalSelected = (TextView) emergency_dialog.findViewById(R.id.medicalSelected);
            assaultSelected = (TextView) emergency_dialog.findViewById(R.id.assaultSelected);
            progress_text = (TextView) emergency_dialog.findViewById(R.id.pbText);
            em_pb = (ProgressBar) emergency_dialog.findViewById(R.id.emProgressBar);

            final RadioButton last_day = (RadioButton) last_dialog.findViewById(R.id.lastDayRB);
            final RadioButton last_week = (RadioButton) last_dialog.findViewById(R.id.lastWeekRB);
            final RadioButton last_month = (RadioButton) last_dialog.findViewById(R.id.lastMonthRB);

        /*
         Search Button Click
        */

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    onSearchRequested();
                }
            });


        /*
         SOS Button Click
        */
            sos_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String message = "";
//                    List<PhoneWithEMCase> list;
//                    list = GetEmContacts(WelcomeActivity.this, 1);
                    GetEmContacts(getApplicationContext(), 1);
                    Log.d("ListSizeIs:", emCasesList.size() + "");
                    Toast.makeText(WelcomeActivity.this, emCasesList.size() + "", Toast.LENGTH_LONG).show();

//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Do something after 1000ms
//
//                        }
//                    }, 1000);


                    for ( PhoneWithEMCase emCase : emCasesList) {
                        String number = emCase.getPhoneNumber();
                        Log.d("NumberIs:", number);
//                        Toast.makeText(WelcomeActivity.this, "Number is: " + number, Toast.LENGTH_LONG).show();
                        List<Boolean> cases = emCase.getEmCases();
                        Log.d("NumberIs:", cases.size()+"");
                        if (cases.get(0) && (accidentSelected.getVisibility() == View.VISIBLE)) {
                            Log.d("Accident:", "Send to this number");
                            message = "Accident: Send to this number" + number;
//                            Toast.makeText(WelcomeActivity.this, "Accident: Send to this number" + number, Toast.LENGTH_LONG).show();
                        }
                        else {
                            String vis = accidentSelected.getVisibility() + "";
                            Log.d("Accident:", vis);

                        }
                        if (cases.get(1) && (medicalSelected.getVisibility() == View.VISIBLE)) {
                            Log.d("Medical:", "Send to this number");
                            message = "Medical: Send to this number" + number;
//                            Toast.makeText(WelcomeActivity.this, "Medical: Send to this number" + number, Toast.LENGTH_LONG).show();
                        }
                        if (cases.get(2) && (assaultSelected.getVisibility() == View.VISIBLE)) {
                            Log.d("Assault:", "Send to this number");
                            message = "Assault: Send to this number" + number;
//                            Toast.makeText(WelcomeActivity.this, "Assault: Send to this number" + number, Toast.LENGTH_LONG).show();
                        }
                    }
                    final ObjectAnimator[] animation = {ObjectAnimator.ofInt(em_pb, "progress", 0, 100)};

                    if (!emergency_dialog.equals(null))
                        Log.d("onClick: ", "hahaha");


                    final String finalMessage = message;
                    final AlertDialog builder = new AlertDialog.Builder(WelcomeActivity.this)
                            .setView(emergency_dialog)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(LOGTAG, "OK button clicked");
                                    animation[0] = null;
                                    makeText(getApplicationContext(), finalMessage + ", with the message: " +sosMessage.getText(), Toast.LENGTH_LONG).show();
                                    ((ViewGroup) emergency_dialog.getParent()).removeView(emergency_dialog);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    makeText(getApplicationContext(), "No S.O.S message sent!", Toast.LENGTH_LONG).show();
                                    ((ViewGroup) emergency_dialog.getParent()).removeView(emergency_dialog);
                                }
                            })
                            .show();

                    final int duration = 10000;
                    CountDownTimer timer = new CountDownTimer(duration, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            double left = millisUntilFinished / 1000;
                            progress_text.setText(left + "s until an automatic S.O.S is sent.");
                        }

                        @Override
                        public void onFinish() {
                            progress_text.setText("");
                        }
                    }.start();


                    animation[0].setDuration(duration);
                    animation[0].setInterpolator(new LinearInterpolator());
                    animation[0].addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            //do something when the countdown is complete
                            if (builder.isShowing()) {
                                makeText(getApplicationContext(), "A S.O.S message was sent to the pre-set emergency contacts!", Toast.LENGTH_LONG).show();
                                builder.dismiss();
                                ((ViewGroup) emergency_dialog.getParent()).removeView(emergency_dialog);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            makeText(getApplicationContext(), "S.O.S canceled!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                    animation[0].start();

                }
            });

        /*
         My ServiceButton Click
        */
            my_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 1000ms
                            startActivity(new Intent(getApplicationContext(), MyServiceMainActivity.class),
                                    ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                        }
                    }, 100);

                }
            });

            /*
            Profile Button Click
            */
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), MainProfileActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                }
            });


            /*
             Events list Button Click
            */
            events_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 1000ms
                            startActivity(new Intent(getApplicationContext(), EventListActivity.class),
                                    ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this).toBundle());
                        }
                    }, 100);
                }
            });


            /*
             Events list Button Click
            */
            last_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Intent intent = new Intent(WelcomeActivity.this, EventListActivity.class);
                    intent.putExtra("type", "event");

                    last_day.setChecked(true);
                    final AlertDialog builder = new AlertDialog.Builder(WelcomeActivity.this)
                            .setView(last_dialog)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (last_day.isChecked()) {
                                        intent.putExtra("time", "day");
                                    }

                                    if (last_week.isChecked()) {
                                        intent.putExtra("time", "week");
                                    }

                                    if (last_month.isChecked()) {
                                        intent.putExtra("time", "month");
                                    }

                                    startActivity(intent);

                                    ((ViewGroup) last_dialog.getParent()).removeView(last_dialog);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((ViewGroup) last_dialog.getParent()).removeView(last_dialog);
                                }
                            })
                            .show();
                }
            });


            /*
             Events list Button Click
            */
            last_services.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Intent intent = new Intent(WelcomeActivity.this, EventListActivity.class);
                    intent.putExtra("type", "service");

                    last_day.setChecked(true);
                    final AlertDialog builder = new AlertDialog.Builder(WelcomeActivity.this)
                            .setView(last_dialog)
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (last_day.isChecked()) {
                                        intent.putExtra("time", "day");
                                    }

                                    if (last_week.isChecked()) {
                                        intent.putExtra("time", "week");
                                    }

                                    if (last_month.isChecked()) {
                                        intent.putExtra("time", "month");
                                    }

                                    startActivity(intent);

                                    ((ViewGroup) last_dialog.getParent()).removeView(last_dialog);
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((ViewGroup) last_dialog.getParent()).removeView(last_dialog);
                                }
                            })
                            .show();

                }
            });


            accident_em.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    medicalSelected.setVisibility(View.GONE);
                    assaultSelected.setVisibility(View.GONE);
                    accidentSelected.setVisibility(View.VISIBLE);
//                current_em.setText("Accident");

                }
            });

            medical_em.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accidentSelected.setVisibility(View.GONE);
                    assaultSelected.setVisibility(View.GONE);
                    medicalSelected.setVisibility(View.VISIBLE);
//                current_em.setText("Medical Emergency");
                }
            });

            assault_em.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    medicalSelected.setVisibility(View.GONE);
                    accidentSelected.setVisibility(View.GONE);
                    assaultSelected.setVisibility(View.VISIBLE);
//                current_em.setText("Assault");
                }
            });

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }



    ////////////////////////
    //// HELPER METHODS ////
    ////////////////////////

    public List<PhoneWithEMCase> GetEmContacts_2(final Context context, final int userId) {

        final List<PhoneWithEMCase> caseList = new ArrayList<>();
        final String[] res = new String[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_EM_CONTACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //Showing toast message of the response
                        res[0] = s;
                        Log.d("response", res[0]);
                        try {
//                            JSONObject object = new JSONObject();
                            JSONArray array = new JSONArray(res[0]);
                            for (int i = 0; i < array.length(); i++) {
                                Log.d("Inside jsonarray", "Entered");

                                JSONObject responseObject = array.getJSONObject(i);

                                String emId = responseObject.getString("Id");
                                String contactName = responseObject.getString("ContactName");
                                String contactNumber = responseObject.getString("ContactNumber");
                                String emAccident = responseObject.getString("EM_Accident");
                                String emHealthProblem = responseObject.getString("EM_Health_Problem");
                                String emAgression = responseObject.getString("EM_Agression");

                                boolean accident = (emAccident.equals("1"));
                                boolean medical = (emHealthProblem.equals("1"));
                                boolean agression = (emAgression.equals("1"));
                                Log.d("Booleans", "" + accident + " " + medical+ " " + agression);
                                List<Boolean> cases = new ArrayList<>();
                                cases.add(accident);
                                cases.add(medical);
                                cases.add(agression);

                                PhoneWithEMCase phoneWithEMCase = new PhoneWithEMCase(contactNumber, cases);

                                caseList.add(phoneWithEMCase);
                                Log.d("ListSize", "" + caseList.size());
                            }
                        }catch (JSONException e){
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                        Toast.makeText(context, "Success (line 399)", Toast.LENGTH_LONG).show();
                        Log.d("Mmessage", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog

                        volleyError.printStackTrace();
                        //Showing toast
                        Log.d("MmessageError", volleyError.getCause()+"");
                        Toast.makeText(context, "eeee: " + volleyError.networkResponse + ", " + volleyError.getCause(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("userId", "1");

                //returning parameters
                return params;
            }

        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(stringRequest);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                Log.d("MmessageError", LIST.size()+"");
//            }
//        }, 1000);


        Log.d("ListSize", "" + caseList.size());
        return caseList;
    }



    public void GetEmContacts(final Context context, final int userId) {

        final String[] res = new String[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_EM_CONTACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //Showing toast message of the response
                        res[0] = s;
                        Log.d("response", res[0]);
                        try {
//                            JSONObject object = new JSONObject();
                            JSONArray array = new JSONArray(res[0]);
                            for (int i = 0; i < array.length(); i++) {
                                Log.d("Inside jsonarray", "Entered");

                                JSONObject responseObject = array.getJSONObject(i);

                                String emId = responseObject.getString("Id");
                                String contactName = responseObject.getString("ContactName");
                                String contactNumber = responseObject.getString("ContactNumber");
                                String emAccident = responseObject.getString("EM_Accident");
                                String emHealthProblem = responseObject.getString("EM_Health_Problem");
                                String emAgression = responseObject.getString("EM_Agression");

                                boolean accident = (emAccident.equals("1"));
                                boolean medical = (emHealthProblem.equals("1"));
                                boolean agression = (emAgression.equals("1"));
                                Log.d("Booleans", "" + accident + " " + medical+ " " + agression);
                                List<Boolean> cases = new ArrayList<>();
                                cases.add(accident);
                                cases.add(medical);
                                cases.add(agression);

                                PhoneWithEMCase phoneWithEMCase = new PhoneWithEMCase(contactNumber, cases);

                                emCasesList.add(phoneWithEMCase);
                                Log.d("ListSize", "" + emCasesList.size());
                            }
                        }catch (JSONException e){
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                        Toast.makeText(context, "Success (line 399)", Toast.LENGTH_LONG).show();
                        Log.d("Mmessage", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog

                        volleyError.printStackTrace();
                        //Showing toast
                        Log.d("MmessageError", volleyError.getCause()+"");
                        Toast.makeText(context, "eeee: " + volleyError.networkResponse + ", " + volleyError.getCause(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("userId", "1");

                //returning parameters
                return params;
            }

        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        //Adding request to the queue
        requestQueue.add(stringRequest);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                Log.d("MmessageError", LIST.size()+"");
//            }
//        }, 1000);


        Log.d("ListSize", "" + emCasesList.size());
    }


}
