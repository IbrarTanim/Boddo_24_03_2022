package net.boddo.btm.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.ChatRoomActivity;
import net.boddo.btm.Activity.Chatroom.chatroomuser.ChatRoomAllUserActivity;
import net.boddo.btm.Event.ChatRoomEvent;
import net.boddo.btm.Activity.PrivateChatActivity;
import net.boddo.btm.Adepter.ChatRoomMessageAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRoom;
import net.boddo.btm.Model.ChatRoomMessage;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalChatroomFragment extends Fragment {


    public static boolean isGlobalChatRoomInView = false;

    public GlobalChatroomFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.emoji_btn)
    ImageView emojiButton;

    @BindView(R.id.emojicon_edit_text)
    EmojiconEditText editTextMessage;

    @BindView(R.id.submit_btn)
    ImageView submitButton;

    ImageView submitButtonTouch;

    @BindView(R.id.image_view_option_menu)
    ImageView userListButton;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.layout_footer)
    RelativeLayout footer;

    @BindView(R.id.text_view_kick)
    TextView textViewKickOut;


    EmojiconTextView textView;
    EmojIconActions emojIcon;
    ApiInterface apiInterface;

    //variables
    private long mLastClickTime = 0;
    String msgContent = "";
    List<ChatRoomMessage.RoomMessage> messageList;
    ChatRoomMessage.RoomMessage message;
    ChatRoomMessageAdapter adapter;
    boolean isBannedFromGlobal = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_global_chatroom, container, false);
        ButterKnife.bind(this, view);
        emojIcon = new EmojIconActions(getActivity(), view, editTextMessage, emojiButton);
        emojIcon.ShowEmojIcon();

        getAllGlobalMessage();


        submitButton = view.findViewById(R.id.submit_btn);
        submitButtonTouch = view.findViewById(R.id.submitButtonTouch);
        editTextMessage = view.findViewById(R.id.emojicon_edit_text);
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String valueCheck = String.valueOf(s);
                if (valueCheck.isEmpty()) {
                    submitButton.setVisibility(View.GONE);
                    submitButtonTouch.setVisibility(View.VISIBLE);
                } else {
                    submitButtonTouch.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (ChatRoomFragment.isBannedFromGlobal) {
            footer.setVisibility(View.GONE);
            textViewKickOut.setVisibility(View.VISIBLE);
        } else {
            ChatRoomFragment.isBannedFromGlobal = false;
            footer.setVisibility(View.VISIBLE);
            textViewKickOut.setVisibility(View.GONE);
        }
        return view;
    }

    private void checkIsBanForGlobalChat() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRoom> call = apiInterface.checkIsKickedOut(Constants.SECRET_KEY, Data.userId, Constants.GLOBAL);
        call.enqueue(new Callback<ChatRoom>() {
            @Override
            public void onResponse(Call<ChatRoom> call, Response<ChatRoom> response) {
                if (response.body() != null) {
                    ChatRoom chatRoom = response.body();
                    if (chatRoom.getIsBanned().equals("1")) {
                        ChatRoomFragment.isBannedFromGlobal = true;
                        footer.setVisibility(View.GONE);
                        textViewKickOut.setVisibility(View.VISIBLE);
                    } else {
                        ChatRoomFragment.isBannedFromGlobal = false;
                        footer.setVisibility(View.VISIBLE);
                        textViewKickOut.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatRoom> call, Throwable t) {
                Log.d(ChatRoomActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }

    public void getAllGlobalMessage() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRoomMessage> call = apiInterface.getAllChatRoomMessage(Constants.SECRET_KEY, Constants.GLOBAL, Data.userId);
        call.enqueue(new Callback<ChatRoomMessage>() {
            @Override
            public void onResponse(Call<ChatRoomMessage> call, Response<ChatRoomMessage> response) {
                if (response.body().getStatus().equals("success")) {
                    messageList = response.body().getMessage();
                    adapter = new ChatRoomMessageAdapter(getActivity(), messageList, Constants.GLOBAL);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ChatRoomMessage> call, Throwable t) {
                Log.d(GlobalChatroomFragment.class.getSimpleName(), t.getMessage());
            }
        });
    }

    @OnClick(R.id.submit_btn)
    public void onSendButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        msgContent = editTextMessage.getText().toString();
        if (msgContent.length() > 0 && !msgContent.equals("")) {
            editTextMessage.setText("");
            submitButtonTouch.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ChatRoomMessage> call = apiInterface.startRoomChat(Constants.SECRET_KEY, Data.userId, Constants.GLOBAL, msgContent);
            call.enqueue(new Callback<ChatRoomMessage>() {
                @Override
                public void onResponse(Call<ChatRoomMessage> call, Response<ChatRoomMessage> response) {
                    String responeCheck = response.toString();
                    if (response.body().getStatus().equals("success")) {
                        message = response.body().getSingleMessage();
                        messageList.add(message);
                        int newMsgPosition = messageList.size() - 1;
                        // NotifyEvent recycler view insert one new data.
                        adapter.notifyItemInserted(newMsgPosition);
                        // Scroll RecyclerView to the last message.
                        recyclerView.scrollToPosition(newMsgPosition);
                        // Empty the input edit text box.
                        //increase the sender message amount
                        getAllGlobalMessage();
                    }
                }

                @Override
                public void onFailure(Call<ChatRoomMessage> call, Throwable t) {
                    Log.d("PrivateChatActivity", t.getMessage());
                }
            });
        } else {
            Log.d(PrivateChatActivity.class.getSimpleName(), "Nothing ");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIsBanForGlobalChat();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChatRoomEvent event) {
        if (event != null) {
            if (event.getChatRoomType().equals(Constants.GLOBAL)
                    || event.getChatRoomType().equals("deleteMessage")
                    || event.getChatRoomType().equals(Constants.ENTERED_INTO_CHAT_ROOM)
                    || event.getChatRoomType().equals(Constants.LEFT_FROM_CHAT_ROOM)
            ) {
                new MessageLoadInBackground().execute();
            }
            if (event.getChatRoomType().equals(Constants.CHAT_ROOM)) {
                new MessageLoadInBackground().execute();
            }
            if (!event.getEventName().equals("")) {
                if (event.getEventName().equals(Constants.KICK_OUT_FROM_CHAT_ROOM) && event.getEventType().equals(Constants.GLOBAL)) {

                    checkIsBanForGlobalChat();
                }
            }
        }
    }

    @OnClick(R.id.image_view_option_menu)
    public void showAllUserList() {
        //TOdo get Global user list
        Intent intent = new Intent(getContext(), ChatRoomAllUserActivity.class);
        intent.putExtra(Constants.TYPE, Constants.GLOBAL);
        startActivity(intent);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //todo update ui
            getAllGlobalMessage();
        }
    }

    class MessageLoadInBackground extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ChatRoomMessage> call = apiInterface.getAllChatRoomMessage(Constants.SECRET_KEY, Constants.GLOBAL, Data.userId);
            call.enqueue(new Callback<ChatRoomMessage>() {
                @Override
                public void onResponse(Call<ChatRoomMessage> call, Response<ChatRoomMessage> response) {
                    if (response.body().getStatus().equals("success")) {
                        messageList = response.body().getMessage();
                        adapter = new ChatRoomMessageAdapter(getActivity(), messageList, Constants.GLOBAL);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.scrollToPosition(messageList.size() - 1);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<ChatRoomMessage> call, Throwable t) {
                    Log.d(GlobalChatroomFragment.class.getSimpleName(), t.getMessage());
                }
            });
            return null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        isGlobalChatRoomInView = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isGlobalChatRoomInView = false;
    }
}
