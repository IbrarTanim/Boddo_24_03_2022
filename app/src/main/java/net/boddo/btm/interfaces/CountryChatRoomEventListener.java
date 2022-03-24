package net.boddo.btm.interfaces;

public interface CountryChatRoomEventListener {
    void onCountryMessageReceived(String message, String profileUrl, String id,String type);
}
