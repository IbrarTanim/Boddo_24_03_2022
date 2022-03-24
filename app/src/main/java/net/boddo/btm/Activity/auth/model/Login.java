package net.boddo.btm.Activity.auth.model;

public class Login {

    private String user_name;
    private String email;
    private String first_name;
    private String password;
    private String gender;
    private String date_of_birth;
    private int secret_key;
    private String access_token;
    private String fcm_token;
    private String user_agent;
    private String android_id;

    public Login(String user_name, String email, String first_name, String password, String gender, String date_of_birth, int secret_key, String access_token, String fcm_token, String user_agent, String android_id) {
        this.user_name = user_name;
        this.email = email;
        this.first_name = first_name;
        this.password = password;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.secret_key = secret_key;
        this.access_token = access_token;
        this.fcm_token = fcm_token;
        this.user_agent = user_agent;
        this.android_id = android_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public int getSecret_key() {
        return secret_key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public String getAndroid_id() {
        return android_id;
    }
}
