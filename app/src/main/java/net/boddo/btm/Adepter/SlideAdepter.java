package net.boddo.btm.Adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import net.boddo.btm.R;
import net.boddo.btm.ViewPager.TransformationPage;

public class SlideAdepter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdepter(Context context) {
        this.context = context;
    }

    public int[] slide_img = {
            R.drawable.boddo_logo_slider
    };

    public String[] slide_heading = {
            "WELCOME TO BODDO",
            "LIKE OTHER USER PROFILE",
            "PRIVATE CHAT",
            "CHAT ROOMS",
            "SHARE YOUR PICTURES",
            "STAY LIKE A STAR"
    };

    public String[] slide_message = {

            "Discover new friends nearby you or around the world",
            "Great opportunity to get matched and meet with your dream partner",
            "Keep in touch with your friends and family even with your soul mate",
            "Great way to meet with thousands of peoples from the world",
            "Share your best selfies and show your glamorous moments",
            "Do them on palup and live like a star",
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slid_layout, container, false);

        ImageView slide_imgView = view.findViewById(R.id.backIMG1);
        TextView  slide_title = view.findViewById(R.id.titleTV);
        TextView  slide_messageView = view.findViewById(R.id.messageTV1);

        TransformationPage pager = new TransformationPage();
        pager.transformPage(view,position);

        if (position == 0){
            slide_imgView.setImageResource(slide_img[position]);
        }
        slide_title.setText(slide_heading[position]);
        slide_messageView.setText(slide_message[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
