package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.Activity.Settings.SettingsActivity;
import net.boddo.btm.BuildConfig;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AuthPreference;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;

public class ProfileAndAccountsActivity extends AppCompatActivity implements View.OnClickListener {
    ProfileAndAccountsActivity activity;
    Intent intent;
    ApiInterface apiInterface;
    private SharedPref sharedPref;
    private TextView tvBackProfileAndAccounts, tvUID, tvVersion,tvFullName,
            tvUserName,tvGender,tvDate,tvPassword,tvEmail;
    private TextView llDeleteAccount;
    private AuthPreference authPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_and_accounts);

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
        authPreference = new AuthPreference(activity);
        tvBackProfileAndAccounts = findViewById(R.id.tvBackProfileAndAccounts);
        tvUID = findViewById(R.id.tvUID);
        tvVersion = findViewById(R.id.tvVersion);
        tvFullName = findViewById(R.id.tvFullName);
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvGender = findViewById(R.id.tvGender);
        tvDate = findViewById(R.id.tvBirthDate);
        tvPassword = findViewById(R.id.tvPassword);
        llDeleteAccount = findViewById(R.id.llDeleteAccount);

        tvFullName.setText(Data.userFirstName);
        tvUserName.setText(Data.userName);
        tvGender.setText(Data.userGender);
        tvDate.setText(Data.userDateOfBirgh);

        tvEmail.setText(AuthPreference.getEmail("email"));

        /*if(Data.isUserEmailVerified){
            tvEmailVerified.setText("(Verified)");
        }else {
            tvEmailVerified.setText("(Not Verified)");
        }*/

        tvUID.setText(Data.userId);

        tvUID.setText("UID: " + Data.userId);
        tvVersion.setText(BuildConfig.VERSION_NAME);
        tvBackProfileAndAccounts.setOnClickListener(this);
        tvFullName.setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        tvGender.setOnClickListener(this);
        llDeleteAccount.setOnClickListener(this);
        tvPassword.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvEmail.setOnClickListener(this);

     //   profile_update();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBackProfileAndAccounts:
                intent = new Intent(activity, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
                case R.id.tvFullName:
                    intent = new Intent(activity,FullNameActivity.class);
                    startActivity(intent);
                    finish();
                break;
                case R.id.tvUserName:
                    intent = new Intent(activity,UserName.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.tvGender:
                    intent = new Intent(activity,GenderActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.tvBirthDate:
                    intent = new Intent(activity, BirthDateActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.tvEmail:
                    intent = new Intent(activity,EmailVerifiedActivity.class);
                    startActivity(intent);
                    finish();
                finish();
                break;
                case R.id.tvPassword:
                    startActivity(new Intent(activity, PasswordActivity.class));
                    finish();
                    break;
            case R.id.llDeleteAccount:
                startActivity(new Intent(activity, DeleteAccountActivity.class));
                finish();
                break;

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