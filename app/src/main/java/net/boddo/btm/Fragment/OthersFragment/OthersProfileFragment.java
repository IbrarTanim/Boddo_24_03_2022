package net.boddo.btm.Fragment.OthersFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import net.boddo.btm.Activity.BlockListActivity;
import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.PrivateChatActivity;
import net.boddo.btm.Adepter.ProfileViewPagerAdepter;
import net.boddo.btm.Adepter.othersuser.OthersProfileImageLoader;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Model.Favorite;
import net.boddo.btm.Model.Liked;
import net.boddo.btm.Model.Love;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.test.MetchDailog;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersProfileFragment extends Fragment {

    TextView otherMoto;

    private static final String TAG = "OthersProfileFragment";
    @BindView(R.id.text_view_first_name_and_user_name)
    TextView firstNameAndUserName;
    @BindView(R.id.text_view_age_gender_country)
    TextView ageGenderCountry;
    @BindView(R.id.image_indicator)
    PageIndicatorView indecator;

    @BindView(R.id.htab_viewpager)
    ViewPager profileViewPager;
    @BindView(R.id.profile_image_view_pager)
    ViewPager imageViewPager;
    @BindView(R.id.htab_tabs)
    TabLayout tabLayout;

    public static boolean isMatch = false;

    @BindView(R.id.ivFavorite)
    ImageView ivFavorite;

    @BindView(R.id.image_view_love)
    ImageView loveButton;

    @BindView(R.id.image_view_chat)
    ImageView chatButton;

    @BindView(R.id.moto_otherProfile)
    TextView otherProfileMoto;

    @BindView(R.id.htab_toolbar)
    Toolbar toolbar;


    @BindView(R.id.text_view_total_favourite_counter)
    TextView textViewAllFavorite;
    List<Liked.LikedMe> amountOfFavoutire;


    @BindView(R.id.text_view_total_love_counter)
    TextView textViewAllLove;
    List<Liked.LikedMe> amountOfLikes;


    MetchDailog metchDailog;

    ApiInterface apiInterface;
    OthersProfileImageLoader othersProfileImageLoader;
    RelativeLayout image_view_love_layout, image_view_favorite_layout;


    int date = 0;
    int month = 0;
    int year = 0;


    int loveCount = 0;
    int favoritCount = 0;

    CircleImageView myImageView;
    CircleImageView otherUserImageView;

    public OthersProfileFragment() {
    }

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others_profile_frament, container, false);
        ButterKnife.bind(this, view);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.black));
        }
        image_view_love_layout = view.findViewById(R.id.image_view_love_layout);
        image_view_favorite_layout = view.findViewById(R.id.image_view_favorite_layout);
        toolbar.inflateMenu(R.menu.report_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.report) {
                    onReportButton();
                } else if (menuItem.getItemId() == R.id.block) {

                    onBlockButton();
                }
                return false;
            }
        });
        getAllLoved();
        getAllFavorite();

        otherMoto = view.findViewById(R.id.moto_otherProfile);
        if (!Data.otherUserMoto.equals("")) {
            otherMoto.setText(Data.otherUserMoto);
        } else {
            otherMoto.setText("Not  set yet!");
        }
        if (Data.otherUserId.equals(Data.userId)) {
            loveButton.setVisibility(View.GONE);
            ivFavorite.setVisibility(View.GONE);
            chatButton.setVisibility(View.GONE);
            image_view_favorite_layout.setVisibility(View.GONE);
            image_view_love_layout.setVisibility(View.GONE);
            toolbar.setVisibility(View.INVISIBLE);
        } else {
            textViewAllFavorite.setVisibility(View.VISIBLE);
            textViewAllLove.setVisibility(View.VISIBLE);
            if (!Data.isLoved.equals("")) {

                loveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_love_fill));
            } else {
                loveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_love));
            }
            if (!Data.isFavorite.equals("")) {
                ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_fill));
            } else {
                ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
            }
        }
        if (!Data.otherUserMoto.equals("")) {
            otherProfileMoto.setText(Data.otherUserMoto);
        } else {
            otherProfileMoto.setText("Update your status!");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);

        othersProfileImageLoader = new OthersProfileImageLoader(getContext());
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
        firstNameAndUserName.setText(Data.otherUserFirstName + " @" + Data.otherUserName);
        if (!Data.userDateOfBirgh.equals("")) {
            convertStringToDateFormat(Data.otherUserDateOfBirgh);
            String age = getAge(year, month, date);
            ageGenderCountry.setText(age + "-" + Data.otherUserGender + ", " + Data.otherUserLocation);
        }
        setupViewPager(profileViewPager);
        tabLayout.setupWithViewPager(profileViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_profile);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_gallary);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivateChatActivity.class);
                if (Data.isMatched.equals("yes")) {
                    isMatch = true;
                }
                intent.putExtra("other_user_id", Data.otherUserId);
                startActivity(intent);
            }
        });
        return view;
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
                    Toast.makeText(getActivity(), "You have blocked "+Data.otherUserName + " successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), BlockListActivity.class);
                    getActivity().finish();
                    startActivity(i);
                    EventBus.getDefault().post(new Event(Constants.BLOCKED));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.image_view_love)
    public void onLove() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
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
                            if (love.getIsLoved().equals("yes")) {
                                new LoadLoveAndFavoriteCounteInBackground().execute();
                                loveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_love_fill));
                                if (love.getIsMatched().equals("yes")) {
                                    isMatch = true;
                                    //TODO there will be a notification bar when matched
                                    final Dialog alertadd = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                                    LayoutInflater factory = LayoutInflater.from(getContext());
                                    final View view = factory.inflate(R.layout.matching_model, null);
                                    alertadd.getWindow().setBackgroundDrawableResource(android.R.color.black);

                                    alertadd.setContentView(view);
                                    Button cancelButton = view.findViewById(R.id.cancel_button);
                                    Button chatButton = view.findViewById(R.id.chat_button);
                                    TextView matchedMessage = view.findViewById(R.id.text_view_message);
                                    matchedMessage.setText("You & " + Data.otherUserName + " have liked each other and It's a match.");
                                    myImageView = view.findViewById(R.id.user_profile_photo);
                                    otherUserImageView = view.findViewById(R.id.other_user_profile_photo);
                                    Picasso.get().load(Data.otherProfilePhoto).into(otherUserImageView);
                                    Picasso.get().load(Data.profilePhoto).into(myImageView);
                                    chatButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getContext(), PrivateChatActivity.class);
                                            intent.putExtra(Constants.OTHER_USER_ID, Data.otherUserId);
                                            startActivity(intent);
                                            alertadd.dismiss();
                                        }
                                    });
                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertadd.dismiss();
                                        }
                                    });
                                    alertadd.show();
                                }
                            } else {
                                new LoadLoveAndFavoriteCounteInBackground().execute();
                                loveButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_love_mono));
                                isMatch = false;
                            }
                        } else if (love.getStatus().equals("limit expired")) {
                            if (Data.isPalupPlusSubcriber) {
                                //farabi
                                limitExpiredDialog();

                            } else {

                                //farabi
                                limitExpiredDialog();
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
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert_dialog_chat_room_option);
        final EditText editTextDescription = dialog.findViewById(R.id.edit_text_report);
        TextView title = dialog.findViewById(R.id.title);
        LinearLayout reportLayout = dialog.findViewById(R.id.report_layout);
        Button cancelButton = (Button) dialog.findViewById(R.id.button_decline);
        Button reportButton = (Button) dialog.findViewById(R.id.button_done);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<String> call = apiInterface.getReportUser(Constants.SECRET_KEY, Data.userId, Data.otherUserId, editTextDescription.getText().toString());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if (res.equals("success")) {
                            Toast.makeText(getContext(), "Report send Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Report send Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void goToPalupPlusWindow() {
        Intent intent = new Intent(getContext(), BuyCreditActivity.class);
        intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
        startActivity(intent);
    }

    @OnClick(R.id.ivFavorite)
    public void onFavoriteClicked() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Favorite> call = apiInterface.selectFavorite(Data.userId, Data.otherUserId, Constants.SECRET_KEY);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                Favorite favorite = response.body();
                if (favorite.getStatus().equals("success")) {
                    if (favorite.getIsFavorite().equals("yes")) {
                        ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_fill));
                        new LoadFavoutireInBackground().execute();
                    } else {
                        ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                        new LoadFavoutireInBackground().execute();
                    }
                } else if (favorite.getStatus().equals("limit expired")) {
                    if (Data.isPalupPlusSubcriber) {
                        PrettyDialog jj = new PrettyDialog(getContext());
                        jj.setTitle("Dear " + Data.userName);
                        String message = " you can favorite 15 profiles in a day!\n" + "But it can be changed \n" + "You can favorite 30 profiles daily";
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            jj.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary);
                        }
                        goToPalupPlusWindow(jj, message);
                    } else {
                        PrettyDialog jj = new PrettyDialog(getContext());
                        String message = " you can favorite 15 profiles in a day!\n" + "But it can be changed" + "\n" + "You can favorite 30 profiles daily";
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            jj.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary);
                        }
                        goToPalupPlusWindow(jj,message);
                    }
                }

            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void goToPalupPlusWindow(PrettyDialog jj, String message) {
        jj.setMessage(message).setMessageColor(R.color.red_A700);
        jj.addButton("ACTIVE PALUP PLUS", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                //    palupDailog();
                Intent intent = new Intent(getContext(), BuyCreditActivity.class);
                intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
                getContext().startActivity(intent);
            }
        });
        jj.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ProfileViewPagerAdepter adapter = new ProfileViewPagerAdepter(getChildFragmentManager());
        adapter.addFragment(new OtherProfileInfoFragment(), "Profile");
        adapter.addFragment(new OthersProfilePhotoFragment(), "Photos");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        setRetainInstance(true);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Data.isLoved = "";
        Data.isFavorite = "";
        Data.isMatched = "";
    }



    private void limitExpiredDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.limit_expired_dialog);

        Button button_decline = dialog.findViewById(R.id.button_decline);
        Button discoverBoddo = dialog.findViewById(R.id.discoverBoddo);
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
                        textViewAllFavorite.setText(String.valueOf(favoritCount));
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
            return null;
        }
    }

    class LoadLoveAndFavoriteCounteInBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Liked> call = apiInterface.getAllLiked(Constants.SECRET_KEY, Data.otherUserId);
            call.enqueue(new Callback<Liked>() {
                @Override
                public void onResponse(Call<Liked> call, Response<Liked> response) {
                    amountOfLikes = response.body().getLikedMe();
                    loveCount = amountOfLikes.size();
                    if (amountOfLikes != null) {
                        textViewAllLove.setText(String.valueOf(loveCount));
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

