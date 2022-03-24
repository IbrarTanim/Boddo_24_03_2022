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

public class HobbiesActivity extends AppCompatActivity {

    HobbiesActivity activity;

    @BindView(R.id.radios)
    RadioGroup radioGroup;

    @BindView(R.id.rbNoResponse)
    RadioButton rbNoResponse;

    @BindView(R.id.rbSports)
    RadioButton rbSports;

    @BindView(R.id.rbTourism)
    RadioButton rbTourism;

    @BindView(R.id.rbBlogging)
    RadioButton rbBlogging;

    @BindView(R.id.rbDancing)
    RadioButton rbDancing;

    @BindView(R.id.rbReading)
    RadioButton rbReading;

    @BindView(R.id.rbActing)
    RadioButton rbActing;

    @BindView(R.id.rbCooking)
    RadioButton rbCooking;

    @BindView(R.id.rbSinging)
    RadioButton rbSinging;

    @BindView(R.id.rbGaming)
    RadioButton rbGaming;

    @BindView(R.id.rbCollecting)
    RadioButton rbCollecting;

    @BindView(R.id.rbFashion)
    RadioButton rbFashion;

    @BindView(R.id.rbCoding)
    RadioButton rbCoding;


    TextView tvSave, tvBack;

    String value = "";
    String key = "habits";
    boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies);
        activity = this;

        ButterKnife.bind(this);

        if (!Data.userHabits.equals("")) {
            String doUserHabits = Data.userHabits;
            switch (doUserHabits) {
                case "Sports":
                    savedBackground(rbSports);
                    break;
                case "Tourism":
                    savedBackground(rbTourism);
                    break;
                case "Blogging":
                    savedBackground(rbBlogging);
                    break;
                case "Dancing":
                    savedBackground(rbDancing);
                    break;
                case "Reading":
                    savedBackground(rbReading);
                    break;
                case "Acting":
                    savedBackground(rbActing);
                    break;
                case "Cooking":
                    savedBackground(rbCooking);
                    break;
                case "Singing":
                    savedBackground(rbSinging);
                    break;
                case "Gaming":
                    savedBackground(rbGaming);
                    break;
                case "Collecting":
                    savedBackground(rbCollecting);
                    break;
                case "Fashion":
                    savedBackground(rbFashion);
                    break;
                case "Coding":
                    savedBackground(rbCoding);
                    break;
                default:
                    savedBackground(rbNoResponse);
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
                    case R.id.rbNoResponse:
                        value = rbNoResponse.getText().toString();
                        setBackground(rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.rbSports:
                        isChanged = true;
                        setBackground(rbSports, rbNoResponse, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbSports.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbTourism:
                        isChanged = true;
                        setBackground(rbTourism, rbNoResponse, rbSports, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbTourism.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbBlogging:
                        isChanged = true;
                        setBackground(rbBlogging, rbNoResponse, rbSports, rbTourism, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbBlogging.getText().toString();
                        saveToServer();
                        break;

                    case R.id.rbDancing:
                        isChanged = true;
                        setBackground(rbDancing, rbNoResponse, rbSports, rbTourism, rbBlogging, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbDancing.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbReading:
                        isChanged = true;
                        setBackground(rbReading, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbReading.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbActing:
                        isChanged = true;
                        setBackground(rbActing, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbActing.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbCooking:
                        isChanged = true;
                        setBackground(rbCooking, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbSinging, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbCooking.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbSinging:
                        isChanged = true;
                        setBackground(rbSinging, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbGaming, rbCollecting, rbFashion, rbCoding);
                        value = rbSinging.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbGaming:
                        isChanged = true;
                        setBackground(rbGaming, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbCollecting, rbFashion, rbCoding);
                        value = rbGaming.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbCollecting:
                        isChanged = true;
                        setBackground(rbCollecting, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbFashion, rbCoding);
                        value = rbCollecting.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbFashion:
                        isChanged = true;
                        setBackground(rbFashion, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbCoding);
                        value = rbFashion.getText().toString();
                        saveToServer();
                        break;
                    case R.id.rbCoding:
                        isChanged = true;
                        setBackground(rbCoding, rbNoResponse, rbSports, rbTourism, rbBlogging, rbDancing, rbReading, rbActing, rbCooking, rbSinging, rbGaming, rbCollecting, rbFashion);
                        value = rbCoding.getText().toString();
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

    private void setBackground(RadioButton expectedbtn, RadioButton button_1, RadioButton button_2, RadioButton button_3, RadioButton button_4, RadioButton button_5, RadioButton button_6, RadioButton button_7, RadioButton button_8, RadioButton button_9, RadioButton button_10, RadioButton button_11, RadioButton button_12) {

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

        button_5.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_5.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_6.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_6.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_7.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_7.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_8.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_8.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_9.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_9.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_10.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_10.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_11.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_11.setTextColor(ContextCompat.getColor(activity, R.color.gray));

        button_12.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        button_12.setTextColor(ContextCompat.getColor(activity, R.color.gray));

    }

    private void dataSave() {
        if (isChanged) {
            if (AboutUpdate.result) {
                Data.userHabits = value;
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
        Intent intent = new Intent(context, HobbiesActivity.class);
        return intent;
    }
}
