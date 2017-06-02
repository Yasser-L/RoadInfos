package com.example.yasser.roadinfos;

import android.content.Context;
import android.util.Log;
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

/**
 * Created by Yasser on 16/05/2017.
 */

public class EventData {

    private int Id, UserId;
    private String  EventName, EventDesc, EventType, EventDate, EventPlace, EventLocX, EventLocY, Validated;
    private static Context context;

    private static List<EventData> EVENTS_LIST = new ArrayList<>();

//    static {
//        EVENTS_LIST = GetEvents(MyApp.GetGlobalContext(), 1);
//    }

//    public EventData(Context context){
//        this.context = context;
//    }

    public EventData(int id, int userId, String eventName, String eventDesc, String eventType, String eventDate, String eventPlace, String eventLocX, String eventLocY, String validated) {
        Id = id;
        UserId = userId;
        EventName = eventName;
        EventDesc = eventDesc;
        EventType = eventType;
        EventDate = eventDate;
        EventPlace = eventPlace;
        EventLocX = eventLocX;
        EventLocY = eventLocY;
        Validated = validated;
    }


    public int getId() {
        return Id;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDesc() {
        return EventDesc;
    }

    public void setEventDesc(String eventDesc) {
        EventDesc = eventDesc;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventPlace() {
        return EventPlace;
    }

    public void setEventPlace(String eventPlace) {
        EventPlace = eventPlace;
    }

    public String getEventLocX() {
        return EventLocX;
    }

    public void setEventLocX(String eventLocX) {
        EventLocX = eventLocX;
    }

    public String getEventLocY() {
        return EventLocY;
    }

    public void setEventLocY(String eventLocY) {
        EventLocY = eventLocY;
    }

    public String getValidated() {
        return Validated;
    }

    public void setValidated(String validated) {
        Validated = validated;
    }



    ////////////////////////
    //// HELPER METHODS ////
    ////////////////////////

    public static List<EventData> GetEvents(final Context context, final int userId) {


        final String[] res = new String[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_EVENTS,
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

                                String eventId = responseObject.getString("Id");
                                String eventName = responseObject.getString("EventName");
                                String eventDesc = responseObject.getString("EventDesc");
                                String eventType = responseObject.getString("EventType");
                                String eventDate = responseObject.getString("EventDate");
                                String eventPlace = responseObject.getString("EventPlace");
                                String eventLocX = responseObject.getString("EventLocationX");
                                String eventLocY = responseObject.getString("EventLocationY");
                                String validated = responseObject.getString("Validated");

                                EventData event = new EventData(Integer.parseInt(eventId), userId, eventName, eventDesc, eventType, eventDate, eventPlace, eventLocX, eventLocY, validated);

                                if (!EVENTS_LIST.contains(event))
                                    EVENTS_LIST.add(event);
                            }
                        }catch (JSONException e){
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                        Toast.makeText(context, "Success (line 172)", Toast.LENGTH_LONG).show();
                        Log.d("Mmessage", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog

                        //Showing toast
                        if (volleyError != null)
                            Log.d("MmessageError", volleyError.getMessage()+"");
                        Toast.makeText(context, "eeee: " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
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


        return EVENTS_LIST;
    }

}
