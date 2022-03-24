package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class AllComments {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("comment")
    @Expose
    private List<Comment> comment = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }



    public class Comment {
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("profile_photo")
        @Expose
        private String profilePhoto;
        @SerializedName("comment")
        @Expose
        private String comment;

        @SerializedName("user_name")
        @Expose
        private String userName;

        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

    }

}
