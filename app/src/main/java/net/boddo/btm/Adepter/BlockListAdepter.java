package net.boddo.btm.Adepter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Blocklist;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockListAdepter extends RecyclerView.Adapter<BlockListAdepter.BlockListViewHolder> {


    Context context;
    List<Blocklist> blockList;

    ApiInterface apiInterface;

    public BlockListAdepter(Context context, List<Blocklist> blockList) {
        this.context = context;
        this.blockList = blockList;
    }

    @NonNull
    @Override
    public BlockListAdepter.BlockListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blocklist_model, viewGroup, false);

        return new BlockListViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull BlockListAdepter.BlockListViewHolder holder, final int i) {

        Picasso.get().load(blockList.get(i).getProfilePhoto()).into(holder.blockUserPhoto);
        holder.userName.setText(blockList.get(i).getFirstName());
        holder.user_name.setText("@"+blockList.get(i).getUserName());
       // holder.userName.setText(blockList.get(i).getCreatedAt());
        Log.e("createdAT", "onBindViewHolder: "+blockList.get(i).getCreatedAt() );
       // Helper.getLastActionTime(chatList.get(position).getLastMessageTime())
        holder.block_user_last_message_time.setText(Helper.getLastActionTime(blockList.get(i).getCreatedAt()));

        holder.unBlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<String>call = apiInterface.onBlockUser(Constants.SECRET_KEY,blockList.get(i).getBlockedUserId(),Data.userId);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        String stute = response.body();

                        if (stute.equals("unblock")){
                            Toast.makeText(context, blockList.get(i).getFirstName()+" Successfully Unblocked", Toast.LENGTH_SHORT).show();
                            blockList.remove(i);
                            notifyDataSetChanged();
                            EventBus.getDefault().post(new Event(Constants.UNBLOCKED));
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }

    public class BlockListViewHolder extends RecyclerView.ViewHolder {

        ImageView blockUserPhoto;
        TextView userName,block_user_last_message_time,user_name;
        Button unBlockButton;

        public BlockListViewHolder(@NonNull View itemView) {
            super(itemView);

            blockUserPhoto = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.block_user_name);
            unBlockButton = itemView.findViewById(R.id.unBlockButton);
            user_name = itemView.findViewById(R.id.user_name);
            block_user_last_message_time = itemView.findViewById(R.id.block_user_last_message_time);


        }
    }
}
