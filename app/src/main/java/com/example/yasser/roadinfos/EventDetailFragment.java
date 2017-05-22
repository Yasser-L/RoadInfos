package com.example.yasser.roadinfos;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yasser.roadinfos.dummy.DummyContent;

import static com.example.yasser.roadinfos.EventData.GetEvents;

/**
 * A fragment representing a single EventData detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "eventId";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private EventData eventData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getInt());

//            eventData = GetEvents(getActivity(), 1).get((int)getArguments().get(ARG_ITEM_ID));

            EventListActivity activityForList = new EventListActivity();
            eventData = GetEvents(getActivity().getApplicationContext(), 1).get((int)getArguments().get(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(eventData.getEventName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (eventData != null) {
            ((TextView) rootView.findViewById(R.id.event_detail)).setText(eventData.getEventName());

        }

        return rootView;
    }
}
