package net.boddo.btm.Model;

public class RoomFriendMessageClass {

    private int id;
    private String user_id;
    private String message;
    private String created_at;
    private int type;
    private String status;

    public RoomFriendMessageClass(int id, String user_id, String message, String created_at, int type, String status) {
        this.id = id;
        this.user_id = user_id;
        this.message = message;
        this.created_at = created_at;
        this.type = type;
        this.status = status;
    }

    public RoomFriendMessageClass() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
