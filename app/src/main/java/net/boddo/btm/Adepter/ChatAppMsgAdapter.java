package net.boddo.btm.Adepter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.FullImageViewActivity;
import net.boddo.btm.Model.ChatAppMsgDTO;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatAppMsgAdapter extends RecyclerView.Adapter<ChatAppMsgAdapter.ChatAppMsgViewHolder> {

    private List<ChatAppMsgDTO.Message> msgDtoList = null;
    private Context context;
    private long oneHourInMillis = 1000 * 60 * 60;
    public ChatAppMsgAdapter(Context context, List<ChatAppMsgDTO.Message> msgDtoList) {
        this.msgDtoList = msgDtoList;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatAppMsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_app_item_view, parent, false);
        return new ChatAppMsgViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAppMsgViewHolder holder, final int position) {

        String message = msgDtoList.get(position).getMessage();

        if (position > 0) {
            long previousTime = 0;
            long presentTime = 0;
            long difference = 0;
            previousTime = Helper.currentTimeStempToLongConversion(msgDtoList.get(position - 1).getCreatedAt());
            presentTime = Helper.currentTimeStempToLongConversion(msgDtoList.get(position).getCreatedAt());
            difference = Math.abs(presentTime-previousTime);
            if (difference >=oneHourInMillis){
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setText(String.valueOf(Helper.getLastActionTime(msgDtoList.get(position).getCreatedAt())));
            }else{
                holder.timeLayout.setVisibility(View.GONE);
                holder.textViweLastMessageHour.setVisibility(View.GONE);
            }
        }else{
            if (Helper.getLastActionTime(msgDtoList.get(position).getCreatedAt()).equals("0 minutes ago")){
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setText(context.getResources().getString(R.string.just_now));
            }else{
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setText(String.valueOf(Helper.getLastActionTime(msgDtoList.get(position).getCreatedAt())));
            }
        }

        if (msgDtoList.get(position).getSender().equals(Data.userId)) {
            holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            if (message.contains("$2y$12$5C05oYdYo0ZJkLZZVZNulu5GhjEnwDXOeJrh2yev4rWYgF04qSjeK")){
                String requestMessage = message.replace("$2y$12$5C05oYdYo0ZJkLZZVZNulu5GhjEnwDXOeJrh2yev4rWYgF04qSjeK", "");
                holder.rightMsgTextView.setText("You accepted the request");
                holder.rightImageView.setVisibility(View.GONE);
                holder.rightImageView.setImageBitmap(null);
                holder.rightImageBorder.setVisibility(View.GONE);
            }else if(message.contains("https://bluetigermobile.com/palup/apis/images/")) {
                holder.rightImageBorder.setVisibility(View.VISIBLE);
                holder.rightImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(message).into(holder.rightImageView);
                holder.rightMsgTextView.setVisibility(View.GONE);
//                holder.textViweRightSeen.setVisibility(View.VISIBLE);
            } else {
                holder.rightMsgTextView.setText(message);
                holder.rightImageView.setVisibility(View.GONE);
                holder.rightImageView.setImageBitmap(null);
                holder.rightImageBorder.setVisibility(View.GONE);
//                holder.textViweRightSeen.setVisibility(View.VISIBLE);
            }
            holder.leftMsgLayout.setVisibility(LinearLayout.GONE);
        } else {
            holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
            if (message.contains(Constants.REQUEST_STRING)){
                String requestMessage = message.replace(Constants.REQUEST_STRING, "");
                holder.leftMsgTextView.setText(requestMessage);
                holder.leftImageBorder.setVisibility(View.GONE);
                holder.leftImageView.setVisibility(View.GONE);
                holder.rightImageView.setVisibility(View.GONE);
                holder.rightImageView.setImageBitmap(null);
                holder.rightImageBorder.setVisibility(View.GONE);
            }else if(message.contains("https://bluetigermobile.com/palup/apis/images/")) {
                holder.leftImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(message).into(holder.leftImageView);
                holder.leftMsgTextView.setVisibility(View.GONE);
                holder.leftImageBorder.setVisibility(View.VISIBLE);
//                holder.textViweLeftSeen.setVisibility(View.VISIBLE);
            } else {
                holder.leftMsgTextView.setText(message);
                holder.leftImageView.setImageBitmap(null);
                holder.leftImageView.setVisibility(View.GONE);
                holder.leftImageBorder.setVisibility(View.GONE);
//                holder.textViweLeftSeen.setVisibility(View.VISIBLE);
            }
        }

        holder.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgDtoList.get(position).getMessage().contains("https://bluetigermobile.com/palup/apis/images/")){
                    Intent intent = new Intent(context,FullImageViewActivity.class);
                    intent.putExtra(Constants.FULL_SCREEN_IMAGE,msgDtoList.get(position).getMessage());
                    context.startActivity(intent);
                }
            }
        });
        holder.rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgDtoList.get(position).getMessage().contains("https://bluetigermobile.com/palup/apis/images/")){
                    Intent intent = new Intent(context,FullImageViewActivity.class);
                    intent.putExtra(Constants.FULL_SCREEN_IMAGE,msgDtoList.get(position).getMessage());
                    context.startActivity(intent);
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        if (msgDtoList == null) {
            msgDtoList = new ArrayList<ChatAppMsgDTO.Message>();
        }
        return msgDtoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ChatAppMsgViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftMsgLayout;
        LinearLayout rightMsgLayout;
        TextView leftMsgTextView;
        TextView rightMsgTextView;
        ImageView rightImageView, leftImageView;
        TextView textViweLastMessageHour;
        TextView textViweLeftSeen;
        TextView textViweRightSeen;
        LinearLayout timeLayout;
        RelativeLayout leftImageBorder,rightImageBorder;

        public ChatAppMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView != null) {
                leftMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_msg_layout);
                rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_msg_layout);
                leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
                rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
                rightImageView = itemView.findViewById(R.id.chat_right_msg_imageView);
                leftImageView = itemView.findViewById(R.id.chat_left_msg_imageView);
                textViweLastMessageHour = itemView.findViewById(R.id.last_text_time);
                timeLayout = itemView.findViewById(R.id.time_layout);
                textViweLeftSeen = itemView.findViewById(R.id.left_seen_text_view);
                textViweRightSeen = itemView.findViewById(R.id.right_seen_text_view);
                leftImageBorder = itemView.findViewById(R.id.left_image_borger);
                rightImageBorder = itemView.findViewById(R.id.right_image_borger);
            }
        }
    }
    @Override
    public void onViewAttachedToWindow(ChatAppMsgViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!holder.textViweLastMessageHour.getText().toString().contentEquals(""))
            holder.textViweLastMessageHour.setVisibility(View.VISIBLE);

        Log.i("ATTACHED_VISIBILITY", holder.textViweLastMessageHour.getText().toString() + "\t" + holder.textViweLastMessageHour.getVisibility());
    }
    @Override
    public void onViewRecycled(@NonNull ChatAppMsgViewHolder holder) {
        super.onViewRecycled(holder);
        if (!holder.textViweLastMessageHour.getText().toString().contentEquals("")) {
            holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
        }
    }

}
