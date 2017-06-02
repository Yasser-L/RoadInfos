package com.example.yasser.roadinfos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
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
    List<String> event_images_list = new ArrayList<>();
    DetailGalleryAdapter adapter;
    TextView ed_title, ed_date, ed_type, ed_place, ed_desc;
    ImageView type_icon;
    RecyclerView ed_images;

    String externalDirectory= Environment.getExternalStorageDirectory().toString();
    private static final String IMAGE_DIRECTORY_NAME = "RoadInfos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_detail_toolbar);
        setSupportActionBar(toolbar);

        Window window = EventDetailActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(EventDetailActivity.this, R.color.colorPrimaryDark));

        ed_title = (TextView) findViewById(R.id.eventDetailTitle);
        ed_date = (TextView) findViewById(R.id.eventDetailDate);
        ed_type = (TextView) findViewById(R.id.eventDetailType);
        ed_place = (TextView) findViewById(R.id.eventDetailPlace);
        ed_desc = (TextView) findViewById(R.id.eventDetailDesc);

        type_icon = (ImageView) findViewById(R.id.eventTypeIcon);

        ed_images = (RecyclerView) findViewById(R.id.eventDetailImagesRV);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        ed_images.setLayoutManager(manager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.event_detail_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Show the Up button in the action bar.
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle("EventData 1");
//        }

        Intent intent = getIntent();
        int position = intent.getIntExtra(ARG_ITEM_ID, 0);


        if (position > 0)
            event = GetEvents(getApplicationContext(), 1).get(position-1);
        else
            event = GetEvents(getApplicationContext(), 1).get(position);

        if (event != null) {
            ed_title.setText(event.getEventName());
            ed_date.setText(event.getEventDate());
            ed_type.setText("Type: " + event.getEventType());
            ed_place.setText("Location: " + event.getEventPlace());
            ed_desc.setText(event.getEventDesc());

            toolbar.setTitle(event.getEventName());

            switch (event.getEventType()){
                case "Accident":
                    type_icon.setImageResource(R.drawable.accident_marker);
                    break;
                case "Road closure":
                    type_icon.setImageResource(R.drawable.road_closure_marker);
                    break;
                case "Heavy vehicles banned":
                    type_icon.setImageResource(R.drawable.heavy_vehicles_marker);
                    break;
                case "Traffic jam":
                    type_icon.setImageResource(R.drawable.traffic_jam_marker);
                    break;
                case "Traffic obstruction":
                    type_icon.setImageResource(R.drawable.traffic_obstruction_marker);
                    break;
                case "Falling stones":
                    type_icon.setImageResource(R.drawable.rocks_collapse_marker);
                    break;
                case "Ice / Snow":
                    type_icon.setImageResource(R.drawable.snow_marker);
                    break;
                case "Strong winds":
                    type_icon.setImageResource(R.drawable.wind_marker);
                    break;
                case "Speed breaker":
                    type_icon.setImageResource(R.drawable.speed_breaker_marker);
                    break;
                case "Damaged roads / Potholes":
                    type_icon.setImageResource(R.drawable.damaged_road_marker);
                    break;
                case "Fog / Smoke":
                    type_icon.setImageResource(R.drawable.fog_marker);
                    break;
                case "Rain":
                    type_icon.setImageResource(R.drawable.rain_marker);
                    break;
                default:
                    type_icon.setImageResource(R.drawable.cone);
            }

        }



        List<String> imagesList = DownloadImages(1);
//        Toast.makeText(EventDetailActivity.this, imagesList.size()+"", Toast.LENGTH_LONG).show();
        Log.d("Download Images", imagesList.size()+"");


        adapter = new DetailGalleryAdapter(getApplicationContext(), event_images_list);
        ed_images.setAdapter(adapter);



        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putInt(ARG_ITEM_ID,
//                    getIntent().getIntExtra(ARG_ITEM_ID, 0));
//            EventDetailFragment fragment = new EventDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.event_detail_container, fragment)
//                    .commit();
//        }
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



    private List<String> DownloadImages (final int eventId){

        final ProgressDialog progressDialog = ProgressDialog.show(EventDetailActivity.this, "Fetching the event's images",
                                                                  "Please wait...", false, false);

        final String[] res = new String[1];


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DOWNLOAD_EVENT_IMAGES,
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

//                                JSONObject responseObject = array.getJSONObject(i);

                                String value = array.getString(i);
                                String path = Config.SERVER_DIRECTORY + value;
                                event_images_list.add(path);
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();

                            }}catch (JSONException e){
//                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("JSONObject error:", e.getMessage());
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {


                        //Showing toast
//                        Toast.makeText(getApplicationContext(), "eeee: " +volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("eventId", "1");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);

//        adapter.notifyDataSetChanged();

        return event_images_list;

    }



}
