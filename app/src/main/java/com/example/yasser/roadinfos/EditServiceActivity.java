package com.example.yasser.roadinfos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EditServiceActivity extends Fragment {

    private ServiceData service;
    List<String> service_images_list = new ArrayList<>();
    DetailGalleryAdapter adapter;
    EditText sd_title, sd_type, sd_place, sd_desc;
    RecyclerView sd_images;
    Button newLocation, addImages, saveChanges;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_edit_service, container, false);

        sd_title = (EditText) rootView.findViewById(R.id.serviceEditName);
        sd_desc = (EditText) rootView.findViewById(R.id.serviceEditDesc);

        newLocation = (Button) rootView.findViewById(R.id.serviceEditLocation);
        addImages = (Button) rootView.findViewById(R.id.serviceEditAddImages);
        saveChanges = (Button) rootView.findViewById(R.id.serviceEditSave);


        sd_images = (RecyclerView) rootView.findViewById(R.id.serviceEditImagesRV);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
        sd_images.setLayoutManager(manager);

        int serviceId = 1;
        service_images_list = DownloadImages(serviceId);

        adapter = new DetailGalleryAdapter(getActivity(), service_images_list);
        sd_images.setAdapter(adapter);

        return rootView;
    }



    ////////////////////////
    //// HELPER METHODS ////
    ////////////////////////


    private boolean UpdateService (final int serviceId, final String sName, final String sDesc,
                                   final String sType, final String sPlace, final String sLocX, final String sLocY){

//        final ProgressDialog progressDialog = ProgressDialog.show(ServiceDetailActivity.this, "Fetching the event's images",
//                "Please wait...", false, false);

        final boolean[] success = {false};
        final String[] res = new String[1];


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_SERVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Showing toast message of the response
//                        Toast.makeText(getApplicationContext(), "Success: " +s, Toast.LENGTH_LONG).show();
                        Log.d("messg",s);
//                        if (s.equals("success"))
                        success[0] = true;

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
                params.put("serviceId", Integer.toString(serviceId));
                params.put("sName", sName);
                params.put("sDesc", sDesc);
                params.put("sType", sType);
                params.put("sPlace", sPlace);
                params.put("sLocX", sLocX);
                params.put("sLocY", sLocY);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

//        adapter.notifyDataSetChanged();

        return success[0];

    }


    private List<String> DownloadImages (final int serviceId){

//        final ProgressDialog progressDialog = ProgressDialog.show(ServiceDetailActivity.this, "Fetching the event's images",
//                "Please wait...", false, false);

        final String[] res = new String[1];


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DOWNLOAD_SERVICE_IMAGES,
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
                                service_images_list.add(path);
                                adapter.notifyDataSetChanged();
//                                progressDialog.dismiss();

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
                params.put("serviceId", "1");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

//        adapter.notifyDataSetChanged();

        return service_images_list;

    }


}
