package net.boddo.btm.Activity.Settings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.Activity.AboutActivity;
import net.boddo.btm.Activity.BlockListActivity;
import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.LoginActivity;
import net.boddo.btm.Activity.ProfileAndAccountsActivity;
import net.boddo.btm.BuildConfig;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    SettingsActivity activity;
    /*@BindView(R.id.status)
    TextView plupPlusStatus;*/

    Intent intent;
    TextView tvBackSettings, tvLogout;
    ImageView ivProfileAndAccounts, ivMembership, ivMyTransaction, ivNotification, ivTermsOfService, ivBuyCredits,ivAboutSettingActivity;


    /*@BindView(R.id.notification_settings_switch)
    Switch notificationSettings;*/
    @BindView(R.id.text_view_block_user)
    ImageView blockUser;

    @BindView(R.id.verification_status)
    TextView verification;
   /* @BindView(R.id.text_view_user_id)
    TextView userUID;*/

    @BindView(R.id.text_view_privacy_policy)
    ImageView privacyPolicy;

    /*@BindView(R.id.text_view_about_us)
    TextView aboutUs;*/

    /*@BindView(R.id.text_view_palup_version)
    TextView palupVersion;*/

    Dialog dialog;
    ImageView btClose;
    TextView closeButton;

    boolean isMembership;
    boolean isMyTransaction;
    boolean isBuyCredits;

    SharedPref pref;
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = findViewById(R.id.blankView);
        int statusBarHeight = GetStatusBarHeight();
        if (statusBarHeight != 0) {
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = statusBarHeight;
            blankView.setLayoutParams(params);
            //Log.e(TAG, "Status Bar Height: " + statusBarHeight );
        }
        /**
         * Set
         * Status
         * Bar
         * Size
         * End
         * */

        activity = this;

        ButterKnife.bind(this);
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //userUID.setText("UID: " + Data.userId);

        pref = new SharedPref(this);

        tvBackSettings = findViewById(R.id.tvBackSettings);
        ivProfileAndAccounts = findViewById(R.id.ivProfileAndAccounts);
        ivMembership = findViewById(R.id.ivMembership);
        ivNotification = findViewById(R.id.ivNotification);
        ivMyTransaction = findViewById(R.id.ivMyTransaction);
        ivBuyCredits = findViewById(R.id.ivBuyCredits);
        ivTermsOfService = findViewById(R.id.ivTermsOfService);
        tvLogout = findViewById(R.id.tvLogout);
        ivAboutSettingActivity = findViewById(R.id.ivAboutSettingActivity);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isMembership = extras.getBoolean("Membership");
            isMyTransaction = extras.getBoolean("MyTransaction");
            isBuyCredits = extras.getBoolean("BuyCredits");
        }



        tvLogout.setOnClickListener(this);
        ivTermsOfService.setOnClickListener(this);
        ivMyTransaction.setOnClickListener(this);
        ivBuyCredits.setOnClickListener(this);
        ivMembership.setOnClickListener(this);
        ivNotification.setOnClickListener(this);
        tvBackSettings.setOnClickListener(this);
        ivProfileAndAccounts.setOnClickListener(this);
        ivAboutSettingActivity.setOnClickListener(this);


        /*if (Data.isPalupPlusSubcriber == true) {
            plupPlusStatus.setText("Active");
            plupPlusStatus.setTextColor(getResources().getColor(R.color.green_A700));
        } else {
            plupPlusStatus.setText("Not Active");
            plupPlusStatus.setTextColor(getResources().getColor(R.color.colorRed));
        }*/

        if (Data.isUserEmailVerified == true) {
            verification.setText("Verified");
            verification.setTextColor(getResources().getColor(R.color.green_A700));
        } else {
            verification.setText("Not Verified");
            verification.setTextColor(getResources().getColor(R.color.colorRed));
        }

       /* if (SharedPref.getNotificationEnabled("NotificationEnabled").isEmpty()) {
            notificationSettings.setChecked(true);
            Data.isNotificationOn = true;
        } else if (SharedPref.getNotificationEnabled("NotificationEnabled").equals("on")) {
            notificationSettings.setChecked(true);
            Data.isNotificationOn = true;
        } else if (SharedPref.getNotificationEnabled("NotificationEnabled").equals("off")) {
            notificationSettings.setChecked(false);
            Data.isNotificationOn = false;
        }*/


       /* notificationSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Data.isNotificationOn = true;
                    SharedPref.setNotificationEnabled("NotificationEnabled", "on");
                } else {
                    Data.isNotificationOn = false;
                    SharedPref.setNotificationEnabled("NotificationEnabled", "off");
                }
            }
        });*/
        //palupVersion.setText(versionName);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvBackSettings:
                finish();
                break;
            case R.id.ivProfileAndAccounts:
                intent = new Intent(activity, ProfileAndAccountsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.ivMembership:

                intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra("Membership", true);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);


                break;

            case R.id.ivMyTransaction:
                intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra("MyTransaction", true);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                break;

            case R.id.ivBuyCredits:
                intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra("BuyCredits", true);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                break;

            case R.id.ivAboutSettingActivity:
                startActivity(new Intent(activity, AboutActivity.class));
                break;


            case R.id.tvLogout:
                logout();
                break;


        }
    }

    private void logout() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.logout(Data.userId, Constants.SECRET_KEY);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("success")) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SharedPref.setLastChatRoom(Constants.LAST_CHAT_ROOM, "");
                    SharedPref.setIsLoggedIn(Constants.IS_LOGGED_IN, false);
                    SharedPref.setUserId(Constants.USER_ID, "");
                    SharedPref.setUserAccessToken(Constants.ACCESS_TOKEN, "");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.text_view_block_user)
    public void onBlockUser() {
        Intent intent = new Intent(SettingsActivity.this, BlockListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.text_view_privacy_policy)
    public void onPrivacyPolicy() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVERY_AND_POLICY));
        startActivity(browserIntent);
    }

   /* @OnClick(R.id.text_view_about_us)
    public void onAboutUs() {
        aboutDailog();
    }*/

    private void aboutDailog() {

        dialog.setContentView(R.layout.custom_about_dailog);
        btClose = dialog.findViewById(R.id.bt_close);
        closeButton = dialog.findViewById(R.id.close_button);

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public int GetStatusBarHeight() {
        // returns 0 for no result found
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
