package net.boddo.btm.Adepter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import net.boddo.btm.Activity.BlockListActivity;
import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Activity.FullPhotoViewActivity;
import net.boddo.btm.Activity.PrivateChatActivity;
import net.boddo.btm.Activity.photoblog.OnLoveListener;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.PhotoBlog;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.LikeUnlikePicturePrefs;
import net.boddo.btm.Utills.Limitation;
import net.boddo.btm.Utills.Response;
import net.boddo.btm.Utills.SearchUser;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;

public class PhotoBlogAdapter extends RecyclerView.Adapter<PhotoBlogAdapter.PhotoBlogViewHolder> {

    Context context;
    PhotoBlog[] photoBlogList;
    String viewType;
    String about, age, matchedCount, posi;
    Dialog mDialog;
    boolean flag = false;

    private final static int FADE_DURATION = 300;
    private static final String TAG = "PhotoBlogAdapter";
    OnLoveListener onLoveListener;
    LikeUnlikePicturePrefs likeUnlikePicturePrefs;

    public PhotoBlogAdapter(Context context, PhotoBlog[] photoBlogList, String viewType, OnLoveListener listener) {
        this.context = context;
        this.photoBlogList = photoBlogList;
        this.viewType = viewType;
        this.onLoveListener = listener;
    }

    @NonNull
    @Override
    public PhotoBlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_blog_model_all_user, parent, false);
        return new PhotoBlogViewHolder(view);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final PhotoBlogAdapter.PhotoBlogViewHolder holder, final int position) {

        likeUnlikePicturePrefs = new LikeUnlikePicturePrefs(context);
        if (viewType.equals("TopPhoto")) {
            if (Data.isPalupPlusSubcriber) {
                seeAllData(holder, position);
                holder.quetionMark.setVisibility(View.INVISIBLE);
                holder.blurEffect.setVisibility(View.INVISIBLE);
                holder.profileQuetionMark.setVisibility(View.INVISIBLE);
                holder.profileBlurEffect.setVisibility(View.INVISIBLE);
                Glide.with(holder.ProfileImageView.getContext()).load(photoBlogList[position].getProfilePhoto()).into(holder.ProfileImageView);
                Glide.with(holder.PhotoBlogImageView.getContext()).load(photoBlogList[position].getPhoto()).into(holder.PhotoBlogImageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isBlockedFullPage(position);
                    }
                });
                holder.ProfileImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isBlockedUserCheck(position);
                    }
                });
                holder.imageViewLove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo onLove call
                        Log.e("hello", "onClick: hay" );
                        onLoveListener.giveLove(position);



                    }
                });
