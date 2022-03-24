package net.boddo.btm.Adepter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatAppMsgDTO;
import net.boddo.btm.Model.ChatRequest;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SearchUser;
import net.boddo.btm.interfaces.getChartRequestAccepted;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRequestAdepter extends RecyclerView.Adapter<ChatRequestAdepter.ChatRequestViewHolder> {

    private Context context;
    private ArrayList<ChatRequest.RequestedMessage> chatRequestModelArrayList;
    private getChartRequestAccepted chartRequestAccepted;

    public ChatRequestAdepter(Context context, List<ChatRequest.RequestedMessage> chatRequestModelArrayList) {
        this.context = context;
        this.chatRequestModelArrayList = (ArrayList<ChatRequest.RequestedMessage>) chatRequestModelArrayList;
        this.chartRequestAccepted = chartRequestAccepted;
    }

    public ChatRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_all_chat_request,parent,false);
        return new ChatRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatRequestAdepter.ChatRequestViewHolder holder, int position) {
        holder.name.setText(chatRequestModelArrayList.get(position).getFirstName());
        holder.chatRequest_messageTV.setText(chatRequestModelArrayList.get(position).getLastMessage());
        Picasso.get().load(chatRequestModelArrayList.get(position).getProfilePhoto()).into(holder.civChatRequestItem);


        holder.civChatRequestItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.pd = new ProgressDialog(context);
                Data.pd.setTitle("Loading...");
                Data.pd.setMessage("Please wait for a while...");
                SearchUser userProfile = new SearchUser(context);
                Data.otherUserId = chatRequestModelArrayList.get(position).getUserOne();
                userProfile.searchUserInfo();
                Data.pd.show();

            }
        });


        holder.chatRequest_acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptedRequest(chatRequestModelArrayList.get(position).getUserOne(),position);
            }
        });





    }

    @Override
    public int getItemCount() {
        return chatRequestModelArrayList.size();
    }


    public class ChatRequestViewHolder extends RecyclerView.ViewHolder {

        TextView name,chatRequest_messageTV, chatRequest_acceptBtn;
        CircleImageView civChatRequestItem;

        public ChatRequestViewHolder(View itemView) {
            super(itemView);

            civChatRequestItem = itemView.findViewById(R.id.civChatRequestItem);
            name = itemView.findViewById(R.id.chatRequest_usernameTV);
            chatRequest_messageTV = itemView.findViewById(R.id.chatRequest_messageTV);
            chatRequest_acceptBtn = itemView.findViewById(R.id.chatRequest_acceptBtn);



        }
    }


    public void acceptedRequest(String otherUserId, final int position) {
        String msgContent  = Data.userFirstName + " accepted the request. "+ Constants.REQUEST_STRING;
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
        chatRequestModelArrayList.remove(position);
//        notifyItemRemoved(position);
        //chartRequestAccepted.onClickChat(chatRequestModelArrayList);
        notifyDataSetChanged();
       // notifyItemRangeChanged(position, chatRequestModelArrayList.size());

    }

}
