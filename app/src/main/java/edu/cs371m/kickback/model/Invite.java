package edu.cs371m.kickback.model;

public class Invite {
    private String eventId;
    private boolean viewed;

    public Invite() {}

    public Invite(String eventId) {
        this.eventId = eventId;
        this.viewed = false;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean getViewed() {
        return viewed;
    }
}
