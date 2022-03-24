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

public class RelationshipActivity extends AppCompatActivity {

    RelationshipActivity activity;

    @BindView(R.id.radios)
    RadioGroup radioGroup;

    @BindView(R.id.no_response)
    RadioButton noResponse;
    @BindView(R.id.ask_me)
    RadioButton askMeButton;
    @BindView(R.id.single)
    RadioButton singleButton;

    String value = "";
    String key = "relationship";
    boolean isChanged = false;

    TextView tvRelationShipSave, tvBackRelationship;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        activity = this;
        ButterKnife.bind(this);


        tvRelationShipSave = findViewById(R.id.tvRelationShipSave);
        tvBackRelationship = findViewById(R.id.tvBackRelationship);
        tvRelationShipSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSave();
            }
        });

        tvBackRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (!Data.userRelationShip.equals("")) {
            String relationShip = Data.userRelationShip;
            switch (relationShip) {
                case "Ask Me":
                    savedBackground(askMeButton);
                    break;
                case "Single":
                    savedBackground(singleButton);
                    break;
                default:
                    savedBackground(noResponse);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.no_response:
                        value = noResponse.getText().toString();
                        setBackground(noResponse, askMeButton, singleButton);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.ask_me:
                        isChanged = true;
                        value = askMeButton.getText().toString();
                        setBackground(askMeButton, noResponse, singleButton);

                        saveToServer();
                        break;
                    case R.id.single:
                        isChanged = true;
                        value = singleButton.getText().toString();
                        setBackground(singleButton, noResponse, askMeButton);
                        saveToServer();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataSave();
    }


    private void savedBackground(RadioButton savedButton) {
        savedButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.app_color));
        savedButton.setTextColor(ContextCompat.getColor(activity, R.color.white));
    }

    private void setBackground(RadioButton expectedbtn, RadioButton button_1, RadioButton button_2) {

        expectedbtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.app_color));
        expectedbtn.setTextColor(ContextCompat.getColor(activity, R.color.white));

        button_1.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_1.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_2.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_2.setTextColor(ContextCompat.getColor(activity, R.color.gray));


    }

    private void dataSave() {
        if (isChanged) {
            if (AboutUpdate.result) {
                Data.userRelationShip = value;
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
        Intent intent = new Intent(context, RelationshipActivity.class);
        return intent;
    }
}
