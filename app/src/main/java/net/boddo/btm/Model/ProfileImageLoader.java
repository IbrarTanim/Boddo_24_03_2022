package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileImageLoader {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("profileImageInfo")
    @Expose
    private List<ProfileImageInfo> profileImageInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProfileImageInfo> getProfileImageInfo() {
        return profileImageInfo;
    }

    public void setProfileImageInfo(List<ProfileImageInfo> profileImageInfo) {
        this.profileImageInfo = profileImageInfo;
    }

    public class ProfileImageInfo {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("action_at")
        @Expose
        private Object actionAt;
        @SerializedName("serial")
        @Expose
        private String serial;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Object getActionAt() {
            return actionAt;
        }

        public void setActionAt(Object actionAt) {
            this.actionAt = actionAt;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

    }


}
