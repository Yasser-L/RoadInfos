package com.example.yasser.roadinfos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.makeText;
import static com.example.yasser.roadinfos.ItemPickerDialogFragment.LOGTAG;

public class WelcomeActivity extends AppCompatActivity {

    private ViewGroup container;
    private DrawerLayout main_drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav_view;
    private ImageButton sos_button, my_events, events_list, accident_em, medical_em, assault_em;
    private ArrayList<String> pickerItems;
    private int selectedIndex;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private RadioButton accidentRB, breakdownRB, outOfGasRB;
    private EditText sosMessage;
    private LinearLayout emContactsList, emptyEmContactsList;
    private Button pickEmContact;
    private TextView emListTitle, current_em;

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                switch (id){
                    case R.id.nav_logout:
                        startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                        finish();
                }
                return false;
            }
        });

        //
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View emergency_dialog = inflater.inflate(R.layout.emergency_dialog,
                container, false);

        sos_button = (ImageButton) findViewById(R.id.sos_button);
        my_events = (ImageButton) findViewById(R.id.my_events_w_button);
        events_list = (ImageButton) findViewById(R.id.my_events_button);
//        accidentRB = (RadioButton) emergency_dialog.findViewById(R.id.accidentCB);
//        breakdownRB = (RadioButton) emergency_dialog.findViewById(R.id.carBreakdownCB);
//        outOfGasRB = (RadioButton) emergency_dialog.findViewById(R.id.outOfGas);
        sosMessage = (EditText) emergency_dialog.findViewById(R.id.sosMessage);
        emContactsList = (LinearLayout)emergency_dialog.findViewById(R.id.emContactsList);
        emptyEmContactsList = (LinearLayout)emergency_dialog.findViewById(R.id.emEmptyContactsList);
        emListTitle = (TextView) emergency_dialog.findViewById(R.id.emListTitle);
        accident_em = (ImageButton) emergency_dialog.findViewById(R.id.accidentEm);
        medical_em = (ImageButton) emergency_dialog.findViewById(R.id.medicalEm);
        assault_em = (ImageButton) emergency_dialog.findViewById(R.id.assaultEm);
        current_em = (TextView) emergency_dialog.findViewById(R.id.currentEmergency);
        Log.d("onCreate:", emContactsList.toString());

        // The list of emergency contacts

        pickerItems = new ArrayList<>();
        pickerItems.add("One");
        pickerItems.add("Two");
        pickerItems.add("Three");





        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (pickerItems.size() < 1){
            emContactsList.setVisibility(View.GONE);
            emptyEmContactsList.setVisibility(View.VISIBLE);
        }

        for (String item : pickerItems) {
            CheckBox cb = new CheckBox(this);
            cb.setText(item);
            params.setMargins(5, 5, 5, 5);
            cb.setLayoutParams(params);
            emContactsList.addView(cb);
        }


        /*
         SOS Button Click
        */
        sos_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment f =  getFragmentManager().findFragmentById(R.id.em_contacts);
                FragmentManager fm = getSupportFragmentManager();
                EmContactsFragment ecf = new EmContactsFragment();


                if(!emergency_dialog.equals(null))
                    Log.d("onClick: ", "hahaha");

                new AlertDialog.Builder(WelcomeActivity.this)
                        .setView(emergency_dialog)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(LOGTAG, "OK button clicked");

                                makeText(getApplicationContext(), sosMessage.getText(), Toast.LENGTH_LONG).show();


//
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                makeText(getApplicationContext(), "No S.O.S message sent!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();

            }
        });

        /*
         My Events Button Click
        */
        my_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyEvents.class));
            }
        });

        events_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EventListActivity.class));
            }
        });

        accident_em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (medical_em.getDrawable() == getDrawable(R.drawable.syringe_clicked))
//                    medical_em.setImageDrawable(getDrawable(R.drawable.syringe));
//                if (assault_em.getDrawable() == getDrawable(R.drawable.assault_clicked))
//                    assault_em.setImageDrawable(getDrawable(R.drawable.assault));
//                v.setBackground(getDrawable(R.color.gradient_start));
//                ((ImageButton)v).setImageResource(R.drawable.accident_2_clicked);
                current_em.setText("Accident");
            }
        });

        medical_em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (accident_em.getDrawable() == getDrawable(R.drawable.accident_2_clicked))
//                    accident_em.setImageDrawable(getDrawable(R.drawable.accident_2));
//                if (assault_em.getDrawable() == getDrawable(R.drawable.assault_clicked))
//                    assault_em.setImageDrawable(getDrawable(R.drawable.assault));
//                v.setBackground(getDrawable(R.color.gradient_start));
//                ((ImageButton)v).setImageResource(R.drawable.syringe_clicked);
                current_em.setText("Medical Emergency");
            }
        });

        assault_em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (medical_em.getDrawable() == getDrawable(R.drawable.syringe_clicked))
//                    medical_em.setImageDrawable(getDrawable(R.drawable.syringe));
//                if (accident_em.getDrawable() == getDrawable(R.drawable.accident_2_clicked))
//                    accident_em.setImageDrawable(getDrawable(R.drawable.accident_2));
//                v.setBackground(getDrawable(R.color.gradient_start));
//                ((ImageButton)v).setImageResource(R.drawable.assault_clicked);
                current_em.setText("Assault");
            }
        });

        accident_em.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton ) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        // Your action here on button click

                        current_em.setText("Accident");

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }
}
