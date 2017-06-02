package com.example.yasser.roadinfos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayOutputStream;

/**
 * Created by Yasser on 24/05/2017.
 */

    /**
     * Sketch Project Studio
     * Created by Angga on 12/04/2016 14.27.
     */
    public class AppHelper {

        /**
         * Turn drawable resource into byte array.
         *
         * @param context parent context
         * @param id      drawable resource id
         * @return byte array
         */
        public static byte[] getFileDataFromDrawable(Context context, int id) {
            Drawable drawable = ContextCompat.getDrawable(context, id);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }

        /**
         * Turn drawable into byte array.
         *
         * @param drawable data
         * @return byte array
         */
        public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }


        public static byte[] getFileDataFromBitmap(Context context, Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }


        public static String GetFileName(String path){
            return path.substring(path.lastIndexOf("/")+1);
        }


        /**
         Current Activity instance will go through its lifecycle to onDestroy() and a new instance then created after it.
         */
        @SuppressLint("NewApi")
        public static final void recreateActivityCompat(final Activity a) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                a.recreate();
            } else {
                final Intent intent = a.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.finish();
                a.overridePendingTransition(0, 0);
                a.startActivity(intent);
                a.overridePendingTransition(0, 0);
            }
        }


    }

