package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatAppMsgDTO {
    //    public final static String MSG_TYPE_SENT = "MSG_TYPE_SENT";
//    public final static String MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED";
//
//    // Message content.
//    @SerializedName("message")
//    @Expose
//    private String msgContent;
//
//    @SerializedName("msg_type")
//    @Expose
//    // Message type.
//    private String msgType;
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    @SerializedName("status")
//    @Expose
//    // Message type.
//    private String status;
//
//
//
//    public ChatAppMsgDTO(String msgType, String msgContent) {
//        this.msgType = msgType;
//        this.msgContent = msgContent;
//    }
//
//    public String getMsgContent() {
//        return msgContent;
//    }
//
//    public void setMsgContent(String msgContent) {
//        this.msgContent = msgContent;
//    }
//
//    public String getMsgType() {
//        return msgType;
//    }
//
//    public void setMsgType(String msgType) {
//        this.msgType = msgType;
//    }


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("request")
    @Expose
    private String request;

    @SerializedName("messages")
    @Expose
    private List<Message> message = null;

    @SerializedName("message")
    @Expose
    private Message singleMessage = null;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Message getSingleMessage() {
        return singleMessage;
    }

    public void setSingleMessage(Message singleMessage) {
        this.singleMessage = singleMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }


    public ChatAppMsgDTO(List<Message> message) {
        this.message = message;
    }

    public ChatAppMsgDTO(){

    }

    public class Message {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("conversation_id")
        @Expose
        private String conversationId;
        @SerializedName("sender")
        @Expose
        private String sender;
        @SerializedName("receiver")
        @Expose
        private String receiver;

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("created_at")
        @Expose
        private String createdAt;


        public Message(String message) {
            this.message = message;
        }


        @SerializedName("image")
        @Expose
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getConversationId() {
            return conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
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

    }

}
