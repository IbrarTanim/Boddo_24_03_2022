package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Adepter.RecentMatchProPicAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.RecentMatchModel;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentMatchProPicActivity extends AppCompatActivity {
    RecentMatchProPicActivity activity;

    private RecyclerView rvRecentMatch;
    private ArrayList<RecentMatchModel> recentMatchModelArrayList;
    private ApiInterface apiInterface;
    private RecentMatchProPicAdepter recentMatchAdepter;
    private TextView tvBackNewMatches, tvDiscoverUserProfileChatFragment;
    private RecentMatchModel recentMatchModel;
    private List<RecentMatchModel.Match> matchList;
    private LinearLayout llmNoMatch, llRecentMatchBlank;
    private ProgressBar recentMatchPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_match_pro_pic);
        activity = this;

        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = findViewById(R.id.blankView);
        if (Data.STATUS_BAR_HEIGHT != 0) {
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
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

        ProgressDialog.show(activity);

        rvRecentMatch = findViewById(R.id.rvRecentMatch);
        tvBackNewMatches = findViewById(R.id.tvBackNewMatches);
        tvDiscoverUserProfileChatFragment = findViewById(R.id.tvDiscoverUserProfileChatFragment);
        llmNoMatch = findViewById(R.id.llmNoMatch);
        recentMatchPB = findViewById(R.id.recentMatchPB);
        llRecentMatchBlank = findViewById(R.id.llRecentMatchBlank);
        recentMatchModelArrayList = new ArrayList<>();


        tvDiscoverUserProfileChatFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AllUsersActivity.class);
                startActivity(intent);

            }
        });


         tvBackNewMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<RecentMatchModel> call = apiInterface.getRecentMatchData(Data.userId, Constants.SECRET_KEY);
        call.enqueue(new Callback<RecentMatchModel>() {
            @Override
            public void onResponse(Call<RecentMatchModel> call, Response<RecentMatchModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getMatches().size()!=0){
                        // Testing .....
                        recentMatchModel = response.body();
                        matchList = recentMatchModel.getMatches();
                        rvRecentMatch.setVisibility(View.VISIBLE);
                        llmNoMatch.setVisibility(View.GONE);
                        //recentMatchPB.setVisibility(View.GONE);
                        //  matchList.clear();
                        recentMatchAdepter = new RecentMatchProPicAdepter(getApplicationContext(), matchList);
                        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 2);
                        rvRecentMatch.setLayoutManager(glm);
                        rvRecentMatch.setAdapter(recentMatchAdepter);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDialog.cancel();
                            }
                        }, 2000);

                    }else {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rvRecentMatch.setVisibility(View.GONE);
                                //recentMatchPB.setVisibility(View.GONE);
                                ProgressDialog.cancel();
                                llmNoMatch.setVisibility(View.VISIBLE);
                            }
                        },2000);

                    }
                }
            }

            @Override
            public void onFailure(Call<RecentMatchModel> call, Throwable t) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvRecentMatch.setVisibility(View.GONE);
                        //recentMatchPB.setVisibility(View.GONE);
                        ProgressDialog.cancel();
                        llmNoMatch.setVisibility(View.VISIBLE);
                    }
                },2000);

            }
        });
    }
}