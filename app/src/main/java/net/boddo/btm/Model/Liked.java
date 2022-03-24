package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.Nullable;

public class Liked {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("likedMe")
    @Expose
    private List<LikedMe> likedMe = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LikedMe> getLikedMe() {
        return likedMe;
    }

    public void setLikedMe(List<LikedMe> likedMe) {
        this.likedMe = likedMe;
    }

    @Override
    public String toString() {
        return "Liked{" +
                "status='" + status + '\'' +
                ", likedMe=" + likedMe +
                '}';
    }

    public class LikedMe {

        @Nullable
        @SerializedName("is_seen")
        @Expose
        private String isSeen;
        @SerializedName("user_id")
        @Expose
        private String userId;

        @SerializedName("user_name")
        @Expose
        private String name;

        @SerializedName("gender")
        @Expose
        private String gender;

        @SerializedName("date_of_birth")
        @Expose
        private String dateOfBirth;

        @SerializedName("profile_photo")
        @Expose
        private String profilePhoto;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getIsSeen() {
            return isSeen;
        }

        public void setIsSeen(String isSeen) {
            this.isSeen = isSeen;
        }

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

        @Override
        public String toString() {
            return "LikedMe{" +
                    "isSeen='" + isSeen + '\'' +
                    ", userId='" + userId + '\'' +
                    ", name='" + name + '\'' +
                    ", gender='" + gender + '\'' +
                    ", dateOfBirth='" + dateOfBirth + '\'' +
                    ", profilePhoto='" + profilePhoto + '\'' +
                    '}';
        }
    }
}
