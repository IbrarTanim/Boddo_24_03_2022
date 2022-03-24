package net.boddo.btm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rd.PageIndicatorView;

import net.boddo.btm.Adepter.ProfileImageLoaderAdapter;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Fragment.ProfileInfoFragment;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;

public class ProfileOneActivity extends AppCompatActivity implements View.OnClickListener {
    ProfileOneActivity activity;
    String age;
    TextView motoTextViewButton, tvImageCountProfile;

    ViewPager imageViewPager;
    TextView firstNameAndUserName, tvUserNameProfile;
    TextView ageGenderCountry, tvAge, tvAboutProfile, tvGalleryProfile, tvGiftsProfile, tvStoryProfile;
    ImageView ivBackProfileOneActivity, ivProfileImage;
    PageIndicatorView indecator;
    String position = "0";

    String testData;
    ProfileImageLoaderAdapter imageLoaderAdapter;
    public static List<ProfileImageLoader.ProfileImageInfo> imageList;

    int date = 0;
    int month = 0;
    int year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_one);
        activity = this;
        ButterKnife.bind(this);

        testData = Data.userMoto;
        /*String name = getIntent().getStringExtra("name");
        int position = getIntent().getIntExtra("position",0);*/


        //Log.e("details", "onCreate: "+name+" "+position );
        imageViewPager = findViewById(R.id.profile_image_view_pager);
        firstNameAndUserName = findViewById(R.id.text_view_first_name_and_user_name);
        tvUserNameProfile = findViewById(R.id.tvUserNameProfile);
        ageGenderCountry = findViewById(R.id.text_view_age_gender_country);
        tvAge = findViewById(R.id.tvAge);
        tvAboutProfile = findViewById(R.id.tvAboutProfile);
        ivBackProfileOneActivity = findViewById(R.id.ivBackProfileOneActivity);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvGalleryProfile = findViewById(R.id.tvGalleryProfile);
        tvGiftsProfile = findViewById(R.id.tvGiftsProfile);
        tvStoryProfile = findViewById(R.id.tvStoryProfile);
        indecator = findViewById(R.id.image_indicator);
        motoTextViewButton = findViewById(R.id.moto_textView);
        tvImageCountProfile = findViewById(R.id.tvImageCountProfile);

        ivBackProfileOneActivity.setOnClickListener(this);
        ivProfileImage.setOnClickListener(this);
        tvGalleryProfile.setOnClickListener(this);
        tvGiftsProfile.setOnClickListener(this);
        tvStoryProfile.setOnClickListener(this);
        tvAboutProfile.setOnClickListener(this);

        //if(position>0){
        //Data.userMoto = name;
        //Toast.makeText(activity, "Status updated successfully", Toast.LENGTH_SHORT).show();
        motoTextViewButton.setText(Data.userMoto);
        final Dialog motoDialog = new Dialog(activity);
        motoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        motoDialog.setContentView(R.layout.custom_moto_alert_dialog);
        if (!Data.userMoto.equals("")) {
            motoTextViewButton.setText(Data.userMoto);
        } else {
            motoTextViewButton.setText("Update your status!");
        }

        //}

        final Dialog motoDialogDialog = new Dialog(activity);
        motoDialogDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        motoDialogDialog.setContentView(R.layout.custom_moto_alert_dialog);
        if (!Data.userMoto.equals("")) {
            motoTextViewButton.setText(Data.userMoto);
        } else {
            motoTextViewButton.setText("Update your status!");
        }

        motoTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editTextMoto = motoDialog.findViewById(R.id.edit_text_moto);

                if (!Data.userMoto.equals("")) {
                    editTextMoto.setText(Data.userMoto);
                    int position = editTextMoto.length();
                    Editable etext = editTextMoto.getText();
                    Selection.setSelection(etext, position);

                }
                Button cancelAbout = motoDialog.findViewById(R.id.cancel_moto);
                Button selectStatus = motoDialog.findViewById(R.id.btnSelectStatus);
                final ImageView saveMoto = motoDialog.findViewById(R.id.save_moto);
                final ImageView submitButtonTouch = motoDialog.findViewById(R.id.submitButtonTouch);
                final TextView counter = motoDialog.findViewById(R.id.counter_moto);
                editTextMoto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String valueCheck = String.valueOf(s);
                        if (valueCheck.isEmpty()) {
                            saveMoto.setVisibility(View.GONE);
                            submitButtonTouch.setVisibility(View.VISIBLE);
                        } else {
                            submitButtonTouch.setVisibility(View.GONE);
                            saveMoto.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() >= 2 && s.length() <= 50) {
                            saveMoto.setEnabled(true);
                        }
                        counter.setText(String.valueOf(s.length()));
                    }
                });
                cancelAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("cancel", "onClick: cancel");
                        motoDialog.dismiss();
                    }
                });
                selectStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext(), SelectStatusActivity.class));
                        finish();
                    }
                });
                saveMoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdate obj = new AboutUpdate(activity);
                        obj.updateAbout("moto", editTextMoto.getText().toString());
                        Data.userMoto = editTextMoto.getText().toString();
                        //Toast.makeText(activity, "Status updated successfully", Toast.LENGTH_SHORT).show();
                        motoDialog.dismiss();
                        motoTextViewButton.setText(Data.userMoto);

                        submitButtonTouch.setVisibility(View.VISIBLE);
                        saveMoto.setVisibility(View.GONE);
                    }
                });
                motoDialog.show();
                motoDialog.setCancelable(false);
            }
        });

        motoTextViewButton.setText(Data.userMoto);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity.getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);
        imageLoaderAdapter = new ProfileImageLoaderAdapter(activity);
        imageViewPager.setAdapter(imageLoaderAdapter);
        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                indecator.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        tvUserNameProfile.setText(" @" + Data.userName);

        String value = new StringBuilder(Data.userLocation).delete(0, 1).toString();
        ageGenderCountry.setText(" Current location : " + Data.userLocation);
        if (ProfileFragment.imageList != null) {
            tvImageCountProfile.setText(String.valueOf(ProfileFragment.imageList.size()));
        }

        if (!Data.userDateOfBirgh.equals("")) {
            convertStringToDateFormat(Data.userDateOfBirgh);
            age = getAge(year, month, date);
            firstNameAndUserName.setText(Data.userFirstName);
            tvAge.setText(age);

            //ageGenderCountry.setText(age + "-" + Data.userGender + ", " + Data.userLocation);
        }
        /*setupViewPager(profileViewPager);
         *//*tabLayout.setupWithViewPager(profileViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_profile);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_gallary);*/

        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.custom_photo_blog_bottom_sheet_layout);
        dialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackProfileOneActivity:
                finish();
                break;

            case R.id.tvAboutProfile:
                setFragment(new ProfileInfoFragment());
                break;

            case R.id.ivProfileImage:
                Intent intent = new Intent(activity, AddPhotoActivity.class);
                startActivity(intent);
                finish();
                break;


            case R.id.tvGalleryProfile:
                Intent intentGallary = new Intent(activity, MyBlogPhotoActivity.class);
                startActivity(intentGallary);
                finish();
                break;


            case R.id.tvGiftsProfile:
                Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tvStoryProfile:
                Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
                break;

        }
    }



    /*public void onPhotoClicked() {
        try {
            CustomBottomSheetProfile bottomSheetProfile = new CustomBottomSheetProfile();
            bottomSheetProfile.show(getChildFragmentManager(), "bottomsheetdialog");
            Log.d("ProfileFrament", "Successfull");
        } catch (Exception e) {
            Log.d("ProfileFragment", e.getMessage());
        }
    }*/


   /* private void setupViewPager(ViewPager viewPager) {
        ProfileViewPagerAdepter adapter = new ProfileViewPagerAdepter(getChildFragmentManager());
        adapter.addFragment(new ProfileInfoFragment(), "Profile");
        adapter.addFragment(new PhotoFragment(), "My blog");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        setRetainInstance(true);

    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        imageLoaderAdapter.notifyDataSetChanged();
        if (!Data.userMoto.equals("")) {
            motoTextViewButton.setText(Data.userMoto);
        } else {
            motoTextViewButton.setText("Update your status!");
        }
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }*/

    private void convertStringToDateFormat(String stringDate) {
        String[] stringArray = stringDate.split("/");
        date = Integer.parseInt(stringArray[0]);
        month = Integer.parseInt(stringArray[1]);
        year = Integer.parseInt(stringArray[2]);
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

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_frofile_one, fragment).disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPause() {
        super.onPause();
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
    }*/


}