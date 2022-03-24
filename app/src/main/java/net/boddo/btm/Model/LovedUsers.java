package net.boddo.btm.Model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.boddo.btm.Utills.Response;
public class LovedUsers extends Response implements Parcelable {

    @SerializedName("profile_photo")
    @Expose
    private String profileImage;

    @SerializedName("user_id")
    @Expose
    private String userId;

    protected LovedUsers(Parcel in) {
        profileImage = in.readString();
        userId = in.readString();
    }
    public static final Creator<LovedUsers> CREATOR = new Creator<LovedUsers>() {
        @Override
        public LovedUsers createFromParcel(Parcel in) {
            return new LovedUsers(in);
        }

        @Override
        public LovedUsers[] newArray(int size) {
            return new LovedUsers[size];
        }
    };

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileImage);
        dest.writeString(userId);
    }
}
