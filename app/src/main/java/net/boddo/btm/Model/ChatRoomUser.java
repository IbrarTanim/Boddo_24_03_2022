package net.boddo.btm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatRoomUser {

    @SerializedName("Global")
    @Expose
    private Integer global;
    @SerializedName("Love")
    @Expose
    private Integer love;
    @SerializedName("Date")
    @Expose
    private Integer date;
    @SerializedName("Friends")
    @Expose
    private Integer friends;
    @SerializedName("Country")
    @Expose
    private Integer country;

    public Integer getGlobal() {
        return global;
    }

    public void setGlobal(Integer global) {
        this.global = global;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getFriends() {
        return friends;
    }

    public void setFriends(Integer friends) {
        this.friends = friends;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

}