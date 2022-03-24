package net.boddo.btm.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valdesekamdem.library.mdtoast.MDToast;

import net.boddo.btm.Activity.auth.RegistrantionActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AuthPreference;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;
import net.boddo.btm.dialog.LoadingDialog;
import net.boddo.btm.dialog.LoginBottomSheetDialog;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Constants, LoginBottomSheetDialog.LoginBottomSheetListener {

    @BindView(R.id.edit_text_email)
    TextInputEditText email;
    @BindView(R.id.edit_text_password)
    TextInputEditText userPassword;
    @BindView(R.id.checkbox_Remember_me)
    CheckBox mRemamberMe;
    @BindView(R.id.button_login)
    Button loginBTN;

    TextView tvForgetPassword;
    @BindView(R.id.text_view_more_option)
    TextView moreOption;

    int likeNotifacition, favoriteNotifacition, visitorNotifacition;
    long messageCountNotification, messageRequestNotifacition;

    ApiInterface apiInterface;
    private static final String TAG = "LoginActivity";
    User user;
    String userAgent;
    Pojo pojo;
    SharedPref sharedPref;
    AuthPreference authPreference;
    public static int isBonusAvailable = 0;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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

        sharedPref = new SharedPref(this);
        authPreference = new AuthPreference(this);

        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        loadingDialog = new LoadingDialog(this);
        //user Agents
        userAgent = System.getProperty("http.agent");
        if (SharedPref.getCheckBoxResult(IS_CHECKED)) {
            mRemamberMe.setChecked(true);
            String s = SharedPref.getUserEmail(EMAIL);
            email.setText(SharedPref.getUserEmail(EMAIL));
            userPassword.setText(SharedPref.getUserPassword(PASSWORD));
        } else {
            mRemamberMe.setChecked(false);
            email.setText("");
            userPassword.setText("");
        }

        ImageButton backBTN = findViewById(R.id.back_btn);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(LoginActivity.this, LandingActivity.class);
                backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(backIntent);
            }
        });
    }

    // TODO keyboard mangement is necessary
    @OnClick(R.id.text_view_more_option)
    public void onMoreOptionClicked() {
        LoginBottomSheetDialog bottomSheetDialog = new LoginBottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetDialog");
    }

    @OnClick(R.id.button_login)
    public void onLoginButtonClicked() {
        loadingDialog.showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (email.getText().toString().length() > 8 && !email.getText().toString().equals("")) {
                    String fcmToken = FirebaseInstanceId.getInstance().getToken();
                    if (!fcmToken.equals("")) {
                        login(fcmToken);
                    } else {
                        fcmToken = FirebaseInstanceId.getInstance().getToken();
                        if (fcmToken != null) {
                            login(fcmToken);
                        }
                    }
                } else {
                    if (loadingDialog != null) {
                        loadingDialog.hideDialog();
                    }
                    if (email.getText().toString().equals("")) {
                        email.setError("Please enter your email");
                    }
                    if (userPassword.getText().toString().equals("")) {
                        userPassword.setError("Please enter your password");
                    }
                }
            }
        }, 2000);
    }

    private void login(String fcmToken) {
        Log.e("fcmToken", "login: "+fcmToken );
        final String token = UUID.randomUUID().toString();
        Log.e("fcmToken", "login: "+token );

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String emailData = email.getText().toString().trim();
        String passwordData = userPassword.getText().toString();
        int keyData = Constants.SECRET_KEY;
        String tokenData = token;
        String fcmTokenData = fcmToken;
        String userAgentData = userAgent;



        Call<Pojo> call = apiInterface.login(email.getText().toString().trim(), userPassword.getText().toString(), Constants.SECRET_KEY, token, fcmToken, userAgent);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                pojo = response.body();
                if (pojo.getStatus().equals("success")) {
                    user = pojo.getUser();
                    messageCountNotification = pojo.getMessage();
                    messageRequestNotifacition = pojo.getRequest();

                    SharedPref.setLikeCount("LikeCount", likeNotifacition);
                    SharedPref.setFavoriteCount("FavoriteCount", favoriteNotifacition);
                    SharedPref.setVisitorCount("VisitorCount", visitorNotifacition);
                    AuthPreference.setEmail("email",emailData);
                    AuthPreference.setEmail("password",passwordData);
                    if (!pojo.getLoginBonus().equals("no")) {
                        isBonusAvailable = Integer.parseInt(pojo.getLoginBonus());
                    }
                    if (!user.getIsSuspended().equals("") && user.getIsSuspended().equals("1")) {


                        Intent intent = new Intent(LoginActivity.this, AccountCloseStatusActivity.class);
                        intent.putExtra("UserName", user.getUserName());
                        startActivity(intent);
                        finish();


                        //farabi
                        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            final PrettyDialog prettyDialog = new PrettyDialog(LoginActivity.this);
                            prettyDialog.setTitle("Dear " + user.getUserName() + ",")
                                    .setMessage("You have been suspended from palup.Please try to contact with palup administrator for more information")
                                    .addButton("Ok", R.color.white, R.color.colorAccent, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            //farabi
                            final PrettyDialog prettyDialog = new PrettyDialog(LoginActivity.this);
                            prettyDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setTitle("Dear " + user.getUserName() + ",")
                                    .setMessage("You have been suspended from palup.Please try to contact with palup administrator for more information")
                                    .addButton("Ok", R.color.white, R.color.colorAccent, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    }).show();
                        }*/


                    } else {
                        SharedPref.setUserId(USER_ID, user.getUserId());
                        SharedPref.setUserAccessToken(ACCESS_TOKEN, token);
                        Data.saveLoggedInData(user);
                        String[] currentCountry;
                        if (!Data.userCountry.equals("")) {
                            currentCountry = Data.userCountry.split(",");
                            Data.country = currentCountry[1];
                        }

                        SharedPref.setIsLoggedIn(IS_LOGGED_IN, true);
                        loadProfileImages();
                        if (mRemamberMe.isChecked()) {
                            SharedPref.setCheckBox(IS_CHECKED, true);
                            SharedPref.setUserEmail(EMAIL, email.getText().toString());
                            SharedPref.setUserPassword(PASSWORD, userPassword.getText().toString());
                        } else {
                            SharedPref.setCheckBox(IS_CHECKED, false);
                            SharedPref.clearIsChecked(EMAIL);
                            SharedPref.clearIsChecked(PASSWORD);
                        }
                    }
                } else if (pojo.getStatus().equals("failed")) {
                    MDToast mdToast = MDToast.makeText(LoginActivity.this, getResources().getString(R.string.invalid_username_or_password), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                    loadingDialog.hideDialog();
                } else {
                    MDToast mdToast = MDToast.makeText(LoginActivity.this, getResources().getString(R.string.something_went_wrong), MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                    mdToast.show();
                    loadingDialog.hideDialog();
                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {
                loadingDialog.hideDialog();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Please check your connection or something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
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
                        Data.profilePhoto = Constants.IMAGE_REGEX + "/default.jpg";
                    } else {
                        Data.profilePhoto = profileFragment.imageList.get(0).getPhoto();
                    }
                    Intent intent = new Intent(LoginActivity.this, DashBoadActivity.class);
                    intent.putExtra(USER_DETAILS, user);
                    intent.putExtra("message_count", messageCountNotification);
                    intent.putExtra("message_request_count", messageRequestNotifacition);
                    startActivity(intent);
                    finish();
                    if (loadingDialog != null) {
                        loadingDialog.hideDialog();
                    }
                } else {
                    if (loadingDialog != null) {
                        loadingDialog.hideDialog();
                    }
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {
                Log.d(LoginActivity.class.getSimpleName(), t.getMessage());
                if (loadingDialog != null) {
                    loadingDialog.hideDialog();
                }
            }
        });
    }

    public static Intent newLoginIntent(Context context) {
        Intent intent = new Intent(context, LandingActivity.class);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(this, LandingActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backIntent);
    }

    @Override
    public void onItemClicked(String text) {
        if (text.equals(CREATE_NEW_ACCOUNT)) {
            Intent intent = new Intent(LoginActivity.this, RegistrantionActivity.class);
            startActivity(intent);
        }
        if (text.equals(FORGOT_PASSWORD)) {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
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