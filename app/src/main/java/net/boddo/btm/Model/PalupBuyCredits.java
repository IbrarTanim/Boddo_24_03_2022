package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PalupBuyCredits {
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("purchased_pp")
    @Expose
    private String purchasedPp;
    @SerializedName("purchased_pp_cost")
    @Expose
    private String purchasedPpCost;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurchasedPp() {
        return purchasedPp;
    }

    public void setPurchasedPp(String purchasedPp) {
        this.purchasedPp = purchasedPp;
    }

    public String getPurchasedPpCost() {
        return purchasedPpCost;
    }

    public void setPurchasedPpCost(String purchasedPpCost) {
        this.purchasedPpCost = purchasedPpCost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
