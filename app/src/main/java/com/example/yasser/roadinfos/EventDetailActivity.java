package com.example.yasser.roadinfos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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

import static com.example.yasser.roadinfos.EventData.GetEvents;
import static com.example.yasser.roadinfos.EventDetailFragment.ARG_ITEM_ID;

/**
 * An activity representing a single EventData detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link EventListActivity}.
 */
public class EventDetailActivity extends AppCompatActivity {

    private EventData event;
    TextView ed_title, ed_date, ed_type, ed_place, ed_desc;
    RecyclerView ed_images;

    private String DOWNLOAD_URL ="http://192.168.1.2/RoadInfos/GetEventImages.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ed_title = (TextView) findViewById(R.id.eventDetailTitle);
        ed_date = (TextView) findViewById(R.id.eventDetailDate);
        ed_type = (TextView) findViewById(R.id.eventDetailType);
        ed_place = (TextView) findViewById(R.id.eventDetailPlace);
        ed_desc = (TextView) findViewById(R.id.eventDetailDesc);

        ed_images = (RecyclerView) findViewById(R.id.eventDetailImagesRV);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        ed_images.setLayoutManager(manager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.event_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("EventData 1");
        }

        Intent intent = getIntent();
        int eventId = intent.getIntExtra(ARG_ITEM_ID, 0);

        EventListActivity activity = new EventListActivity();
        event = GetEvents(getApplicationContext(), 1).get(eventId);

        if (event != null) {
            ed_title.setText(event.getEventName());
            ed_date.setText(event.getEventDate());
            ed_type.setText(event.getEventType());
            ed_place.setText(event.getEventPlace());
            ed_desc.setText(event.getEventDesc());
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_ITEM_ID,
                    getIntent().getIntExtra(ARG_ITEM_ID, 0));
            EventDetailFragment fragment = new EventDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.event_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, EventListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


                                    ////////////////////////
                                    //// HELPER METHODS ////
                                    ////////////////////////

    private void DownloadImages (final String eventId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOWNLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), "Success: " +s, Toast.LENGTH_LONG).show();
                        Log.d("messg",s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {


                        //Showing toast
                        Toast.makeText(getApplicationContext(), "eeee: " +volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("eventId", eventId);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}
