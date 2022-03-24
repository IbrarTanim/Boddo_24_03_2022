package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseCloudMessagingService;
import net.boddo.btm.Utills.Connectivity;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity implements Constants {
    private static int Splash_Screen_Time_Out = 2000;
    SharedPref sharedPref;
    ApiInterface apiInterface;
    NoInternetDialog noInternetDialog;
    Pojo pojo;
    User user;
    long messageCountNotification, messageRequestNotifacition;
    String fcmToken = "";

    private static final String TAG = "SplashScreenActivity";
    int likeNotifacition, favoriteNotifacition, visitorNotifacition;
    int likeCountNotifacition = 0, favoriteCountNotifacition = 0, visitorCountNotifacition = 0;
    boolean like_fav_visitor_Notifacation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        sharedPref = new SharedPref(this);
        noInternetDialog = new NoInternetDialog.Builder(SplashScreenActivity.this).build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //start of run
                if (Connectivity.isConnectedMobile(SplashScreenActivity.this) || Connectivity.isConnected(SplashScreenActivity.this) || Connectivity.isConnectedWifi(SplashScreenActivity.this)) {
                    if (sharedPref.getHasClickedSkipOrLetsGoButton(isLetsGoClicked)) {
                        if (SharedPref.getIsLoggedIn(IS_LOGGED_IN)) {
                            if (!SharedPref.getUserId(USER_ID).equals("")) {
                                fcmToken = SharedPref.getFCMToken(Constants.FCM_TOKEN);
                                if (!fcmToken.equals("")) {
                                    autoLogin(fcmToken);
                                } else {
                                    fcmToken = FirebaseInstanceId.getInstance().getToken();
                                    if (fcmToken != null) {
                                        SharedPref.setFCMToken(Constants.FCM_TOKEN, fcmToken);
                                        autoLogin(fcmToken);
                                    } else {
                                        fcmToken = FirebaseInstanceId.getInstance().getToken();
                                        SharedPref.setFCMToken(Constants.FCM_TOKEN, fcmToken);
                                        autoLogin(fcmToken);
                                    }
                                }
                            } else {
                                startActivity(new Intent(SplashScreenActivity.this, LandingActivity.class));
                                finish();
                            }
                        } else {
                            startActivity(LoginActivity.newLoginIntent(SplashScreenActivity.this));
                            finish();
                        }
                    } else {
                        startActivity(IntoSlideActivity.newIntroSliderIntent(SplashScreenActivity.this));
                        finish();
                    }
                } else {
                    if (!SharedPref.getIsLoggedIn(IS_LOGGED_IN)) {
                        if (sharedPref.getHasClickedSkipOrLetsGoButton(Constants.isLetsGoClicked)) {
                            startActivity(new Intent(SplashScreenActivity.this, LandingActivity.class));
                            finish();
                        } else {
                            startActivity(IntoSlideActivity.newIntroSliderIntent(SplashScreenActivity.this));
                            finish();
                        }
                    } else {
                        Toast.makeText(SplashScreenActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                //end of run method
            }
        }, Splash_Screen_Time_Out);
    }

    private void loadProfileImages() {

        Call<ProfileImageLoader> call = apiInterface.allPhotos(Data.userId, Constants.SECRET_KEY);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                if (response.body().getStatus().equals("success")) {
                    ProfileImageLoader photos = response.body();
                    ProfileFragment.imageList = photos.getProfileImageInfo();
                    ProfileFragment profileFragment = new ProfileFragment();
                    if (ProfileFragment.imageList.size() < 1) {
                        SharedPref.setString(SplashScreenActivity.this,Constants.USER_PROFILE_PHOTO,Constants.DEFAULT_PROFILE_IMAGE);

                    } else {
                        Data.profilePhoto = profileFragment.imageList.get(0).getPhoto();
                    }
                    Data.saveLoggedInData(user);
                    String[] currentCountry;
                    if (!Data.userCountry.equals("")){
                        currentCountry = Data.userCountry.split(",");
                        SharedPref.setString(SplashScreenActivity.this,Constants.COUNTRY,currentCountry[1]);
                    }
                    Intent intent = new Intent(SplashScreenActivity.this, DashBoadActivity.class);
                    if (getIntent().hasExtra(Constants.CHAT_LIST)) {
                        if (getIntent().hasExtra("badigeNotifacition")) {
                            intent.putExtra("badigeChatNotifacation", "badigeChatNotifacation");
                        }
                        intent.putExtra(Constants.CHAT_LIST, "activeChatList");
                    }

                    intent.putExtra("message_count", messageCountNotification);
                    intent.putExtra("message_request_count", messageRequestNotifacition);
                    intent.putExtra("like_fav_visitor_Notifacation", like_fav_visitor_Notifacation);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashScreenActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {

            }
        });
    }

    private void autoLogin(String fcmToken) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String id = SharedPref.getUserId(USER_ID);
        String acccc = SharedPref.getUserAccessToken(ACCESS_TOKEN);
        Call<Pojo> call = apiInterface.autoLogin(SharedPref.getUserId(USER_ID), SharedPref.getUserAccessToken(ACCESS_TOKEN), SECRET_KEY, fcmToken);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                pojo = response.body();
                messageCountNotification = pojo.getMessage();
                messageRequestNotifacition = pojo.getRequest();

                likeCountNotifacition = SharedPref.getLikeCount("LikeCount");
                favoriteCountNotifacition = SharedPref.getFavoriteCount("FavoriteCount");
                visitorCountNotifacition = SharedPref.getVisitorCount("VisitorCount");

                likeNotifacition = pojo.getLike();
                favoriteNotifacition = pojo.getFavorite();
                visitorNotifacition = pojo.getVisitor();
                if (likeCountNotifacition >= likeNotifacition || favoriteCountNotifacition >= favoriteNotifacition || visitorCountNotifacition >= visitorNotifacition) {
                    SharedPref.setLikeCount("LikeCount", likeNotifacition);
                    SharedPref.setFavoriteCount("FavoriteCount", favoriteNotifacition);
                    SharedPref.setVisitorCount("VisitorCount", visitorNotifacition);
                    like_fav_visitor_Notifacation = false;

                } else {

                    SharedPref.setLikeCount("LikeCount", likeNotifacition);
                    SharedPref.setFavoriteCount("FavoriteCount", favoriteNotifacition);
                    SharedPref.setVisitorCount("VisitorCount", visitorNotifacition);
                    like_fav_visitor_Notifacation = true;
                }

                FirebaseCloudMessagingService.bottom_chatButton_dot = (int) messageCountNotification;
                FirebaseCloudMessagingService.bottom_chatButton_dot = (int) messageRequestNotifacition;
                EventBus.getDefault().post(new Event(Constants.MESSAGE_DOT_INCREASE));

                if (pojo.getStatus().equals("success")) {
                    user = pojo.getUser();
                    if (!pojo.getLoginBonus().equals("no")) {
                        LoginActivity.isBonusAvailable = Integer.parseInt(pojo.getLoginBonus());
                    }
                    if (!user.getIsSuspended().equals("") && user.getIsSuspended().equals("1")) {

                        // farabi

                        Intent intent = new Intent(SplashScreenActivity.this, AccountCloseStatusActivity.class);
                        intent.putExtra("UserName", user.getUserName());
                        startActivity(intent);
                        finish();

                        /*final PrettyDialog prettyDialog = new PrettyDialog(SplashScreenActivity.this);
                        prettyDialog.setTitle("Dear " + user.getUserName() + ",")
                                .setMessage("You have been suspended from palup.Please try to contact with palup administrator for more information")
                                .setIcon(R.drawable.ic_warning_white_24dp)
                                .addButton("Ok", R.color.white, R.color.colorAccent, new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        prettyDialog.dismiss();
                                        finish();
                                    }
                                }).show();*/
                    }

                    else {
                        Data.userId = user.getUserId();
                        loadProfileImages();
                    }
                } else if (pojo.getStatus().equals("failed")) {
                    SharedPref.setIsLoggedIn(IS_LOGGED_IN, false);
                    SharedPref.setUserId(USER_ID, "");
                    SharedPref.setUserAccessToken(ACCESS_TOKEN, "");
                    startActivity(LoginActivity.newLoginIntent(SplashScreenActivity.this));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {
                Log.d(SplashScreenActivity.class.getSimpleName(), t.getMessage().toString());
                startActivity(new Intent(SplashScreenActivity.this, LandingActivity.class));
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noInternetDialog != null) {
            try {
                noInternetDialog.onDestroy();
            } catch (Exception e) {
                Log.e(TAG, "onDestroy: " + e.getMessage());
            }

        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (noInternetDialog != null) {

            noInternetDialog.onDestroy();
        }
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
    }

}
