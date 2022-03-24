package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

public class LocationActivity extends AppCompatActivity {
    LocationActivity activity;
    TextView tvSave, tvBack, counter;
    ImageView tvAboutMeClear;
    EditText edtAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        activity = this;

        edtAboutMe = findViewById(R.id.edtLocation);
        tvAboutMeClear = findViewById(R.id.tvClearLocation);
        tvSave = findViewById(R.id.tvSave);
        tvBack = findViewById(R.id.tvBack);
        counter = findViewById(R.id.counter);

        if (!Data.userHomeTown.equals("")) {
            edtAboutMe.setText(Data.userHomeTown);
            counter.setText(String.valueOf(Data.userHomeTown.length()));
        }

        edtAboutMe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 2 && count <= 250) {
                    //saveAbout.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                counter.setText(String.valueOf(s.length()));
            }
        });

        tvAboutMeClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAboutMe.setText("");
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AboutUpdate obj = new AboutUpdate(activity);
                obj.updateAbout("about", edtAboutMe.getText().toString());
                Data.userHomeTown = edtAboutMe.getText().toString();
                Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LocationActivity.class);
        return intent;
    }
}
