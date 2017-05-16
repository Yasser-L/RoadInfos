package com.example.yasser.roadinfos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddEvent extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private Button take_picture, new_folder, add_event;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView event_image, event_image_2, event_image_3, event_image_4;
//    private TextView EIText, EIText2, EIText3;
    private SliderLayout event_images_slider;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    String externalDirectory= Environment.getExternalStorageDirectory().toString();
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "RoadInfos";

    //
    static File Photos_Directory;

    private Uri fileUri; // file url to store image/video

    static File mediaFile = null;

    private String UPLOAD_URL ="http://192.168.1.2/RoadInfos/UploadImages.php";
    private String UPLOADS_FOLDER ="http://localhost/RoadInfos/Uploads/";

//    private String KEY_IMAGE = "image";


    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        take_picture = (Button) findViewById(R.id.takePicture);
        add_event = (Button) findViewById(R.id.confirmAddEvent);
        new_folder = (Button) findViewById(R.id.newFolderTest);
        event_image = (ImageView) findViewById(R.id.eventImage);
        event_image_2 = (ImageView) findViewById(R.id.eventImage2);
        event_image_3 = (ImageView) findViewById(R.id.eventImage3);
        event_image_4 = (ImageView) findViewById(R.id.eventImage4);
//        EIText = (TextView) findViewById(R.id.eventImageText);
        event_images_slider = (SliderLayout) findViewById(R.id.eventImagesSlider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("S.O.S", R.drawable.breakdown_alert);
        file_maps.put("Navigate", R.drawable.navigate_the_map);
        file_maps.put("Search", R.drawable.search_event);
        file_maps.put("Last", R.drawable.last_events);

//        for(String name : file_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(this);
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(file_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            event_images_slider.addSlider(textSliderView);
//        }



        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();
                SetEventImages();

            }
        });

        new_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (File image : GetEventImages()) {
//                    Toast.makeText(getApplicationContext(), image.toString(), Toast.LENGTH_LONG).show();
//                }
//                SetImages();

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);

            }
        });

        final Bitmap[] bm = new Bitmap[1];
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event_image.getDrawable() == null && event_image_2.getDrawable() == null && event_image_3.getDrawable() == null
                        && event_image_4.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), "No images!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (event_image.getDrawable() != null){
                    bm[0] = ((BitmapDrawable)event_image.getDrawable()).getBitmap();
                    UploadImage(bm[0], 1);
                }
                if (event_image_2.getDrawable() != null){
                    bm[0] = ((BitmapDrawable)event_image_2.getDrawable()).getBitmap();
                    UploadImage(bm[0], 1);
                }
                if (event_image_3.getDrawable() != null){
                    bm[0] = ((BitmapDrawable)event_image_3.getDrawable()).getBitmap();
                    UploadImage(bm[0], 1);
                }
                if (event_image_4.getDrawable() != null){
                    bm[0] = ((BitmapDrawable)event_image_4.getDrawable()).getBitmap();
                    UploadImage(bm[0], 1);
                }
            }
        });


    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {


                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
//                EIText.setText(filePath);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                options.outWidth = 100;
                options.outHeight = 200;

                if (event_image.getDrawable() == null) {
                    event_image.setImageBitmap(BitmapFactory.decodeFile(filePath, options));
//                        Picasso.with(getApplicationContext()).load(UPLOADS_FOLDER+"Event1").into(event_image);
                }
                else if (event_image_2.getDrawable() == null){
                    event_image_2.setImageBitmap(BitmapFactory.decodeFile(filePath, options));
                }
                else if (event_image_3.getDrawable() == null){
                    event_image_3.setImageBitmap(BitmapFactory.decodeFile(filePath, options));
                }
                else if (event_image_4.getDrawable() == null){
                    event_image_4.setImageBitmap(BitmapFactory.decodeFile(filePath, options));
                }
                else
                    Toast.makeText(getApplicationContext(), "You can't add more pictures!", Toast.LENGTH_LONG).show();

            }

    }



    /*
    * Capturing Camera Image will lauch camera app requrest image capture
    */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    /*
            * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview


            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 4;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            if (event_image.getDrawable() == null) {
                event_image.setImageBitmap(bitmap);
            }
            else if (event_image_2.getDrawable() == null){
                event_image_2.setImageBitmap(bitmap);
            }
            else if (event_image_3.getDrawable() == null){
                event_image_3.setImageBitmap(bitmap);
            }
            else if (event_image_4.getDrawable() == null){
                event_image_4.setImageBitmap(bitmap);
            }
            else
                Toast.makeText(getApplicationContext(), "You can't add more pictures!", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /*
     * ------------ Helper Methods ----------------------
     * */




    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }



    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Getting images in the event folder
     */
    public File[] GetEventImages() {
        File f = new File(externalDirectory + "/" + IMAGE_DIRECTORY_NAME +"/Pictures/Event");
        return f.listFiles();
    }

    /**
     * Setting images in the slider
     */
    public void SetEventImages() {
        for (File picture: GetEventImages()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(picture.getName())
                    .image(picture)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra","Event");

            event_images_slider.addSlider(textSliderView);
        }
    }

    /**
     * Setting images in the slider
     */
    public void SetImages()  {
        File[] images = GetEventImages();
        Bitmap img1 = BitmapFactory.decodeFile(images[0].getPath());
        Bitmap img2 = BitmapFactory.decodeFile(images[1].getPath());
        Bitmap img3 = BitmapFactory.decodeFile(images[2].getPath());
//        OutputStream stream1 = null;
//        img1.compress(Bitmap.CompressFormat.PNG, 10, stream1);
        int w = img1.getWidth();
        Log.d("Weight", w+"");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.outWidth = 100;
        options.outHeight = 200;

//        BitmapFactory.decodeStream()

        event_image.setImageBitmap(BitmapFactory.decodeFile(images[0].getPath(), options));
        event_image_2.setImageBitmap(BitmapFactory.decodeFile(images[1].getPath(), options));
        event_image_3.setImageBitmap(BitmapFactory.decodeFile(images[2].getPath(), options));
        event_image_4.setImageBitmap(BitmapFactory.decodeFile(images[3].getPath(), options));
    }



    /*
     * returning image / video
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        Photos_Directory = new File(externalDirectory + "/" + IMAGE_DIRECTORY_NAME +"/Pictures/Event");
//        Photos_Directory.mkdirs();

        // Create the storage directory if it does not exist
        if (!Photos_Directory.exists()) {
            if (!Photos_Directory.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed to create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".png");
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
//        } else {
//            return null;
//        }

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(Photos_Directory.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");

        } else {
            return null;
        }

        return mediaFile;
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }



    private void UploadImage(final Bitmap bitmap, final int imageNumber){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), "Success: " +s, Toast.LENGTH_LONG).show();
                        Log.d("messg",s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(), "eeee: " +volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);


                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("name", "Event1_Image"+imageNumber);
                params.put("image", image);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }





    String RandomId (){
        return UUID.randomUUID().toString();
    }




    ///////////////////////////////////////////////
    ///                 TEMPORARY              ///
    //////////////////////////////////////////////


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.yasser.roadinfos.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private boolean createDirectoryAndSaveFile(Bitmap imageToSave, String fileName, String dirName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/" + dirName);

        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getPath() +"/" + dirName + "/");
            return wallpaperDirectory.mkdirs();
        }

        File file = new File(new File(Environment.getExternalStorageDirectory().getPath() +"/" + dirName + "/"), fileName);
        if (file.exists()) {
            return file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    /*
     * ------------ Gallery Methods ----------------------
     * */



    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
