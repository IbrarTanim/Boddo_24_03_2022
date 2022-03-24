package net.boddo.btm.Adepter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import net.boddo.btm.Model.Hotlist;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SearchUser;

public class HotlistAdapter extends RecyclerView.Adapter<HotlistAdapter.HotlistViewHolder> {

    Context context;
    Hotlist[] hotlists;

    public HotlistAdapter(Context context, Hotlist[] hotlists) {
        this.context = context;
        this.hotlists = hotlists;


    }

    @NonNull
    @Override
    public HotlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_hotlist, parent, false);
        return new HotlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotlistViewHolder holder, final int position) {
        holder.name.setText("@"+hotlists[position].getUserName());
        String bid = "BID: " + hotlists[position].getBid() + " Credits";
        holder.bid.setText(bid);
        holder.serial.setText(String.valueOf(position + 1));

        /*RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));*/

        Glide.with(holder.itemView.getContext())
                .load(hotlists[position].getProfilePhoto())
                //.apply(requestOptions)
                .into(holder.userProfileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchUser userProfile = new SearchUser(context);
                Data.pd = new ProgressDialog(context);
                Data.pd.setTitle("Loading...");
                Data.pd.setMessage("Please wait for a while...");
                Data.pd.show();
                Data.otherUserId = hotlists[position].getUserId();
                userProfile.searchUserInfo();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotlists.length;
    }

    public class HotlistViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView userProfileImage;
        TextView name, bid, serial;

        public HotlistViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.user_imageView);
            name = itemView.findViewById(R.id.userName_textView);
            bid = itemView.findViewById(R.id.credits_textView);
            serial = itemView.findViewById(R.id.serial);
        }
    }
}
