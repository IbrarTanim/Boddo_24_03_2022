package net.boddo.btm.Adepter.photoblog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import net.boddo.btm.Model.Likes;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static net.boddo.btm.Utills.StaticAccess.ITEM_COUNT_LIMIT;

public class PhotoLovedAdapter extends RecyclerView.Adapter<PhotoLovedAdapter.ViewHolder> {


    private Context context;
    private List<Likes.AllLike> list;

    public PhotoLovedAdapter(Context context, List<Likes.AllLike> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_blog_sinlge_photo_loved_user_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Glide.with(context).load(list.get(i).getProfilePhoto()).into(viewHolder.profileImage);

        viewHolder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showOtherUserProfile(context, list.get(i).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {

        /*if (list.size() > ITEM_COUNT_LIMIT) {
            return ITEM_COUNT_LIMIT;
        } else {
            return list.size();
        }*/
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image)
        CircleImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
