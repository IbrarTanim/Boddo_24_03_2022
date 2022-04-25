package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import net.boddo.btm.Activity.Settings.SettingsActivity;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.PhotoBlogTabAdapter;
import net.boddo.btm.Fragment.BuyCreditsFragment;
import net.boddo.btm.Fragment.PalupPlusFragment;
import net.boddo.btm.Fragment.TransActionsFragment;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;

public class BuyCreditActivity extends AppCompatActivity {
    BuyCreditActivity activity;
    Intent intent;

    private PhotoBlogTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static boolean isConsumable = false;
    public static boolean isSubscribed = false;
    public static boolean isTransaction = false;

    public boolean isFirstTimePurchase = false;
    public boolean isFirstTimeSubscriptionPage = false;
    public boolean isFirstTimeInTransactionHistoryPage = false;

    BuyCreditsFragment buyCreditFragment;
    PalupPlusFragment palupPlusFragment;
    TransActionsFragment transActionsFragment;
    boolean isMembership;
    boolean isMyTransaction;
    boolean isBuyCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_credit);
        activity = this;

        /*viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new PhotoBlogTabAdapter(getSupportFragmentManager());*/




        /*Bundle extras = getIntent().getExtras();
        if(extras!=null && extras.containsKey("flag")) {
            boolean flag = extras.getBoolean("flag");
        }
        if(flag){
            currentFragment = getOneFragment();
        }*/


        Bundle extras = getIntent().getExtras();
        if (extras != null)
            isMembership = extras.getBoolean("Membership");
        isMyTransaction = extras.getBoolean("MyTransaction");
        isBuyCredits = extras.getBoolean("BuyCredits");
        if (isMembership) {
            setFragment(new PalupPlusFragment(activity));
        } else if (isMyTransaction) {
            setFragment(new TransActionsFragment(activity));
        } else if (isBuyCredits) {
            setFragment(new BuyCreditsFragment(activity));
        }





        /*adapter.addFragment(buyCreditFragment, "Buy Credits");
        adapter.addFragment(palupPlusFragment, "Palup Plus");
        adapter.addFragment(transActionsFragment, "TransActions");*/

        /*viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        isConsumable = false;
                        isSubscribed = false;
                        isTransaction = false;
                        break;
                    case 1:
                        isSubscribed = false;
                        isConsumable = false;
                        isTransaction = false;
                        break;
                    case 2:
                        isConsumable = false;
                        isConsumable = false;
                        isSubscribed = false;
                        EventBus.getDefault().post(new Event("TransectionCall"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });*/


       /* tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2){
                    EventBus.getDefault().post(new Event("TransectionCall"));
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.setAdapter(adapter);

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        },500);
        if (getIntent().hasExtra(Constants.PALUP_PLUS)) {
            if (getIntent().getStringExtra(Constants.PALUP_PLUS).equals(Constants.PALUP_PLUS)) {
                viewPager.setCurrentItem(1);
            }
        } else {
            viewPager.setCurrentItem(0);
        }
        tabLayout.setupWithViewPager(viewPager);*/
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* startActivity(new Intent(activity, SettingsActivity.class));
        finish();*/

        //finish();
        //goBack();

    }



    public void goBack(){
        intent = new Intent(activity, SettingsActivity.class);
        intent.putExtra("BuyCredits", isBuyCredits);
        intent.putExtra("MyTransaction", isMyTransaction);
        intent.putExtra("Membership", isMembership);
        startActivity(intent);
        //finish();
    }

    public void goBackFromTransaction(){
        intent = new Intent(activity, SettingsActivity.class);
        startActivity(intent);
        finish();

    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameSettings, fragment).disallowAddToBackStack();
        fragmentTransaction.commit();

    }


}
