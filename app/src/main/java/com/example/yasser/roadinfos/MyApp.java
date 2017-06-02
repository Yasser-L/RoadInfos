package com.example.yasser.roadinfos;

import android.app.Application;
import android.content.Context;
import android.util.Log;
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

import static com.example.yasser.roadinfos.ServiceData.service;

/**
 * Created by Yasser on 21/05/2017.
 */

public class MyApp extends Application{

    public static List<EventData> EVENTS_GLOBAL = new ArrayList<>();

    public static User currentUser;

    public static ServiceData CURRENT_SERVICE;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        MyApp.context = getApplicationContext();

        // Your methods here...

        EVENTS_GLOBAL = GetEvents(1);
        CURRENT_SERVICE = GetService(1);

    }

    public static Context GetGlobalContext(){
        return MyApp.context;
    }


    public static List<EventData> GetEvents(final int userId) {
        Log.d("MyAPP", "GetEvents: Entered");

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

                                EVENTS_GLOBAL.add(event);
                            }}catch (JSONException e){
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                        Log.d("Mmessage", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog

                        //Showing toast
                        Log.d("MmessageError", volleyError.getMessage());
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


        return EVENTS_GLOBAL;
    }


    public static ServiceData GetService(final int userId) {

        Log.d("MyAPP", "Get service: Entered");

        final String[] res = new String[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //Showing toast message of the response
                        res[0] = s;
                        Log.d("Rresponse", res[0]);
                        try {
//
                            JSONArray array = new JSONArray(res[0]);
                            for (int i = 0; i < array.length(); i++) {
                                Log.d("Inside jsonarray", "Entered");

                                JSONObject responseObject = array.getJSONObject(i);

                                String serviceId = responseObject.getString("Id");
                                String serviceName = responseObject.getString("ServiceName");
                                String serviceDesc = responseObject.getString("ServiceDesc");
                                String serviceType = responseObject.getString("ServiceType");
                                String serviceDate = responseObject.getString("ServiceDate");
                                String servicePlace = responseObject.getString("ServicePlace");
                                String serviceLocX = responseObject.getString("ServiceLocationX");
                                String serviceLocY = responseObject.getString("ServiceLocationY");
                                String validated = responseObject.getString("Validated");

                                service = new ServiceData(Integer.parseInt(serviceId), userId, serviceName, serviceDesc, serviceType, serviceDate, servicePlace, serviceLocX, serviceLocY, validated);

                            }
                        }catch (JSONException e){
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                        Toast.makeText(context, "Success (line 170)", Toast.LENGTH_LONG).show();
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
                params.put("userId", userId+"");

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


        return service;
    }

}
