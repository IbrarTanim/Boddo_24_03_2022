package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveChat {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("unseen_message")
    @Expose
    private int unseenMessage;
    @SerializedName("chat_list")
    @Expose
    private List<ChatList> chatList = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUnseenMessage() {
        return unseenMessage;
    }

    public void setUnseenMessage(int unseenMessage) {
        this.unseenMessage = unseenMessage;
    }

    public List<ChatList> getChatList() {
        return chatList;
    }

    public void setChatList(List<ChatList> chatList) {
        this.chatList = chatList;
    }

    public class ChatList {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("profile_photo")
        @Expose
        private String profilePhoto;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("conversation_id")
        @Expose
        private String conversationId;
        @SerializedName("is_seen")
        @Expose
        private String isSeen;
        @SerializedName("last_message_time")
        @Expose
        private String lastMessageTime;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getConversationId() {
            return conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public String getIsSeen() {
            return isSeen;
        }

        public void setIsSeen(String isSeen) {
            this.isSeen = isSeen;
        }

        public String getLastMessageTime() {
            return lastMessageTime;
        }

        public void setLastMessageTime(String lastMessageTime) {
            this.lastMessageTime = lastMessageTime;
        }

    }
}

