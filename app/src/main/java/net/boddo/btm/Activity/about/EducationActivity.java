package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class EducationActivity extends AppCompatActivity {

    EducationActivity activity;

    String value = "";
    String key = "education";

    @BindView(R.id.radios)
    RadioGroup radioGroup;

    @BindView(R.id.no_response)
    RadioButton noResponse;

    @BindView(R.id.no_degree)
    RadioButton noDegree;

    @BindView(R.id.special_school)
    RadioButton specialSchool;

    @BindView(R.id.some_high_school)
    RadioButton someHighSchool;

    @BindView(R.id.associate_degree)
    RadioButton associateDegree;

    @BindView(R.id.high_school_graduate)
    RadioButton highSchoolGraduate;

    @BindView(R.id.some_college_studies)
    RadioButton someCollegeStudies;

    @BindView(R.id.current_college_student)
    RadioButton currentCollegeStudent;

    @BindView(R.id.bachelor_degree)
    RadioButton bachelorDegree;

    @BindView(R.id.master_degree)
    RadioButton masterDegree;

    @BindView(R.id.phd_md_post_doctorate)
    RadioButton phdMDPostDoctorate;

    @BindView(R.id.other)
    RadioButton other;


    TextView tvEducationSave, tvBackEducation;

    boolean isChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        activity = this;
        ButterKnife.bind(this);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        tvEducationSave = findViewById(R.id.tvEducationSave);
        tvBackEducation = findViewById(R.id.tvBackEducation);
        tvEducationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSave();
            }
        });

        tvBackEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!Data.userEducation.equals("")) {
            String education = Data.userEducation;
            switch (education) {
                case "No response":
                    savedBackground(noResponse);
                    break;
                case "No degree":
                    savedBackground(noDegree);
                    break;
                case "Special school":
                    savedBackground(specialSchool);
                    break;
                case "Some high school":
                    savedBackground(someHighSchool);
                    break;
                case "Associate degree":
                    savedBackground(associateDegree);
                    break;
                case "High school graduate":
                    savedBackground(highSchoolGraduate);
                    break;
                case "Some college studies":
                    savedBackground(someCollegeStudies);
                    break;
                case "Current college student":
                    savedBackground(currentCollegeStudent);
                    break;
                case "Bachelor's degree":
                    savedBackground(bachelorDegree);
                    break;
                case "Master's degree":
                    savedBackground(masterDegree);
                    break;
                case "Phd/MD/Post doctorate":
                    savedBackground(phdMDPostDoctorate);
                    break;
                case "Other":
                    savedBackground(other);
                    break;
            }
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.no_response:
                        value = noResponse.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(noResponse, noDegree, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;
                    case R.id.no_degree:
                        value = noDegree.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(noDegree, noResponse, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;
                    case R.id.special_school:
                        value = specialSchool.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(specialSchool, noResponse, noDegree, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;
                    case R.id.some_high_school:
                        value = someHighSchool.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(someHighSchool, noResponse, noDegree, specialSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;
                    case R.id.associate_degree:
                        value = associateDegree.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(associateDegree, noResponse, noDegree, specialSchool, someHighSchool, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;

                    case R.id.high_school_graduate:
                        value = highSchoolGraduate.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(highSchoolGraduate, noResponse, noDegree, specialSchool, someHighSchool, associateDegree, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;

                    case R.id.some_college_studies:
                        value = someCollegeStudies.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(someCollegeStudies, noResponse, noDegree, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;

                    case R.id.current_college_student:
                        value = currentCollegeStudent.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(currentCollegeStudent, noResponse, noDegree, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, bachelorDegree, masterDegree, phdMDPostDoctorate, other);
                        break;

                    case R.id.bachelor_degree:
                        value = bachelorDegree.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(bachelorDegree, noResponse, noDegree, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, masterDegree, phdMDPostDoctorate, other);
                        break;

                    case R.id.master_degree:
                        value = masterDegree.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(masterDegree, noResponse, noDegree, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, phdMDPostDoctorate, other);
                        break;

                    case R.id.phd_md_post_doctorate:
                        value = phdMDPostDoctorate.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(phdMDPostDoctorate, noResponse, noDegree, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, other);
                        break;

                    case R.id.other:
                        value = other.getText().toString();
                        isChanged = true;
                        saveToServer();
                        setBackground(other, noResponse, noDegree, specialSchool, someHighSchool, associateDegree, highSchoolGraduate, someCollegeStudies, currentCollegeStudent, bachelorDegree, masterDegree, phdMDPostDoctorate);
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

    private void dataSave() {
        if (isChanged) {
            if (AboutUpdate.result) {
                Data.userEducation = value;
                Toast.makeText(this, "Update Successfull", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void savedBackground(RadioButton savedButton) {
        savedButton.setBackgroundColor(ContextCompat.getColor(activity, R.color.app_color));
        savedButton.setTextColor(ContextCompat.getColor(activity, R.color.white));
    }

    private void setBackground(RadioButton expectedbtn, RadioButton button_1, RadioButton button_2, RadioButton button_3, RadioButton button_4, RadioButton button_5, RadioButton button_6, RadioButton button_7, RadioButton button_8, RadioButton button_9, RadioButton button_10, RadioButton button_11) {

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

    }


    private void saveToServer() {
        if (isChanged) {
            AboutUpdate update = new AboutUpdate(this);
            update.updateAbout(key, value);
            Log.e("key_value", "saveToServer: "+key+"\n"+value );
        }
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, EducationActivity.class);
        return intent;
    }

}
