package net.boddo.btm.Activity;

import static net.boddo.btm.Utills.StaticAccess.TAG_PHOTOBLOG_ID_VALUE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.boddo.btm.Adepter.PhotoBlogAdapter;
import net.boddo.btm.Adepter.photoblog.PhotoLovedAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Fragment.FragmentPhotoBlogAllUser;
import net.boddo.btm.Model.AllComments;
import net.boddo.btm.Model.Likes;
import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.SearchUser;
import net.boddo.btm.test.BottomSheetNavigationComment;
import net.boddo.btm.test.PrivacyNetworkCall;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FullPhotoViewActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    FullPhotoViewActivity activity;

    ImageView imageView;
    ApiInterface apiInterface;
    String type;


    /*@BindView(R.id.comment_button)
    EditText commentButton;*/

    @BindView(R.id.love_count_text_view_photoblog)
    TextView likeCountTextView;

    ImageView ivTitleShowHide;
    ImageView ivAllLikes;
    ImageView ivFullImageHandle;

    ImageView ivFullScreen;

    boolean isImageFitToScreen;
    boolean isDescriptionShow;

    // @BindView(R.id.like_button)
    /*ImageView likeButton;
    ImageView comment_button;*/

    ImageView like_button;
    ImageView comment_button;
    RelativeLayout rlComentLikeButton;

    @BindView(R.id.massage_count_text_view)
    TextView messageCountTextView;
    /*@BindView(R.id.total_matched_count)
    TextView totalMatchedCount;*/

    /*@BindView(R.id.privacyType)
    TextView privacyType;*/

    @BindView(R.id.date_tv)
    TextView dateTV;

    LinearLayout llFullPhotoUserName;
    TextView tvUserName;

    /*@BindView(R.id.user_full_ImageView)
    PhotoView photoView;*/

    @BindView(R.id.photo_description)
    TextView description;

    @BindView(R.id.recycler_view_love)
    RecyclerView recyclerView;

    SharedPreferences sharedpreferences;


    //Updated
    @BindView(R.id.iv_back)
    ImageView backButton;

    //@BindView(R.id.profile_image_view)
    CircleImageView profileImageView;

    @BindView(R.id.tv_total_view)
    TextView totalViewCount;

    @BindView(R.id.image_details_layout)
    LinearLayout imageDetailsLayout;

    @BindView(R.id.top_navigation_layout)
    LinearLayout topLayout;

    LinearLayout llBlankWhite;

    @BindView(R.id.love_comment_count_layout)
    LinearLayout loveCommentCountLayout;

    LinearLayout llDescription;

    private boolean isSingleTap = true;
    private boolean isLikeButtonClicked = false;

    /*@Override
    public void onViewTap(View view, float x, float y) {
        if (isSingleTap) {
            imageDetailsLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            topLayout.setVisibility(View.GONE);
            loveCommentCountLayout.setVisibility(View.GONE);
            isSingleTap = false;
            //isLikeButtonClicked = true;
        } else {
            //show all views
            imageDetailsLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.VISIBLE);
            loveCommentCountLayout.setVisibility(View.VISIBLE);
            isSingleTap = true;
            //isLikeButtonClicked = false;
        }
    }*/


    String onCheckuserId;
    public static int REQUEST_CODE = 111;
    Intent userInfoDetailIntent;
    Likes likes;
    List<Likes.AllLike> allLikeList;
    AllComments allComments;
    List<AllComments.Comment> commentList;
    BottomSheetNavigationComment bottomSheetNavigationComment;
    int photoId;
    int amountOfLikes = 0;
    String amountOfComments = "";
    PhotoBlog detailsOfPhotoBlogImage;
    boolean didILikedThisPhoto = false;

    int position;

    RelativeLayout mainLayout;


    @BindView(R.id.loadingBar)
    ProgressBar progressBar;

    @BindView(R.id.more_button)
    ImageView morebutton;



    PhotoBlogAdapter adapter;
    PrivacyNetworkCall privacyNetworkCall;
    boolean isHidden = false;

    //PhotoViewAttacher mAttacher;


    List<Likes.AllLike> loverList;
    PhotoLovedAdapter loverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity = this;
        sharedpreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_full_photo_view);
        ButterKnife.bind(this);
        detailsOfPhotoBlogImage = getIntent().getExtras().getParcelable("Details");
        position = getIntent().getExtras().getInt("position");
        int posi = position;

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View view = findViewById(R.id.top_bar);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            view.setLayoutParams(params);
        }



       /* topLayout.setEnabled(false);
        topLayout.setOnClickListener(null);*/


        allLikeList = new ArrayList<>();
        commentList = new ArrayList<>();
        like_button = findViewById(R.id.like_button);
        rlComentLikeButton = findViewById(R.id.rlComentLikeButton);

        llFullPhotoUserName = findViewById(R.id.llFullPhotoUserName);
        llFullPhotoUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllData();
            }
        });
        profileImageView = findViewById(R.id.profile_image_view);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllData();
            }
        });

        comment_button = findViewById(R.id.comment_button);
        ivTitleShowHide = findViewById(R.id.ivTitleShowHide);

        int value = wordCount(detailsOfPhotoBlogImage.getDescription());
        if (value > 10) {
            ivTitleShowHide.setVisibility(View.VISIBLE);
            ivTitleShowHide.setBackgroundResource(R.drawable.description_down_icon_20_26_12_2020);
        } else {
            ivTitleShowHide.setVisibility(View.GONE);
        }

        ivFullImageHandle = findViewById(R.id.ivFullImageHandle);
        ivFullImageHandle.setBackgroundResource(R.drawable.full_screen_in_15_03_01_2021);

        tvUserName = findViewById(R.id.tvUserName);
        llDescription = findViewById(R.id.llDescription);

        llBlankWhite = findViewById(R.id.llBlankWhite);
        ivFullScreen = findViewById(R.id.ivFullScreen);
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

        ivAllLikes = findViewById(R.id.ivAllLikes);
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

        isLiked();
        loverList = new ArrayList<>();
        String id = detailsOfPhotoBlogImage.getId();
        onCheckuserId = detailsOfPhotoBlogImage.getUserId();
        privacyNetworkCall = new PrivacyNetworkCall(this);

        initValues();


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

