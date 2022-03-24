package net.boddo.btm.Adepter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatAppMsgDTO;
import net.boddo.btm.Model.ChatRequest;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import net.boddo.btm.Utills.SearchUser;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRequestAdapter extends RecyclerView.Adapter<ChatRequestAdapter.ChatRequestViewHolder> {

    private Context context;
    private List<ChatRequest.RequestedMessage> chatRequestList;
    LocalBroadcastManager broadcaster;

    private OnAcceptClickListener mListener;
    public interface OnAcceptClickListener {

        void onAcceptClicked(View view, int position);
    }

    public void setOnItemClickListener(OnAcceptClickListener listener) {
        mListener = listener;
    }

    public ChatRequestAdapter(Context context, List<ChatRequest.RequestedMessage> chatRequestList) {
        this.context = context;
        this.chatRequestList = chatRequestList;

    }

    @NonNull
    @Override
    public ChatRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_request_row_item,parent,false);
        return new ChatRequestViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatRequestViewHolder holder, final int position) {
        Picasso.get().load(chatRequestList.get(position).getProfilePhoto()).into(holder.imageView);
        holder.requestedUserName.setText(chatRequestList.get(position).getFirstName());
        if (chatRequestList.get(position).getLastMessage().contains(Constants.IMAGE_REGEX)){
            holder.requestedUserText.setText(chatRequestList.get(position).getFirstName()+ " sent you an image !!");
        }else{
            holder.requestedUserText.setText(chatRequestList.get(position).getLastMessage());
        }
        holder.requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptedRequest(chatRequestList.get(position).getUserOne(),position);
            }
        });
        holder.imageView.setOnClickListener(                        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.pd = new ProgressDialog(context);
                Data.pd.setTitle("Loading...");
                Data.pd.setMessage("Please wait for a while...");
                Data.otherUserId = chatRequestList.get(position).getUserOne();
                SearchUser loadOtherUserProfile = new SearchUser(context);
                loadOtherUserProfile.searchUserInfo();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRequestList.size();
    }

    public class ChatRequestViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView requestedUserName;
        TextView requestedUserText;
        TextView requestButton;
        public ChatRequestViewHolder(@NonNull View itemView ,final OnAcceptClickListener listener ) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_requested_user_photo);
            requestedUserName = itemView.findViewById(R.id.text_view_requested_user_name);
            requestedUserText = itemView.findViewById(R.id.text_view_message);
            requestButton = itemView.findViewById(R.id.button_chat_request);
            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAcceptClicked(v, position);
                            EventBus.getDefault().post(new Event(Constants.GET_CHAT_LIST));
                        }
                    }
                }
            });
        }
    }

    public void acceptedRequest(String otherUserId, final int position) {
        String msgContent  = Data.userFirstName + " accepted the request. "+Constants.REQUEST_STRING;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatAppMsgDTO> call = apiInterface.startChat(Constants.SECRET_KEY, Data.userId, otherUserId, msgContent);
        call.enqueue(new Callback<ChatAppMsgDTO>() {
            @Override
            public void onResponse(Call<ChatAppMsgDTO> call, Response<ChatAppMsgDTO> response) {
                String responeCheck = response.toString();
                if (response.body().getStatus().equals("success")) {
                    if (response.body().getRequest().equals("accepted")) {
                        removeAt(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatAppMsgDTO> call, Throwable t) {
                Log.d("PrivateChatActivity", t.getMessage());
            }
        });
    }

    public void removeAt(int position) {
        chatRequestList.remove(position);
//        notifyItemRemoved(position);
        notifyItemRangeChanged(position, chatRequestList.size());

    }
}
