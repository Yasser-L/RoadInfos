package com.example.yasser.roadinfos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;
import static com.example.yasser.roadinfos.EventData.GetEvents;
import static com.example.yasser.roadinfos.EventDetailFragment.ARG_ITEM_ID;

/**
 * An activity representing a list of Events. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EventDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class EventListActivity extends AppCompatActivity {

//    static Context context = MyApp.GetGlobalContext();
    private boolean mTwoPane;
    public List<EventData> LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        /*
      Whether or not the activity is in two-pane mode, i.e. running on a tablet
      device.
     */
        FloatingActionButton add_event = (FloatingActionButton) findViewById(R.id.addEvent2);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), AddEvent.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.event_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.event_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.d("EventListActivity 107", "Entered");
        if (GetEvents(getApplicationContext(), 1) != null && GetEvents(getApplicationContext(), 1).size() != 0) {
            Log.d("115", "setupRecyclerView: EventList works");
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(GetEvents(getApplicationContext(), 1)));
        }
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(response()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<EventData> mValues;

        public SimpleItemRecyclerViewAdapter(List<EventData> items) {
            Log.d("EventListActivity 117:", "Entered");
//            response();
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.eventType.setText(mValues.get(position).getEventType());
            holder.eventPlace.setText(mValues.get(position).getEventPlace());
            holder.eventDate.setText(mValues.get(position).getEventDate());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ARG_ITEM_ID, holder.mItem.getId());
                        EventDetailFragment fragment = new EventDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.event_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra(ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mValues == null) {
                Log.d("mValues size = null", "Entered");
                return 0;
            }
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView eventType;
            public final TextView eventPlace;
            public final TextView eventDate;
            public EventData mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                eventType = (TextView) view.findViewById(R.id.eventType);
                eventPlace = (TextView) view.findViewById(R.id.eventPlace);
                eventDate = (TextView) view.findViewById(R.id.eventDate);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + eventType.getText() + "'";
            }
        }
    }


    ////////////////////////
    //// HELPER METHODS ////
    ////////////////////////


     public List<EventData> response() {

        String GET_EVENTS_URL = "http://192.168.1.7/RoadInfos/GetEvents.php";

        final String[] res = new String[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_EVENTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //Showing toast message of the response
                        res[0] = s;
                        Log.d("response", res[0]);
                        try {
//                            JSONObject object = new JSONObject();
                            JSONArray array = new JSONArray(delete_first_char(res[0]));
                            for (int i = 0; i < array.length(); i++) {
                                Log.d("Inside jsonarray", "Entered");

                                JSONObject responseObject = array.getJSONObject(i);

                                String eventId = responseObject.getString("Id");
                                String eventName = responseObject.getString("EventName");
                                String eventDesc = responseObject.getString("EventDesc");
                                String eventType = responseObject.getString("EventType");
                                String eventDate = responseObject.getString("EventDate");
                                String eventPlace = responseObject.getString("EventPlace");
                                String eventLocX = responseObject.getString("EventLocationX");
                                String eventLocY = responseObject.getString("EventLocationY");
                                String validated = responseObject.getString("Validated");

                                EventData event = new EventData(Integer.parseInt(eventId), 1, eventName, eventDesc, eventType, eventDate, eventPlace, eventLocX, eventLocY, validated);

                                LIST.add(event);
                        }}catch (JSONException e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                            Toast.makeText(getApplicationContext(), "Success: " + s, Toast.LENGTH_LONG).show();
                        Log.d("Mmessage", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog

                        //Showing toast
                        Log.d("MmessageError", volleyError.getMessage());
                        Toast.makeText(getApplicationContext(), "eeee: " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);


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


        return LIST;
    }

    public static String delete_first_char(String string) {

        String rephrase = null;
        if (string != null && string.length() > 1) {
            rephrase = string.substring(1, string.length());
        }

        return rephrase;
    }

}


