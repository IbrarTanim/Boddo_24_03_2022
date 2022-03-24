package net.boddo.btm.Adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import net.boddo.btm.Model.Likes;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class AllLikesAdapter extends RecyclerView.Adapter<AllLikesAdapter.MyViewHolder> {

    private Context context;
    private List<Likes.AllLike> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civAllLikesProfilePic;
        TextView tvAllLikesName, tvAllLikesTime,tvAllLikesFirstName;

        public MyViewHolder(View view) {
            super(view);
            civAllLikesProfilePic = view.findViewById(R.id.civAllLikesProfilePic);
            tvAllLikesName = view.findViewById(R.id.tvAllLikesName);
            tvAllLikesTime = view.findViewById(R.id.tvAllLikesTime);
            tvAllLikesFirstName = view.findViewById(R.id.tvAllLikesFirstName);
        }
    }

    public AllLikesAdapter(Context context, List<Likes.AllLike> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AllLikesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_likes, parent, false);
        final AllLikesAdapter.MyViewHolder myViewHolder = new AllLikesAdapter.MyViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AllLikesAdapter.MyViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getProfilePhoto()).into(holder.civAllLikesProfilePic);

        holder.civAllLikesProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showOtherUserProfile(context, list.get(position).getUserId());
            }
        });

        holder.tvAllLikesFirstName.setText(list.get(position).getFirstName());
        holder.tvAllLikesName.setText("@" + list.get(position).getUserName());
        //holder.tvAllLikesName.setText("@" + Data.userFirstName);
        //holder.tvAllLikesTime.setText(Data.userAccountCreated+" dfd");
        holder.tvAllLikesTime.setText(Helper.getLastActionTime(Data.userAccountCreated));


    }

    @Override
    public int getItemCount() {
        //return projectList.size();
        return list == null ? 0 : list.size();
    }
}
