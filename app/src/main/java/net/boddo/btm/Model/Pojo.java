package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pojo {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("login_bonus")
    @Expose
    private String loginBonus;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("request")
    @Expose
    private int request;
    @SerializedName("like")
    @Expose
    private int like;
    @SerializedName("favorite")
    @Expose
    private int favorite;
    @SerializedName("visitor")
    @Expose
    private int visitor;

    @SerializedName("isLoved")
    @Expose
    private String isLoved;

    @SerializedName("isMatched")
    @Expose
    private String isMatched;

    @SerializedName("isFavorite")
    @Expose
    private String isFavorite;

    public String getLoginBonus() {
        return loginBonus;
    }

    public void setLoginBonus(String loginBonus) {
        this.loginBonus = loginBonus;
    }

    public String getIsLoved() {
        return isLoved;
    }

    public void setIsLoved(String isLoved) {
        this.isLoved = isLoved;
    }

    public String getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(String isMatched) {
        this.isMatched = isMatched;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    @SerializedName("token_id")
    @Expose
    private String tokenID;

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getVisitor() {
        return visitor;
    }

    public void setVisitor(int visitor) {
        this.visitor = visitor;
    }







}
