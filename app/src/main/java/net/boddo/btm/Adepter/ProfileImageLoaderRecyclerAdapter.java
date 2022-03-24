package net.boddo.btm.Adepter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import net.boddo.btm.Activity.about.EducationActivity;
import net.boddo.btm.Activity.about.EyeColorActivity;
import net.boddo.btm.Activity.about.HairColorActivity;
import net.boddo.btm.Activity.about.LookingForActivity;
import net.boddo.btm.Activity.about.ProfessionActivity;
import net.boddo.btm.Activity.about.RelationshipActivity;
import net.boddo.btm.Activity.about.SmokingActivity;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Model.Likes;
import net.boddo.btm.Model.PojoClass;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;

import java.util.ArrayList;
import java.util.List;


public class ProfileImageLoaderRecyclerAdapter extends RecyclerView.Adapter<ProfileImageLoaderRecyclerAdapter.MyViewHolder> {


    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView sender_imageView;

        public MyViewHolder(View view) {
            super(view);
            sender_imageView = view.findViewById(R.id.sender_imageView);

        }
    }


    public ProfileImageLoaderRecyclerAdapter(Context context) {
        this.context = context;
    }


    @Override
    public ProfileImageLoaderRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {


        final View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_image_sliding_layout, parent, false);
        final ProfileImageLoaderRecyclerAdapter.MyViewHolder myViewHolder = new ProfileImageLoaderRecyclerAdapter.MyViewHolder(convertView);


        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final ProfileImageLoaderRecyclerAdapter.MyViewHolder holder, int position) {

        Picasso.get().load(ProfileFragment.imageList.get(position).getPhoto()).into(holder.sender_imageView);
        // Glide.with(context).load(ProfileFragment.imageList.get(position).getPhoto()).into(holder.sender_imageView);
    }


    @Override
    public int getItemCount() {
        //return projectList.size();
        return ProfileFragment.imageList == null ? 0 : ProfileFragment.imageList.size();
    }
}
