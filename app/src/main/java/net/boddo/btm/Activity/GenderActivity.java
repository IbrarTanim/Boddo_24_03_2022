package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenderActivity extends AppCompatActivity {

    private RadioGroup rgGender;
    private RadioButton rbMale,rbFemale;
    private String gender = "Male";
    private TextView tvBack,tvSave;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        tvBack = findViewById(R.id.tvBack);
        tvSave = findViewById(R.id.tvSave);


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTOProfileAndAccountActivity();

            }
        });

        gender = Data.userGender;

        if(gender.equals("Male")){
            rbMale.setChecked(true);

        }else {
            rbFemale.setChecked(true);
        }


        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                gender = rb.getText().toString();


            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<User> call = apiInterface.editProfile(Constants.SECRET_KEY,"gender",gender, Data.userId);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(GenderActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            Data.userGender = gender;
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
        startActivity(new Intent(GenderActivity.this,ProfileAndAccountsActivity.class));
        finish();
    }


}