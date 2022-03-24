package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorite {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("favorite")
    @Expose
    private String isFavorite;

    public Favorite(String status, String isFavorite) {
        this.status = status;
        this.isFavorite = isFavorite;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }
}
