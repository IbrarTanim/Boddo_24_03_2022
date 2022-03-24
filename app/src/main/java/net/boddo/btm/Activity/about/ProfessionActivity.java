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

public class ProfessionActivity extends AppCompatActivity {

    ProfessionActivity activity;
    String value = "";
    String key = "profession";
    boolean isChanged = false;

    RadioGroup radioGroup;
    RadioButton noResponse, unEmployed, apprentice, employee, civilServant, homeMaker, retired, selfEmployed, highSchoolStudent, collegeStudent;

    TextView tvSave, tvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession);
        activity = this;

        radioGroup = findViewById(R.id.radios);
        noResponse = findViewById(R.id.no_response);
        unEmployed = findViewById(R.id.Unemployed);
        apprentice = findViewById(R.id.Apprentice);
        employee = findViewById(R.id.Employee);
        civilServant = findViewById(R.id.CivilServant);
        homeMaker = findViewById(R.id.Homemaker);
        retired = findViewById(R.id.Retired);
        selfEmployed = findViewById(R.id.SelfEmployed);
        highSchoolStudent = findViewById(R.id.HighSchoolStudent);
        collegeStudent = findViewById(R.id.CollegeStudent);

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


        if (!Data.userProfession.equals("")) {
            String profession = Data.userProfession;
            switch (profession) {
                case "no_response":
                    savedBackground(noResponse);
                    break;
                case "Apprentice":
                    savedBackground(apprentice);
                    break;
                case "Unemployed":
                    savedBackground(unEmployed);
                    break;
                case "Employee":
                    savedBackground(employee);
                    break;
                case "Civil Servant":
                    savedBackground(civilServant);
                    break;
                case "Homemaker":
                    savedBackground(homeMaker);
                    break;
                case "Retired":
                    savedBackground(retired);
                    break;
                case "Self Employed":
                    savedBackground(selfEmployed);
                    break;
                case "High School Student":
                    savedBackground(highSchoolStudent);
                    break;
                case "College Student":
                    savedBackground(collegeStudent);
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
                        setBackground(noResponse, unEmployed, apprentice, employee, civilServant, homeMaker, retired, selfEmployed, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;

                    case R.id.Unemployed:
                        value = unEmployed.getText().toString();
                        setBackground(unEmployed, noResponse, apprentice, employee, civilServant, homeMaker, retired, selfEmployed, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;

                    case R.id.Apprentice:
                        value = apprentice.getText().toString();
                        setBackground(apprentice, noResponse, unEmployed, employee, civilServant, homeMaker, retired, selfEmployed, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;

                    case R.id.Employee:
                        value = employee.getText().toString();
                        setBackground(employee, noResponse, unEmployed, apprentice, civilServant, homeMaker, retired, selfEmployed, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.CivilServant:
                        value = civilServant.getText().toString();
                        setBackground(civilServant, noResponse, unEmployed, apprentice, employee, homeMaker, retired, selfEmployed, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.Homemaker:
                        value = homeMaker.getText().toString();
                        setBackground(homeMaker, noResponse, unEmployed, apprentice, employee, civilServant, retired, selfEmployed, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.Retired:
                        value = retired.getText().toString();
                        setBackground(retired, noResponse, unEmployed, apprentice, employee, civilServant, homeMaker, selfEmployed, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.SelfEmployed:
                        value = selfEmployed.getText().toString();
                        setBackground(selfEmployed, noResponse, unEmployed, apprentice, employee, civilServant, homeMaker, retired, highSchoolStudent, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.HighSchoolStudent:
                        value = highSchoolStudent.getText().toString();
                        setBackground(highSchoolStudent, noResponse, unEmployed, apprentice, employee, civilServant, homeMaker, retired, selfEmployed, collegeStudent);
                        isChanged = true;
                        saveToServer();
                        break;
                    case R.id.CollegeStudent:
                        value = collegeStudent.getText().toString();
                        setBackground(collegeStudent, noResponse, unEmployed, apprentice, employee, civilServant, homeMaker, retired, selfEmployed, highSchoolStudent);
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

    private void setBackground(RadioButton expectedbtn, RadioButton button_1, RadioButton button_2, RadioButton button_3, RadioButton button_4, RadioButton button_5, RadioButton button_6, RadioButton button_7, RadioButton button_8, RadioButton button_9) {

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

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dataSave();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ProfessionActivity.class);
        return intent;
    }


    private void dataSave() {
        if (isChanged) {
            if (AboutUpdate.result) {
                Data.userProfession = value;
                Toast.makeText(this, "Update Successfull", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void saveToServer() {
        AboutUpdate update = new AboutUpdate(this);
        update.updateAbout(key, value);
    }

}
