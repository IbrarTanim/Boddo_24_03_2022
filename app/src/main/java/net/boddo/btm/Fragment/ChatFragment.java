package net.boddo.btm.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nex3z.notificationbadge.NotificationBadge;

import net.boddo.btm.Activity.ChatRequestActivity;
import net.boddo.btm.Activity.ProfileOneActivity;
import net.boddo.btm.Activity.RecentMatchProPicActivity;
import net.boddo.btm.Activity.StoryActivity;
import net.boddo.btm.Adepter.ChatHistoryListAdeptar;
import net.boddo.btm.Adepter.PhotoBlogTabAdapter;
import net.boddo.btm.Adepter.photoblog.HotlistAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Model.ActiveChat;
import net.boddo.btm.Model.ChatRequest;
import net.boddo.btm.Model.Hotlist;
import net.boddo.btm.Model.RecentMatchModel;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseCloudMessagingService;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    //private static final String TAG = "ChatFragment";

    private Context context;

    NotificationBadge badgingMessCountView;
    NotificationBadge badgingMessageRequestView;
    Unbinder unbinder;
    /*private TabLayout chatTablayout;
    private ViewPager chatViewPager;*/
    private PhotoBlogTabAdapter adapter;
    String URL = "https://bluetigermobile.com/palup/apis/random_hotlist.php";
    Hotlist randomHotList[];
    public static boolean isActiveChatOpen = false;

    //from Active Chat
    RecyclerView chatHistoryRecyclerView;
    //ChatHistoryListAdeptar chatListAdeptar;
    public List<ActiveChat.ChatList> chatList;
    public List<ActiveChat.ChatList> chatListSearch;
    LinearLayoutManager linearLayoutManager;

    ChatHistoryListAdeptar chatHistoryListAdeptar;
    //SwipeRefreshLayout refreshLayout;
    TextView emptyView, tvUpdateProfileChatFragment;
    CircleImageView civMesssageProfic, civRecentMatchs, civChatRequest, civStory;

    EditText edtSearch;
    ActiveChat activeChat;
    RecentMatchModel recentMatch;
    BroadcastReceiver broadcastReceiver;
    //private static final String TAG = "ActiveChatFragment";
    ApiInterface apiInterface;
    private LinearLayout llmNoChatListMsg;
    private ProgressBar active_chatProgressBar;
    //until here


    public ChatFragment() {
        // Required empty public constructor
    }

    LocalBroadcastManager broadcaster;
    //ActiveChatFragment activeChatFragment;


    /*@BindView(R.id.user_image_view)
    ImageView userImageView;*/

    /*@BindView(R.id.text_view_hotlist)
    TextView hotlistTitle;*/


    @BindView(R.id.top_photo_layout)
    LinearLayout hotlistLayout;

    /*@BindView(R.id.profile_image_hotlist)
    CardView hotlistImageView;*/

    /*@BindView(R.id.recent_hot_list_recycler_view)
    RecyclerView hotListRecyclerView;*/

    Hotlist hotlist[];
    HotlistAdapter hotListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        //init(view);

        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = view.findViewById(R.id.blankView);
        int statusBarHeight = GetStatusBarHeight();
        if (statusBarHeight != 0) {
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = statusBarHeight;
            blankView.setLayoutParams(params);
            //Log.e(TAG, "Status Bar Height: " + statusBarHeight );
        }
        /**
         * Set
         * Status
         * Bar
         * Size
         * End
         * */

        ProgressDialog.show(context);

        badgingMessCountView = view.findViewById(R.id.badging_messCount_view);
        badgingMessageRequestView = view.findViewById(R.id.badging_message_request_view);
        active_chatProgressBar = view.findViewById(R.id.active_chatProgressBar);
        //refreshLayout = view.findViewById(R.id.refresh_layout);
        emptyView = view.findViewById(R.id.empty_view);
        tvUpdateProfileChatFragment = view.findViewById(R.id.tvUpdateProfileChatFragment);
        edtSearch = view.findViewById(R.id.edtSearch);
        civMesssageProfic = view.findViewById(R.id.civMesssageProfic);
        civRecentMatchs = view.findViewById(R.id.civRecentMatchs);
        civChatRequest = view.findViewById(R.id.civChatRequest);
        civStory = view.findViewById(R.id.civStory);
        llmNoChatListMsg = view.findViewById(R.id.llmNoChatListMsg);
        Glide.with(getActivity()).load(Data.profilePhoto).into(civMesssageProfic);

        tvUpdateProfileChatFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileOneActivity.class);
                startActivity(intent);
            }
        });

        civStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                startActivity(intent);
            }
        });


        civRecentMatchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecentMatchProPicActivity.class);
                startActivity(intent);
            }
        });


        civChatRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatRequestActivity.class);
                startActivity(intent);
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
            }
        });


        //Glide.with(getActivity()).load(Data.profilePhoto).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(userImageView);

        /*getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        adapter = new PhotoBlogTabAdapter(getActivity().getSupportFragmentManager());
        activeChatFragment = new ActiveChatFragment();
        adapter.addFragment(activeChatFragment, "Chats");
        //adapter.addFragment(new ChatRequestFragment(), "CHAT REQUEST");
        chatViewPager.setAdapter(adapter);
        chatTablayout.setupWithViewPager(chatViewPager);*/

        //getRecentHotList();

