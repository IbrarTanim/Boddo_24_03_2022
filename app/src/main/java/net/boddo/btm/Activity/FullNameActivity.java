package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import retrofit2.Retrofit;

public class FullNameActivity extends AppCompatActivity {

    FullNameActivity activity;
    private EditText edtFullName;
    private TextView counterFullName,tvSave,tvBack;
    private ApiInterface apiInterface;
    private ImageView tvClearFullName;
    private SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_name);

        activity = this;

        edtFullName = findViewById(R.id.edtFullName);
        counterFullName = findViewById(R.id.counterFullName);
        tvSave = findViewById(R.id.tvSave);
        tvClearFullName = findViewById(R.id.tvClearFullName);
        tvBack = findViewById(R.id.tvBack);
        sharedPref = new SharedPref(activity);

        edtFullName.setText(Data.userFirstName);
        edtFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counterFullName.setText(""+s.length()+"/25");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTOProfileAndAccountActivity();
            }
        });


        tvClearFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFullName.setText("");
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myName = edtFullName.getText().toString();
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<User> call = apiInterface.editProfile(Constants.SECRET_KEY,"first_name",myName, Data.userId);
                Log.e("myName", "onCreate: "+myName );
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(FullNameActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            Data.userFirstName  =  myName;
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
        startActivity(new Intent(activity,ProfileAndAccountsActivity.class));
        finish();
    }
}