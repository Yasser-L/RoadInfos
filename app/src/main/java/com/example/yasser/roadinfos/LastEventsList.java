package com.example.yasser.roadinfos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.yasser.roadinfos.EventDetailFragment.ARG_ITEM_ID;

public class LastEventsList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_events_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_last_events);
        toolbar.setTitle("Latest events");

    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.d("EventListActivity 107", "Entered");

        Log.d("115", "setupRecyclerView: EventList works");
//        if (comingFrom.equals(""))
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(MyApp.EVENTS_GLOBAL));

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<EventData> mValues = new ArrayList<>();

        public SimpleItemRecyclerViewAdapter(List<EventData> items) {
            Log.d("EventListActivity 117:", "Entered");
//            response();
            if (mValues.size() < 1)
                mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.eventType.setText(mValues.get(position).getEventType());
            holder.eventPlace.setText(mValues.get(position).getEventPlace());
            holder.eventDate.setText(mValues.get(position).getEventDate());

            holder.previewImage.setImageResource(R.drawable.photo_accident);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Context context = v.getContext();
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra(ARG_ITEM_ID, holder.getAdapterPosition());

                        context.startActivity(intent);

                }
            });
        }


        @Override
        public int getItemCount() {
            if (mValues == null) {
                Log.d("mValues size = null", "Entered");
                return 0;
            }
            return mValues.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView eventType;
            public final TextView eventPlace;
            public final TextView eventDate;
            public final ImageView previewImage;
            public EventData mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                eventType = (TextView) view.findViewById(R.id.eventType);
                eventPlace = (TextView) view.findViewById(R.id.eventPlace);
                eventDate = (TextView) view.findViewById(R.id.eventDate);
                previewImage = (ImageView) view.findViewById(R.id.eventPreviewImage);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + eventType.getText() + "'";
            }
        }
    }


}
