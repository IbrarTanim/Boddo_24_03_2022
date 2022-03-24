package net.boddo.btm.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import net.boddo.btm.Adepter.SlideAdepter;
import net.boddo.btm.R;
import net.boddo.btm.Utills.SharedPref;

public class IntoSlideActivity extends AppCompatActivity implements ViewPager.PageTransformer {

    ViewPager mSlideViewPager;
    LinearLayout mDotlayout;
    SlideAdepter mSlideAdepter;
    TextView[] mDots;
    Button mSkipBTN;
    Button mFinishBTN;
    int mCurrentPage;
    SharedPref sharedPref;

    public static Intent newIntroSliderIntent(Context context) {
        Intent intent = new Intent(context, IntoSlideActivity.class);
        return intent;
    }

    //Test For swipe
    private static final float MIN_SCALE = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into__slide);

        mSlideViewPager = findViewById(R.id.view_Page);
        mDotlayout = findViewById(R.id.dot_layout);
        mSkipBTN = findViewById(R.id.skipBTN);

        mFinishBTN = findViewById(R.id.finishBTN);
        mFinishBTN.setVisibility(View.GONE);
        mSlideAdepter = new SlideAdepter(this);
        mSlideViewPager.setAdapter(mSlideAdepter);
        mDotIndigator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        sharedPref = new SharedPref(this);
    }

    private void mDotIndigator(int position) {
        mDots = new TextView[6];
        mDotlayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.trasparent_white));
            mDotlayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.readDeep));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mDotIndigator(position);
            mCurrentPage = position;
            mSlideViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    int pageWidth = page.getWidth();
                    page.setTranslationX(-position * page.getWidth());
                    if (position < -1) { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        page.setAlpha(0f);

                    } else if (position <= 0) { // [-1,0]
                        // Use the default_img slide transition when moving to the left page
                        page.setAlpha(1f);
                        page.setTranslationX(0f);
                        page.setScaleX(1f);
                        page.setScaleY(1f);

                    } else if (position <= 1) { // (0,1]
                        // Fade the page out.
                        page.setAlpha(1 - position);

                        // Counteract the default_img slide transition
                        page.setTranslationX(pageWidth * -position);
                        // Scale the page down (between MIN_SCALE and 1)
                        float scaleFactor = MIN_SCALE
                                + (1 - MIN_SCALE) * (1 - Math.abs(position));
                        page.setScaleX(scaleFactor);
                        page.setScaleY(scaleFactor);

                    } else { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        page.setAlpha(0f);
                    }
                }
            });
            if (position == 0) {

                mSkipBTN.setEnabled(true);
                mFinishBTN.setVisibility(View.GONE);
                mSkipBTN.setText("Skip");
            } else if (position == mDots.length - 1) {
                mSkipBTN.setVisibility(View.GONE);
                mFinishBTN.setVisibility(View.GONE);
                mFinishBTN.setEnabled(true);
                mFinishBTN.setVisibility(View.VISIBLE);
            } else {
                mSkipBTN.setEnabled(true);
                mSkipBTN.setVisibility(View.VISIBLE);
                mFinishBTN.setVisibility(View.GONE);
                mSkipBTN.setText("Skip");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private float x1, x2;
    static final int MIN_DISTANCE = 100;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void transformPage(View page, float position) {

        page.setTranslationX(-position * page.getWidth());

        if (Math.abs(position) < 0.5) {
            page.setVisibility(View.VISIBLE);
            page.setScaleX(1 - Math.abs(position));
            page.setScaleY(1 - Math.abs(position));
        } else if (Math.abs(position) > 0.5) {
            page.setVisibility(View.GONE);
        }
    }


    public void skipBTN(View view) {
        sharedPref.setHasClickedSkipOrLetsGoButton("isSkippedOrLetsGoButtonClicked", true);
        Intent intent = new Intent(IntoSlideActivity.this, LandingActivity.class);
        startActivity(intent);
        finish();
    }

    public void finishBTN(View view) {
        sharedPref.setHasClickedSkipOrLetsGoButton("isSkippedOrLetsGoButtonClicked", true);
        Intent intent = new Intent(IntoSlideActivity.this, LandingActivity.class);
        startActivity(intent);
        finish();
    }
}
