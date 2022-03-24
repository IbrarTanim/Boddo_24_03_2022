package net.boddo.btm.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import net.boddo.btm.Adepter.ProfileViewPagerAdepter;
import net.boddo.btm.Fragment.OthersFragment.OthersProfileFragment;
import net.boddo.btm.Fragment.OthersFragment.OthersProfilePhotoFragment;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OthersProfileActivity extends AppCompatActivity {

    @BindView(R.id.main_frame)
    FrameLayout framelayout;

    OthersProfileFragment othersProfileFragment;
    //fragment declareation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        if (savedInstanceState == null){
            Helper.changeFragment(OthersProfileActivity.this, R.id.main_frame, new OthersProfileFragment());
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ProfileViewPagerAdepter adapter = new ProfileViewPagerAdepter(getSupportFragmentManager());
        adapter.addFragment(new OthersProfileFragment(), "Profile");
        adapter.addFragment(new OthersProfilePhotoFragment(), "Photos");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Data.isLoved = "";
        Data.isFavorite = "";
        Data.isMatched = "";
        finish();
    }

    public static Intent newInntent(Context context){
        Intent intent = new Intent(context, OthersProfileActivity.class);
        return intent;
    }

}
