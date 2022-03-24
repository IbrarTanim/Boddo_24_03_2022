package net.boddo.btm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import net.boddo.btm.Activity.AddPhotoActivity;
import net.boddo.btm.Activity.AllUsersActivity;
import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Activity.ProfileOneActivity;
import net.boddo.btm.Activity.Settings.SettingsActivity;
import net.boddo.btm.Activity.SupportWebViewActivity;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOneFragment extends Fragment implements View.OnClickListener {



    DashBoadActivity activity;
    View view;
    Intent intent;
    ImageView ivProfileOnePropicSearch, ivProfileOnePropicSupport, ivProfileOnePropicSettings, ivProfilePicture;
    CircleImageView civProfileOnePropic;
    TextView tvProfileOneEdit, tvProfileOneUserName, tvAge, tvProfileOneBalance, tvProfileOneAddCredits, tvProfileOneBoddoPlus, tvProfileOneActivate;

    String age;
    int date = 0;
    int month = 0;
    int year = 0;

    public ProfileOneFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile_one, container, false);
        }
        activity = (DashBoadActivity) getActivity();

        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = view.findViewById(R.id.blankView);
        int statusBarHeight = GetStatusBarHeight();
        if (statusBarHeight != 0) {
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = statusBarHeight;
            blankView.setLayoutParams(params);
            //Log.e(TAG, "Status Bar Height: " + statusBarHeight );
        }
        /**
         * Set
         * Status
         * Bar
         * Size
         * End
         * */

        Log.e("Palup Points", Data.userPalupPoint);
        Log.e("TAG", "Secret Key: " + Constants.SECRET_KEY);
        Log.e("TAG", "User Id: " + Data.userId);

        ivProfileOnePropicSearch = view.findViewById(R.id.ivProfileOnePropicSearch);
        ivProfileOnePropicSupport = view.findViewById(R.id.ivProfileOnePropicSupport);
        ivProfileOnePropicSettings = view.findViewById(R.id.ivProfileOnePropicSettings);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        civProfileOnePropic = view.findViewById(R.id.civProfileOnePropic);
        tvProfileOneEdit = view.findViewById(R.id.tvProfileOneEdit);
        tvProfileOneUserName = view.findViewById(R.id.tvProfileOneUserName);
        tvAge = view.findViewById(R.id.tvAge);
        tvProfileOneBalance = view.findViewById(R.id.tvProfileOneBalance);
        tvProfileOneAddCredits = view.findViewById(R.id.tvProfileOneAddCredits);
        tvProfileOneBoddoPlus = view.findViewById(R.id.tvProfileOneBoddoPlus);
        tvProfileOneActivate = view.findViewById(R.id.tvProfileOneActivate);

        convertStringToDateFormat(Data.userDateOfBirgh);
        age = getAge(year, month, date);

        tvProfileOneUserName.setText(Data.userFirstName);
        //tvAge.setText(age);
        if(Data.profilePhoto.equals("")){
            Glide.with(activity).load(R.drawable.ic_default_message_screen_profile_picture_12_07_2021).into(civProfileOnePropic);
        }else {
            Glide.with(activity).load(Data.profilePhoto).into(civProfileOnePropic);
        }

        tvProfileOneBalance.setText(Data.userPalupPoint + " Credits");


        ivProfileOnePropicSearch.setOnClickListener(this);
        ivProfileOnePropicSupport.setOnClickListener(this);
        ivProfileOnePropicSettings.setOnClickListener(this);
        ivProfilePicture.setOnClickListener(this);
        civProfileOnePropic.setOnClickListener(this);
        tvProfileOneEdit.setOnClickListener(this);
        tvProfileOneAddCredits.setOnClickListener(this);
        tvProfileOneActivate.setOnClickListener(this);
        tvProfileOneUserName.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfileOnePropicSearch:
                intent = new Intent(activity, AllUsersActivity.class);
                startActivity(intent);

                break;

            case R.id.ivProfileOnePropicSupport:
                intent = new Intent(activity, SupportWebViewActivity.class);
                startActivity(intent);

                break;

            case R.id.ivProfileOnePropicSettings:
                intent = new Intent(activity, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.ivProfilePicture:
                intent = new Intent(activity, AddPhotoActivity.class);
                startActivity(intent);
                activity.finish();
                break;


            case R.id.civProfileOnePropic:

                setActivity();

                break;

            case R.id.tvProfileOneUserName:
                setActivity();
                //setFragment(new ProfileFragment());
                break;

            case R.id.tvProfileOneEdit:

                setActivity();

                break;

            case R.id.tvProfileOneAddCredits:

                intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra("BuyCredits", true);
                activity.overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.finish();
                startActivity(intent);


                break;

            case R.id.tvProfileOneActivate:

                intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra("Membership", true);
                activity.overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.finish();
                startActivity(intent);

                break;


        }
    }


    private void setActivity() {
        Intent intent = new Intent(getActivity(), ProfileOneActivity.class);
        startActivity(intent);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).disallowAddToBackStack();
        fragmentTransaction.commit();

    }


    private void convertStringToDateFormat(String stringDate) {
      try{
          String[] stringArray = stringDate.split("/");
          date = Integer.parseInt(stringArray[0]);
          month = Integer.parseInt(stringArray[1]);
          year = Integer.parseInt(stringArray[2]);
      }catch (Exception e){}

    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = String.valueOf(ageInt);
        return ageS;
    }

    public int GetStatusBarHeight() {
        // returns 0 for no result found
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}