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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorsFragment extends Fragment {

    private Context context;
    RecyclerView visitorRecyclerView;
    StaggeredGridLayoutManager layoutManager;

    ApiInterface apiInterface;
    LikeFavoriteAdepter likeFavoriteAdepter;
    SwipeRefreshLayout swipeRefreshLayout;
    ConstraintLayout notVisitorView;
    CircleImageView emptyCIV;
    TextView emptyHeaderTV, emptyBodyTV;

    public VisitorsFragment() {
        // Required empty public constructor
    }

    Liked likedMe;
    List<Liked.LikedMe> likedMeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visitors, container, false);

        notVisitorView = view.findViewById(R.id.not_visitor_view);
        emptyCIV = view.findViewById(R.id.empty_civ);
        emptyHeaderTV = view.findViewById(R.id.empty_header_tv);
        emptyBodyTV = view.findViewById(R.id.empty_body_tv);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        visitorRecyclerView = view.findViewById(R.id.visitor_recyclerView);
        visitorRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        getAll_visitors();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAll_visitors();
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

    private void getAll_visitors() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Liked> call = apiInterface.getAllvisitor(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<Liked>() {
            @Override
            public void onResponse(Call<Liked> call, Response<Liked> response) {
                likedMe = response.body();
                if (likedMe.getStatus().equals("success")) {
                    likedMeList = likedMe.getLikedMe();

                    if (likedMeList.size() == 0) {
                        visitorRecyclerView.setVisibility(View.GONE);
                        emptyCIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.activity_visitor));
                        emptyHeaderTV.setText("No visitors yet.");
                        notVisitorView.setVisibility(View.VISIBLE);
                    } else {
                        visitorRecyclerView.setVisibility(View.VISIBLE);
                        notVisitorView.setVisibility(View.GONE);
                    }
                    layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    visitorRecyclerView.setLayoutManager(layoutManager);
                    likeFavoriteAdepter = new LikeFavoriteAdepter(getContext(), likedMeList, "visitors");
                    visitorRecyclerView.setAdapter(likeFavoriteAdepter);
                }else {
                    visitorRecyclerView.setVisibility(View.GONE);
                    emptyCIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.activity_visitor));
                    emptyHeaderTV.setText("No visitors yet.");
                    notVisitorView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Liked> call, Throwable t) {
                visitorRecyclerView.setVisibility(View.GONE);
                emptyCIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.activity_visitor));
                emptyHeaderTV.setText("No visitors yet.");
                notVisitorView.setVisibility(View.VISIBLE);
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
            getAll_visitors();
        }
        if (event.getEventType().equals(Constants.LIKE_FAV_VISITOR)|| event.getEventType().equals(Constants.LIKE_FAV_SHOW)){
            getAll_visitors();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Data.VisitorCount = 0;
        EventBus.getDefault().post(new Event(Constants.LIKE_FAV_SHOW));
    }

}
