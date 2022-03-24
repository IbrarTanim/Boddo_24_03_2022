package net.boddo.btm.Event;

public class Event {
    private String eventType;

    private String eventName;

    public Event(String eventType) {
        this.eventType = eventType;
    }
    public Event(String eventName,String eventType){
        this.eventName = eventName;
        this.eventType = eventType;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
