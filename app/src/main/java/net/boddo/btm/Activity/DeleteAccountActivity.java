package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteAccountActivity extends AppCompatActivity implements View.OnClickListener {

    DeleteAccountActivity activity;
    private TextView tvMeetSomeone,tvMeetSomeoneElseWhere,tvResetMyAccount,
            tvNoLongerInterested,tvQualityOfApp,tvOther,tvSave, tvBackDelete;

    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

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
        tvMeetSomeone = findViewById(R.id.tvMeetSomeone);
        tvMeetSomeoneElseWhere = findViewById(R.id.tvMeetSomeoneElseWhere);
        tvResetMyAccount = findViewById(R.id.tvResetMyAccount);
        tvNoLongerInterested = findViewById(R.id.tvNoLongerInterested);
        tvQualityOfApp = findViewById(R.id.tvQualityOfApp);
        tvOther = findViewById(R.id.tvOther);
        tvSave = findViewById(R.id.tvSave);
        tvBackDelete = findViewById(R.id.tvBackDelete);

        tvMeetSomeone.setOnClickListener(this);
        tvMeetSomeoneElseWhere.setOnClickListener(this);
        tvResetMyAccount.setOnClickListener(this);
        tvNoLongerInterested.setOnClickListener(this);
        tvQualityOfApp.setOnClickListener(this);
        tvOther.setOnClickListener(this);
        tvBackDelete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvMeetSomeone:
                tvMeetSomeone.setBackground(getResources().getDrawable(R.drawable.green_shade_button_bg));
                tvMeetSomeoneElseWhere.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvResetMyAccount.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvNoLongerInterested.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvQualityOfApp.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvOther.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                break;

            case R.id.tvMeetSomeoneElseWhere:
                tvMeetSomeone.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvMeetSomeoneElseWhere.setBackground(getResources().getDrawable(R.drawable.green_shade_button_bg));
                tvResetMyAccount.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvNoLongerInterested.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvQualityOfApp.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvOther.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                break;

            case R.id.tvResetMyAccount:
                tvMeetSomeone.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvMeetSomeoneElseWhere.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvResetMyAccount.setBackground(getResources().getDrawable(R.drawable.green_shade_button_bg));
                tvNoLongerInterested.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvQualityOfApp.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvOther.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                break;

            case R.id.tvNoLongerInterested:
                tvMeetSomeone.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvMeetSomeoneElseWhere.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvResetMyAccount.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvNoLongerInterested.setBackground(getResources().getDrawable(R.drawable.green_shade_button_bg));
                tvQualityOfApp.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvOther.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                break;

            case R.id.tvQualityOfApp:
                tvMeetSomeone.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvMeetSomeoneElseWhere.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvResetMyAccount.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvNoLongerInterested.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvQualityOfApp.setBackground(getResources().getDrawable(R.drawable.green_shade_button_bg));
                tvOther.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                break;

            case R.id.tvOther:
                tvMeetSomeone.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvMeetSomeoneElseWhere.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvResetMyAccount.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvNoLongerInterested.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvQualityOfApp.setBackground(getResources().getDrawable(R.drawable.username_save_button));
                tvOther.setBackground(getResources().getDrawable(R.drawable.green_shade_button_bg));
                break;


                case R.id.tvBackDelete:
                    startActivity(new Intent(activity,ProfileAndAccountsActivity.class));
                    finish();
                break;




        }

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<User> call = apiInterface.closeAccount(Data.userId,Constants.SECRET_KEY);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            logout();
                            Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("error", "onFailure: "+t.getLocalizedMessage() );
                    }
                });

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