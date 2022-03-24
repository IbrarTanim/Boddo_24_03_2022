package net.boddo.btm.Activity.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseIDService;
import net.boddo.btm.Utills.Connectivity;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.EditTextToString;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.SharedPref;
import net.boddo.btm.dialog.LoadingDialog;
import net.boddo.btm.test.TestAlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.boddo.btm.Utills.Constants.IS_LOGGED_IN;
import static net.boddo.btm.Utills.Constants.SECRET_KEY;
import static net.boddo.btm.Utills.Constants.USER_DETAILS;
import static net.boddo.btm.Utills.Constants.USER_ID;

public class RegistrationSecondStepActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationSecondStepA";
    Intent intent;
    private String userName, password;
    public String age = "";

    @BindView(R.id.button_join)
    Button buttonJoin;

    @BindView(R.id.edit_text_fullname)
    EditText editTextFirstName;

    @BindView(R.id.edit_text_email)
    EditText editTextEmail;

    @BindView(R.id.edit_text_dateOfBirth)
    EditText dateOfBirth;

    @BindView(R.id.genderGroup)
    RadioGroup radioGroup;

    @BindView(R.id.male)
    RadioButton radioMale;
    @BindView(R.id.female)
    RadioButton radioFemale;

    @BindView(R.id.accept_terms_and_conditions)
    CheckBox checkBox;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    String gender = "";
    String fName = "";
    String birthDate = "";
    String emailAddress = "";

    ApiInterface apiInterface;

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    boolean isEmailExist = false;

    String userAgent;
    String uniqueId = null;
    String androidID;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_second_step);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);// action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Create New Account");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));



        loadingDialog = new LoadingDialog(this);

        intent = getIntent();
        if (intent.hasExtra(Constants.USER_NAME) && intent.hasExtra(Constants.PASSWORD)) {
            userName = intent.getStringExtra(Constants.USER_NAME);
            password = intent.getStringExtra(Constants.PASSWORD);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        gender = radioMale.getText().toString();
                        break;
                    case R.id.female:
                        // do operations specific to this selection
                        gender = radioFemale.getText().toString();
                        break;
                }
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                emailAddress = editTextEmail.getText().toString().trim();
                if (Connectivity.isConnected(RegistrationSecondStepActivity.this) || Connectivity.isConnectedWifi(RegistrationSecondStepActivity.this) && isValidEmail(emailAddress)) {
                    if (emailAddress.length() >= 10) {
                        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<String> call = apiInterface.checkUserEmailExist(emailAddress, SECRET_KEY);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = response.body();
                                if (result.equals("Email Already Exist")) {
                                    isEmailExist = true;
                                    editTextEmail.setError(getResources().getString(R.string.email_already_exist));
                                } else {
                                    isEmailExist = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d(RegistrationSecondStepActivity.this.getClass().getSimpleName(), t.getMessage().toString());
                            }
                        });
                    }

                }
            }
        });
    }

    @OnClick(R.id.button_join)
    public void onJoinButtonClicked() {
        loadingDialog.showDialog();
        age = dateOfBirth.getText().toString();
        fName = EditTextToString.etToString(editTextFirstName);
        birthDate = EditTextToString.etToString(dateOfBirth);
        emailAddress = EditTextToString.etToString(editTextEmail);
        if (!fName.equals("") && !birthDate.equals("") && !emailAddress.equals("") && !gender.equals("")) {
            if (Integer.parseInt(Helper.getAge(age)) >= 14) {
                if (fName.length() >= 5) {
                    if (isValidEmail(emailAddress) && isEmailExist == false) {
                        if (checkBox.isChecked()) {
                            if (!isEmailExist) {
                                newUserSignUp();
                            } else {
                                editTextEmail.setError("Email Already Exist.");
                            }
                        } else {
                            MDToast mdToast = MDToast.makeText(RegistrationSecondStepActivity.this, "Please accept the terms & conditions.", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                            mdToast.show();
                        }
                    } else {
                        editTextEmail.setError("Email is not valid");
                    }
                } else {
                    if (fName.length() < 5)
                        editTextFirstName.setError("First Name is too short");
                }
            } else {
                showDialog();
            }
        } else {
            if (fName.equals(""))
                editTextFirstName.setError("Full Name mush not be empty.");
            if (birthDate.equals(""))
                dateOfBirth.setError("Birth Date is Empty");
            if (emailAddress.equals(""))
                editTextEmail.setError("Email is empty");
        }


    }

    @OnClick(R.id.edit_text_dateOfBirth)
    public void onDateOfBirthClicked() {
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(RegistrationSecondStepActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int current_month = month + 1;
//                String currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
                dateOfBirth.setText(dayOfMonth + "/" + current_month + "/" + year);
                age = dayOfMonth + "/" + current_month + "/" + year;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void showDialog() {
        TestAlertDialog testAlertDialog = new TestAlertDialog();
        testAlertDialog.show(getSupportFragmentManager(), "testAlertDialog");
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void newUserSignUp() {

        uniqueId = UUID.randomUUID().toString();
        buttonJoin.setVisibility(View.GONE);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        if (!FirebaseIDService.refreshedToken.equals("")) {
            final String fcmToken = FirebaseIDService.refreshedToken;
            signUpCall(fcmToken);
        } else {
            String generateToken = FirebaseInstanceId.getInstance().getToken();
            if (!generateToken.equals("")) {
                signUpCall(generateToken);
            }
        }
    }

    public void signUpCall(final String fcmToken) {
        //User Agents initillized
        try{
             androidID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            userAgent = System.getProperty("http.agent");
        }catch (Exception e){
            Log.e(TAG, "signUpCall: "+e.getMessage() );
        }
        Call<Pojo> call = apiInterface.storeNewUser(userName, emailAddress, fName, password, gender, birthDate, SECRET_KEY, uniqueId, FirebaseIDService.refreshedToken, userAgent, androidID);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                Pojo pojo = response.body();
                if (pojo.getStatus().equals("success")) {
                    final User user = pojo.getUser();
                    try {
                        Data.userId = user.getUserId();
                        Data.userEmail = emailAddress;
                        Data.saveLoggedInData(user);
                        loadProfileImages();
                        SharedPref.setIsLoggedIn(IS_LOGGED_IN, true);
                        SharedPref.setUserId(USER_ID, user.getUserId());
                        SharedPref.setUserAccessToken(Constants.ACCESS_TOKEN, uniqueId);
                        SharedPref.setFCMToken(Constants.FCM_TOKEN, fcmToken);
                    } catch (Exception e) {
                        Toast.makeText(RegistrationSecondStepActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //progressbar view gone
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.hideDialog();
                            loadingDialog = null;
                            Intent intent = new Intent(RegistrationSecondStepActivity.this, DashBoadActivity.class);
                            intent.putExtra(USER_DETAILS, user);
                            startActivity(intent);
                            RegistrationSecondStepActivity.this.finish();
                        }
                    }, Constants.LOADING_SIGNUP_DURATION);
                } else if (pojo.getStatus().equals("failed")) {

                    MDToast mdToast = MDToast.makeText(RegistrationSecondStepActivity.this, getResources().getString(R.string.something_went_wrong), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                } else {

                    MDToast mdToast = MDToast.makeText(RegistrationSecondStepActivity.this, getResources().getString(R.string.something_went_wrong), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                }
            }
            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {
                Log.d(RegistrationSecondStepActivity.this.getClass().getSimpleName(), "" + t.getMessage());
                loadingDialog.hideDialog();
            }
        });
    }

    private void loadProfileImages() {
        Call<ProfileImageLoader> call = apiInterface.allPhotos(Data.userId, SECRET_KEY);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                if (response.body().getStatus().equals("success")) {
                    ProfileImageLoader photos = response.body();
                    ProfileFragment.imageList = photos.getProfileImageInfo();
                } else {
                    Toast.makeText(RegistrationSecondStepActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
