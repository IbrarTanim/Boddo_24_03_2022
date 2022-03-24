package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatRoomMessage {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("messages")
    @Expose
    private List<RoomMessage> message = null;

    @SerializedName("message")
    @Expose
    private RoomMessage singleMessage = null;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RoomMessage> getMessage() {
        return message;
    }

    public void setMessage(List<RoomMessage> message) {
        this.message = message;
    }

    public RoomMessage getSingleMessage() {
        return singleMessage;
    }

    public void setSingleMessage(RoomMessage singleMessage) {
        this.singleMessage = singleMessage;
    }


    public class RoomMessage {
        @SerializedName("message_id")
        @Expose
        private String messageId;
        @SerializedName("chat_room_id")
        @Expose
        private String chatRoomId;
        @SerializedName("user_id")
        @Expose
        private String userId;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("is_reported")
        @Expose
        private String isReported;
        @SerializedName("report_message")
        @Expose
        private String reportMessage;

        @SerializedName("profile_photo")
        @Expose
        private String image;

        @SerializedName("user_name")
        @Expose
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @SerializedName("is_admin")
        @Expose
        private String isAdmin;

        @SerializedName("is_banned")
        @Expose
        private String isBanned;

        public String getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getIsBanned() {
            return isBanned;
        }

        public void setIsBanned(String isBanned) {
            this.isBanned = isBanned;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public RoomMessage(String message) {
            this.message = message;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getChatRoomId() {
            return chatRoomId;
        }

        public void setChatRoomId(String chatRoomId) {
            this.chatRoomId = chatRoomId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getIsReported() {
            return isReported;
        }

        public void setIsReported(String isReported) {
            this.isReported = isReported;
        }

        public String getReportMessage() {
            return reportMessage;
        }

        public void setReportMessage(String reportMessage) {
            this.reportMessage = reportMessage;
        }
    }
}
