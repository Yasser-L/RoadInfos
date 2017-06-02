package com.example.yasser.roadinfos;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Yasser on 18/05/2017.
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.GalleryViewHolder> {

    private List<Bitmap> imagesList;
    private Context context;


    public ImageGalleryAdapter(Context context, List<Bitmap> imagesList){
//        super();
        this.imagesList = imagesList;
        this.context = context;
    }

    @Override
    public ImageGalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_card, viewGroup, false);


        return new GalleryViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.GalleryViewHolder viewHolder, final int i) {
//        viewHolder.title.setText(imagesList.get(i).toString());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageBitmap((imagesList.get(i)));

//        Glide.with(context).load(imagesList.get(i)).into(viewHolder.img);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView title;
        private ImageView img, overflow;
        private FloatingActionButton removeImage;

        // Constructor
        public GalleryViewHolder(View view) {
            super(view);

//            title = (TextView)view.findViewById(R.id.album_title);
            img = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);

//        overflow.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){

//            if (v.getId() == overflow.getId()){
//                new AlertDialog.Builder(context)
//                        .setTitle("Remove picture")
//                        .setMessage("Are you sure you want to remove this picture?")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                imagesList.remove(getAdapterPosition());
//                                notifyItemRemoved(getAdapterPosition());
//                                notifyDataSetChanged();
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(context, "Operation canceled.", Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .create()
//                        .show();


//            }

        }


    }

}
