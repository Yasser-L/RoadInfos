package com.example.yasser.roadinfos;

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

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Yasser on 16/05/2017.
 */

public class ServiceData {

    private int Id, UserId;
    private String  ServiceName, ServiceDesc, ServiceType, ServiceDate, ServicePlace, ServiceLocX, ServiceLocY, Validated;
    private static Context context;

    public static ServiceData service = null;


//    static {
//        SERVICES_LIST = GetServices(MyApp.GetGlobalContext(), 1);
//    }

//    public ServiceData(Context context){
//        this.context = context;
//    }

    public ServiceData(int id, int userId, String eventName, String eventDesc, String eventType, String eventDate, String eventPlace, String eventLocX, String eventLocY, String validated) {
        Id = id;
        UserId = userId;
        ServiceName = eventName;
        ServiceDesc = eventDesc;
        ServiceType = eventType;
        ServiceDate = eventDate;
        ServicePlace = eventPlace;
        ServiceLocX = eventLocX;
        ServiceLocY = eventLocY;
        Validated = validated;
    }


    public int getId() {
        return Id;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String eventName) {
        ServiceName = eventName;
    }

    public String getServiceDesc() {
        return ServiceDesc;
    }

    public void setServiceDesc(String eventDesc) {
        ServiceDesc = eventDesc;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String eventType) {
        ServiceType = eventType;
    }

    public String getServiceDate() {
        return ServiceDate;
    }

    public void setServiceDate(String eventDate) {
        ServiceDate = eventDate;
    }

    public String getServicePlace() {
        return ServicePlace;
    }

    public void setServicePlace(String eventPlace) {
        ServicePlace = eventPlace;
    }

    public String getServiceLocX() {
        return ServiceLocX;
    }

    public void setServiceLocX(String eventLocX) {
        ServiceLocX = eventLocX;
    }

    public String getServiceLocY() {
        return ServiceLocY;
    }

    public void setServiceLocY(String eventLocY) {
        ServiceLocY = eventLocY;
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

    public static ServiceData GetService(final Context context, final int userId) {

        Log.d("GetService", "Entered");

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


        return service;
    }

}
