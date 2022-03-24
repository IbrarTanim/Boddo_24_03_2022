package net.boddo.btm.Adepter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Model.AllComments;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.SearchUser;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context context;
    List<AllComments.Comment> commentList;

    public CommentAdapter(Context context, List<AllComments.Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        Picasso.get().load(commentList.get(position).getProfilePhoto()).into(holder.userProfilePhoto);
        holder.comment.setText(commentList.get(position).getComment());
        //holder.commenterName.setText(commentList.get(position).);
        holder.commenterName.setText("@" + commentList.get(position).getUserName());
        String serverTime = commentList.get(position).getCreatedAt();

        CharSequence ago = Helper.getLastActionTime(serverTime);

        holder.commentTime.setText(ago);
        if (String.valueOf(ago).equals("In 0 minutes") || ago.toString().equals("0 minutes ago")) {
            holder.commentTime.setText(context.getResources().getString(R.string.just_now));
        } else {
            holder.commentTime.setText(ago);
        }
        holder.userProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.otherUserId = commentList.get(position).getUserId();
                if (Data.userId.equals(Data.otherUserId)) {
                    Intent intent = new Intent(context, DashBoadActivity.class);
                    intent.putExtra("profile", "profile");
                    context.startActivity(intent);
                } else {
                    Data.pd = new ProgressDialog(context);
                    Data.pd.show();
                    SearchUser otherUserInfo = new SearchUser(context);
                    otherUserInfo.searchUserInfo();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commenterName, comment, commentTime;
        CircleImageView userProfilePhoto;
        ImageView ivMenueComments;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfilePhoto = itemView.findViewById(R.id.commentor_image_view);
            commenterName = itemView.findViewById(R.id.text_view_commentor);
            comment = itemView.findViewById(R.id.text_view_comment);
            commentTime = itemView.findViewById(R.id.text_view_comment_time);
            ivMenueComments = itemView.findViewById(R.id.ivMenueComments);
        }
    }

}
