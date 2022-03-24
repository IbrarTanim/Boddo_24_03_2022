package net.boddo.btm.Adepter.othersuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;

public class OthersProfileImageLoader extends PagerAdapter {

    public static boolean isDataChanged = false;

    private Context context;
    private LayoutInflater layoutInflater;

    public OthersProfileImageLoader(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        //todo this will be imageList
        int otherList = Data.othersImageList.size();
        return Data.othersImageList.size();
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
        View view = layoutInflater.inflate(R.layout.profile_image_sliding_layout, container, false);
        final RoundedImageView imageView = view.findViewById(R.id.sender_imageView);
        Picasso.get().load(Data.othersImageList.get(position).getPhoto()).into(imageView);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}