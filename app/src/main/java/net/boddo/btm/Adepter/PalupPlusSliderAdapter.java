package net.boddo.btm.Adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import net.boddo.btm.R;

public class PalupPlusSliderAdapter extends PagerAdapter {


    int image[] = {
            R.drawable.slider1,
            R.drawable.slider2,
            R.drawable.slider3,
            R.drawable.slider4,
            R.drawable.slider5,
            R.drawable.slider6,
            R.drawable.slider7,
            R.drawable.slider8
    };

    private Context context;
    private LayoutInflater layoutInflater;

    public PalupPlusSliderAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        //todo this will be imageList

        return image.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (RelativeLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //profile_image_sliding_layout
        View view = layoutInflater.inflate(R.layout.profile_image_sliding_layout, container, false);
        final ImageView imageView = view.findViewById(R.id.sender_imageView);
        imageView.setImageResource(image[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
