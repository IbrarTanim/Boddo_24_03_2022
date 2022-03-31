package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AuthPreference;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity {

    private EditText edtNewPassword,edtRepeatYourPassword;
    private TextView tvSave;
    private ApiInterface apiInterface;
    private TextView tvBack;
    private AuthPreference authPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        authPreference = new AuthPreference(this);
        tvSave = findViewById(R.id.tvSave);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtRepeatYourPassword = findViewById(R.id.edtRepeatYourPassword);
        tvBack = findViewById(R.id.tvBack);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTOProfileAndAccountActivity();
            }
        });


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edtNewPassword.getText().toString();
                String repeatPassword = edtRepeatYourPassword.getText().toString();
                if(newPassword.isEmpty()){
                    edtNewPassword.setError("Enter your new password");
                    edtNewPassword.requestFocus();
                    return;
                }if(repeatPassword.isEmpty()){
                    edtRepeatYourPassword.setError("Enter repeat your password");
                    edtRepeatYourPassword.requestFocus();
                    return;
                }if(newPassword.equals(repeatPassword) ){
                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<User> call = apiInterface.editProfile(Constants.SECRET_KEY,"password",newPassword, Data.userId);
                    Log.e("myName", "onCreate: "+newPassword );
                    Log.e("myName", "onCreate: "+repeatPassword );
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(PasswordActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                                AuthPreference.setPassword("password",newPassword);
                                goTOProfileAndAccountActivity();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("error", "onFailure: "+t.getLocalizedMessage() );
                        }
                    });

                }else if(!newPassword.equals(repeatPassword)) {
                    Toast.makeText(PasswordActivity.this, "Password don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goTOProfileAndAccountActivity() {
        startActivity(new Intent(PasswordActivity.this,ProfileAndAccountsActivity.class));
        finish();
    }
}