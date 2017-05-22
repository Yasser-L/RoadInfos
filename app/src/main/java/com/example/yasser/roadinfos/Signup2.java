package com.example.yasser.roadinfos;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yasser on 11/03/2017.
 */
public class Signup2 extends AppCompatActivity{

    public final int PICK_CONTACT = 2017;
    View ECLayout;
    Button testButton;
    ImageButton removeContact;
    TextView c_name, c_number;
    RecyclerView ECRecyclerView;
    ExpandableListView ECListView;
    ContactsAdapter contactsAdapter;
//    ContactsListviewAdapter adapter;
    List<CheckBox> cases;
    List<PhoneContact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_2);

        contactsAdapter = new ContactsAdapter(contactList);

        ECLayout = findViewById(R.id.ECLayout);
        c_name = (TextView) findViewById(R.id.contact_name);
        c_number = (TextView) findViewById(R.id.contact_number);
        View contactRow = findViewById(R.id.contactRow);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ECRecyclerView = (RecyclerView) findViewById(R.id.ECRecyclerView);
        RecyclerView.LayoutManager contactsLM = new LinearLayoutManager(getApplicationContext());
        ECRecyclerView.setLayoutManager(contactsLM);
        ECRecyclerView.setItemAnimator(new DefaultItemAnimator());

        HashMap<PhoneContact, List<CheckBox>> numbers = new HashMap<>();
//        adapter = new ContactsListviewAdapter(contactList, getApplicationContext());

        ECListView = (ExpandableListView) findViewById(R.id.ECListView);
//        ECListView.setAdapter(adapter);




        testButton = (Button) findViewById(R.id.noEC);


        (findViewById(R.id.yesEC)).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
                ECRecyclerView.setVisibility(View.VISIBLE);
                ECRecyclerView.setAdapter(contactsAdapter);
//                ECListView.setAdapter(adapter);
            }
        });


        (findViewById(R.id.noEC)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < contactList.size(); i++) {
                    RecyclerView.ViewHolder holder = ECRecyclerView.findViewHolderForAdapterPosition(i);
                    int g = holder.getAdapterPosition();

                }


                List<PhoneWithEMCase> phoneWithEMCases = contactsAdapter.phoneWithEMCases;
                for (int i = 0; i<phoneWithEMCases.size(); i++) {
                    String phone = phoneWithEMCases.get(i).getPhoneNumber();
                    List<Boolean> checkedCases = phoneWithEMCases.get(i).getEmCases();
                    Toast.makeText(getApplicationContext(), "Phone " + i + ", " + phone + ", " + checkedCases.toString(), Toast.LENGTH_SHORT).show();
                }


//                if(ECRecyclerView.getVisibility() == View.VISIBLE){
//                ECRecyclerView.setVisibility(View.GONE);
//                }
//                else {
//                    ECRecyclerView.setVisibility(View.VISIBLE);
//                }

//

            }
        });


    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
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


    static final int REQUEST_TAKE_PHOTO = 1;

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ECLayout = findViewById(R.id.ECLayout);
        c_name = (TextView) findViewById(R.id.contact_name);
        c_number = (TextView) findViewById(R.id.contact_number);

        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

            while (cursor.moveToNext()){
                int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                PhoneContact contact = new PhoneContact(cursor.getString(nameColumn), cursor.getString(numberColumn));

                contactList.add(contact);

//                contactsAdapter.notifyItemInserted(cursor.getPosition());
            }

            Log.d("contactList ", contactList.toString());
//
            contactsAdapter.notifyDataSetChanged();
//            adapter.notifyDataSetChanged();
            ECRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
