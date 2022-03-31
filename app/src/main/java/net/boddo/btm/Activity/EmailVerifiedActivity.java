package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EmailVerifiedActivity extends AppCompatActivity {

    private EditText edtEmail;
    private TextView tvSave;
    private ApiInterface apiInterface;
    private AuthPreference authPreference;
    private ImageView tvClearEmail;
    private TextView tvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verified);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        authPreference = new AuthPreference(this);
        edtEmail = findViewById(R.id.edtEmail);
        tvSave = findViewById(R.id.tvSave);
        tvClearEmail = findViewById(R.id.tvClearEmail);
        tvBack = findViewById(R.id.tvBack);

        edtEmail.setText(AuthPreference.getEmail("email"));

        tvClearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEmail.setText("");
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTOProfileAndAccountActivity();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myName = edtEmail.getText().toString();
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<User> call = apiInterface.editProfile(Constants.SECRET_KEY,"email",myName, Data.userId);
                Log.e("myName", "onCreate: "+myName );
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(EmailVerifiedActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            AuthPreference.setEmail("email",myName);
                            goTOProfileAndAccountActivity();
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

    private void goTOProfileAndAccountActivity() {
        startActivity(new Intent(EmailVerifiedActivity.this,ProfileAndAccountsActivity.class));
        finish();
    }
}