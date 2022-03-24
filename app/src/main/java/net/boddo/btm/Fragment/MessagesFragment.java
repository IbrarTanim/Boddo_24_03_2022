package net.boddo.btm.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.boddo.btm.Adepter.ChatHistoryListAdeptar;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Model.ActiveChat;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseCloudMessagingService;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.boddo.btm.Activity.AddPhotoActivity.TAG;


public class MessagesFragment extends Fragment {

    RecyclerView rvActiveChats;
    LinearLayoutManager linearLayoutManager;
    public List<ActiveChat.ChatList> activeChatList;
    ApiInterface apiInterface;
    ActiveChat activeChat;
    ChatHistoryListAdeptar chatHistoryListAdeptar;
    TextView emptyView;
    BroadcastReceiver broadcastReceiver;
    private static final String TAG = "ActiveChatFragment";

    SwipeRefreshLayout refreshLayout;

    public MessagesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        rvActiveChats = view.findViewById(R.id.rvActiveChats);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        emptyView = view.findViewById(R.id.empty_view);

        activeChatList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvActiveChats.setLayoutManager(linearLayoutManager);
        getAllActiveChatList();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvActiveChats.getContext(), linearLayoutManager.getOrientation());
        rvActiveChats.addItemDecoration(dividerItemDecoration);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getAllActiveChatList();
                chatHistoryListAdeptar = new ChatHistoryListAdeptar(getContext(), activeChatList);
                rvActiveChats.setAdapter(chatHistoryListAdeptar);
                chatHistoryListAdeptar.notifyDataSetChanged();
                if (activeChatList.size() == 0) {
                    rvActiveChats.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    rvActiveChats.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
        getResponseFromLocalBroadcast();

        if (ChatFragment.isActiveChatOpen) {
            getAllActiveChatList();
            ChatFragment.isActiveChatOpen = false;
        }


        return view;
    }

    public void getResponseFromLocalBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.hasExtra(Constants.CHAT_REQUEST_ACCEPTED)) {
                        getAllActiveChatList();
                    } else {
                        Log.d(ActiveChatFragment.class.getSimpleName(), "Nothing to do");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void getAllActiveChatList() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ActiveChat> call = apiInterface.getAllChatList(Constants.SECRET_KEY, Data.userId);

        call.enqueue(new Callback<ActiveChat>() {
            @Override
            public void onResponse(Call<ActiveChat> call, Response<ActiveChat> response) {
                activeChat = response.body();
                if (activeChat.getStatus().equals(Constants.SUCCESS)) {

                    if (activeChat.getUnseenMessage() != 0) {
                        Data.UnseenMessageCount = activeChat.getUnseenMessage();
                        FirebaseCloudMessagingService.bottom_chatButton_dot = 1;
                    } else {
                        Data.UnseenMessageCount = 0;
                        FirebaseCloudMessagingService.bottom_chatButton_dot = 0;

                    }
                    EventBus.getDefault().post(new Event(Constants.MESSAGE_COUNT));

                } else {
                    Log.d(TAG, "onResponse: something wrong");
                }

                if (activeChat.getChatList().size() == 0) {
                    rvActiveChats.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    rvActiveChats.setVisibility(View.VISIBLE);
                    chatHistoryListAdeptar = new ChatHistoryListAdeptar(getActivity(), activeChat.getChatList());
                    rvActiveChats.setAdapter(chatHistoryListAdeptar);
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ActiveChat> call, Throwable t) {

            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event.getEventType().equals(Constants.NEW_MESSAGE_RECEIVED) || event.getEventType().equals(Constants.GET_CHAT_LIST)) {
            getAllActiveChatList();
        }
    }


}