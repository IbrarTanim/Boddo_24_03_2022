package net.boddo.btm.Activity.auth.stepperfragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valdesekamdem.library.mdtoast.MDToast;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Activity.auth.BaseCommunicator;
import net.boddo.btm.Activity.auth.model.Login;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseIDService;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.SharedPref;
import net.boddo.btm.dialog.LoadingDialog;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class BirthdateFragment extends Fragment {


    private static final String TAG = "BirthdateFragment";

    public BirthdateFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.join_button)
    Button joinButton;
    @BindView(R.id.edit_text_dateOfBirth)
    EditText dateOfBirth;


   /* @BindView(R.id.accept_terms_and_conditions)
    CheckBox checkBox;*/


    String userName, fullName, password, email, gender, uniqueId;
    String[] allInfo;
    //variable
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    String age = "";
    String bAge = "";
    ArrayList<String> userInfo;

    String fcmToken;
    String androidID;
    String userAgent;
    Period p;

    TextView more_option_text_view;
    StepperIndicator indicator;

    LoadingDialog loadingDialog;

    int day,month,year;

    private BaseCommunicator communicator;

    public static Fragment newInstance() {
        return new BirthdateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_birthdate, container, false);
        ButterKnife.bind(this, view);

        //communicator.setTitle("Date of birth");
        loadingDialog = new LoadingDialog(getContext());
        more_option_text_view = view.findViewById(R.id.more_option_text_view);
        more_option_text_view.setText(Html.fromHtml("By clicking 'JOIN' you agree with <font color=#E11D74>'Terms & Conditions'.</font> Learn about how process your data in our <font color=#E11D74>'Privacy Policy'.</font>"));


        indicator = getActivity().findViewById(R.id.stepper);
        indicator.setCurrentStep(5);

        userName = getArguments().getString(Constants.USER_NAME);
        fullName = getArguments().getString(Constants.FULL_NAME);
        password = getArguments().getString(Constants.PASSWORD);
        email = getArguments().getString(Constants.EMAIL);
        gender = getArguments().getString(Constants.GENDER);

        userInfo = new ArrayList<>();


        if (!FirebaseIDService.refreshedToken.equals("")) {
            fcmToken = FirebaseIDService.refreshedToken;
        } else {
            fcmToken = FirebaseInstanceId.getInstance().getToken();
        }


        return view;
    }

    @OnClick(R.id.edit_text_dateOfBirth)
    public void onDateOfBirthClicked() {
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int current_month = month + 1;
//              String currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
                dateOfBirth.setText(dayOfMonth + "/" + current_month + "/" + year);
                age = dayOfMonth + "/" + current_month + "/" + year;
                bAge = year+""+current_month+""+dayOfMonth;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.join_button)
    public void onJoinButtonClicked() {


        loadingDialog.showDialog();
        uniqueId = UUID.randomUUID().toString();
        androidID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        userAgent = System.getProperty("http.agent");

        Login login = new Login(userName, email, fullName, password, gender, age, Constants.SECRET_KEY, uniqueId, fcmToken, userAgent, androidID);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();



        SimpleDateFormat simpleDateForma = new SimpleDateFormat("dd/MM/yyyy");
        Log.e("today", "onCreate: "+ formatter.format(date));

        Log.e("birthdayDate", "onJoinButtonClicked: "+simpleDateForma.format(Date.parse(age)));
        Log.e("birthdayDate", "onJoinButtonClicked: "+age);
        Log.e("birthdayDate", "onJoinButtonClicked: "+bAge);

       int currentDate = Integer.parseInt(formatter.format(date));
       int birthdayDate = Integer.parseInt(bAge);

        int ageCalculate = (currentDate - birthdayDate) / 10000;

        Log.e("ageCalculate", "onJoinButtonClicked: "+currentDate+" "+birthdayDate+" "+ageCalculate);



        if(ageCalculate < 18){
            Toast.makeText(getContext(), "You have to be 18+ to use Boddo", Toast.LENGTH_SHORT).show();
            loadingDialog.hideDialog();
            return;
        }


        if (!age.equals("")) {
            Log.e("age", "onJoinButtonClicked: "+age );
            new LoginAsync().execute();
            indicator.setCurrentStep(6);
            /*if (Helper.isNetworkAvailable(getActivity())) {
                if (Helper.internetIsConnected()) {
                    // here
                } else {
                    Toast.makeText(getActivity(), "No Internet Access.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            Toast.makeText(getActivity(), "Please select your date of birth", Toast.LENGTH_SHORT).show();
        }

    /*if (!age.equals("") && checkBox.isChecked()) {
            if (Helper.isNetworkAvailable(getActivity())) {
                if (Helper.internetIsConnected()) {

                    new LoginAsync().execute();
                    indicator.setCurrentStep(6);
                } else {
                    Toast.makeText(getActivity(), "No Internet Access.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!checkBox.isChecked()) {
                Toast.makeText(getActivity(), "Please check the terms and conditions.", Toast.LENGTH_SHORT).show();
            }
        }

    */


    }

    class LoginAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Pojo> call = apiInterface.storeNewUser(userName, email, fullName, password, gender, age, Constants.SECRET_KEY, uniqueId, fcmToken, userAgent, androidID);
            call.enqueue(new Callback<Pojo>() {
                @Override
                public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                    Pojo pojo = response.body();
                    if (pojo.getStatus().equals("success")) {
                        final User user = pojo.getUser();
                        try {
                            Data.userId = user.getUserId();
                            Data.userEmail = email;
                            Data.saveLoggedInData(user);
                            loadProfileImages();
                            SharedPref.setIsLoggedIn(IS_LOGGED_IN, true);
                            SharedPref.setUserId(USER_ID, user.getUserId());
                            SharedPref.setUserAccessToken(Constants.ACCESS_TOKEN, uniqueId);
                            SharedPref.setFCMToken(Constants.FCM_TOKEN, fcmToken);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //progressbar view gone
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.hideDialog();
                                loadingDialog = null;
                                Intent intent = new Intent(getActivity(), DashBoadActivity.class);
                                intent.putExtra(USER_DETAILS, user);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }, Constants.LOADING_SIGNUP_DURATION);
                    } else if (pojo.getStatus().equals("failed")) {

                        MDToast mdToast = MDToast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                        mdToast.show();
                    } else {

                        MDToast mdToast = MDToast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                        mdToast.show();
                    }
                }

                @Override
                public void onFailure(Call<Pojo> call, Throwable t) {
                    Log.d(getActivity().getClass().getSimpleName(), "" + t.getMessage());
                    loadingDialog.hideDialog();
                }
            });
            return null;
        }
    }

    private void loadProfileImages() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<ProfileImageLoader> call = apiInterface.allPhotos(Data.userId, SECRET_KEY);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                if (response.body().getStatus().equals("success")) {
                    ProfileImageLoader photos = response.body();
                    ProfileFragment.imageList = photos.getProfileImageInfo();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (BaseCommunicator) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //communicator.setTitle("Date of birth");
        }
    }

}
