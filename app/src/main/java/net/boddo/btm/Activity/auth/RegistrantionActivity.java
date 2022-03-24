package net.boddo.btm.Activity.auth;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.badoualy.stepperindicator.StepperIndicator;

import net.boddo.btm.Activity.auth.adapter.StepperAdapter;
import net.boddo.btm.Activity.auth.stepperfragments.UserNameFragment;
import net.boddo.btm.Activity.auth.viewpager.CustomRegistrationViewPager;
import net.boddo.btm.Adepter.ViewPageAdepter;
import net.boddo.btm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrantionActivity extends AppCompatActivity implements BaseCommunicator {

    @BindView(R.id.custom_back_button)
    ImageView backArrow;


    @BindView(R.id.registration_viewpager)
    CustomRegistrationViewPager viewPager;

    @BindView(R.id.stepper)
    StepperIndicator indicator;

    StepperAdapter stepperAdapter;
    ViewPageAdepter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

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

        if (savedInstanceState == null) {
            UserNameFragment userNameFragment = new UserNameFragment();
            stepForward(userNameFragment);
        }
        stepperAdapter = new StepperAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
    }

    private void stepForward(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @OnClick(R.id.custom_back_button)
    public void backPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }

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
