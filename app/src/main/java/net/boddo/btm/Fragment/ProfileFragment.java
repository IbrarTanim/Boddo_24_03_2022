package net.boddo.btm.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rd.PageIndicatorView;

import net.boddo.btm.Adepter.ProfileImageLoaderAdapter;
import net.boddo.btm.Adepter.ProfileViewPagerAdepter;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    String age;
    int color;
    TextView motoTextViewButton;



    @BindView(R.id.profile_image_view_pager)
    ViewPager imageViewPager;
    @BindView(R.id.text_view_first_name_and_user_name)
    TextView firstNameAndUserName;
    @BindView(R.id.text_view_age_gender_country)
    TextView ageGenderCountry;
    @BindView(R.id.image_view_photo)
    ImageView photoImage;
    @BindView(R.id.image_indicator)
    PageIndicatorView indecator;
    @BindView(R.id.htab_viewpager)
    ViewPager profileViewPager;
    /*@BindView(R.id.htab_tabs)
    TabLayout tabLayout;*/


    ProfileImageLoaderAdapter imageLoaderAdapter;
    FloatingActionButton fabButtonAddImage;
    Context mContext;

    ApiInterface apiInterface;
    public static List<ProfileImageLoader.ProfileImageInfo> imageList;
    String[] imagesUrl;

    int date = 0;
    int month = 0;
    int year = 0;
    View view;


    //bottom sheet
    BottomSheetDialog bottomSheetDialog;

    BottomSheetBehavior bottomSheetBehavior;

    public ProfileFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.profile_fragment, container, false);
        }

        ButterKnife.bind(this, view);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.transparent));
        }
        final Dialog motoDialog = new Dialog(getContext());
        motoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        motoDialog.setContentView(R.layout.custom_moto_alert_dialog);
        motoTextViewButton = view.findViewById(R.id.moto_textView);
        if (!Data.userMoto.equals("")) {
            motoTextViewButton.setText(Data.userMoto);
        }
        else {
            motoTextViewButton.setText("Not  set yet!");
        }
        motoTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editTextMoto = motoDialog.findViewById(R.id.edit_text_moto);
                if (!Data.userMoto.equals("")) {
                    editTextMoto.setText(Data.userMoto);
                }
                Button cancelAbout = (Button) motoDialog.findViewById(R.id.cancel_moto);
                final Button saveMoto = motoDialog.findViewById(R.id.save_moto);
                final TextView counter = motoDialog.findViewById(R.id.counter_moto);
                editTextMoto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() >= 2 && s.length() <= 30) {
                            saveMoto.setEnabled(true);
                        }
                        counter.setText(String.valueOf(s.length()));
                    }
                });
                cancelAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        motoDialog.dismiss();
                    }
                });
                saveMoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutUpdate obj = new AboutUpdate(getContext());
                        obj.updateAbout("moto", editTextMoto.getText().toString());
                        Data.userMoto = editTextMoto.getText().toString();
                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        motoDialog.dismiss();
                        motoTextViewButton.setText(Data.userMoto);
                    }
                });
                motoDialog.show();
            }
        });

        motoTextViewButton.setText(Data.userMoto);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);
        imageLoaderAdapter = new ProfileImageLoaderAdapter(getContext());
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

        firstNameAndUserName.setText(Data.userFirstName + " @" + Data.userName);
        if (!Data.userDateOfBirgh.equals("")) {
            convertStringToDateFormat(Data.userDateOfBirgh);
            age = getAge(year, month, date);
            ageGenderCountry.setText(age + "-" + Data.userGender + ", " + Data.userLocation);
        }
        setupViewPager(profileViewPager);
       /* tabLayout.setupWithViewPager(profileViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_profile);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_gallary);*/

        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.custom_photo_blog_bottom_sheet_layout);
        dialog.setCanceledOnTouchOutside(false);


        return view;
    }


    @OnClick(R.id.image_view_photo)
    public void onPhotoClicked() {
        try {
            CustomBottomSheetProfile bottomSheetProfile = new CustomBottomSheetProfile();
            bottomSheetProfile.show(getChildFragmentManager(), "bottomsheetdialog");
            Log.d("ProfileFrament", "Successfull");
        } catch (Exception e) {
            Log.d("ProfileFragment", e.getMessage());
        }
    }

    private void setupViewPager(ViewPager viewPager) {
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

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        imageLoaderAdapter.notifyDataSetChanged();
        if (!Data.userMoto.equals("")) {
            motoTextViewButton.setText(Data.userMoto);
        }
        else {
            motoTextViewButton.setText("Not  set yet!");
        }
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
        }
    }

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPause() {
        super.onPause();
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }

}