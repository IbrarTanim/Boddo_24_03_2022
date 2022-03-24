package net.boddo.btm.Adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class ProfileImageLoaderAdapter extends PagerAdapter {

    public static boolean isDataChanged = false;
    private Context context;
    private LayoutInflater layoutInflater;

    public ProfileImageLoaderAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        //todo this will be imageList
        if (ProfileFragment.imageList == null) {
         Helper.loadMyPhotos(Data.userId);
        }
        return ProfileFragment.imageList.size();
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
        Picasso.get().load(ProfileFragment.imageList.get(position).getPhoto()).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);

    }
}
