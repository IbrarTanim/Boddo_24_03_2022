package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

public class LookingForActivity extends AppCompatActivity {

    LookingForActivity activity;

    String value = "";
    String key = "looking_for";

    boolean isChanged = false;

    RadioGroup radioGroup;
    RadioButton noResponse, Man, Women, MenAndWomen;

    TextView tvSave, tvBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looking_for);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        activity = this;
        radioGroup = findViewById(R.id.radios);
        noResponse = findViewById(R.id.no_response);
        Man = findViewById(R.id.Man);
        Women = findViewById(R.id.Women);
        MenAndWomen = findViewById(R.id.MenAndWomen);


        tvSave = findViewById(R.id.tvSave);
        tvBack = findViewById(R.id.tvBack);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSave();
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (!Data.userLookingFor.equals("")) {
            String education = Data.userLookingFor;
            switch (education) {
                case "No response":
                    savedBackground(noResponse);
                    break;
                case "Dating":
                    savedBackground(Man);
                    break;
                case "Friendship":
                    savedBackground(Women);
                    break;
                case "Marriage":
                    savedBackground(MenAndWomen);
                    break;
            }
        } else {
            noResponse.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.no_response:
                        value = noResponse.getText().toString();
                        setBackground(noResponse, Man, Women, MenAndWomen);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.Man:
                        value = Man.getText().toString();
                        setBackground(Man, noResponse, Women, MenAndWomen);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.Women:
                        value = Women.getText().toString();
                        setBackground(Women, noResponse, Man, MenAndWomen);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.MenAndWomen:
                        value = MenAndWomen.getText().toString();
                        setBackground(MenAndWomen, noResponse, Man, Women);
                        isChanged = true;
                        saveToServer();
                        break;
                }
            }
        });
    }


    private void savedBackground(RadioButton savedButton) {
        savedButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.app_color));
        savedButton.setTextColor(ContextCompat.getColor(activity, R.color.white));
    }

    private void setBackground(RadioButton expectedbtn, RadioButton button_1, RadioButton button_2, RadioButton button_3) {

        expectedbtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.app_color));
        expectedbtn.setTextColor(ContextCompat.getColor(activity, R.color.white));

        button_1.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_1.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_2.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_2.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_3.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_3.setTextColor(ContextCompat.getColor(activity, R.color.gray));
    }

    private void dataSave() {
        if (AboutUpdate.result) {
            Data.userLookingFor = value;
            isChanged = false;
            Toast.makeText(this, "updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void saveToServer() {
        if (isChanged) {
            AboutUpdate update = new AboutUpdate(this);
            update.updateAbout(key, value);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataSave();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LookingForActivity.class);
        return intent;
    }
}
