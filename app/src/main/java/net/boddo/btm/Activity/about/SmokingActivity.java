package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmokingActivity extends AppCompatActivity {

    SmokingActivity activity;

    @BindView(R.id.radios)
    RadioGroup radioGroup;

    @BindView(R.id.no_response)
    RadioButton noResponse;

    @BindView(R.id.ask_me)
    RadioButton askMeButton;

    @BindView(R.id.no)
    RadioButton no;

    @BindView(R.id.yes_regular)
    RadioButton yesRegular;

    @BindView(R.id.sometimes)
    RadioButton sometimes;

    TextView tvSave, tvBack;

    String value = "";
    String key = "smoking";
    boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoking);
        activity = this;

        ButterKnife.bind(this);

        if (!Data.doUserSmooke.equals("")) {
            String doUserSmooke = Data.doUserSmooke;
            switch (doUserSmooke) {
                case "Ask Me":
                    askMeButton.setChecked(true);
                    savedBackground(askMeButton);
                    break;
                case "No":
                    savedBackground(no);
                    break;
                case "Yes regular":
                    savedBackground(yesRegular);
                    break;
                case "Sometimes":
                    savedBackground(sometimes);
                    break;
                default:
                    savedBackground(noResponse);
            }
        }


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


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.no_response:
                        value = noResponse.getText().toString();
                        setBackground(noResponse, askMeButton, no, yesRegular, sometimes);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.ask_me:
                        isChanged = true;
                        setBackground(askMeButton, noResponse, no, yesRegular, sometimes);
                        value = askMeButton.getText().toString();
                        saveToServer();
                        break;
                    case R.id.no:
                        isChanged = true;
                        setBackground(no, noResponse, askMeButton, yesRegular, sometimes);
                        value = no.getText().toString();
                        saveToServer();
                        break;
                    case R.id.yes_regular:
                        isChanged = true;
                        setBackground(yesRegular, noResponse, askMeButton, no, sometimes);
                        value = yesRegular.getText().toString();
                        saveToServer();
                        break;

                    case R.id.sometimes:
                        isChanged = true;
                        setBackground(sometimes, noResponse, askMeButton, no, yesRegular);
                        value = sometimes.getText().toString();
                        saveToServer();
                        break;

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void savedBackground(RadioButton savedButton) {
        savedButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.app_color));
        savedButton.setTextColor(ContextCompat.getColor(activity, R.color.white));
    }

    private void setBackground(RadioButton expectedbtn, RadioButton button_1, RadioButton button_2, RadioButton button_3, RadioButton button_4) {

        expectedbtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.app_color));
        expectedbtn.setTextColor(ContextCompat.getColor(activity, R.color.white));

        button_1.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_1.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_2.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_2.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_3.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_3.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_4.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_4.setTextColor(ContextCompat.getColor(activity, R.color.gray));


    }

    private void dataSave() {
        if (isChanged) {
            if (AboutUpdate.result) {
                Data.doUserSmooke = value;
                Toast.makeText(this, "Update Successfull", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void saveToServer() {
        AboutUpdate update = new AboutUpdate(this);
        update.updateAbout(key, value);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SmokingActivity.class);
        return intent;
    }
}
