package com.example.yasser.roadinfos;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class EventsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static Context context;
    public static final List<Event> EVENTS = new ArrayList<Event>();
    public static List<EventData> EVENTS_LIST = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, EventData> ITEM_MAP = new HashMap<String, EventData>();

    private static final int COUNT = 5;

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(EventListActivity.GetEvents("1").get(i));
//        }
//    }
//
//    private static void addItem(EventData item) {
//        EVENTS.add(item);
//        ITEM_MAP.put(item.id, item);
//    }

//    private static EventData createEvent(int position, String title, String type) {
//        return new EventData(String.valueOf(position), title, makeDetails(position), type);
//    }


//    static {
//        // Add some sample items.
////        for (int i = 1; i <= EVENTS_LIST.size(); i++) {
////            addItem(GetEvents(1).get(i));
////        }
//
//        EVENTS_LIST = GetEvents(1);
//
//    }

    private static void addItem(EventData item) {
        EVENTS_LIST.add(item);
        ITEM_MAP.put(item.getEventName(), item);
    }

//    private static EventData createEvent(int position, String title, String type) {
//        return new EventData(String.valueOf(position), title, makeDetails(position), type);
//    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * EventData item
     */
    public static class Event {
        public final String id;
        public final String title;
        public final String details;
        public final String type;
        public final List<File> images = new ArrayList<>();

        public Event(String id, String title, String details, String type) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.type = type;
        }

        @Override
        public String toString() {
            return title;
        }
    }


    ////////////////////////
    //// HELPER METHODS ////
    ////////////////////////


    public static List<EventData> GetEvents(final int userId) {

        String GET_EVENTS_URL = "http://192.168.1.2/RoadInfos/GetEvents.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET_EVENTS_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject responseObject = jsonArray.getJSONObject(i);

                        String eventName = responseObject.getString("EventName");
                        String eventDesc = responseObject.getString("EventDesc");
                        String eventType = responseObject.getString("EventType");
                        String eventDate = responseObject.getString("EventDate");
                        String eventPlace = responseObject.getString("EventPlace");
                        String eventLocX = responseObject.getString("EventLocationX");
                        String eventLocY = responseObject.getString("EventLocationY");
                        boolean validated = responseObject.getBoolean("Validated");

//                        EventData event = new EventData(userId, eventName, eventDesc, eventType, eventDate, eventPlace, eventLocX, eventLocY, validated);
//
//                        EVENTS_LIST.add(event);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: ", error.getMessage());
                        //pDialog.dismiss();

                    }
                });

        return EVENTS_LIST;
    }

}
