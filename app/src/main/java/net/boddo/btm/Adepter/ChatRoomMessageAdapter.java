package net.boddo.btm.Adepter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Event.ChatRoomEvent;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRoomMessage;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.SearchUser;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomMessageAdapter extends RecyclerView.Adapter<ChatRoomMessageAdapter.ChatRoomViewHolder> {

    private List<ChatRoomMessage.RoomMessage> msgDtoList = null;
    private Context context;

    private long oneHourInMillis = 1000 * 60 * 60;
    ApiInterface apiInterface;
    private String type;

    public ChatRoomMessageAdapter(Context context, List<ChatRoomMessage.RoomMessage> msgDtoList, String type) {
        this.msgDtoList = msgDtoList;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ChatRoomMessageAdapter.ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_room_message_item, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomMessageAdapter.ChatRoomViewHolder holder, final int position) {
        String message = msgDtoList.get(position).getMessage();
        int colonData = message.indexOf(":");
        if (position > 0) {
            long previousTime = 0;
            long presentTime = 0;
            long difference = 0;
            previousTime = Helper.currentTimeStempToLongConversion(msgDtoList.get(position - 1).getCreatedAt());
            presentTime = Helper.currentTimeStempToLongConversion(msgDtoList.get(position).getCreatedAt());
            difference = Math.abs(presentTime - previousTime);
            if (difference >= oneHourInMillis) {
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setText(String.valueOf(Helper.getLastActionTime(msgDtoList.get(position).getCreatedAt())));
            } else {
                holder.timeLayout.setVisibility(View.GONE);
                holder.textViweLastMessageHour.setVisibility(View.GONE);
            }
        } else {
            if (String.valueOf(Helper.getLastActionTime(msgDtoList.get(position).getCreatedAt())).equals("0 minutes ago") ||
                    String.valueOf(Helper.getLastActionTime(msgDtoList.get(position).getCreatedAt())).equals("In 0 minutes")) {
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setText(context.getResources().getString(R.string.just_now));
            } else {
                holder.timeLayout.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
                holder.textViweLastMessageHour.setText(String.valueOf(Helper.getLastActionTime(msgDtoList.get(position).getCreatedAt())));
            }
        }
        //end of time part
        if (message.contains("has been kicked by the administrator")) {
            holder.leftMsgLayout.setVisibility(View.GONE);
            holder.rightMsgLayout.setVisibility(View.GONE);
            holder.kickedOutLayout.setVisibility(View.VISIBLE);
            holder.kickedOutMessage.setText(msgDtoList.get(position).getMessage());
            //Picasso.get().load(msgDtoList.get(position).getImage()).into(holder.kickOutAdminImage);
            //holder.kickOutAdminImage.setVisibility(View.VISIBLE);
        } else if (message.contains(context.getString(R.string.entering_hash))) {
            holder.leftMsgLayout.setVisibility(View.GONE);
            holder.rightMsgLayout.setVisibility(View.GONE);
            holder.timeLayout.setVisibility(View.GONE);
            holder.kickedOutLayout.setVisibility(View.GONE);
            holder.enterdIntoRoomLayout.setVisibility(View.VISIBLE);
            holder.enteredIntoRoomTextView.setVisibility(View.VISIBLE);
            String txt = message.replace("f09283123ca585b2e9e5abce3f9e82c0", "");
            holder.enteredIntoRoomTextView.setText(txt);
        } else {
            holder.kickedOutLayout.setVisibility(View.GONE);
            if (msgDtoList.get(position).getUserId().equals(Data.userId)) {
                holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
                holder.rightMsgTextView.setText(message);
                holder.rightImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(msgDtoList.get(position).getImage()).into(holder.rightImageView);
//                holder.textViweRightSeen.setVisibility(View.VISIBLE);
                holder.leftMsgLayout.setVisibility(LinearLayout.GONE);

                //Right message time
                holder.rightMessageTime.setVisibility(View.VISIBLE);
                holder.rightMessageTime.setText(String.valueOf(Helper.milliToString(Helper.currentTimeStempToLongConversion(msgDtoList.get(position).getCreatedAt()))));
                //Right of left message time

            } else {
                holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
                //user name dibo aikhane
                holder.chatUserName.setText(msgDtoList.get(position).getUserName());
                if (msgDtoList.get(position).getIsAdmin().equals("1")) {
                    holder.chatUserName.setTextColor(context.getResources().getColor(R.color.admin_color));
                }

                holder.leftMsgTextView.setText(message);
//              holder.leftImageView.setImageBitmap(null);
                holder.leftImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(msgDtoList.get(position).getImage()).into(holder.leftImageView);
                holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
//                holder.textViweLeftSeen.setVisibility(View.VISIBLE);


                //left message time
                holder.leftMessageTime.setVisibility(View.VISIBLE);
                holder.leftMessageTime.setText(String.valueOf(Helper.milliToString(Helper.currentTimeStempToLongConversion(msgDtoList.get(position).getCreatedAt()))));
                //end of left message time
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDialog(position);
                    return false;
                }
            });
            holder.leftImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Data.pd = new ProgressDialog(context);
                    Data.pd.setTitle("Loading...");
                    Data.pd.setMessage("Please wait for a while...");
                    SearchUser userProfile = new SearchUser(context);
                    Data.otherUserId = msgDtoList.get(position).getUserId();
                    userProfile.searchUserInfo();
                    Data.pd.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (msgDtoList == null) {
            msgDtoList = new ArrayList<ChatRoomMessage.RoomMessage>();
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

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftMsgLayout;
        RelativeLayout rightMsgLayout;
        TextView leftMsgTextView;
        TextView rightMsgTextView;
        ImageView rightImageView, leftImageView;
        TextView textViweLastMessageHour;
        TextView textViweLeftSeen;
        TextView textViweRightSeen;
        LinearLayout timeLayout;
        LinearLayout enterdIntoRoomLayout;
        RelativeLayout kickedOutLayout;
        //ImageView kickOutAdminImage;
        TextView kickedOutMessage;
        TextView enteredIntoRoomTextView;
        TextView chatUserName;

        //time
        TextView leftMessageTime;
        TextView rightMessageTime;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView != null) {
                //layouts
                leftMsgLayout = itemView.findViewById(R.id.chat_left_msg_layout);
                rightMsgLayout = itemView.findViewById(R.id.chat_right_msg_layout);
                //message body
                leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
                rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
                //profile images
                rightImageView = itemView.findViewById(R.id.image_view_right);
                leftImageView = itemView.findViewById(R.id.image_view_left);
                //last time of text
                textViweLastMessageHour = itemView.findViewById(R.id.last_text_time);
                timeLayout = itemView.findViewById(R.id.time_layout);
                //seen indecator
                textViweLeftSeen = itemView.findViewById(R.id.left_seen_text_view);
                textViweRightSeen = itemView.findViewById(R.id.right_seen_text_view);
                kickedOutLayout = itemView.findViewById(R.id.kicked_out_layout_relative);
                //kickOutAdminImage = itemView.findViewById(R.id.image_view_kicked_out_by_admin);
                kickedOutMessage = itemView.findViewById(R.id.text_view_kicked_out_message);
                enterdIntoRoomLayout = itemView.findViewById(R.id.linear_into_chat_room);
                enteredIntoRoomTextView = itemView.findViewById(R.id.text_view_entered);
                chatUserName = itemView.findViewById(R.id.chat_user_name);

                //timing left or right
                leftMessageTime = itemView.findViewById(R.id.message_time_left);
                rightMessageTime = itemView.findViewById(R.id.message_time_right);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(ChatRoomViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!holder.textViweLastMessageHour.getText().toString().contentEquals(""))
            holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
        Log.i("ATTACHED_VISIBILITY", holder.textViweLastMessageHour.getText().toString() + "\t" + holder.textViweLastMessageHour.getVisibility());
    }

    @Override
    public void onViewRecycled(@NonNull ChatRoomViewHolder holder) {
        super.onViewRecycled(holder);
        if (!holder.textViweLastMessageHour.getText().toString().contentEquals("")) {
            holder.textViweLastMessageHour.setVisibility(View.VISIBLE);
        }
    }


    public void showDialog(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_alert_dialog_chat_room_option);


        TextView tvNormalInterfaceText = dialog.findViewById(R.id.tvNormalInterfaceText);
        TextView deleteOption = dialog.findViewById(R.id.text_view_delete);
        TextView tvAdminInterfaceText = dialog.findViewById(R.id.tvAdminInterfaceText);
        TextView tvDontWasteText = dialog.findViewById(R.id.tvDontWasteText);

        TextView kickOutOption = dialog.findViewById(R.id.text_view_kick_out);
        final EditText editTextDescription = dialog.findViewById(R.id.edit_text_report);
        TextView title = dialog.findViewById(R.id.title);
        LinearLayout reportLayout = dialog.findViewById(R.id.report_layout);
        Button cancelButton = dialog.findViewById(R.id.button_decline);
        Button buttonDone = dialog.findViewById(R.id.button_done);


        if (Data.isChatRoomAdmin) {
            if (msgDtoList.get(position).getUserId().equals(Data.userId)) {
                tvNormalInterfaceText.setVisibility(View.VISIBLE);
                deleteOption.setVisibility(View.VISIBLE);
                tvAdminInterfaceText.setVisibility(View.GONE);
                tvDontWasteText.setVisibility(View.GONE);
                buttonDone.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                kickOutOption.setVisibility(View.GONE);
            } else {
                // todo if he is
                if (msgDtoList.get(position).getIsAdmin().equals("1")) {
                    kickOutOption.setVisibility(View.GONE);
                    tvNormalInterfaceText.setVisibility(View.VISIBLE);
                    deleteOption.setVisibility(View.GONE);
                    tvAdminInterfaceText.setVisibility(View.GONE);
                    tvDontWasteText.setVisibility(View.GONE);
                    reportLayout.setVisibility(View.GONE);
                } else {
                    kickOutOption.setVisibility(View.VISIBLE);
                    tvNormalInterfaceText.setVisibility(View.GONE);
                    deleteOption.setVisibility(View.VISIBLE);
                    tvAdminInterfaceText.setVisibility(View.VISIBLE);
                    tvDontWasteText.setVisibility(View.VISIBLE);
                    reportLayout.setVisibility(View.GONE);
                }
            }
        } else {
            if (msgDtoList.get(position).getUserId().equals(Data.userId)) {
                /*tvNormalInterfaceText.setVisibility(View.GONE);
                deleteOption.setVisibility(View.VISIBLE);
                tvAdminInterfaceText.setVisibility(View.VISIBLE);
                tvDontWasteText.setVisibility(View.VISIBLE);*/

                tvNormalInterfaceText.setVisibility(View.VISIBLE);
                deleteOption.setVisibility(View.VISIBLE);
                tvAdminInterfaceText.setVisibility(View.GONE);
                tvDontWasteText.setVisibility(View.GONE);
                buttonDone.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                kickOutOption.setVisibility(View.GONE);

            } else {
                reportLayout.setVisibility(View.GONE);
            }
        }


        kickOutOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              onChatRoomKickOut(msgDtoList.get(position).getUserId());
                onChatRoomKickOut(msgDtoList.get(position).getMessageId(), position);
                dialog.dismiss();
            }
        });
        deleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChatRoomMessageDelete(msgDtoList.get(position).getMessageId());
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextDescription.getText().toString().isEmpty()) {
                    if (editTextDescription.getText().length() <= 80) {
                        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<String> call = apiInterface.reportMessage(Constants.SECRET_KEY, msgDtoList.get(position).getMessageId(), editTextDescription.getText().toString());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body().equals("successfully reported")) {
                                    Toast.makeText(context, "Thank you. We have got your report.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d(context.getClass().getSimpleName(), t.getMessage());
                                dialog.cancel();
                            }
                        });
                    } else {
                        editTextDescription.setError("Only 80 words available for you.");
                    }
                } else {
                    editTextDescription.setError("This field should not be empty");
                }
            }
        });
        dialog.show();
    }

    private void onChatRoomKickOut(String messageID, int position) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.kickOutFromChatRoom(Constants.SECRET_KEY, Data.userId, type, msgDtoList.get(position).getChatRoomId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                assert response.body() != null;
                if (response.body().equals("banned")) {
                    Toast.makeText(context, "Successfully Kicked out", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new ChatRoomEvent(Constants.KICK_OUT_FROM_CHAT_ROOM, type));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(context.getClass().getSimpleName(), t.getMessage());
            }
        });
    }

    private void onChatRoomMessageDelete(String messageId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Void> call = apiInterface.deleteMessageChatRoom(Constants.SECRET_KEY, messageId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new Event("deleteMessage"));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
