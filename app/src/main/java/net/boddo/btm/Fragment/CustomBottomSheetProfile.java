package net.boddo.btm.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.boddo.btm.Activity.AddPhotoActivity;
import net.boddo.btm.Activity.MyBlogPhotoActivity;
import net.boddo.btm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomBottomSheetProfile extends BottomSheetDialogFragment {


    @BindView(R.id.profile_upload_layout)
    LinearLayout uploadProfilPhoto;

    @BindView(R.id.photo_blog_layout)
    LinearLayout photoBlog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getParentFragment().getContext()).inflate(R.layout.custom_photo_blog_bottom_sheet_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.profile_upload_layout)
    public void onProfileUploadClick(){
        Intent intent = new Intent(getContext(),AddPhotoActivity.class);
        startActivity(intent);
        dismiss();
    }

    @OnClick(R.id.photo_blog_layout)
    public void onPhotoBlogClicked(){
        Intent intent = new Intent(getActivity(),MyBlogPhotoActivity.class);
        startActivity(intent);
        dismiss();
    }





}
