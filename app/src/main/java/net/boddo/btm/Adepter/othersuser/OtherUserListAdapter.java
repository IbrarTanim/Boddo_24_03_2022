package net.boddo.btm.Adepter.othersuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SearchUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserListAdapter extends RecyclerView.Adapter<OtherUserListAdapter.OtherUserHolder> {


    Context context;
    List<User> otherUserList;

    public OtherUserListAdapter(Context context, List<User> otherUserList) {
        this.context = context;
        this.otherUserList = otherUserList;
    }

    @NonNull
    @Override
    public OtherUserListAdapter.OtherUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_user_list_row_item, parent, false);
        return new OtherUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherUserListAdapter.OtherUserHolder holder, final int position) {
        Picasso.get().load(otherUserList.get(position).getProfilePhoto()).into(holder.imageView);
        holder.otherUserName.setText(otherUserList.get(position).getUserName());
        holder.otherUserGender.setText(otherUserList.get(position).getGender());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchUser searchUser = new SearchUser(context);
                Data.otherUserId = otherUserList.get(position).getUserId();
                searchUser.searchUserInfo();
            }
        });
    }

    @Override
    public int getItemCount() {
        return otherUserList.size();
    }

    public class OtherUserHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView otherUserName;
        TextView otherUserGender;

        public OtherUserHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.other_user_profile);
            otherUserName = itemView.findViewById(R.id.other_user_name);
            otherUserGender = itemView.findViewById(R.id.other_user_gender);

        }

    }
}
