package net.boddo.btm.Fragment;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.ChatRequestAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRequest;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseCloudMessagingService;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRequestFragment extends Fragment {
    public ChatRequestFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.request_message_recycler_view)
    RecyclerView requestRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView emptyView;
    ApiInterface apiInterface;
    List<ChatRequest.RequestedMessage> chatRequestList;
    ChatRequestAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    BroadcastReceiver receiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_chat_request, container, false);
        ButterKnife.bind(this,view);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        linearLayoutManager = new LinearLayoutManager(getContext());
        requestRecyclerView.setLayoutManager(linearLayoutManager);
        getAllChatRequestList();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getAllChatRequestList();
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public void getAllChatRequestList() {
            Call<ChatRequest> call = apiInterface.getAllRequestedList(Constants.SECRET_KEY, Data.userId);
            call.enqueue(new Callback<ChatRequest>() {
                @Override
                public void onResponse(Call<ChatRequest> call, Response<ChatRequest> response) {
                    ChatRequest request = response.body();

                    if (request.getRequestedMessage().size() != 0){
                        Data.RequestMessageCount = request.getRequestedMessage().size();
                        FirebaseCloudMessagingService.bottom_chatButton_dot = 1;

                    }else{
                        Data.RequestMessageCount = 0;
                        FirebaseCloudMessagingService.bottom_chatButton_dot =0;
                    }
                    EventBus.getDefault().post(new Event(Constants.REQUEST_COUNT));

                    if (request.getStatus().equals("success")){
                        chatRequestList = request.getRequestedMessage();
                            adapter = new ChatRequestAdapter(getActivity(),chatRequestList);
                            requestRecyclerView.setAdapter(adapter);

                        if (chatRequestList.size() == 0){
                            requestRecyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);

                        }else{
                            requestRecyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                                    linearLayoutManager.getOrientation());
                        requestRecyclerView.addItemDecoration(dividerItemDecoration);
                    }
                }
                @Override
                public void onFailure(Call<ChatRequest> call, Throwable t) {
                    Log.d(ChatRequestFragment.class.getSimpleName(),t.getMessage());
                }
            });
    }
    @Override
    public void onResume() {
        super.onResume();
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

        if (  event.getEventType().equals(Constants.MESSAGE_REQUEST)){
            getAllChatRequestList();
        }

    }

}
