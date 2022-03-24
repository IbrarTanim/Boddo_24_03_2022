package net.boddo.btm.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullImageViewActivity extends AppCompatActivity {


    @BindView(R.id.user_full_ImageView)
    PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        ButterKnife.bind(this);

        String image = getIntent().getStringExtra(Constants.FULL_SCREEN_IMAGE);
        if (getIntent().hasExtra(Constants.FULL_SCREEN_IMAGE)){
            Picasso.get().load(getIntent().getStringExtra(Constants.FULL_SCREEN_IMAGE)).into(photoView);
        }
    }
}
