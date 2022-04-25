package net.boddo.btm.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.PhotoBlogTabAdapter;
import net.boddo.btm.R;


public class TransActionsFragment extends Fragment {

    BuyCreditActivity activity;
    private TabLayout transactionTablayout;
    private ViewPager transactionViewPager;
    TextView tvBackTransaction;

    private PhotoBlogTabAdapter adapter;

    public TransActionsFragment(BuyCreditActivity activity) {
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trans_actions, container, false);

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

        transactionTablayout = view.findViewById(R.id.transaction_tablayout);
        transactionViewPager = view.findViewById(R.id.transaction_viewPager);
        tvBackTransaction = view.findViewById(R.id.tvBackTransaction);
        tvBackTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goBackFromTransaction();
            }
        });

        adapter = new PhotoBlogTabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new TransactionHistoryFragment(), "Credit History");
        adapter.addFragment(new PurchaseHistoryFragment(), "Purchase History");

        transactionViewPager.setAdapter(adapter);
        transactionTablayout.setupWithViewPager(transactionViewPager);
        transactionViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                EventBus.getDefault().postSticky(new Event("TransectionCall"));

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        PhotoBlogTabAdapter pagerAdapter = new PhotoBlogTabAdapter(getChildFragmentManager());
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