//              setScaleAnimation(holder.itemView);
            } else {
                if (position <= Limitation.TOP_PHOTO_SHOW_PHOTO - 1) {
                    seeAllData(holder, position);
                    holder.quetionMark.setVisibility(View.INVISIBLE);
                    holder.blurEffect.setVisibility(View.INVISIBLE);
                    holder.profileQuetionMark.setVisibility(View.INVISIBLE);
                    holder.profileBlurEffect.setVisibility(View.INVISIBLE);
                    Glide.with(holder.ProfileImageView.getContext()).load(photoBlogList[position].getProfilePhoto()).into(holder.ProfileImageView);
                    Glide.with(holder.PhotoBlogImageView.getContext()).load(photoBlogList[position].getPhoto()).into(holder.PhotoBlogImageView);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isBlockedFullPage(position);
                        }
                    });
                    holder.ProfileImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isBlockedUserCheck(position);
                        }
                    });
                    holder.imageViewLove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Todo onLove call
                            Log.e("hello", "onClick: hay" );
                            onLoveListener.giveLove(position);
                        }
                    });
                    // setScaleAnimation(holder.itemView);
                } else {
                    seeAllData(holder, position);
                    Glide.with(holder.ProfileImageView.getContext()).load(photoBlogList[position].getProfilePhoto()).into(holder.ProfileImageView);
                    Glide.with(holder.PhotoBlogImageView.getContext()).load(photoBlogList[position].getPhoto()).into(holder.PhotoBlogImageView);
                    holder.quetionMark.setVisibility(View.VISIBLE);
                    holder.blurEffect.setVisibility(View.VISIBLE);
                    holder.post.setVisibility(View.GONE);

                    holder.profileQuetionMark.setVisibility(View.VISIBLE);
                    holder.profileBlurEffect.setVisibility(View.VISIBLE);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            palupDailog();
                        }
                    });

                    holder.ProfileImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            palupDailog();
                        }
                    });
                    holder.imageViewLove.setVisibility(View.GONE);

                    // setScaleAnimation(holder.itemView);
                }
            }
        } else {
            //.........All User Photo Blog
            seeAllData(holder, position);
            Glide.with(holder.ProfileImageView.getContext()).load(photoBlogList[position].getProfilePhoto()).into(holder.ProfileImageView);
            Glide.with(holder.PhotoBlogImageView.getContext()).load(photoBlogList[position].getPhoto()).centerCrop().into(holder.PhotoBlogImageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isBlockedFullPage(position);
                }
            });
            holder.ProfileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isBlockedUserCheck(position);
                }
            });
            holder.imageViewLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo onLove call
                    Log.e("hello", "onClick: hay3" );
                    if(photoBlogList[position].getIsPhotoLikedByMe() == 1){
                        Log.e("hello", "onClick: "+position );
                        Log.e("hello", "onClick: "+photoBlogList[position].getIsPhotoLikedByMe() );
                        flag = false;
                        LikeUnlikePicturePrefs.setFlagStatus("likeUnlike",flag);
                        Log.e("flag", "seeAllData: "+flag );
                    }else {
                        Log.e("hello", "onClick: "+photoBlogList[position].getIsPhotoLikedByMe() );
                        flag = true;
                        LikeUnlikePicturePrefs.setFlagStatus("likeUnlike",flag);
                        Log.e("flag", "seeAllData: "+flag );
                    }
                    onLoveListener.giveLove(position);
                }
            });
        }
    }


    private void viewImage(final int position) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Response> call = apiInterface.viewPhoto(Constants.SECRET_KEY,
                Data.userId,
                photoBlogList[position].getUserId(),
                photoBlogList[position].getId());

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200) {
                    Response respns = response.body();
                    if (respns.getStatus().equals("success")) {
                        fullImageActivity(true, position);
                    } else if (respns.getStatus().equals("already_viewed")) {
                        fullImageActivity(false, position);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                fullImageActivity(false, position);
            }
        });
    }

    private void fullImageActivity(boolean isViewed, int position) {
        Intent intent = new Intent(context, FullPhotoViewActivity.class);
        intent.putExtra("Details", photoBlogList[position]);
        intent.putExtra(Constants.IS_PHOTO_VIEWED_BEFORE, isViewed);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    private void isBlockedUserCheck(int position) {
        boolean isBlocked = false;
        boolean isBlockedMe = false;
        String otherUserID = photoBlogList[position].getUserId();
        for (int i = 0; i < DashBoadActivity.myBlockList.size(); i++) {
            if (DashBoadActivity.myBlockList.get(i).getBlockedUserId().equals(otherUserID)) {
                isBlocked = true;
                break;
            }
        }
        if (isBlocked) {

            final PrettyDialog myBlockDialog = new PrettyDialog(context);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
               /* myBlockDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                        .setMessage("You have  Blocked this user")
                        .setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        myBlockDialog.dismiss();
                    }
                }).show();
*/
                haveBlockedUser();

            } else {
                /*myBlockDialog.setMessage(" You have  Blocked this user")
                        .setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        myBlockDialog.dismiss();
                    }
                }).show();*/
                haveBlockedUser();

            }

        } else {
            for (int j = 0; j < DashBoadActivity.whoBlockMeList.size(); j++) {

                if (DashBoadActivity.whoBlockMeList.get(j).getBlockBy().equals(otherUserID)) {
                    isBlockedMe = true;
                    break;
                }
            }
            if (isBlockedMe) {
              //  final PrettyDialog myBlockDialog = new PrettyDialog(context);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    /*myBlockDialog.setMessage(" You have been Blocked by this user")
                            .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            myBlockDialog.dismiss();
                        }
                    }).show();*/
                    Toast.makeText(context, "Sorry, You have been blocked by this user", Toast.LENGTH_SHORT).show();


                } else {
                   /* myBlockDialog.setMessage(" You have been Blocked by this user")
                            .setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            myBlockDialog.dismiss();
                        }
                    }).show();*/
                    Toast.makeText(context, "Sorry, you have been blocked by this user", Toast.LENGTH_SHORT).show();
                }
            } else {
                viewImage(position);
            }
        }
    }

    private void haveBlockedUser() {
        //Toast.makeText(context, "blocked", Toast.LENGTH_SHORT).show();

        final PrettyDialog myBlockDialog = new PrettyDialog(context);
        myBlockDialog.setContentView(R.layout.blocked_user);
        final Button blockedList =myBlockDialog.findViewById(R.id.BtnBlockedList);
        final Button cancel =myBlockDialog.findViewById(R.id.button_decline);


        blockedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BlockListActivity.class));
                myBlockDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBlockDialog.dismiss();
            }
        });

        myBlockDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessage("You have  Blocked this user")
                .setMessageColor(R.color.red_A700).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
            @Override
            public void onClick() {

                myBlockDialog.dismiss();

            }
        }).show();
        if (Data.pd != null){
            Data.pd.dismiss();
        }
    }



    private void isBlockedFullPage(int position) {
        boolean isBlocked = false;
        boolean isBlockedMe = false;
        String otherUserID = photoBlogList[position].getUserId();
        for (int i = 0; i < DashBoadActivity.myBlockList.size(); i++) {
            if (DashBoadActivity.myBlockList.get(i).getBlockedUserId().equals(otherUserID)) {
                isBlocked = true;
                break;
            }
        }
        if (isBlocked) {
            final PrettyDialog myBlockDialog = new PrettyDialog(context);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
               /* Toast.makeText(context, "block", Toast.LENGTH_SHORT).show();
                myBlockDialog.setMessage(" You have  Blocked  this user")
                        .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        myBlockDialog.dismiss();
                    }
                }).show();*/
                haveBlockedUser();
            } else {
               /* Toast.makeText(context, "block1", Toast.LENGTH_SHORT).show();
                myBlockDialog.setMessage(" You have  Blocked  this user")
                        .setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        myBlockDialog.dismiss();
                    }
                }).show();*/

                haveBlockedUser();
            }
        } else {
            for (int j = 0; j < DashBoadActivity.whoBlockMeList.size(); j++) {
                if (DashBoadActivity.whoBlockMeList.get(j).getBlockBy().equals(otherUserID)) {
                    isBlockedMe = true;
                    break;
                }
            }
            if (isBlockedMe) {
                final PrettyDialog myBlockDialog = new PrettyDialog(context);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                   /* myBlockDialog.setMessage(" You have been Blocked by this user")
                            .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            myBlockDialog.dismiss();
                        }
                    }).show();*/
                    Toast.makeText(context, "Sorry, you have been blocked by this user", Toast.LENGTH_SHORT).show();

                } else {
                    /*myBlockDialog.setMessage(" You have been Blocked by this user")
                            .setMessageColor(R.color.app_color).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            myBlockDialog.dismiss();
                        }
                    }).show();*/
                    Toast.makeText(context, "Sorry, you have been blocked by this user",Toast.LENGTH_SHORT).show();

                }
            } else {
                viewImage(position);
            }
        }
    }

    private void allData(int position) {
        Data.pd = new ProgressDialog(context);
        Data.pd.setTitle("Loading...");
        Data.pd.setMessage("Please wait for a while...");
        SearchUser userProfile = new SearchUser(context);
        Data.otherUserId = photoBlogList[position].getUserId();
        userProfile.searchUserInfo();
        Data.pd.show();
    }

    private void seeAllData(PhotoBlogAdapter.PhotoBlogViewHolder holder, int position) {
        PhotoBlog blog = photoBlogList[position];
        age = Helper.getAge(blog.getDateOfBirth());
        about = blog.getUserName();
        holder.aboutInfo.setText(context.getResources().getString(R.string.at_the_rate) + about);
        matchedCount = blog.getMatched();
        if (!blog.getDescription().equals("")) {
            holder.post.setVisibility(View.VISIBLE);
            holder.post.setText(blog.getDescription());
        }
        holder.textViewLikedCount.setText(blog.getLike());
        Data.likeCountValue = blog.getLike();
        Log.e("like", "seeAllData: "+Data.likeCountValue);
        holder.textViewCommentCount.setText(blog.getComment());
        holder.tvViewCount.setText(blog.getViews() + "");

        if (photoBlogList[position].getIsPhotoLikedByMe() != null) {
            if (photoBlogList[position].getIsPhotoLikedByMe() == 1) {
                holder.imageViewLove.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));



            } else {
                holder.imageViewLove.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_black_love_or_like));

            }
        }
