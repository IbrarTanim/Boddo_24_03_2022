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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.LikeFavoriteAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Liked;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {

    private Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView favoriteRecyclerView;
    StaggeredGridLayoutManager layoutManager;
    ApiInterface apiInterface;
    LikeFavoriteAdepter likeFavoriteAdepter;
    TextView noFavoriteView;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    Liked likedMe;
    List<Liked.LikedMe> likedMeList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        noFavoriteView = view.findViewById(R.id.not_favorite_view);
        favoriteRecyclerView = view.findViewById(R.id.favorite_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        favoriteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        getAllfavourite();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllfavourite();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return view;
    }

    private void getAllfavourite() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Liked> call = apiInterface.getAllfavourite(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<Liked>() {
            @Override
            public void onResponse(Call<Liked> call, Response<Liked> response) {
                likedMe = response.body();
                if (likedMe.getStatus().equals("success")) {
                    likedMeList = likedMe.getLikedMe();

                    if (likedMeList.size() == 0){
                        favoriteRecyclerView.setVisibility(View.GONE);
                        noFavoriteView.setVisibility(View.VISIBLE);
                    }else{
                        favoriteRecyclerView.setVisibility(View.VISIBLE);
                        noFavoriteView.setVisibility(View.GONE);
                    }

                    layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    favoriteRecyclerView.setLayoutManager(layoutManager);
                    likeFavoriteAdepter = new LikeFavoriteAdepter(getContext(), likedMeList,Constants.FAVORITE_VIEW_TYPE);
                    favoriteRecyclerView.setAdapter(likeFavoriteAdepter);
                }
            }

            @Override
            public void onFailure(Call<Liked> call, Throwable t) {
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //Todo update ui
            getAllfavourite();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Data.FevoriteCount = 0;
        EventBus.getDefault().post(new Event(Constants.LIKE_FAV_SHOW));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
