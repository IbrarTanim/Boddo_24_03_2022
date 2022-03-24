package net.boddo.btm.Adepter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.PrivateChatActivity;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ActiveChat;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.SearchUser;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatHistoryListAdeptar extends RecyclerView.Adapter<ChatHistoryListAdeptar.ChathistoryHolder> {

    Context context;
    List<ActiveChat.ChatList> chatList;
    ApiInterface apiInterface;


    public ChatHistoryListAdeptar(Context context, List<ActiveChat.ChatList> chatList) {
        this.context = context;
        this.chatList = chatList;
    }


    @NonNull
    @Override
    public ChathistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_list_history_model, parent, false);

        return new ChathistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChathistoryHolder holder, final int position) {

        Picasso.get().load(chatList.get(position).getProfilePhoto()).into(holder.userPhoto);
        holder.userName.setText(chatList.get(position).getFirstName());
        holder.tvMessageTime.setText(Helper.getLastActionTime(chatList.get(position).getLastMessageTime()));
        Log.e("time", "onBindViewHolder: "+chatList.get(position).getLastMessageTime() );
        //String valueCheck = String.valueOf(Helper.getLastActionTime(chatList.get(position).getLastMessageTime()));


        if (chatList.get(position).getIsSeen().equals(Data.userId)) {
            String dataCheck = chatList.get(position).getMessage();

            if (chatList.get(position).getMessage() != null && chatList.get(position).getMessage().contains(Constants.IMAGE_REGEX)) {
                holder.lastMessage.setText(chatList.get(position).getFirstName() + ": sent you an image !!!");
            } else if (chatList.get(position).getMessage() != null && chatList.get(position).getMessage().contains(Constants.REQUEST_STRING)) {
                holder.lastMessage.setText(chatList.get(position).getMessage().replace(Constants.REQUEST_STRING, ""));
            } else {
                holder.lastMessage.setText(chatList.get(position).getMessage());
            }
            holder.lastMessage.setTypeface(null, Typeface.BOLD);
            holder.lastMessage.setTextSize(18);


        } else {
            if (chatList.get(position).getMessage() != null && chatList.get(position).getMessage().contains(Constants.IMAGE_REGEX)) {
                String[] name = chatList.get(position).getMessage().split(":");
                holder.lastMessage.setText(name[0] + ": sent an image");
            } else if (chatList.get(position).getMessage() != null && chatList.get(position).getMessage().contains(Constants.REQUEST_STRING)) {
                holder.lastMessage.setText(chatList.get(position).getMessage().replace(Constants.REQUEST_STRING, ""));
            } else {
                holder.lastMessage.setText(chatList.get(position).getMessage());
            }

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.otherUserId = chatList.get(position).getUserId();
                Data.otherProfilePhoto = chatList.get(position).getProfilePhoto();
                Data.conversationID = chatList.get(position).getConversationId();
                Data.otherUserName = chatList.get(position).getFirstName();
                Data.otherUserName = chatList.get(position).getFirstName();
                String otherUserName = chatList.get(position).getFirstName();

                Intent intent = new Intent(context, PrivateChatActivity.class);
                intent.putExtra(Constants.CONVERSATION_ID, chatList.get(position).getConversationId());
                intent.putExtra(Constants.OTHER_USER_ID, chatList.get(position).getUserId());
                intent.putExtra(Constants.RECEIVER_NAME, otherUserName);
                context.startActivity(intent);
                if (chatList.get(position).getIsSeen().equals(Data.userId)) {
                    messageSeen(chatList.get(position).getConversationId());
                }
            }
        });

        holder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.pd = new ProgressDialog(context);
                Data.pd.setTitle("Loading...");
                Data.pd.setMessage("Please wait for a while...");
                Data.otherUserId = chatList.get(position).getUserId();
                SearchUser loadOtherUserProfile = new SearchUser(context);
                loadOtherUserProfile.searchUserInfo();
            }
        });


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChathistoryHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView lastMessage, userName, tvMessageTime;

        public ChathistoryHolder(@NonNull View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.user_imageView);
            userName = itemView.findViewById(R.id.online_username_textView);
            lastMessage = itemView.findViewById(R.id.message_textView);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
        }
    }

    private void messageSeen(String conversationId) {
        Helper.messageSeen(conversationId);
    }


    public void updateList(List<ActiveChat.ChatList> list) {
        chatList = list;
        notifyDataSetChanged();
    }


}
