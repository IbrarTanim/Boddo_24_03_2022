package net.boddo.btm.Event;

public class ChatRoomEvent {

    private String chatRoomType;
    private String messageBody;
    private String userId;
    private String userPhoto;
    private String message;
    private String eventType;
    private String eventName;

    public ChatRoomEvent(String chatRoomType, String messageBody, String userId, String userPhoto,String message) {
        this.chatRoomType = chatRoomType;
        this.messageBody = messageBody;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.message = message;
    }

    public ChatRoomEvent(String eventName, String eventType){
        this.eventType = eventType;
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getChatRoomType() {
        return chatRoomType;
    }

    public void setChatRoomType(String chatRoomType) {
        this.chatRoomType = chatRoomType;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
