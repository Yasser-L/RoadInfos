package com.example.yasser.roadinfos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class EventsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Event> EVENTS = new ArrayList<Event>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Event> ITEM_MAP = new HashMap<String, Event>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createEvent(i, "Event "+i, "Type "+i));
        }
    }

    private static void addItem(Event item) {
        EVENTS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Event createEvent(int position, String title, String type) {
        return new Event(String.valueOf(position), title, makeDetails(position), type);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * Event item
     */
    public static class Event {
        public final String id;
        public final String title;
        public final String details;
        public final String type;
        public final List<File> images = new ArrayList<>();

        public Event(String id, String title, String details, String type) {
            this.id = id;
            this.title = title;
            this.details = details;
            this.type = type;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
