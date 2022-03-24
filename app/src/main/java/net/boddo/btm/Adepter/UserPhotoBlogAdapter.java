package net.boddo.btm.Adepter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.boddo.btm.Activity.FullImageFromOwnProfileActivity;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;

public class UserPhotoBlogAdapter extends RecyclerView.Adapter<UserPhotoBlogAdapter.UserPhotoBlogViewHolder> {

    private Context context;
    private UserPhotoBlogImages[] userPhotoBlogImagesList;

    //OthersProfilePhotoFragment, MyBlogPhotoActivity
    public UserPhotoBlogAdapter(Context context, UserPhotoBlogImages[] userPhotoBlogImagesList) {
        this.context = context;
        this.userPhotoBlogImagesList = userPhotoBlogImagesList;
    }

    @NonNull
    @Override
    public UserPhotoBlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_images_list_row, parent, false);

        return new UserPhotoBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPhotoBlogViewHolder holder, final int position) {

/*
        if (isLiked.getIsLiked().equals("yes")) {
            like_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));
        } else if (isLiked.getIsLiked().equals("no")) {
            like_button.setImageDrawable(getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));
        }*/

        int pos = position;

        Picasso.get().load(userPhotoBlogImagesList[pos].getPhoto()).into(holder.userImage);

        if (!userPhotoBlogImagesList[pos].getDescription().equals("")) {
            holder.post.setVisibility(View.VISIBLE);
            holder.post.setText(userPhotoBlogImagesList[pos].getDescription());
        }

        if (userPhotoBlogImagesList[pos].getLike().equals("1")) {
            holder.imageViewLove.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));

        } else {
            holder.imageViewLove.setImageDrawable(context.getResources().getDrawable(R.drawable.like_icon_56_05_01_2021));

        }
        holder.userLikedCount.setText(userPhotoBlogImagesList[pos].getLike());
        holder.userCommentCount.setText(userPhotoBlogImagesList[pos].getComment());
        holder.userCount.setText(userPhotoBlogImagesList[pos].getViews());


        holder.imageViewLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onLoveListener.giveLove(position);
                //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                holder.imageViewLove.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_red_love_or_like_fill));

            }
        });

        String check = userPhotoBlogImagesList[pos].getActionAt();
        if (userPhotoBlogImagesList[pos].getActionAt().equals("pending")) {
            holder.llPrivacy.setVisibility(View.VISIBLE);
            holder.tvStatusReview.setText("Status: " + userPhotoBlogImagesList[pos].getActionAt());
        } else {
            holder.llPrivacy.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullImageFromOwnProfileActivity.class);
                intent.putExtra("PhotoBlog", (Parcelable) userPhotoBlogImagesList[pos]);
                intent.putExtra("position", pos);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userPhotoBlogImagesList.length;
    }

    public class UserPhotoBlogViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        ImageView imageViewLove;
        TextView post, tvStatusReview;
        LinearLayout llPrivacy;

        //LinearLayout statusLayout;
        //BubbleLayout bubbleLayout;

        TextView userCount, userLikedCount, userCommentCount;

        public UserPhotoBlogViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.photo_blog_ImageView);
            imageViewLove = itemView.findViewById(R.id.image_view_giving_love);
            userCount = itemView.findViewById(R.id.user_count_textView);
            userLikedCount = itemView.findViewById(R.id.love_count_textView);
            userCommentCount = itemView.findViewById(R.id.comment_count_textView);
            post = itemView.findViewById(R.id.post);
            tvStatusReview = itemView.findViewById(R.id.tvStatusReview);
            llPrivacy = itemView.findViewById(R.id.llPrivacy);
            //statusLayout = itemView.findViewById(R.id.status_layotu);

            //bubbleLayout = itemView.findViewById(R.id.bubble_layout);


        }
    }
}
