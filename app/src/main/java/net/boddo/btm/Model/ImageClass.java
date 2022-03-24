package net.boddo.btm.Model;

import com.google.gson.annotations.SerializedName;

public class ImageClass {
    @SerializedName("title")
    private String imageTitle;

    @SerializedName("image")
    private String image;

    @SerializedName("status")
    private String status;

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
