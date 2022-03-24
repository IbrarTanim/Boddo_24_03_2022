package net.boddo.btm.Activity;

import static net.boddo.btm.Utills.StaticAccess.TAG_PHOTOBLOG_ID_VALUE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.boddo.btm.Adepter.photoblog.PhotoLovedAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Model.AllComments;
import net.boddo.btm.Model.Likes;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.SearchUser;
import net.boddo.btm.test.BottomSheetNavigationComment;
import net.boddo.btm.test.PrivacyNetworkCall;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullImageFromOwnProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    FullImageFromOwnProfileActivity activity;

    ApiInterface apiInterface;

    @BindView(R.id.comment_button)
    ImageView comment_button;

    @BindView(R.id.like_count_text_view)
    TextView likeCountTextView;

    @BindView(R.id.like_button)
    ImageView like_button;

    @BindView(R.id.massage_count_text_view)
    TextView messageCountTextView;

    @BindView(R.id.total_matched_count)
    TextView totalMatchedCount;

    /*@BindView(R.id.user_full_ImageView)
    PhotoView photoView;*/

    TextView tvUserName;
    ImageView ivFullScreen;
    LinearLayout topLayout, llFullPhotoUserName, llBlankWhite, llPrivacy, loveCommentCountLayout, llDescription;
    LinearLayout imageDetailsLayout;
    ImageView iv_back, ivTitleShowHide, ivAllLikes, ivFullImageHandle;
    CircleImageView profileImageView;
    RecyclerView recyclerView;
    //TODO
    RelativeLayout rlComentLikeButton;

    boolean isImageFitToScreen;
    boolean isDescriptionShow;
    private boolean isLikeButtonClicked = false;


    @BindView(R.id.photo_description)
    TextView description;

    @BindView(R.id.privacyType)
    TextView privacyType;

    List<Likes.AllLike> loverList;
    PhotoLovedAdapter loverAdapter;

    AllComments allComments;
    List<AllComments.Comment> commentList;
    BottomSheetNavigationComment bottomSheetNavigationComment;
    String type;
    String onCheckuserId;
    int photoId;
    int amountOfLikes = 0;
    String amountOfComments = "";
    PrivacyNetworkCall privacyNetworkCall;
    int position;


    @BindView(R.id.loadingBar)
    ProgressBar progressBar;

    UserPhotoBlogImages detailsOfPhotoBlogImage;
    @BindView(R.id.date_tv)
    TextView dateTv;

    /*@BindView(R.id.photo_blog_imageView)
    ImageView photoBlogImageView;*/

    /*@BindView(R.id.textView_layout)
    RelativeLayout textViewLayout;*/

   /* @BindView(R.id.br_layout)
    View brLayout;*/

    @BindView(R.id.more_button)
    ImageView moreButton;

    SharedPreferences sharedpreferences;


    /*@BindView(R.id.custom_navi_lovebar)
    RelativeLayout customNaviLovebar;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_image_from_own_profile);

        activity = this;

        sharedpreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        ButterKnife.bind(this);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View view = findViewById(R.id.top_bar);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            view.setLayoutParams(params);
        }

        tvUserName = findViewById(R.id.tvUserName);
        ivFullScreen = findViewById(R.id.ivFullScreen);
        topLayout = findViewById(R.id.top_navigation_layout);
        iv_back = findViewById(R.id.iv_back);
        profileImageView = findViewById(R.id.profile_image_view);
        llFullPhotoUserName = findViewById(R.id.llFullPhotoUserName);
        llBlankWhite = findViewById(R.id.llBlankWhite);
        llPrivacy = findViewById(R.id.llPrivacy);
        loveCommentCountLayout = findViewById(R.id.love_comment_count_layout);
        llDescription = findViewById(R.id.llDescription);
        ivTitleShowHide = findViewById(R.id.ivTitleShowHide);
        imageDetailsLayout = findViewById(R.id.image_details_layout);
        recyclerView = findViewById(R.id.recycler_view_love);
        ivAllLikes = findViewById(R.id.ivAllLikes);
        rlComentLikeButton = findViewById(R.id.rlComentLikeButton);
        ivFullImageHandle = findViewById(R.id.ivFullImageHandle);
        ivFullImageHandle.setBackgroundResource(R.drawable.full_screen_in_15_03_01_2021);

        detailsOfPhotoBlogImage = getIntent().getParcelableExtra("PhotoBlog");
        position = getIntent().getExtras().getInt("position");
        initValues();
        loverList = new ArrayList<>();
        commentList = new ArrayList<>();

        isLiked();

        onCheckuserId = detailsOfPhotoBlogImage.getUserId();
        privacyNetworkCall = new PrivacyNetworkCall(this);


        int value = wordCount(detailsOfPhotoBlogImage.getDescription());
        if (value > 10) {
            ivTitleShowHide.setVisibility(View.VISIBLE);
            ivTitleShowHide.setBackgroundResource(R.drawable.description_down_icon_20_26_12_2020);
        } else {
            ivTitleShowHide.setVisibility(View.GONE);
        }


        ivTitleShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDescriptionShow) {
                    ivTitleShowHide.setBackgroundResource(R.drawable.description_down_icon_20_26_12_2020);
                    description.setMaxLines(1);
                    isDescriptionShow = false;
                } else {
                    ivTitleShowHide.setBackgroundResource(R.drawable.description_up_icon_20_26_12_2020);
                    description.setMaxLines(5);
                    description.setMovementMethod(new ScrollingMovementMethod());
                    isDescriptionShow = true;
                }

            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(activity, MyBlogPhotoActivity.class));
                finish();

            }
        });

        ivAllLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AllLikesActivity.class);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(TAG_PHOTOBLOG_ID_VALUE, Integer.parseInt(detailsOfPhotoBlogImage.getId()));
                editor.commit();
                startActivity(intent);

            }
        });



        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllData();
            }
        });

        llFullPhotoUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllData();
            }
        });


        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fullImageHandle();
            }
        });

        ivFullImageHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fullImageHandle();
            }
        });

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComments();
            }
        });

        likeCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoveListClicked();
            }
        });


        messageCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComments();
            }
        });


    }


    //rokan

    private void writeComments() {

        /*Bundle args = new Bundle();
        args.putString(Constants.PHOTO_BLOG_PHOTO_ID, detailsOfPhotoBlogImage.getId());
        args.putString(Constants.PHOTO_BLOG_PHOTO_COMMENT_AMOUNT, detailsOfPhotoBlogImage.getComment());

        bottomSheetNavigationComment = new BottomSheetNavigationComment();
        bottomSheetNavigationComment.setArguments(args);
        bottomSheetNavigationComment.show(getSupportFragmentManager(), "bottomSheet");*/


        Intent intent = new Intent(activity, AllCommentsActivity.class);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.PHOTO_BLOG_PHOTO_ID, detailsOfPhotoBlogImage.getId());
        editor.putString(Constants.PHOTO_BLOG_PHOTO_COMMENT_AMOUNT, detailsOfPhotoBlogImage.getComment());
        editor.commit();

        startActivity(intent);
    }


    public void onLoveListClicked() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Likes> call = apiInterface.getAllLikes(Constants.SECRET_KEY, Integer.parseInt(detailsOfPhotoBlogImage.getId()));
        call.enqueue(new Callback<Likes>() {
            @Override
            public void onResponse(Call<Likes> call, Response<Likes> response) {
                loverList = response.body().getAllLikes();
                if (response.body().getStatus().equals("success")) {

                    /*recyclerView.setLayoutManager(new LinearLayoutManager(FullPhotoViewActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    loverAdapter = new PhotoLovedAdapter(FullPhotoViewActivity.this, loverList);
                    recyclerView.setAdapter(loverAdapter);*/


                    if (!isLikeButtonClicked && loverList.size() > 0) {
                        imageDetailsLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        loverAdapter = new PhotoLovedAdapter(activity, loverList);
                        recyclerView.setAdapter(loverAdapter);
                        isLikeButtonClicked = true;

                    } else {
                        imageDetailsLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        isLikeButtonClicked = false;

                    }

                }
            }

            @Override
            public void onFailure(Call<Likes> call, Throwable t) {

            }
        });
    }