//        posi = String.valueOf(photoBlogList.get(position));
    }


    private void palupDailog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.top_photos_limit_expired_dialog);

        Button button_decline = dialog.findViewById(R.id.button_decline);
        Button discoverBoddo = dialog.findViewById(R.id.discoverBoddo);
        discoverBoddo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuyCreditActivity.class);
                intent.putExtra("Membership", true);
                //overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        button_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /*private void palupDailog() {

        final PrettyDialog myBlockDialog = new PrettyDialog(context);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            myBlockDialog.setMessage("Dear " + Data.userName + ", find out all the handsome & pretty peoples pictures! " +
                    " \n We have thousands of handsome & beautiful peoples picture for you. Don't miss them.")
                    .setIcon(R.drawable.logo1).
                    setIconTint(R.color.colorPrimary).
                    setMessageColor(R.color.app_color).
                    addButton("Palup Plus", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            Intent intent = new Intent(context, BuyCreditActivity.class);
                            intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
                            context.startActivity(intent);
                            myBlockDialog.dismiss();
                        }
                    }).show();
        } else {
            myBlockDialog.setMessage("Dear " + Data.userName + ", find out all the handsome & pretty peoples pictures! " +
                    " \n We have thousands of handsome & beautiful peoples picture for you. Don't miss them.")
                    .setMessageColor(R.color.app_color).addButton("Palup Plus", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(context, BuyCreditActivity.class);
                    intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
                    context.startActivity(intent);
                    myBlockDialog.dismiss();

                }
            }).show();
        }
    }*/

    @Override
    public int getItemCount() {
        return photoBlogList.length;
    }

    public class PhotoBlogViewHolder extends RecyclerView.ViewHolder {
        ImageView ProfileImageView;
        RoundedImageView PhotoBlogImageView;

        TextView aboutInfo, post;
        TextView tvViewCount, textViewLikedCount, textViewCommentCount;
        TextView quetionMark, profileQuetionMark;
        LinearLayout blurEffect, profileBlurEffect;
        ImageView imageViewLove;
        CardView cardViewID;

        public PhotoBlogViewHolder(@NonNull View itemView) {
            super(itemView);
            ProfileImageView = itemView.findViewById(R.id.sender_imageView);
            PhotoBlogImageView = itemView.findViewById(R.id.photo_blog_ImageView);
            aboutInfo = itemView.findViewById(R.id.about_info_textView);
            post = itemView.findViewById(R.id.post);
            tvViewCount = itemView.findViewById(R.id.text_view_view_count);
            textViewLikedCount = itemView.findViewById(R.id.text_view_like_count);
            textViewCommentCount = itemView.findViewById(R.id.text_view_comment_count);
            quetionMark = itemView.findViewById(R.id.quetionMark);
            blurEffect = itemView.findViewById(R.id.blur_effect);
            profileQuetionMark = itemView.findViewById(R.id.profile_quetionMark);
            profileBlurEffect = itemView.findViewById(R.id.profile_blur_effect);
            imageViewLove = itemView.findViewById(R.id.image_view_giving_love);
        }
    }


}