//likeProblem


        /*commentButton.setOnClickListener(new View.OnClickListener() {*/

        //TODO BY TOUHID

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            FragmentPhotoBlogAllUser.photoBlogs[position].setLike(String.valueOf(amountOfLikes));

                        } else if (like.getStatus().equals("dislike")) {
                            like_button.setImageDrawable(getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));
                            amountOfLikes = Integer.parseInt(like.getTotalLiked());
                            likeCountTextView.setText(amountOfLikes + "");
                            FragmentPhotoBlogAllUser.photoBlogs[position].setLike(String.valueOf(amountOfLikes));
                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {
                        Log.d("FullPhotoViewActivity", t.getMessage());
                    }
                });


            }
        });


        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComments();
            }
        });

        messageCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComments();
            }
        });

        /*mAttacher.setOnViewTapListener(this);
        mAttacher = new PhotoViewAttacher(photoView);*/

    }


    private void fullImageHandle() {
        if (isImageFitToScreen) {
            topLayout.setVisibility(View.VISIBLE);
            llBlankWhite.setVisibility(View.VISIBLE);
            topLayout.setEnabled(false);
            topLayout.setOnClickListener(null);
            loveCommentCountLayout.setEnabled(false);
            loveCommentCountLayout.setOnClickListener(null);
            llDescription.setEnabled(false);
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
            llDescription.setVisibility(View.GONE);
            loveCommentCountLayout.setVisibility(View.GONE);
            like_button.setVisibility(View.GONE);
            rlComentLikeButton.setVisibility(View.GONE);
            comment_button.setVisibility(View.GONE);

            ivFullImageHandle.setBackgroundResource(R.drawable.full_screen_out_15_03_01_2021);
            isImageFitToScreen = true;
        }

    }


    //rokan
    private void writeComments() {
       /* Bundle args = new Bundle();
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

    private void isLiked() {
        //progressBar.setVisibility(View.VISIBLE);
        net.boddo.btm.Utills.ProgressDialog.show(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Likes> call = apiInterface.isLiked(Constants.SECRET_KEY, Data.userId, photoId);
        call.enqueue(new Callback<Likes>() {
            @Override
            public void onResponse(Call<Likes> call, Response<Likes> response) {
                Likes isLiked = response.body();

                Log.e("ILiked", isLiked.toString());
                //TODO api check must added by Touhid
                /**
                 *
                 * API
                 * END
                 * ISSUE
                 *
                 * NEED
                 * API
                 * RESPONSE
                 * TO
                 * CHECK
                 *
                 * **/
                /*if (isLiked.getIsLiked().equals("yes")) {
                    like_button.setImageDrawable(null);
                    like_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));
                } else if (isLiked.getIsLiked().equals("no")) {
                    like_button.setImageDrawable(getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));
                }*/
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
                //progressBar.setVisibility(View.GONE);
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
        //Glide.with(this).load(detailsOfPhotoBlogImage.getPhoto()).into(photoView);
        likeCountTextView.setText(detailsOfPhotoBlogImage.getLike());
        messageCountTextView.setText(detailsOfPhotoBlogImage.getComment());
        // totalMatchedCount.setText(detailsOfPhotoBlogImage.getMatched());
        amountOfLikes = Integer.parseInt(detailsOfPhotoBlogImage.getLike());

        /**
         *
         * Wrong
         * Logic
         * Start
         *
         * Need
         * To
         * Modify
         *
         * **/
        if (amountOfLikes > 0) {
            like_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));
        } else {
            like_button.setImageDrawable(getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));
        }

        /**
         *
         * Wrong
         * Logic
         * End
         *
         * Need
         * To
         * Modify
         *
         * **/

        Log.e("TAG", "Likes" + amountOfLikes);
        photoId = Integer.parseInt(detailsOfPhotoBlogImage.getId());
        description.setText(detailsOfPhotoBlogImage.getDescription());


        tvUserName.setText("@" + detailsOfPhotoBlogImage.getUserName());
        dateTV.setText(Helper.getLastActionTime(detailsOfPhotoBlogImage.getCreatedAt()));
        totalViewCount.setText(detailsOfPhotoBlogImage.getViews());
        Glide.with(this).load(detailsOfPhotoBlogImage.getPhoto()).into(ivFullScreen);
        Glide.with(this).load(detailsOfPhotoBlogImage.getProfilePhoto()).into(profileImageView);
    }

    @OnClick(R.id.iv_back)
    public void onBackButtonClicked() {
        finish();
    }


   /* public void likeButtonClicked() {

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
                    FragmentPhotoBlogAllUser.photoBlogs[position].setLike(String.valueOf(amountOfLikes));

                } else if (like.getStatus().equals("dislike")) {
                    like_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_love_or_like));
                    amountOfLikes = Integer.parseInt(like.getTotalLiked());
                    likeCountTextView.setText(amountOfLikes + "");
                    FragmentPhotoBlogAllUser.photoBlogs[position].setLike(String.valueOf(amountOfLikes));
                }
            }

            @Override
            public void onFailure(Call<Likes> call, Throwable t) {
                Log.d("FullPhotoViewActivity", t.getMessage());
            }
        });

    }*/


    private int wordCount(String s) {
        if (s == null)
            return 0;
        return s.trim().split("\\s+").length;
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
                    FragmentPhotoBlogAllUser.photoBlogs[position].setComment(String.valueOf(amountOfComments));
                }
            }

            @Override
            public void onFailure(Call<AllComments> call, Throwable t) {
                Log.d("FullPhotoViewActivity", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllComments();
        Log.d("FullPhotoViewActivity", "onResume");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("FullPhotoViewActivity", "onDestroy");

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
            case R.id.report_menu:
                onReportButton();
                return true;
            case R.id.public_menu:
                type = "public";
                Toast.makeText(this, "public", Toast.LENGTH_SHORT).show();
                privacyNetworkCall.onPrivacy(Constants.SECRET_KEY, type, photoId, Data.userId);
                //privacyType.setText("Public");
                return true;
            case R.id.private_menu:
                type = "private";
                Toast.makeText(this, "private", Toast.LENGTH_SHORT).show();
                privacyNetworkCall.onPrivacy(Constants.SECRET_KEY, type, photoId, Data.userId);
                //privacyType.setText("Private");
                return true;
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

                Toast.makeText(FullPhotoViewActivity.this, "Please select your reason first..", Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(FullPhotoViewActivity.this, "Report send Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FullPhotoViewActivity.this, "Report send Unsuccessful", Toast.LENGTH_SHORT).show();
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

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).disallowAddToBackStack();
        fragmentTransaction.commit();
    }


    @OnClick(R.id.love_count_text_view_photoblog)
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
                        Log.e("visible", "onResponse: visible" );
                        imageDetailsLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FullPhotoViewActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        loverAdapter = new PhotoLovedAdapter(FullPhotoViewActivity.this, loverList);
                        recyclerView.setAdapter(loverAdapter);
                        isLikeButtonClicked = true;

                    } else {
                        Log.e("visible", "onResponse: nonvisible" );

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

    private void showAllData() {
        /*Data.pd = new ProgressDialog(activity);
        Data.pd.setTitle("Loading...");
        Data.pd.setMessage("Please wait for a while...");*/
        SearchUser userProfile = new SearchUser(activity);
        Data.otherUserId = detailsOfPhotoBlogImage.getUserId();
        userProfile.searchUserInfo();
        //Data.pd.show();
    }


}
