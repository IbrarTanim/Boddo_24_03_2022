
package net.boddo.btm.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.ChatHistoryListAdeptar;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ActiveChat;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseCloudMessagingService;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveChatFragment extends Fragment {

    RecyclerView chatHistoryRecyclerView;
    //ChatHistoryListAdeptar chatListAdeptar;
    public List<ActiveChat.ChatList> chatList;
    LinearLayoutManager linearLayoutManager;

    ChatHistoryListAdeptar chatHistoryListAdeptar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_view)
    TextView emptyView;
    ActiveChat activeChat;
    BroadcastReceiver  broadcastReceiver;


    private static final String TAG = "ActiveChatFragment";
    ApiInterface apiInterface;

    public ActiveChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_chat, container, false);
        ButterKnife.bind(this, view);



        chatList = new ArrayList<>();
        chatHistoryRecyclerView = view.findViewById(R.id.active_user_chatList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        chatHistoryRecyclerView.setLayoutManager(linearLayoutManager);
        getAllActiveChatList();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(chatHistoryRecyclerView.getContext(), linearLayoutManager.getOrientation());
        chatHistoryRecyclerView.addItemDecoration(dividerItemDecoration);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getAllActiveChatList();
                chatHistoryListAdeptar = new ChatHistoryListAdeptar(getContext(), chatList);
                chatHistoryRecyclerView.setAdapter(chatHistoryListAdeptar);
                chatHistoryListAdeptar.notifyDataSetChanged();
                if (chatList.size() == 0) {
                    chatHistoryRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    chatHistoryRecyclerView.setVisibility(View.VISIBLE);
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
        Call<ActiveChat> call = apiInterface.getAllChatList(Constants.SECRET_KEY,Data.userId);

        call.enqueue(new Callback<ActiveChat>() {
            @Override
            public void onResponse(Call<ActiveChat> call, Response<ActiveChat> response) {
               activeChat = response.body();
               if (activeChat.getStatus().equals(Constants.SUCCESS)){

                   if (activeChat.getUnseenMessage() != 0){
                       Data.UnseenMessageCount = activeChat.getUnseenMessage();
                       FirebaseCloudMessagingService.bottom_chatButton_dot =  1;
                   }else{
                       Data.UnseenMessageCount = 0;
                       FirebaseCloudMessagingService.bottom_chatButton_dot =0;

                   }
                   EventBus.getDefault().post(new Event(Constants.MESSAGE_COUNT));

               }else{
                   Log.d(TAG, "onResponse: something wrong");
               }

                if (activeChat.getChatList().size() == 0) {
                    chatHistoryRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    chatHistoryRecyclerView.setVisibility(View.VISIBLE);
                    chatHistoryListAdeptar = new ChatHistoryListAdeptar(getActivity(), activeChat.getChatList());
                    chatHistoryRecyclerView.setAdapter(chatHistoryListAdeptar);
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ActiveChat> call, Throwable t) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        getResponseFromLocalBroadcast();
//    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("lksdflkj", "asdfsad");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("lksdflkj", "asdfsad");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        if (event.getEventType().equals(Constants.NEW_MESSAGE_RECEIVED) || event.getEventType().equals(Constants.GET_CHAT_LIST)){
         getAllActiveChatList();
        }
    }
}
