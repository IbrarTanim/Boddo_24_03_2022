package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Love {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isLoved")
    @Expose
    private String isLoved;
    @SerializedName("isMatched")
    @Expose
    private String isMatched;

//    @SerializedName("message")
//    @Expose
//    private String matchedMessage;
//
//    public String getMatchedMessage() {
//        return matchedMessage;
//    }
//
//    public void setMatchedMessage(String matchedMessage) {
//        this.matchedMessage = matchedMessage;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsLoved() {
        return isLoved;
    }

    public void setIsLoved(String isLoved) {
        this.isLoved = isLoved;
    }

    public String getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(String isMatched) {
        this.isMatched = isMatched;
    }
}
