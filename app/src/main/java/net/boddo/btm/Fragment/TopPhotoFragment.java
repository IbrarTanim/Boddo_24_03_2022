package net.boddo.btm.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.boddo.btm.Activity.photoblog.OnLoveListener;
import net.boddo.btm.Activity.photoblog.SpacesItemDecoration;
import net.boddo.btm.Adepter.PhotoBlogAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Model.Likes;
import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;

public class TopPhotoFragment extends Fragment implements OnLoveListener {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    SpacesItemDecoration decoration;

    private int pastVisibleItems, visibleItemCount;

    private boolean isHotListVisible = true;
    Intent intent;

    private Context context;

    public TopPhotoFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "TopPhotoFragment";

    StaggeredGridLayoutManager layoutManager;
    PhotoBlog[] photoBlogs;

    PhotoBlogAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_photo, container, false);
        recyclerView = view.findViewById(R.id.topBlogRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        refresh();
        intent = new Intent(Constants.SCROLLED_BROAD_CAST);

        decoration = new SpacesItemDecoration(4, 0);
        recyclerView.addItemDecoration(decoration);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                Data.TopPhotoCount = 0;
                EventBus.getDefault().post(new Event(Constants.SET_PHOTO_BLOGE_COUNT));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }
                Log.d("Scrolling", String.valueOf(pastVisibleItems));

                if (isHotListVisible) {
                    if (pastVisibleItems > 2) {
                        sentBroadCast(pastVisibleItems);
                        isHotListVisible = false;
                    }
                } else {
                    if (pastVisibleItems == 0) {
                        sentBroadCast(pastVisibleItems);
                        isHotListVisible = true;
                    }
                }
            }
        });

        return view;
    }

    private void sentBroadCast(int scrolledItemCount) {
        intent.putExtra(Constants.SCROLLED_COUNT, scrolledItemCount);
        getContext().sendBroadcast(intent);
    }

    private void refresh() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PhotoBlog[]> call = apiInterface.getAllTopPhotoBlog(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<PhotoBlog[]>() {
            @Override
            public void onResponse(Call<PhotoBlog[]> call, retrofit2.Response<PhotoBlog[]> response) {
                assert response.body() != null;
                if (response.body().length != 0){
                    photoBlogs = response.body();
                    prepareTopPhotoes();
                }
            }

            @Override
            public void onFailure(Call<PhotoBlog[]> call, Throwable t) {
            }
        });
    }


    private void prepareTopPhotoes(){
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoBlogAdapter(getContext(), photoBlogs, "TopPhoto",this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void giveLove(int position) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Likes> call = apiInterface.giveLike(Constants.SECRET_KEY, Data.userId, Integer.parseInt(photoBlogs[position].getId()));
        call.enqueue(new Callback<Likes>() {
            @Override
            public void onResponse(Call<Likes> call, retrofit2.Response<Likes> response) {
                Likes like = response.body();
                if (like.getStatus().equals("like")) {
                    int previousLiked = Integer.parseInt(photoBlogs[position].getLike()) + 1;
                    photoBlogs[position].setLike(String.valueOf(previousLiked));
                    photoBlogs[position].setIsPhotoLikedByMe(1);
                    adapter.notifyItemChanged(position, photoBlogs[position]);
                } else if (like.getStatus().equals("dislike")) {
                    int previousLiked = Integer.parseInt(photoBlogs[position].getLike()) - 1;
                    photoBlogs[position].setLike(String.valueOf(previousLiked));
                    photoBlogs[position].setIsPhotoLikedByMe(0);
                    adapter.notifyItemChanged(position, photoBlogs[position]);
                }
//                updateLoveUi(position);

            }

            @Override
            public void onFailure(Call<Likes> call, Throwable t) {
                Log.d("FullPhotoViewActivity", t.getMessage());
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
