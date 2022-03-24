package net.boddo.btm.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Adepter.ViewPageAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseIDService;
import net.boddo.btm.Utills.Connectivity;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;
import net.boddo.btm.ViewPager.CustomViewPager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.UUID;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment_3 extends Fragment implements Constants {

    private CheckBox isCheckedBox;
    private Button buttonJoin;

    ApiInterface apiInterface;

    String userName = "";
    String password = "";
    String gender = "";
    String fName = "";
    String birthDate = "";
    String emailAddress = "";
    RelativeLayout loadingContainer;
    CustomViewPager pager;
    ViewPageAdepter viewPageAdepter;


    String uniqueId = null;
    NoInternetDialog noInternetDialog;

    String userAgent;

    public SignupFragment_3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_fragment_3, container, false);
        noInternetDialog = new NoInternetDialog.Builder(getActivity()).build();

        viewPageAdepter = new ViewPageAdepter(getActivity().getSupportFragmentManager());

        pager = new CustomViewPager(getActivity());

        isCheckedBox = view.findViewById(R.id.check_box_term_and_condition);
        loadingContainer = view.findViewById(R.id.google_loading_animation_layout);
        final LottieAnimationView lottieAnimationView = view.findViewById(R.id.animation_view);
        fName = getArguments().getString(Constants.FIRST_NAME);
        userName = getArguments().getString(Constants.USER_NAME);
        password = getArguments().getString(Constants.PASSWORD);
        birthDate = getArguments().getString(Constants.DATE_OF_BIRTH);
        gender = getArguments().getString(Constants.GENDER);
        emailAddress = getArguments().getString(Constants.EMAIL);

        //User Agents initillized
        userAgent = System.getProperty("http.agent");

        buttonJoin = view.findViewById(R.id.join_button);
        isCheckedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonJoin.setEnabled(true);
                } else {
                    buttonJoin.setEnabled(false);
                }
            }
        });
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckedBox.isChecked()) {
                    if (Connectivity.isConnected(getActivity().getBaseContext()) || Connectivity.isConnectedWifi(getActivity().getBaseContext())) {
                        if (!runTimePermission()) {
                            newUserSignUp();
                        } else {
                            Toast.makeText(getActivity(), "Please turn on the network connection", Toast.LENGTH_SHORT).show();
                        }
                    }
//end of checkbox
                } else {
                    Toast.makeText(getActivity(), "Please fill the checkbox", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    private void newUserSignUp() {

        uniqueId = UUID.randomUUID().toString();
        buttonJoin.setVisibility(View.GONE);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        if (!FirebaseIDService.refreshedToken.equals("")){
            final String fcmToken = FirebaseIDService.refreshedToken;
            signUpCall(fcmToken);
        }else{
            String generateToken = FirebaseInstanceId.getInstance().getToken();
            if (!generateToken.equals("")){
                signUpCall(generateToken);
            }else{
                newUserSignUp();
            }
        }
    }

    public void signUpCall(final String fcmToken){
         String androidID = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Call<Pojo> call = apiInterface.storeNewUser(userName, emailAddress, fName, password, gender, birthDate, Constants.SECRET_KEY, uniqueId,FirebaseIDService.refreshedToken,userAgent,androidID);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                Pojo pojo = response.body();
                if (pojo.getStatus().equals("success")) {
                    final User user = pojo.getUser();
                    try {
                        loadingContainer.setVisibility(View.VISIBLE);
                        Data.userId = user.getUserId();
                        Data.userEmail = emailAddress;
                        Data.saveLoggedInData(user);
                        loadProfileImages();
                        SharedPref.setIsLoggedIn(IS_LOGGED_IN, true);
                        SharedPref.setUserId(USER_ID, user.getUserId());
                        SharedPref.setUserAccessToken(Constants.ACCESS_TOKEN, uniqueId);
                        SharedPref.setFCMToken(Constants.FCM_TOKEN,fcmToken);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    pager.setEnableSwipe(true);
                    loadingContainer.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getActivity(), DashBoadActivity.class);
                            intent.putExtra(USER_DETAILS, user);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    }, Constants.LOADING_SIGNUP_DURATION);
                } else if (pojo.getStatus().equals("failed")) {
                    loadingContainer.setVisibility(View.GONE);
                    MDToast mdToast = MDToast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                } else {
                    loadingContainer.setVisibility(View.GONE);
                    MDToast mdToast = MDToast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                    mdToast.show();
                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {
                Log.d(getActivity().getClass().getSimpleName(), "" + t.getMessage());
                loadingContainer.setVisibility(View.GONE);
                buttonJoin.setVisibility(View.GONE);
            }
        });
    }

    private void loadProfileImages() {
        Call<ProfileImageLoader> call = apiInterface.allPhotos(Data.userId, Constants.SECRET_KEY);
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



    public boolean runTimePermission() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 111);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                newUserSignUp();
            } else {
                runTimePermission();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (noInternetDialog != null) {
            noInternetDialog.onDestroy();
        }

    }
}
