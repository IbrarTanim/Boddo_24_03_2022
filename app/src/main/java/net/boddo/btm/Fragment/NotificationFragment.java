package net.boddo.btm.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nex3z.notificationbadge.NotificationBadge;

import net.boddo.btm.Adepter.PhotoBlogTabAdapter;
import net.boddo.btm.Event.Event;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment {


    private Context context;
    @BindView(R.id.badging_like_view)
    NotificationBadge badgingLikeView;
    @BindView(R.id.badging_favorite_view)
    NotificationBadge badgingFavoriteView;
    @BindView(R.id.badging_visitors_view)
    NotificationBadge badgingVisitorsView;

    public NotificationFragment() {
    }

    private TabLayout likeFavoTablayout;
    private ViewPager likeFavoViewPager;
    private PhotoBlogTabAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        ProgressDialog.show(context);
        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = view.findViewById(R.id.blankView);
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

        likeFavoTablayout = view.findViewById(R.id.like_favo_tablayout);
        likeFavoViewPager = view.findViewById(R.id.like_favo_viewPager);

        badgingLikeView.setNumber(Data.LikeCount);
        badgingFavoriteView.setNumber(Data.FevoriteCount);
        badgingVisitorsView.setNumber(Data.VisitorCount);

        adapter = new PhotoBlogTabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new LikeFragment(), "LIKES");
        adapter.addFragment(new FavoriteFragment(), "FAVORITES");
        adapter.addFragment(new VisitorsFragment(), "VISITORS");

        likeFavoViewPager.setAdapter(adapter);
        likeFavoTablayout.setupWithViewPager(likeFavoViewPager);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDialog.cancel();
            }
        }, 2000);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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

        if (event.getEventType().equals(Constants.LIKE_FAV_VISITOR)|| event.getEventType().equals(Constants.LIKE_FAV_SHOW)){

            badgingLikeView.setNumber(Data.LikeCount);
            badgingFavoriteView.setNumber(Data.FevoriteCount);
            badgingVisitorsView.setNumber(Data.VisitorCount);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Data.LikeCount = 0;
        Data.FevoriteCount = 0;
        Data.VisitorCount = 0;
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


