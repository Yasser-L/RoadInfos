package com.example.yasser.roadinfos;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.example.yasser.roadinfos.MyApp.CURRENT_SERVICE;

public class ServiceDetailActivity extends Fragment {

    private ServiceData service;
    List<String> service_images_list = new ArrayList<>();
    DetailGalleryAdapter adapter;
    TextView sd_title, sd_type, sd_place, sd_desc;
    ImageView type_icon;
    RecyclerView sd_images;
    CustomRatingBar ratingBar;
    boolean recreated = true;
//    SharedPreferences preferences = getSharedPreferences("ActivityPREF", MODE_PRIVATE);;

    String externalDirectory= Environment.getExternalStorageDirectory().toString();
    private static final String IMAGE_DIRECTORY_NAME = "RoadInfos";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_service_detail, container, false);

        // set an enter transition
//        getWindow().setEnterTransition(new Slide());
//        // set an exit transition
//        getWindow().setExitTransition(new Slide());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1000ms

            }
        }, 1000);



//        Window window = ServiceDetailActivity.this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(ContextCompat.getColor(ServiceDetailActivity.this, R.color.colorPrimaryDark));

        sd_title = (TextView) rootView.findViewById(R.id.serviceDetailTitle);
        sd_type = (TextView) rootView.findViewById(R.id.serviceDetailType);
        sd_place = (TextView) rootView.findViewById(R.id.serviceDetailPlace);
        sd_desc = (TextView) rootView.findViewById(R.id.serviceDetailDesc);

        type_icon = (ImageView) rootView.findViewById(R.id.serviceTypeIcon);

        ratingBar = (CustomRatingBar) rootView.findViewById(R.id.serviceRatingBar);

        sd_images = (RecyclerView) rootView.findViewById(R.id.serviceDetailImagesRV);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
        sd_images.setLayoutManager(manager);

        service = CURRENT_SERVICE;

        if (service != null)
        {
            sd_title.setText(service.getServiceName());
            sd_type.setText("Type: " + service.getServiceType());
            sd_place.setText("Location: " + service.getServicePlace());
            sd_desc.setText(service.getServiceDesc());

            switch (service.getServiceType()){
                case "Car Towing":
                    type_icon.setImageResource(R.drawable.car_towing);
                    break;
                case "Car mechanic":
                    type_icon.setImageResource(R.drawable.mechanic);
                    break;
                case "Vulcanizer":
                    type_icon.setImageResource(R.drawable.vulcanizer);
                    break;
                case "Auto Electrician":
                    type_icon.setImageResource(R.drawable.electrician);
                    break;
                case "Car Wash":
                    type_icon.setImageResource(R.drawable.car_wash);
                    break;
                case "Car Parts":
                    type_icon.setImageResource(R.drawable.car_parts);
                    break;
                case "Car Accessories":
                    type_icon.setImageResource(R.drawable.car_accessories);
                    break;
                default:
                    type_icon.setImageResource(R.drawable.engine);
            }

//            toolbar.setTitle(service.getServiceName());
        }
        else
            Toast.makeText(getActivity(), "Service not found", Toast.LENGTH_SHORT).show();


        int serviceId = 1;
        service_images_list = DownloadImages(serviceId);

        adapter = new DetailGalleryAdapter(getActivity(), service_images_list);
        sd_images.setAdapter(adapter);

        ratingBar.getOnScoreChanged();

        return rootView;

    }



                                    ////////////////////////
                                    //// HELPER METHODS ////
                                    ////////////////////////



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
