
package net.boddo.btm.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Blocklist {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("blocked_user_id")
    @Expose
    private String blockedUserId;
    @SerializedName("block_by")
    @Expose
    private String blockBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockedUserId(String blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public String getBlockBy() {
        return blockBy;
    }

    public void setBlockBy(String blockBy) {
        this.blockBy = blockBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}