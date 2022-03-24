package net.boddo.btm.Activity;

import static net.boddo.btm.Utills.StaticAccess.TAG_PHOTOBLOG_ID_VALUE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Adepter.AllLikesAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Likes;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllLikesActivity extends AppCompatActivity {
    AllLikesActivity activity;
    AllLikesAdapter allLikesAdapter;
    RecyclerView rvAllLikes;
    int photoBlogIdValue;
    List<Likes.AllLike> loverList;

    TextView tvBackAllLikes;
    private ImageView loveOrLikeBTN;
    SharedPreferences sharedpreferences;

    private boolean isLiked = false;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_likes);
        activity = this;
        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = findViewById(R.id.blankView);
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
        loverList = new ArrayList<>();
        sharedpreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //get photo id
        photoBlogIdValue = sharedpreferences.getInt(TAG_PHOTOBLOG_ID_VALUE, 0);


        //initialize views
        rvAllLikes = findViewById(R.id.rvAllLikes);
        tvBackAllLikes = findViewById(R.id.tvBackAllLStory);
        loveOrLikeBTN = findViewById(R.id.all_likes_love_btn);

        //back btn
        tvBackAllLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //love or like btn
        loveOrLikeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitLikeOrHate();
            }
        });

        //get all data
        getAllLikesOrLove();
    }


    private void getAllLikesOrLove() {

        Call<Likes> call = apiInterface.getAllLikes(Constants.SECRET_KEY, photoBlogIdValue);
        call.enqueue(new Callback<Likes>() {
            @Override
            public void onResponse(Call<Likes> call, Response<Likes> response) {
                loverList = response.body().getAllLikes();
                if (response.body().getStatus().equals("success")) {

                    tvBackAllLikes.setText(String.valueOf(loverList.size() + " " + "Likes"));

                    for (Likes.AllLike allLike : loverList) {
                        if (allLike.getUserId().equals(Data.userId)) {
                            loveOrLikeBTN.setImageDrawable(null);
                            loveOrLikeBTN.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));
                            break;
                        } else {
                            loveOrLikeBTN.setImageDrawable(null);
                            loveOrLikeBTN.setImageDrawable(getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));
                        }
                    }

                    allLikesAdapter = new AllLikesAdapter(AllLikesActivity.this, loverList);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity, 1);
                    rvAllLikes.setLayoutManager(mLayoutManager);
                    rvAllLikes.setItemAnimator(new DefaultItemAnimator());
                    mLayoutManager.setAutoMeasureEnabled(false);

                    rvAllLikes.setHasFixedSize(true);
                    rvAllLikes.setLayoutManager(mLayoutManager);
                    rvAllLikes.setAdapter(allLikesAdapter);

                }
            }

            @Override
            public void onFailure(Call<Likes> call, Throwable t) {

            }
        });

    }


    private void hitLikeOrHate() {

        Call<Likes> call = apiInterface.giveLike(Constants.SECRET_KEY, Data.userId, photoBlogIdValue);
        call.enqueue(new Callback<Likes>() {
            @Override
            public void onResponse(Call<Likes> call, Response<Likes> response) {
                //nothing have to check
                Log.e("Love or like", response.body().getStatus());
                getAllLikesOrLove();
            }

            @Override
            public void onFailure(Call<Likes> call, Throwable t) {
            }
        });

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