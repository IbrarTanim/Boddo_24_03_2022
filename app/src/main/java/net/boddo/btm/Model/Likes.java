package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Likes {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("AllLikes")
    @Expose
    private List<AllLike> allLikes = null;

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    @SerializedName("is_liked")
    @Expose
    private String isLiked;

    @SerializedName("liked")
    @Expose
    private String totalLiked;

    public String getTotalLiked() {
        return totalLiked;
    }

    public void setTotalLiked(String totalLiked) {
        this.totalLiked = totalLiked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllLike> getAllLikes() {
        return allLikes;
    }

    public void setAllLikes(List<AllLike> allLikes) {
        this.allLikes = allLikes;
    }

    @Override
    public String toString() {
        return "Likes{" +
                "status='" + status + '\'' +
                ", allLikes=" + allLikes +
                ", isLiked='" + isLiked + '\'' +
                ", totalLiked='" + totalLiked + '\'' +
                '}';
    }

    public class AllLike {

        @SerializedName("user_id")
        @Expose
        private String userId;

        @SerializedName("user_name")
        @Expose
        private String userName;

        @SerializedName("first_name")
        @Expose
        private String firstName;


        @SerializedName("profile_photo")
        @Expose
        private String profilePhoto;

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


        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @Override
        public String toString() {
            return "AllLike{" +
                    "userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", profilePhoto='" + profilePhoto + '\'' +
                    '}';
        }
    }
}