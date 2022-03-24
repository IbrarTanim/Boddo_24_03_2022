package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatRequest {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("requested_message")
    @Expose
    private List<RequestedMessage> requestedMessage = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RequestedMessage> getRequestedMessage() {
        return requestedMessage;
    }
    public class RequestedMessage {

        @SerializedName("user_one")
        @Expose
        private String userOne;
        @SerializedName("last_message")
        @Expose
        private String lastMessage;
        @SerializedName("last_message_time")
        @Expose
        private String lastMessageTime;
        @SerializedName("profile_photo")
        @Expose
        private String profilePhoto;
        @SerializedName("first_name")
        @Expose
        private String firstName;

        public String getUserOne() {
            return userOne;
        }

        public void setUserOne(String userOne) {
            this.userOne = userOne;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getLastMessageTime() {
            return lastMessageTime;
        }

        public void setLastMessageTime(String lastMessageTime) {
            this.lastMessageTime = lastMessageTime;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

    }
}