//likeProblem
    private void isLiked() {

        //progressBar.setVisibility(View.VISIBLE);
        net.boddo.btm.Utills.ProgressDialog.show(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Likes> call = apiInterface.isLiked(Constants.SECRET_KEY, Data.userId, photoId);
        call.enqueue(new Callback<Likes>() {
            @Override
            public void onResponse(Call<Likes> call, Response<Likes> response) {
                Likes isLiked = response.body();
                String test = isLiked.getIsLiked();
                if (isLiked.getIsLiked().equals("yes")) {
                    like_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));
                } else if (isLiked.getIsLiked().equals("no")) {
                    like_button.setImageDrawable(getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));
                }
                //progressBar.setVisibility(View.GONE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        net.boddo.btm.Utills.ProgressDialog.cancel();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<Likes> call, Throwable t) {
                Log.d("FullPhotoViewActivity", t.getMessage());
                //.setVisibility(View.GONE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        net.boddo.btm.Utills.ProgressDialog.cancel();
                    }
                }, 2000);
            }
        });
    }

    private void initValues() {

        Glide.with(this).load(detailsOfPhotoBlogImage.getPhoto()).into(ivFullScreen);
        Glide.with(this).load(Data.profilePhoto).into(profileImageView);
        //Glide.with(this).load(detailsOfPhotoBlogImage.getProfilePhoto()).into(profileImageView);


        tvUserName.setText("@" + Data.userName);
        likeCountTextView.setText(detailsOfPhotoBlogImage.getLike());
        messageCountTextView.setText(detailsOfPhotoBlogImage.getComment());
        totalMatchedCount.setText(detailsOfPhotoBlogImage.getMatched());
        amountOfLikes = Integer.parseInt(detailsOfPhotoBlogImage.getLike());
        photoId = Integer.parseInt(detailsOfPhotoBlogImage.getId());
        description.setText(detailsOfPhotoBlogImage.getDescription());
        dateTv.setText(Helper.getLastActionTime(detailsOfPhotoBlogImage.getCreatedAt()));

        Log.e("Matched", "initValues: "+detailsOfPhotoBlogImage.getMatched() );
        Log.e("Like", "initValues: "+detailsOfPhotoBlogImage.getLike() );
        Log.e("Comment", "initValues: "+detailsOfPhotoBlogImage.getComment() );
        Log.e("id", "initValues: "+detailsOfPhotoBlogImage.getId() );
        Log.e("Description", "initValues: "+detailsOfPhotoBlogImage.getDescription() );

    }


    private int wordCount(String s) {
        if (s == null)
            return 0;
        return s.trim().split("\\s+").length;
    }


    @OnClick(R.id.like_button)
    public void likeButtonClicked() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_love_alert_dialog_with_lottie_love_loading, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Likes> call = apiInterface.giveLike(Constants.SECRET_KEY, Data.userId, photoId);
                call.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {
                        Likes like = response.body();
                        if (like.getStatus().equals("like")) {
                            amountOfLikes = Integer.parseInt(like.getTotalLiked());
                            like_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));
                            likeCountTextView.setText(amountOfLikes + "");
                            detailsOfPhotoBlogImage.setLike(String.valueOf(amountOfLikes));

                        } else if (like.getStatus().equals("dislike")) {
                            like_button.setImageDrawable(getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));
                            amountOfLikes = Integer.parseInt(like.getTotalLiked());
                            likeCountTextView.setText(amountOfLikes + "");
                            detailsOfPhotoBlogImage.setLike(String.valueOf(amountOfLikes));
                        }
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {
                        Log.d("FullPhotoViewActivity", t.getMessage());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.dismiss();
            }
        }, 1000);
    }


    public void getAllComments() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AllComments> call = apiInterface.getAllComments(Constants.SECRET_KEY, Integer.parseInt(detailsOfPhotoBlogImage.getId()));
        call.enqueue(new Callback<AllComments>() {
            @Override
            public void onResponse(Call<AllComments> call, Response<AllComments> response) {
                allComments = response.body();
                if (allComments.getStatus().equals("success")) {
                    commentList = allComments.getComment();
                    amountOfComments = String.valueOf(commentList.size());
                    messageCountTextView.setText(amountOfComments);
                    ;
                }
            }

            @Override
            public void onFailure(Call<AllComments> call, Throwable t) {
                Log.d("FullPhotoViewActivity", t.getMessage());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        if (event.getEventType().equals("updateComment")) {
            getAllComments();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void moreButton(View view) {

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.more_menu_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);


        if (Data.userId.equals(onCheckuserId)) {
            popupMenu.getMenu().findItem(R.id.delete_menu).setVisible(true);
            popupMenu.getMenu().findItem(R.id.privacy_menu).setVisible(true);
            popupMenu.getMenu().findItem(R.id.report_menu).setVisible(false);
        } else {
            popupMenu.getMenu().findItem(R.id.delete_menu).setVisible(false);
            popupMenu.getMenu().findItem(R.id.privacy_menu).setVisible(false);
            popupMenu.getMenu().findItem(R.id.report_menu).setVisible(true);
        }

        popupMenu.show();


    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {


        switch (menuItem.getItemId()) {

            case R.id.delete_menu:
                type = "delete";
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                privacyNetworkCall.onPrivacy(Constants.SECRET_KEY, type, photoId, Data.userId);

                return true;

            case R.id.public_menu:
                type = "public";
                privacyNetworkCall.onPrivacy(Constants.SECRET_KEY, type, photoId, Data.userId);
                privacyType.setText("Public");
                return true;
            case R.id.private_menu:
                type = "private";
                privacyNetworkCall.onPrivacy(Constants.SECRET_KEY, type, photoId, Data.userId);
                privacyType.setText("Private");
                return true;
            case R.id.report_menu:
                onReportButton();
            default:
                return false;
        }

    }

    public void onReportButton() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.picture_fullview_report_dialog);
        /*final EditText editTextDescription = dialog.findViewById(R.id.edit_text_report);
        LinearLayout orLayout = dialog.findViewById(R.id.or_layout);
        TextView title = dialog.findViewById(R.id.title);
        LinearLayout reportLayout = dialog.findViewById(R.id.report_layout);*/

        RadioGroup reportGroup = dialog.findViewById(R.id.report_radio_group);

        Button cancelButton = (Button) dialog.findViewById(R.id.report_cancel_btn);
        Button reportButton = (Button) dialog.findViewById(R.id.report_submit_btn);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        reportButton.setOnClickListener(v -> {


            int selectedId = reportGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {

                Toast.makeText(FullImageFromOwnProfileActivity.this, "Please select your reason first..", Toast.LENGTH_SHORT).show();

            } else {

                RadioButton selectedBTN = dialog.findViewById(selectedId);
                String reportReason = String.valueOf(selectedBTN.getText());

                if (reportReason != null) {
                    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<String> call = apiInterface.getReportUser(Constants.SECRET_KEY, Data.userId, Data.otherUserId, reportReason);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String res = response.body();
                            if (res.equals("success")) {
                                Toast.makeText(FullImageFromOwnProfileActivity.this, "Report send Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FullImageFromOwnProfileActivity.this, "Report send Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                    dialog.dismiss();
                }


            }

        });
        dialog.show();
    }


    private void fullImageHandle() {
        if (isImageFitToScreen) {
            topLayout.setVisibility(View.VISIBLE);
            llBlankWhite.setVisibility(View.VISIBLE);
            llPrivacy.setVisibility(View.VISIBLE);
            topLayout.setEnabled(false);
            topLayout.setOnClickListener(null);
            loveCommentCountLayout.setEnabled(false);
            loveCommentCountLayout.setOnClickListener(null);
            llDescription.setEnabled(false);
            llPrivacy.setEnabled(false);
            llDescription.setOnClickListener(null);
            llBlankWhite.setEnabled(false);
            llBlankWhite.setOnClickListener(null);

            llDescription.setVisibility(View.VISIBLE);
            loveCommentCountLayout.setVisibility(View.VISIBLE);
            like_button.setVisibility(View.VISIBLE);
            rlComentLikeButton.setVisibility(View.VISIBLE);
            comment_button.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 7.3f);
            params.gravity = Gravity.CENTER;

            ivFullImageHandle.setBackgroundResource(R.drawable.full_screen_in_15_03_01_2021);
            isImageFitToScreen = false;

        } else {
            imageDetailsLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            topLayout.setVisibility(View.GONE);
            llBlankWhite.setVisibility(View.GONE);
            llPrivacy.setVisibility(View.GONE);
            llDescription.setVisibility(View.GONE);
            loveCommentCountLayout.setVisibility(View.GONE);
            like_button.setVisibility(View.GONE);
            rlComentLikeButton.setVisibility(View.GONE);
            comment_button.setVisibility(View.GONE);

            ivFullImageHandle.setBackgroundResource(R.drawable.full_screen_out_15_03_01_2021);
            isImageFitToScreen = true;
        }

    }


    private void showAllData() {
        Data.pd = new ProgressDialog(activity);
        Data.pd.setTitle("Loading...");
        Data.pd.setMessage("Please wait for a while...");
        SearchUser userProfile = new SearchUser(activity);
        Data.otherUserId = detailsOfPhotoBlogImage.getUserId();
        userProfile.searchUserInfo();
        Data.pd.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
