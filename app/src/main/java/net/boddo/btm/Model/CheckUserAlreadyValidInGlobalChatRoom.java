package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUserAlreadyValidInGlobalChatRoom {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_subscribed")
    @Expose

    private String isSubscribed;
    @SerializedName("is_valid")
    @Expose
    private String isValid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
