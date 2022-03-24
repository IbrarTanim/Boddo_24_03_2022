package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountCloseStatusActivity extends AppCompatActivity implements View.OnClickListener {
    AccountCloseStatusActivity activity;

    ImageView tvBackAccountClose;
    TextView tvContactSupport, tvLogoutAccountClose, tvUserNameAccountClose;
    String UserName;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_close_status);

        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = findViewById(R.id.blankView);
        if (Data.STATUS_BAR_HEIGHT != 0) {
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
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

        tvBackAccountClose = findViewById(R.id.tvBackAccountClose);
        tvContactSupport = findViewById(R.id.tvContactSupport);
        tvLogoutAccountClose = findViewById(R.id.tvLogoutAccountClose);
        tvUserNameAccountClose = findViewById(R.id.tvUserNameAccountClose);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            UserName = extras.getString("UserName");
        }
        tvUserNameAccountClose.setText(UserName);

        tvBackAccountClose.setOnClickListener(this);
        tvContactSupport.setOnClickListener(this);
        tvLogoutAccountClose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBackAccountClose:

                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    lastExcessTime();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

                break;
            case R.id.tvContactSupport:
              /* Intent intent = new Intent(activity, SupportWebViewActivity.class);
                startActivity(intent);*/

                break;

            case R.id.tvLogoutAccountClose:
                logout();
                break;


        }
    }


    private void lastExcessTime() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.lastExcessTime(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
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



}