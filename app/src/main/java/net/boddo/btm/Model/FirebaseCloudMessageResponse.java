package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirebaseCloudMessageResponse {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("receiver")
    @Expose
    private String receiver;

    @SerializedName("type")
    @Expose
    private String type;

    public String getOtherUserPhoto() {
        return otherUserPhoto;
    }

    public void setOtherUserPhoto(String otherUserPhoto) {
        this.otherUserPhoto = otherUserPhoto;
    }

    @SerializedName("photo")
    @Expose
    private String otherUserPhoto;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
