package net.boddo.btm.Adepter.photoblog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.boddo.btm.Model.Hotlist;
import net.boddo.btm.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HotlistAdapter extends RecyclerView.Adapter<HotlistAdapter.HotlistViewHolder> {

    private Context ctx;
    private Hotlist[] hotlist;

    private OnPhotoblogImageClickListener listener;

    public void setProfileImageClickListener(OnPhotoblogImageClickListener listener) {
        this.listener = listener;
    }

    public HotlistAdapter(Context context, Hotlist[] hotlist) {
        this.ctx = context;
        this.hotlist = hotlist;
    }

    @NonNull
    @Override
    public HotlistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.round_shape_photoblog_image_layout, viewGroup, false);
        return new HotlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotlistViewHolder hotlistViewHolder, int i) {

        int position = i;

        String hotlistUserName = hotlist[i].getUserName();
        if (hotlistUserName != null) {
            hotlistViewHolder.hotListUserNameTV.setText(hotlistUserName);
        }

        String imageURL = hotlist[i].getProfilePhoto();
        Glide.with(ctx)
                .load(imageURL)
                .into(hotlistViewHolder.imageView);

        //.apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))


        hotlistViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotlist.length;
    }

    public class HotlistViewHolder extends RecyclerView.ViewHolder {

        //@BindView(R.id.hot_list_image_view)

        CircleImageView imageView;
        TextView hotListUserNameTV;

        public HotlistViewHolder(@NonNull View itemView) {
            super(itemView);
            //ButterKnife.bind(this, itemView);
            imageView = itemView.findViewById(R.id.hot_list_image_view);
            hotListUserNameTV = itemView.findViewById(R.id.hotlist_user_name);
        }
    }

    public interface OnPhotoblogImageClickListener {
        void onImageClick(int position);
    }
}
