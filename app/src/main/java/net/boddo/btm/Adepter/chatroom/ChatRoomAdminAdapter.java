package net.boddo.btm.Adepter.chatroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRoomUserInfo;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import net.boddo.btm.Utills.SearchUser;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomAdminAdapter extends RecyclerView.Adapter<ChatRoomAdminAdapter.ChatRoomAdminViewHolder> {

    private Context context;
    private ChatRoomUserInfo[] list;

    public ChatRoomAdminAdapter(Context context, ChatRoomUserInfo[] list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatRoomAdminAdapter.ChatRoomAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_chat_room_row_item, parent, false);
        return new ChatRoomAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatRoomAdminAdapter.ChatRoomAdminViewHolder holder, final int position) {
        if (list[position].getUserId().equals(Data.userId)) {

            if (list[position].getIsAdmin().equals("1")) {
                holder.userName.setVisibility(View.VISIBLE);
                holder.userName.setText(list[position].getUserName());
                holder.tvChatroomAdmin.setText("Admin");
                holder.tvChatroomAdmin.setVisibility(View.VISIBLE);
                holder.tvKickStatus.setVisibility(View.GONE);
                holder.userImage.setVisibility(View.VISIBLE);
                Picasso.get().load(list[position].getProfilePhoto()).into(holder.userImage);
            } else {
                holder.userName.setVisibility(View.VISIBLE);
                holder.userName.setText(list[position].getUserName());
                // holder.tvChatroomAdmin.setText("Admin");
                holder.tvChatroomAdmin.setVisibility(View.VISIBLE);
                holder.tvKickStatus.setVisibility(View.GONE);
                holder.userImage.setVisibility(View.VISIBLE);
                Picasso.get().load(list[position].getProfilePhoto()).into(holder.userImage);
            }
        } else {
            holder.userName.setText(list[position].getUserName());
            Picasso.get().load(list[position].getProfilePhoto()).into(holder.userImage);
            if (Data.isChatRoomAdmin) {
                holder.tvKickStatus.setVisibility(View.VISIBLE);
                //holder.userName.setTextColor(context.getResources().getColor(R.color.deep_yellow));
            } else {
                holder.tvKickStatus.setVisibility(View.GONE);
            }
            if (list[position].getIsBanned().equals("1")) {
                holder.tvKickStatus.setText("kicked");
                holder.tvKickStatus.setClickable(false);
                holder.tvKickStatus.setTextColor(context.getResources().getColor(R.color.white));
            } else if (list[position].getIsAdmin().equals("1")) {
                holder.tvChatroomAdmin.setText("Admin");
                holder.tvChatroomAdmin.setVisibility(View.VISIBLE);
                holder.tvKickStatus.setVisibility(View.INVISIBLE);
                holder.tvChatroomAdmin.setTextColor(context.getResources().getColor(R.color.black));
            }
            holder.tvKickStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.tvKickStatus.getText().toString().equals("kicked")) {

                    } else if (holder.tvChatroomAdmin.getText().toString().equals("Admin")) {

                    } else {
                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<String> call = apiInterface.kickOutFromChatRoom(Constants.SECRET_KEY, Data.userId, list[position].getRoomType(), list[position].getChatRoomId());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                assert response.body() != null;
                                if (response.body().equals("banned")) {
                                    Toast.makeText(context, "Successfully Kicked out", Toast.LENGTH_SHORT).show();
                                    holder.tvKickStatus.setText("kicked");
                                    holder.tvKickStatus.setTextColor(context.getResources().getColor(R.color.white));
                                    EventBus.getDefault().post(new Event(Constants.KICK_OUT_FROM_CHAT_ROOM, list[position].getRoomType()));
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d(context.getClass().getSimpleName(), t.getMessage());
                            }
                        });
                    }
                }
            });
        }
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.pd = new ProgressDialog(context);
                Data.pd.setTitle("Loading...");
                Data.pd.setMessage("Please wait for a while...");
                SearchUser userProfile = new SearchUser(context);
                Data.otherUserId = list[position].getUserId();
                userProfile.searchUserInfo();
                Data.pd.show();

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ChatRoomAdminViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView tvKickStatus, tvChatroomAdmin;

        public ChatRoomAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.image_view_chat_room_admin);
            userName = itemView.findViewById(R.id.text_view_chat_room_admin);
            tvKickStatus = itemView.findViewById(R.id.tvKickStatus);
            tvChatroomAdmin = itemView.findViewById(R.id.tvChatroomAdmin);
        }
    }
}
