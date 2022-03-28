package net.boddo.btm.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//rokan26.01.2021  import com.google.android.gms.ads.InterstitialAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.LikeFavoriteAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Liked;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LikeFragment extends Fragment {

    private Context context;
    RecyclerView likeRecyclerView;
    StaggeredGridLayoutManager layoutManager;

    ApiInterface apiInterface;
    LikeFavoriteAdepter likeFavoriteAdepter;
    ConstraintLayout notlikeView;
    CircleImageView emptyCIV;
    TextView emptyHeaderTV, emptyBodyTV;
    SwipeRefreshLayout swipeRefreshLayout;
    //rokan26.01.2021  private InterstitialAd mInterstitialAd;
    private static final String TAG = "destroy";
    String key = "like";
    int myInt;
    public LikeFragment() {
        // Required empty public constructor
    }

    Liked likedMe;
    List<Liked.LikedMe> likedMeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        notlikeView = view.findViewById(R.id.not_like_view);
        emptyCIV = view.findViewById(R.id.empty_civ);
        emptyHeaderTV = view.findViewById(R.id.empty_header_tv);
        emptyBodyTV = view.findViewById(R.id.empty_body_tv);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        likedMeList = new ArrayList<>();
        likeRecyclerView = view.findViewById(R.id.like_recyclerView);
        likeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        getAllLiked();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getAllLiked();
            }
        });


        return view;

    }

    private void getAllLiked() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Liked> call = apiInterface.getAllLiked(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<Liked>() {
            @Override
            public void onResponse(Call<Liked> call, Response<Liked> response) {
                likedMe = response.body();
                if (likedMe.getStatus().equals("success")) {
                    likedMeList = likedMe.getLikedMe();
                    if (likedMeList == null || likedMeList.isEmpty()) {
                        likeRecyclerView.setVisibility(View.GONE);
                        emptyCIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.activity_like));
                        emptyHeaderTV.setText("No likes yet.");
                        notlikeView.setVisibility(View.VISIBLE);
                    } else {
                        likeRecyclerView.setVisibility(View.VISIBLE);
                        notlikeView.setVisibility(View.GONE);

                        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        likeRecyclerView.setLayoutManager(layoutManager);
                        likeFavoriteAdepter = new LikeFavoriteAdepter(getContext(), likedMeList, "like");
                        likeRecyclerView.setAdapter(likeFavoriteAdepter);
                    }
                }else {
                    likeRecyclerView.setVisibility(View.GONE);
                    emptyCIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.activity_like));
                    emptyHeaderTV.setText("No likes yet.");
                    notlikeView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Liked> call, Throwable t) {
                likeRecyclerView.setVisibility(View.GONE);
                emptyCIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.activity_like));
                emptyHeaderTV.setText("No likes yet.");
                notlikeView.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event.getEventType().equals(Constants.UNLOCK_FAVORITE_IMAGE)) {
            getAllLiked();
        }
        if (event.getEventType().equals(Constants.LIKE_FAV_VISITOR)|| event.getEventType().equals(Constants.LIKE_FAV_SHOW)){

            getAllLiked();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        Data.LikeCount = 0;
        EventBus.getDefault().post(new Event(Constants.LIKE_FAV_SHOW));

    }


}