//        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                GsonBuilder gsonBuilder = new GsonBuilder();
//                Gson gson = gsonBuilder.create();
//                randomHotList = gson.fromJson(response, Hotlist[].class);
//                if (randomHotList.length == 1) {
//                    hotListPepole1.setVisibility(View.VISIBLE);
//                    Glide.with(getActivity()).load(randomHotList[0].getProfilePhoto()).into(hotListPepole1);
//                } else if (randomHotList.length == 2) {
//                    hotListPepole1.setVisibility(View.VISIBLE);
//                    hotListPepole2.setVisibility(View.VISIBLE);
//                    Glide.with(getActivity()).load(randomHotList[0].getProfilePhoto()).into(hotListPepole1);
//                    Glide.with(getActivity()).load(randomHotList[1].getProfilePhoto()).into(hotListPepole2);
//                } else if (randomHotList.length == 3) {
//                    hotListPepole1.setVisibility(View.VISIBLE);
//                    hotListPepole2.setVisibility(View.VISIBLE);
//                    hotListPepole3.setVisibility(View.VISIBLE);
//                    Glide.with(getActivity()).load(randomHotList[0].getProfilePhoto()).into(hotListPepole1);
//                    Glide.with(getActivity()).load(randomHotList[1].getProfilePhoto()).into(hotListPepole2);
//                    Glide.with(getActivity()).load(randomHotList[2].getProfilePhoto()).into(hotListPepole3);
//                } else if (randomHotList.length == 4) {
//                    hotListPepole1.setVisibility(View.VISIBLE);
//                    hotListPepole2.setVisibility(View.VISIBLE);
//                    hotListPepole3.setVisibility(View.VISIBLE);
//                    hotListPepole4.setVisibility(View.VISIBLE);
//                    Glide.with(getActivity()).load(randomHotList[0].getProfilePhoto()).into(hotListPepole1);
//                    Glide.with(getActivity()).load(randomHotList[1].getProfilePhoto()).into(hotListPepole2);
//                    Glide.with(getActivity()).load(randomHotList[2].getProfilePhoto()).into(hotListPepole3);
//                    Glide.with(getActivity()).load(randomHotList[3].getProfilePhoto()).into(hotListPepole4);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(ChatFragment.class.getSimpleName(), error.getMessage());
//            }
//        });
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        queue.add(request);


        chatList = new ArrayList<>();
        chatListSearch = new ArrayList<>();

        chatHistoryRecyclerView = view.findViewById(R.id.active_user_chatList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        chatHistoryRecyclerView.setLayoutManager(linearLayoutManager);
        getAllActiveChatList();
        getAllRecentChatData();
        getChatRequestData();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(chatHistoryRecyclerView.getContext(), linearLayoutManager.getOrientation());
        chatHistoryRecyclerView.addItemDecoration(dividerItemDecoration);


        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        });*/


        getResponseFromLocalBroadcast();

        if (ChatFragment.isActiveChatOpen) {
            getAllActiveChatList();
            ChatFragment.isActiveChatOpen = false;
        }


        //until here






        /*chatViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });*/


        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    private void filter(String text) {
        chatListSearch = new ArrayList();
        for (ActiveChat.ChatList projects : activeChat.getChatList()) {
            if (projects.getFirstName().toLowerCase().contains(text.toLowerCase())) {
                chatListSearch.add(projects);
            }

        }
        chatHistoryListAdeptar.updateList(chatListSearch);
    }


    public void getResponseFromLocalBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.hasExtra(Constants.CHAT_REQUEST_ACCEPTED)) {
                        getAllActiveChatList();
                    } else {
                        // Log.d(ActiveChatFragment.class.getSimpleName(), "Nothing to do");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    //On Test 4/11/2021
    public void getAllActiveChatList() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ActiveChat> call = apiInterface.getAllChatList(Constants.SECRET_KEY, Data.userId);

        call.enqueue(new Callback<ActiveChat>() {
            @Override
            public void onResponse(Call<ActiveChat> call, Response<ActiveChat> response) {
                activeChat = response.body();
                if (activeChat.getStatus().equals(Constants.SUCCESS)) {
                    //chat fragment 4/11/2021
                    if (activeChat.getUnseenMessage() != 0) {
                        Data.UnseenMessageCount = activeChat.getUnseenMessage();
                        FirebaseCloudMessagingService.bottom_chatButton_dot = 1;
                    } else {
                        Data.UnseenMessageCount = 0;
                        FirebaseCloudMessagingService.bottom_chatButton_dot = 0;

                    }
                    EventBus.getDefault().post(new Event(Constants.MESSAGE_COUNT));

                } else {

                }

                if (activeChat.getChatList().size() == 0) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.cancel();
                            llmNoChatListMsg.setVisibility(View.VISIBLE);
                        }
                    }, 2000);

                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.cancel();
                            List<ActiveChat.ChatList> chatLists = activeChat.getChatList();
                            chatHistoryRecyclerView.setVisibility(View.VISIBLE);
                            chatHistoryListAdeptar = new ChatHistoryListAdeptar(getActivity(), chatLists);
                            chatHistoryRecyclerView.setAdapter(chatHistoryListAdeptar);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onFailure(Call<ActiveChat> call, Throwable t) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialog.cancel();
                        llmNoChatListMsg.setVisibility(View.VISIBLE);
                    }
                }, 2000);

            }
        });

    }

    public void getAllRecentChatData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.e("userId", "getAllRecentChatList: " + Data.userId);
        Call<RecentMatchModel> call = apiInterface.getRecentMatchData(Data.userId, Constants.SECRET_KEY);

        call.enqueue(new Callback<RecentMatchModel>() {
            @Override
            public void onResponse(Call<RecentMatchModel> call, Response<RecentMatchModel> response) {
                recentMatch = response.body();

                List<RecentMatchModel.Match> matchList = recentMatch.getMatches();
                //  Log.e("responsevalue", "onResponse: "+recentMatch.setMatches());
                Log.e("rrr", "onResponse: " + response.body().getMatches().size());


                if (recentMatch.getStatus().equals(Constants.SUCCESS)) {

                    for (RecentMatchModel.Match matchData : matchList) {
                        Glide.with(getContext())
                                .load(matchData.getProfilePhoto())
                                .into(civRecentMatchs);
                    }


                   /* if(recentMatch.getMatches().size()!=0){
                        //Glide.with(getActivity()).load(randomHotList[3].getProfilePhoto()).into(hotListPepole4);

                        Glide.with(getActivity())
                                .load(response.body().getMatches().get(response.body().getMatches().size()-1).getProfilePhoto())
                                .centerCrop()
                                .into(civRecentMatchs);



                    }*/


                } else {
                    //Log.d(TAG, "onResponse: something wrong");
                }

            }

            @Override
            public void onFailure(Call<RecentMatchModel> call, Throwable t) {
                Log.e("fail", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    public void getChatRequestData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.e("userId", "getAllRecentChatList: " + Data.userId);
        Call<ChatRequest> call = apiInterface.getAllRequestedList(Constants.SECRET_KEY, Data.userId);

        call.enqueue(new Callback<ChatRequest>() {
            @Override
            public void onResponse(Call<ChatRequest> call, Response<ChatRequest> response) {


                List<ChatRequest.RequestedMessage> matchList = response.body().getRequestedMessage();
                //  Log.e("responsevalue", "onResponse: "+recentMatch.setMatches());
                Log.e("rrr", "onResponse: " + response.body().getRequestedMessage().size());


                if (response.body().getStatus().equals(Constants.SUCCESS)) {

                    for (ChatRequest.RequestedMessage matchData : matchList) {
                        Glide.with(getActivity()).load(matchData.getProfilePhoto()).into(civChatRequest);
                    }


                } else {
                    //Log.d(TAG, "onResponse: something wrong");
                }

            }

            @Override
            public void onFailure(Call<ChatRequest> call, Throwable t) {
                Log.e("fail", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }






    /*@OnClick(R.id.profile_image_hotlist)
    public void hotlistImageViewClicked() {
        Intent intent = new Intent(getActivity(), HotlistActivityNew.class);
        getActivity().startActivity(intent);
    }*/

    /*private void getRecentHotList() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Hotlist[]> call = apiInterface.getRecentHotlist();
        call.enqueue(new Callback<Hotlist[]>() {
            @Override
            public void onResponse(Call<Hotlist[]> call, retrofit2.Response<Hotlist[]> response) {
                hotlist = response.body();
                if (hotlist.length > 0) {
                    hotlist = response.body();
                    prepareList();
                }
            }

            @Override
            public void onFailure(Call<Hotlist[]> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }*/

/*    private void prepareList() {
        hotListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hotListAdapter = new HotlistAdapter(getContext(), hotlist);
        hotListAdapter.setProfileImageClickListener(this);
        hotListRecyclerView.setAdapter(hotListAdapter);
    }*/


   /* public void init(View view) {
        chatTablayout = view.findViewById(R.id.chat_tablayout);
        chatViewPager = view.findViewById(R.id.chat_viewPager);

        chatTablayout.setTabMode(TabLayout.MODE_FIXED);
        chatTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }*/


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {

        String rokanB = event.getEventType();
        if (event.getEventType().equals(Constants.MESSAGE_COUNT)) {
            badgingMessCountView.setNumber(Data.UnseenMessageCount);
            badgingMessCountView.setAnimationEnabled(true);
        }

        if (event.getEventType().equals(Constants.REQUEST_COUNT)) {
            badgingMessageRequestView.setNumber(Data.RequestMessageCount);
            badgingMessageRequestView.setAnimationEnabled(true);
        }
    }


    public int GetStatusBarHeight() {
        // returns 0 for no result found
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
