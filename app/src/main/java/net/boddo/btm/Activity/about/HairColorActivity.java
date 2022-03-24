package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class HairColorActivity extends AppCompatActivity {

    HairColorActivity activity;

    @BindView(R.id.radios)
    RadioGroup radioGroup;

    @BindView(R.id.no_response)
    RadioButton noResponse;

    @BindView(R.id.ask_me)
    RadioButton askMeButton;

    @BindView(R.id.Black)
    RadioButton Black;

    @BindView(R.id.Brown)
    RadioButton Brown;

    @BindView(R.id.Blue)
    RadioButton Blue;

    TextView tvSave, tvBack;

    String value = "";
    String key = "hair_color";
    boolean isChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_hair_color);
        ButterKnife.bind(this);
        activity = this;

        if (!Data.userHairColor.equals("")) {
            String userHairColor = Data.userHairColor;
            switch (userHairColor) {
                case "Ask Me":
                    savedBackground(askMeButton);
                    break;
                case "Black":
                    savedBackground(Black);
                    break;
                case "Brown":
                    savedBackground(Brown);
                    break;
                case "Blue":
                    savedBackground(Blue);
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
                        setBackground(noResponse, askMeButton, Black, Brown, Blue);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.ask_me:
                        isChanged = true;
                        setBackground(askMeButton, noResponse, Black, Brown, Blue);
                        value = askMeButton.getText().toString();
                        saveToServer();
                        break;
                    case R.id.Black:
                        isChanged = true;
                        setBackground(Black, noResponse, askMeButton, Brown, Blue);
                        value = Black.getText().toString();
                        saveToServer();
                        break;
                    case R.id.Brown:
                        isChanged = true;
                        setBackground(Brown, noResponse, askMeButton, Black, Blue);
                        value = Brown.getText().toString();
                        saveToServer();
                        break;
                    case R.id.Blue:
                        isChanged = true;
                        setBackground(Blue, noResponse, askMeButton, Black, Brown);
                        value = Blue.getText().toString();
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
                Data.userHairColor = value;
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataSave();
    }

    private void saveToServer() {
        AboutUpdate update = new AboutUpdate(this);
        update.updateAbout(key, value);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HairColorActivity.class);
        return intent;
    }

}
