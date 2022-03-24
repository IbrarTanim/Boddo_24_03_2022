
package net.boddo.btm.Model;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserPhotoBlogImages implements Parcelable {

    public final static Creator<UserPhotoBlogImages> CREATOR = new Creator<UserPhotoBlogImages>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserPhotoBlogImages createFromParcel(android.os.Parcel in) {
            return new UserPhotoBlogImages(in);
        }

        public UserPhotoBlogImages[] newArray(int size) {
            return (new UserPhotoBlogImages[size]);
        }

    };
    @SerializedName("table_id")
    @Expose
    private String tableId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("like")
    @Expose
    private String like;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("matched")
    @Expose
    private String matched;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("action_at")
    @Expose
    private String actionAt;
    @SerializedName("update_time")
    @Expose
    private String updateTime;

    protected UserPhotoBlogImages(android.os.Parcel in) {
        this.tableId = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.like = ((String) in.readValue((String.class.getClassLoader())));
        this.comment = ((String) in.readValue((String.class.getClassLoader())));
        this.matched = ((String) in.readValue((String.class.getClassLoader())));
        this.views = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.privacy = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.actionAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updateTime = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserPhotoBlogImages() {
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMatched() {
        return matched;
    }

    public void setMatched(String matched) {
        this.matched = matched;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getActionAt() {
        return actionAt;
    }

    public void setActionAt(String actionAt) {
        this.actionAt = actionAt;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(tableId);
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(photo);
        dest.writeValue(description);
        dest.writeValue(like);
        dest.writeValue(comment);
        dest.writeValue(matched);
        dest.writeValue(views);
        dest.writeValue(status);
        dest.writeValue(privacy);
        dest.writeValue(createdAt);
        dest.writeValue(actionAt);
        dest.writeValue(updateTime);
    }

    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return "UserPhotoBlogImages{" +
                "tableId='" + tableId + '\'' +
                ", id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", photo='" + photo + '\'' +
                ", description='" + description + '\'' +
                ", like='" + like + '\'' +
                ", comment='" + comment + '\'' +
                ", matched='" + matched + '\'' +
                ", views='" + views + '\'' +
                ", status='" + status + '\'' +
                ", privacy='" + privacy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", actionAt='" + actionAt + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
