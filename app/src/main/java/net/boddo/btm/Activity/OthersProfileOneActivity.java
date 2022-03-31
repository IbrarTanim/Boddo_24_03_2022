package net.boddo.btm.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import net.boddo.btm.Adepter.othersuser.OthersProfileImageLoader;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Fragment.OthersFragment.OtherProfileInfoFragment;
import net.boddo.btm.Model.Favorite;
import net.boddo.btm.Model.Liked;
import net.boddo.btm.Model.Love;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersProfileOneActivity extends AppCompatActivity implements View.OnClickListener {

    OthersProfileOneActivity activity;
    String age;
    TextView motoTextViewButton, tvImageCountProfile;
    public static boolean isMatch = false;
    List<Liked.LikedMe> amountOfLikes;
    int loveCount = 0;
    int favoritCount = 0;

    ApiInterface apiInterface;
    LinearLayout llOthersProfileMenue;
    ViewPager imageViewPager;
    TextView firstNameAndUserName, tvUserNameProfile;
    TextView ageGenderCountry, tvAge, tvOnlineNow;
    ImageView ivBackOthersActivity, like_button_others, ivRandomDelete, ivCommentsOtherProfile;
    TextView tvAboutOtherProfile, tvGalleryOtherProfile, tvGiftsOtherProfile, tvStoryOtherProfile, tvUserGenderProfileOthers;
    LinearLayout rlComentLikeButton;

    PageIndicatorView indecator;

    OthersProfileImageLoader othersProfileImageLoader;
    public static List<ProfileImageLoader.ProfileImageInfo> imageList;

    int date = 0;
    int month = 0;
    int year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile_one);
        activity = this;
        ButterKnife.bind(this);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        imageViewPager = findViewById(R.id.profile_image_view_pagerOthers);
        firstNameAndUserName = findViewById(R.id.text_view_first_name_and_user_nameOthers);
        tvUserNameProfile = findViewById(R.id.tvUserNameProfileOthers);
        ageGenderCountry = findViewById(R.id.text_view_age_gender_countryOthers);
        tvOnlineNow = findViewById(R.id.tvOnlineNow);
        tvAge = findViewById(R.id.tvAgeOthers);
        ivBackOthersActivity = findViewById(R.id.ivBackOthersActivity);
        llOthersProfileMenue = findViewById(R.id.llOthersProfileMenue);
        //ivProfileImage = findViewById(R.id.ivProfileImageOthers);
        ivRandomDelete = findViewById(R.id.ivRandomDelete);
        like_button_others = findViewById(R.id.like_button_others);
        ivCommentsOtherProfile = findViewById(R.id.ivCommentsOtherProfile);
        indecator = findViewById(R.id.image_indicator_others);
        motoTextViewButton = findViewById(R.id.moto_textViewOthers);
        tvImageCountProfile = findViewById(R.id.tvImageCountProfileOthers);

        tvAboutOtherProfile = findViewById(R.id.tvAboutOtherProfile);
        tvGalleryOtherProfile = findViewById(R.id.tvGalleryOtherProfile);
        tvGiftsOtherProfile = findViewById(R.id.tvGiftsOtherProfile);
        tvStoryOtherProfile = findViewById(R.id.tvStoryOtherProfile);
        tvUserGenderProfileOthers = findViewById(R.id.tvUserGenderProfileOthers);
        rlComentLikeButton = findViewById(R.id.rlComentLikeButton);

        if (Data.userId.equals(Data.otherUserId)) {

            if (rlComentLikeButton.getVisibility() == View.VISIBLE) {
                rlComentLikeButton.setVisibility(View.GONE);
            }

        }


        llOthersProfileMenue.setOnClickListener(this);
        ivBackOthersActivity.setOnClickListener(this);

        tvAboutOtherProfile.setOnClickListener(this);
        tvGalleryOtherProfile.setOnClickListener(this);
        tvGiftsOtherProfile.setOnClickListener(this);
        tvStoryOtherProfile.setOnClickListener(this);


        ivRandomDelete.setOnClickListener(this);
        like_button_others.setOnClickListener(this);
        ivCommentsOtherProfile.setOnClickListener(this);

        tvUserNameProfile.setText(" @" + Data.otherUserName);
        tvUserGenderProfileOthers.setText(Data.otherUserGender);
        ageGenderCountry.setText(" Current location : " + Data.otherUserLocation);
        firstNameAndUserName.setText(Data.otherUserFirstName);

        //Touhid changed/added
        getAllLoved();

        if (!Data.otherUserDateOfBirgh.equals("")) {
            convertStringToDateFormat(Data.otherUserDateOfBirgh);
            age = getAge(year, month, date);
            tvAge.setText(age);

        }


        final Dialog motoDialog = new Dialog(activity);
        motoDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        motoDialog.setContentView(R.layout.custom_moto_alert_dialog);
        if (!Data.otherUserMoto.equals("")) {
            motoTextViewButton.setText(Data.otherUserMoto);
        } else {
            motoTextViewButton.setText("I'm new here, let's chat!");
        }


        /*motoTextViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editTextMoto = motoDialog.findViewById(R.id.edit_text_moto);

                if (!Data.otherUserMoto.equals("")) {
                    editTextMoto.setText(Data.otherUserMoto);
                    int position = editTextMoto.length();
                    Editable etext = editTextMoto.getText();
                    Selection.setSelection(etext, position);

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
                        if (s.length() >= 2 && s.length() <= 50) {
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
                        AboutUpdate obj = new AboutUpdate(activity);
                        obj.updateAbout("moto", editTextMoto.getText().toString());
                        Data.otherUserMoto = editTextMoto.getText().toString();
                        Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        motoDialog.dismiss();
                        motoTextViewButton.setText(Data.otherUserMoto);
                    }
                });
                motoDialog.show();
            }
        });*/

        motoTextViewButton.setText(Data.otherUserMoto);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity.getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);


        othersProfileImageLoader = new OthersProfileImageLoader(activity);
        imageViewPager.setAdapter(othersProfileImageLoader);
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


        if (Data.othersImageList != null) {
            tvImageCountProfile.setText(String.valueOf(Data.othersImageList.size()));
        }


        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.custom_photo_blog_bottom_sheet_layout);
        dialog.setCanceledOnTouchOutside(false);


    }


    public void onLove() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_love_alert_dialog_with_lottie_love_loading, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Love> call = apiInterface.selectLove(Data.userId, Data.otherUserId, Constants.SECRET_KEY);
                call.enqueue(new Callback<Love>() {
                    @Override
                    public void onResponse(Call<Love> call, Response<Love> response) {
                        Love love = response.body();
                        if (love.getStatus().equals("success")) {
                            Log.e("IsLoved?", love.getIsLoved());
                            if (love.getIsLoved().equals("yes")) {
                                new LoadLoveAndFavoriteCounteInBackground().execute();
                                like_button_others.setImageDrawable(getResources().getDrawable(R.drawable.ic_love_fill));
                                if (love.getIsMatched().equals("yes")) {
                                    isMatch = true;
                                    //TODO there will be a notification bar when matched


                                    final Dialog dialog = new Dialog(activity);
                                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.matching_model);

                                    RoundedImageView myImageView = dialog.findViewById(R.id.user_profile_photo);
                                    RoundedImageView otherUserImageView = dialog.findViewById(R.id.other_user_profile_photo);

                                    Button cancelButton = dialog.findViewById(R.id.cancel_button);
                                    Button chatButton = dialog.findViewById(R.id.chat_button);
                                    TextView matchedMessage = dialog.findViewById(R.id.text_view_message);
                                    matchedMessage.setText("You & " + Data.otherUserName + " have liked each other.");

                                    Picasso.get().load(Data.otherProfilePhoto).into(otherUserImageView);
                                    Picasso.get().load(Data.profilePhoto).into(myImageView);

                                    //rokan Temporary Block
                                   /* myImageView = view.findViewById(R.id.user_profile_photo);
                                    otherUserImageView = view.findViewById(R.id.other_user_profile_photo);
                                    Picasso.get().load(Data.otherProfilePhoto).into(otherUserImageView);
                                    Picasso.get().load(Data.profilePhoto).into(myImageView);*/

                                    chatButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(activity, PrivateChatActivity.class);
                                            intent.putExtra(Constants.OTHER_USER_ID, Data.otherUserId);
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    });
                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            } else {
                                new LoadLoveAndFavoriteCounteInBackground().execute();
                                like_button_others.setImageDrawable(getResources().getDrawable(R.drawable.ic_love_mono));
                                isMatch = false;
                            }
                        } else if (love.getStatus().equals("limit expired")) {
                            if (Data.isPalupPlusSubcriber) {

                                //farabi
                                String message = "You have reached your daily like limit. For more likes activate Boddo Plus.";

                                limitExpiredDialog(message);

                            } else {

                                //farabi
                                String message = "You have reached your daily like limit. For more likes activate Boddo Plus.";

                                limitExpiredDialog(message);
                            }
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Love> call, Throwable t) {
                        Log.d("fail", t.getMessage());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.dismiss();
            }
        }, 2000);
    }

    public void onReportButton() {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.profile_report_dialog);

        RadioGroup reportReasonGroup = dialog.findViewById(R.id.profile_report_group);

        Button cancelButton = (Button) dialog.findViewById(R.id.profile_report_cancel_btn);
        Button reportButton = (Button) dialog.findViewById(R.id.profile_report_submit_btn);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedReasonBTN = reportReasonGroup.getCheckedRadioButtonId();

                if (selectedReasonBTN != -1) {

                    RadioButton selectedBTN = dialog.findViewById(selectedReasonBTN);

                    String reasonForReport = String.valueOf(selectedBTN.getText());

                    if (reasonForReport != null) {

                        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<String> call = apiInterface.getReportUser(Constants.SECRET_KEY, Data.userId, Data.otherUserId, reasonForReport);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String res = response.body();
                                if (res.equals("success")) {
                                    Toast.makeText(activity, "Report send Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, "Report send Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                        dialog.dismiss();
                    } else {
                        Toast.makeText(OthersProfileOneActivity.this, "Select your reason first.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(OthersProfileOneActivity.this, "Select your reason first.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
    }

    private void getAllFavorite() {
        new LoadFavoutireInBackground().execute();
    }

    private void getAllLoved() {
        new LoadLoveAndFavoriteCounteInBackground().execute();
    }


    private void onBlockButton() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.onBlockUser(Constants.SECRET_KEY, Data.otherUserId, Data.userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String stute = response.body();
                if (stute.equals("block")) {
                    Toast.makeText(activity, "You have blocked "+Data.otherUserName + " successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(activity, BlockListActivity.class);
                    activity.finish();
                    startActivity(i);
                    EventBus.getDefault().post(new Event(Constants.BLOCKED));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onFavoriteClicked(ImageView ivAddToFavorite, TextView tvAddToFavorite) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Favorite> call = apiInterface.selectFavorite(Data.userId, Data.otherUserId, Constants.SECRET_KEY);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                Favorite favorite = response.body();
                if (favorite.getStatus().equals("success")) {
                    if (favorite.getIsFavorite().equals("yes")) {

                        new LoadFavoutireInBackground().execute();
                        Data.isFavorite = "yes";
                        Toast.makeText(OthersProfileOneActivity.this, "Successfully added to favorite", Toast.LENGTH_SHORT).show();
                        tvAddToFavorite.setText("Cancel Favorite");
                        ivAddToFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_icon_fill_color_22_09_2021));

                    } else {

                        new LoadFavoutireInBackground().execute();
                        Data.isFavorite = "";
                        tvAddToFavorite.setText("Add to Favorite");
                        Toast.makeText(OthersProfileOneActivity.this, "Successfully removed from the favorite", Toast.LENGTH_SHORT).show();
                        ivAddToFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_icon_22_09_2021));
                    }
                } else if (favorite.getStatus().equals("limit expired")) {

                    String message = "You have reached your daily favorite limit. For more favorites activate Boddo Plus.";

                    limitExpiredDialog(message);

                    /*if (Data.isPalupPlusSubcriber) {
                        PrettyDialog jj = new PrettyDialog(activity);
                        jj.setTitle("Dear " + Data.userName);
                        String message = " you can favorite 15 profiles in a day!\n" + "But it can be changed \n" + "You can favorite 30 profiles daily";
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            jj.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary);
                        }
                        goToPalupPlusWindow(jj, message);
                    } else {
                        PrettyDialog jj = new PrettyDialog(activity);
                        String message = " you can favorite 15 profiles in a day!\n" + "But it can be changed" + "\n" + "You can favorite 30 profiles daily";
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            jj.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary);
                        }
                        goToPalupPlusWindow(jj, message);
                    }*/
                }

            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                //Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }


    public void goToPalupPlusWindow() {

        Intent intent = new Intent(activity, BuyCreditActivity.class);
        intent.putExtra("Membership", true);
        activity.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }

    public void goToPalupPlusWindow(PrettyDialog jj, String message) {
        jj.setMessage(message).setMessageColor(R.color.red_A700);
        jj.addButton("ACTIVE PALUP PLUS", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                //    palupDailog();
                Intent intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
                startActivity(intent);
            }
        });
        jj.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackOthersActivity:
                finish();
                break;

            case R.id.llOthersProfileMenue:

                favoriteReporBlockDialog();

                break;

            case R.id.ivCommentsOtherProfile:

                Intent intent = new Intent(activity, PrivateChatActivity.class);
                if (Data.isMatched.equals("yes")) {
                    isMatch = true;
                }
                intent.putExtra("other_user_id", Data.otherUserId);
                startActivity(intent);

                break;


            case R.id.like_button_others:
                /*Intent intent = new Intent(activity, PrivateChatActivity.class);
                if (Data.isMatched.equals("yes")) {
                    isMatch = true;
                }
                intent.putExtra("other_user_id", Data.otherUserId);
                startActivity(intent);*/

                onLove();

                break;

            case R.id.tvAboutOtherProfile:
                setFragment(new OtherProfileInfoFragment());
                break;

            case R.id.tvGalleryOtherProfile:
                Intent intentGallary = new Intent(activity, OthersGalleryPhotoActivity.class);
                startActivity(intentGallary);
                finish();

                break;

            case R.id.tvGiftsOtherProfile:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tvStoryOtherProfile:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;


            case R.id.ivRandomDelete:
                /*Intent intentGallary = new Intent(activity, OthersGalleryPhotoActivity.class);
                startActivity(intentGallary);
                finish();*/
                break;


        }
    }


    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_other_profile_one, fragment).disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    private void favoriteReporBlockDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.favorite_repor_block_dialog);

        TextView tvUserNameDialog = dialog.findViewById(R.id.tvUserNameDialog);
        ImageView ivAddToFavorite = dialog.findViewById(R.id.ivAddToFavorite);
        TextView tvAddToFavorite = dialog.findViewById(R.id.tvAddToFavorite);
        TextView tvReportUser = dialog.findViewById(R.id.tvReportUser);
        TextView tvBlockUser = dialog.findViewById(R.id.tvBlockUser);
        Button btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);

        tvUserNameDialog.setText(" @" + Data.otherUserName);
        tvReportUser.setText("Report" + " " + Data.otherUserName);
        tvBlockUser.setText("Block" + " " + Data.otherUserName);

        if (!Data.isFavorite.equals("")) {
            tvAddToFavorite.setText("Cancel Favorite");
            ivAddToFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_icon_fill_color_22_09_2021));
        } else {
            tvAddToFavorite.setText("Add to Favorite");
            ivAddToFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_icon_22_09_2021));
        }


        tvAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (favoritCount == 0) {
                    tvAddToFavorite.setText("Add to Favorite");
                } else {
                    tvAddToFavorite.setText("Cancel Favorite");
                }*/

                onFavoriteClicked(ivAddToFavorite, tvAddToFavorite);
                dialog.dismiss();
            }
        });


        tvReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReportButton();
                dialog.dismiss();
            }
        });


        tvBlockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBlockButton();
                dialog.dismiss();
            }
        });


        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        othersProfileImageLoader.notifyDataSetChanged();
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


    class LoadFavoutireInBackground extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<String> call = apiInterface.getFavoriteCount(Constants.SECRET_KEY, Data.otherUserId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body() != null) {
                        favoritCount = Integer.parseInt(response.body());
                        //rokan
                        // textViewAllFavorite.setText(String.valueOf(favoritCount));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
            return null;
        }
    }


    private void limitExpiredDialog(String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.limit_expired_dialog);

        TextView tvNormalInterfaceText = dialog.findViewById(R.id.tvNormalInterfaceText);
        Button button_decline = dialog.findViewById(R.id.button_decline);
        Button discoverBoddo = dialog.findViewById(R.id.discoverBoddo);
        tvNormalInterfaceText.setText(message);

        discoverBoddo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPalupPlusWindow();
                dialog.dismiss();
            }
        });
        button_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    class LoadLoveAndFavoriteCounteInBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Liked> call = apiInterface.getAllLiked(Constants.SECRET_KEY, Data.otherUserId);
            call.enqueue(new Callback<Liked>() {
                @Override
                public void onResponse(Call<Liked> call, Response<Liked> response) {
                    Liked liked = response.body();
                    Log.e("DidILiked", liked.toString());
                    amountOfLikes = response.body().getLikedMe();
                    loveCount = amountOfLikes.size();
                    if (amountOfLikes != null) {
                        //rokan
                        //textViewAllLove.setText(String.valueOf(loveCount));
                    }
                }

                @Override
                public void onFailure(Call<Liked> call, Throwable t) {

                }
            });
            return null;
        }

    }

}