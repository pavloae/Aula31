package com.nablanet.aula31.repo.query;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.core.view.Event;

public class EventRecord {

    private DataSnapshot snapshot;
    private Event.EventType eventType;
    private String previousChild;

    public EventRecord(DataSnapshot snapshot, Event.EventType eventType, String previousChild) {
        this.snapshot = snapshot;
        this.eventType = eventType;
        this.previousChild = previousChild;
    }

    public DataSnapshot getSnapshot() {
        return snapshot;
    }

    public Event.EventType getEventType() {
        return eventType;
    }

    public String getPreviousChild() {
        return previousChild;
    }

    @NonNull
    @Override
    public String toString() {
        return "Event: " + eventType + " at " + snapshot.getRef().toString();
    }

}
