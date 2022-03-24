package net.boddo.btm.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoBlog implements Parcelable {

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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("action_at")
    @Expose
    private String actionAt;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("is_suspended")
    @Expose
    private String isSuspended;
    @SerializedName("views")
    @Expose
    private String views;

    @SerializedName("is_like")
    private Integer isPhotoLikedByMe;


    private List<PhotoBlog> photoblogList;

    protected PhotoBlog(Parcel in) {
        id = in.readString();
        userId = in.readString();
        photo = in.readString();
        description = in.readString();
        like = in.readString();
        comment = in.readString();
        matched = in.readString();
        status = in.readString();
        createdAt = in.readString();
        actionAt = in.readString();
        firstName = in.readString();
        userName = in.readString();
        profilePhoto = in.readString();
        gender = in.readString();
        dateOfBirth = in.readString();
        isSuspended = in.readString();
        views = in.readString();
        if (in.readByte() == 0) {
            isPhotoLikedByMe = null;
        } else {
            isPhotoLikedByMe = in.readInt();
        }
        photoblogList = in.createTypedArrayList(PhotoBlog.CREATOR);
    }

    public static final Creator<PhotoBlog> CREATOR = new Creator<PhotoBlog>() {
        @Override
        public PhotoBlog createFromParcel(Parcel in) {
            return new PhotoBlog(in);
        }

        @Override
        public PhotoBlog[] newArray(int size) {
            return new PhotoBlog[size];
        }
    };

    public List<PhotoBlog> getPhotoblogList() {
        return photoblogList;
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

    public String getActionAt() {
        return actionAt;
    }

    public void setActionAt(String actionAt) {
        this.actionAt = actionAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
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

    public String getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(String isSuspended) {
        this.isSuspended = isSuspended;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public Integer getIsPhotoLikedByMe() {
        return isPhotoLikedByMe;
    }

    public void setIsPhotoLikedByMe(Integer isPhotoLikedByMe) {
        this.isPhotoLikedByMe = isPhotoLikedByMe;
    }

    public void setPhotoblogList(List<PhotoBlog> photoblogList) {
        this.photoblogList = photoblogList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(photo);
        dest.writeString(description);
        dest.writeString(like);
        dest.writeString(comment);
        dest.writeString(matched);
        dest.writeString(status);
        dest.writeString(createdAt);
        dest.writeString(actionAt);
        dest.writeString(firstName);
        dest.writeString(userName);
        dest.writeString(profilePhoto);
        dest.writeString(gender);
        dest.writeString(dateOfBirth);
        dest.writeString(isSuspended);
        dest.writeString(views);
        if (isPhotoLikedByMe == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isPhotoLikedByMe);
        }
        dest.writeTypedList(photoblogList);
    }


    @Override
    public String toString() {
        return "PhotoBlog{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", photo='" + photo + '\'' +
                ", description='" + description + '\'' +
                ", like='" + like + '\'' +
                ", comment='" + comment + '\'' +
                ", matched='" + matched + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", actionAt='" + actionAt + '\'' +
                ", firstName='" + firstName + '\'' +
                ", userName='" + userName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", isSuspended='" + isSuspended + '\'' +
                ", views='" + views + '\'' +
                ", isPhotoLikedByMe=" + isPhotoLikedByMe +
                ", photoblogList=" + photoblogList +
                '}';
    }
}