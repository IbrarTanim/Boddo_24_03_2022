package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BirthDateActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TextView tvSave,tvBack;
    private ApiInterface apiInterface;
    String date = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        datePicker = findViewById(R.id.datePicker);
        tvSave = findViewById(R.id.tvSave);
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
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth()+1;
                int year = datePicker.getYear();

                 date = day+"/"+month+"/"+year;

                Log.e("date", "onCreate: "+date );


                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<User> call = apiInterface.editProfile(Constants.SECRET_KEY,"date_of_birth",date, Data.userId);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(BirthDateActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            Data.userDateOfBirgh = date;
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
        startActivity(new Intent(BirthDateActivity.this,ProfileAndAccountsActivity.class));
        finish();
    }
}