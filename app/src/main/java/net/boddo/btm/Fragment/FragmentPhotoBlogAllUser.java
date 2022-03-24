package net.boddo.btm.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.boddo.btm.Activity.FullPhotoViewActivity;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FragmentPhotoBlogAllUser extends Fragment implements OnLoveListener {

    private static final String TAG = "FragmentPhotoBlogAllUse";

    private Context context;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public FragmentPhotoBlogAllUser() {
        // Required empty public constructor
    }

    PhotoBlogAdapter adapter;
    public static PhotoBlog[] photoBlogs;

    LayoutAnimationController animation;
    StaggeredGridLayoutManager layoutManager;

    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount;
    private boolean isHotListVisible = true;

    Intent intent;
    Bundle extras;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_blog_fragment_all_user, container, false);

        intent = new Intent(Constants.SCROLLED_BROAD_CAST);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.photoBlogRecyclerView);
        SpacesItemDecoration decoration = new SpacesItemDecoration(4, 0);
        recyclerView.addItemDecoration(decoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
//                totalItemCount = layoutManager.getItemCount();
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

        getAllPhotos();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getAllPhotos();
                Data.PhotoBlogCount = 0;
                EventBus.getDefault().post(new Event(Constants.SET_PHOTO_BLOGE_COUNT));
                getAllPhotos();
            }
        });
        return view;
    }

    private void sentBroadCast(int scrolledItemCount) {
        intent.putExtra(Constants.SCROLLED_COUNT, scrolledItemCount);
        getContext().sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FragmentPhotoBlog", "onDestroy");
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentPhotoBlog", "onResume");
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    public void getAllPhotos() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PhotoBlog[]> call = apiInterface.getAllPhotoBlogImages(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<PhotoBlog[]>() {
            @Override
            public void onResponse(Call<PhotoBlog[]> call, retrofit2.Response<PhotoBlog[]> response) {
                if (response.body().length != 0) {

                    photoBlogs = response.body();

                    for (PhotoBlog photoBlog : photoBlogs) {

                        Log.e("PhotoBlog", photoBlog.toString());

                    }
                    prepareAllPhoto();


                }
            }

            @Override
            public void onFailure(Call<PhotoBlog[]> call, Throwable t) {
            }
        });
    }

    private void prepareAllPhoto() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoBlogAdapter(getContext(), photoBlogs, "PhotoBlogAllUser", this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void viewFullPhoto(boolean isViewd, int position) {
        Intent intent = new Intent(getActivity(), FullPhotoViewActivity.class);
        intent.putExtra("Details", photoBlogs[position]);
        intent.putExtra("position", position);
        intent.putExtra(Constants.IS_PHOTO_VIEWED_BEFORE, isViewd);
        getContext().startActivity(intent);
    }

    @Override
    public void giveLove(final int position) {
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

    private void updateLoveUi(int position) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Likes> call = apiInterface.getAllLikes(Constants.SECRET_KEY, Integer.parseInt(photoBlogs[position].getId()));
        call.enqueue(new Callback<Likes>() {
            @Override
            public void onResponse(Call<Likes> call, retrofit2.Response<Likes> response) {
                List<Likes.AllLike> loverList = response.body().getAllLikes();
                if (response.body().getStatus().equals("success")) {

                    for (int i = 0; i < loverList.size(); i++) {
                        if (loverList.get(i).getUserId().equals(Data.userId)) {
                            photoBlogs[position].setIsPhotoLikedByMe(1);
                            adapter.notifyItemChanged(position, photoBlogs[position]);
                            return;
                        } else {

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Likes> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
