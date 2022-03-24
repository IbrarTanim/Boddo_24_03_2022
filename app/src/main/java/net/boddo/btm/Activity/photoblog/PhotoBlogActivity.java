package net.boddo.btm.Activity.photoblog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.boddo.btm.Fragment.FragmentPhotoBlogAllUser;
import net.boddo.btm.Fragment.TopPhotoFragment;
import net.boddo.btm.Adepter.PhotoBlogTabAdapter;
import net.boddo.btm.R;

public class PhotoBlogActivity extends AppCompatActivity {

    private PhotoBlogTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_blog);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new PhotoBlogTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPhotoBlogAllUser(), "PHOTO BLOG");
        adapter.addFragment(new TopPhotoFragment(), "TOP PHOTO");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
