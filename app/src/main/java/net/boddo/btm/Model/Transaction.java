package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("t_id")
    @Expose
    private String tId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("purchased_pp_cost")
    @Expose
    private String purchasedPp;

    public String getPurchasedPp() {
        return purchasedPp;
    }

    public void setPurchasedPp(String purchasedPp) {
        this.purchasedPp = purchasedPp;
    }

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getTId() {
        return tId;
    }

    public void setTId(String tId) {
        this.tId = tId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}