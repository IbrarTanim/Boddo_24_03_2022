package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    ForgetPasswordActivity activity;

    @BindView(R.id.forgetPassET)
    EditText emailET;
    String email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @BindView(R.id.forgetPassbt)
    Button ButtonBT;

    ImageView ivBackResetPassword;


    boolean isvalid = false;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        activity = this;
        ButterKnife.bind(this);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        ivBackResetPassword = findViewById(R.id.ivBackResetPassword);
        ivBackResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LoginActivity.class));
                finish();
            }
        });


    }

    @OnClick(R.id.forgetPassbt)
    public void onForgetPassword() {

        email = emailET.getText().toString().trim();

        Helper helper = new Helper();

        if (emailET.getText().toString().equals("")) {

            emailET.setError("field  is empty");

        } else if (email.matches(emailPattern) && email.length() > 0) {
            isvalid = true;
        } else {
            emailET.setError("Invalid email");
        }


        if (isvalid) {

            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<String> call = apiInterface.onForgetPassword(Constants.SECRET_KEY, email);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String stutas = response.body();
                    if (stutas.equals("success")) {
                        successDialog();
                    } else if (stutas.equals("failed")) {

                        failDialog();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private void successDialog() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            PrettyDialog prettyDialog = new PrettyDialog(this);
            prettyDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessage("We sent you a verification email.")
                    .setMessageColor(R.color.colorRed)
                    .show();
        } else {
            PrettyDialog prettyDialog = new PrettyDialog(this);
            prettyDialog.setMessage("We sent you a verification email.")
                    .setMessageColor(R.color.colorRed)
                    .show();
        }
    }

    private void failDialog() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            PrettyDialog prettyDialog = new PrettyDialog(this);
            prettyDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessage("Email is not exist")
                    .setMessageColor(R.color.colorRed)
                    .show();
        } else {
            PrettyDialog prettyDialog = new PrettyDialog(this);
            prettyDialog.setMessage("Email is not exist")
                    .setMessageColor(R.color.colorRed)
                    .show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
