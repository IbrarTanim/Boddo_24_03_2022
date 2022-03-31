package net.boddo.btm.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import net.boddo.btm.Activity.Chatroom.chatroomuser.ChatRoomAllUserActivity;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Fragment.ChatRoomFragment;
import net.boddo.btm.Fragment.CountryChatroomFragment;
import net.boddo.btm.Fragment.DateChatroomFragment;
import net.boddo.btm.Fragment.FriendsChatroomFragment;
import net.boddo.btm.Fragment.GlobalChatroomFragment;
import net.boddo.btm.Fragment.LoveChatroomFragment;
import net.boddo.btm.Adepter.GlobalChatroomAdapter;
import net.boddo.btm.Model.ChatRoom;
import net.boddo.btm.Model.ChatRoomUserInfo;
import net.boddo.btm.Model.CheckUserAlreadyValidInGlobalChatRoom;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Limitation;
import net.boddo.btm.Utills.SharedPref;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity {

    ChatRoomActivity activity;
    private GlobalChatroomAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    TextView tvBackChatRoom;
    ImageView ivChatroomMenueWindow;

    BroadcastReceiver receiver;
    public static boolean isAdmin = false;
    public static int whichInTheLast = 0;

    ApiInterface apiInterface;
    String[] country;
    String lastEnter = "";
    String whichRoomIsClicked = "";
    String activeTabe = "";
    int isSubscribedInGlobal = 0;

    public static List<ChatRoomUserInfo> chatRoomUserInfoList;
    PrettyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        activity = this;
        ButterKnife.bind(this);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        ivChatroomMenueWindow = findViewById(R.id.ivChatroomMenueWindow);
        tvBackChatRoom = findViewById(R.id.tvBackChatRoom);
        tvBackChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpAlertDialog();
            }
        });
        ivChatroomMenueWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatroomMenueWindowClick();
            }
        });

//        toolbarTittle.setText("Chat Room");

//        if (getIntent().hasExtra(Constants.CHAT_ROOM)){
//
//        }

        country = Data.userCountry.split(",");
        adapter = new GlobalChatroomAdapter(getSupportFragmentManager());
        dialog = new PrettyDialog(this);
        adapter.addFragment(new CountryChatroomFragment(), country[1]);

        if (SharedPref.getLastChatRoom(Constants.LAST_CHAT_ROOM).equals("") || SharedPref.getLastChatRoom(Constants.LAST_CHAT_ROOM).equalsIgnoreCase(Constants.FRIENDS)) {
            adapter.addFragment(new FriendsChatroomFragment(), "Friends");
            whichInTheLast = 3;
        } else if (SharedPref.getLastChatRoom(Constants.LAST_CHAT_ROOM).equals(Constants.LOVE)) {
            adapter.addFragment(new LoveChatroomFragment(), "Love");
            whichInTheLast = 4;
        } else if (SharedPref.getLastChatRoom(Constants.LAST_CHAT_ROOM).equals(Constants.DATE)) {
            adapter.addFragment(new DateChatroomFragment(), "Date");
            whichInTheLast = 5;
        }
        adapter.addFragment(new GlobalChatroomFragment(), "Global");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        if (getIntent().hasExtra(Constants.CHAT_ROOM)) {
            if (getIntent().getStringExtra(Constants.CHAT_ROOM).equals(country[1])) {
                viewPager.setCurrentItem(0);
                whichRoomIsClicked = country[1];
                activeTabe = whichRoomIsClicked;
            } else if (getIntent().getStringExtra(Constants.CHAT_ROOM).equals(Constants.GLOBAL)) {
                viewPager.setCurrentItem(2);
                whichRoomIsClicked = Constants.GLOBAL;
                activeTabe = whichRoomIsClicked;
            } else {
                viewPager.setCurrentItem(1);
                whichRoomIsClicked = getIntent().getStringExtra(Constants.CHAT_ROOM);
                activeTabe = whichRoomIsClicked;
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        showEnteredIntoChatRoom(country[1], "enter");
                        activeTabe = country[1];
                        EventBus.getDefault().post(new Event("updateCountry"));
                        break;
                    case 1:
                        //rokan
                        if (whichInTheLast == 3) {
                            showEnteredIntoChatRoom(Constants.FRIENDS, "enter");
                            activeTabe = Constants.FRIENDS;
                        } else if (whichInTheLast == 4) {
                            showEnteredIntoChatRoom(Constants.LOVE, "enter");
                            activeTabe = Constants.LOVE;
                        } else {
                            showEnteredIntoChatRoom(Constants.DATE, "enter");
                            activeTabe = Constants.DATE;
                        }
                        break;
                    case 2:
                        if (ChatRoomFragment.isSubscribed == 1) {
                            showEnteredIntoChatRoom(Constants.GLOBAL, "enter");
                        } else if (isSubscribedInGlobal == 1) {
                            showEnteredIntoChatRoom(Constants.GLOBAL, "enter");
                        } else {
                            new CheckUserAlreadyValidInGlobalChatRoomInBackGround().execute();
                        }
                        activeTabe = Constants.GLOBAL;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    class CheckUserAlreadyValidInGlobalChatRoomInBackGround extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            checkAlreadySubscribeForGlobal();
            return null;
        }
    }


    //Farabi
    private void checkAlreadySubscribeForGlobal() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CheckUserAlreadyValidInGlobalChatRoom> call = apiInterface.checkUserAlreadyEnteredIntoGlobalChatRoom(Constants.SECRET_KEY,
                Data.userId, Constants.GLOBAL);
        call.enqueue(new Callback<CheckUserAlreadyValidInGlobalChatRoom>() {
            @Override
            public void onResponse(Call<CheckUserAlreadyValidInGlobalChatRoom> call, Response<CheckUserAlreadyValidInGlobalChatRoom> response) {
                CheckUserAlreadyValidInGlobalChatRoom objCUAVIGCR = response.body();
                if (objCUAVIGCR.getStatus().equals("success")) {
                    if (objCUAVIGCR.getIsSubscribed().equals("yes")) {
                        isSubscribedInGlobal = 1;
                        showEnteredIntoChatRoom(Constants.GLOBAL, "enter");
                        EventBus.getDefault().post(new Event("updateGlobal"));
                    } else {
                        /*if (Integer.parseInt(Data.userPalupPoint) >= 20) {
                            subscribeToGlobal();
                            isSubscribedInGlobal = 0;
                        } else {
                            insufficientCredits();
                            isSubscribedInGlobal = 0;

                        }*/

                        if (Integer.parseInt(Data.userPalupPoint) >= Limitation.GLOBAL_CHAT_ROOM_CREDITS) {
                            subscribeToGlobal();
                            isSubscribedInGlobal = 0;
                        } else {

                            insufficientCredits();
                            isSubscribedInGlobal = 0;
                        }

                    }
                } else if (objCUAVIGCR.getStatus().equals("not exist")) {
                    /*subscribeToGlobal();
                    //insufficientCredits();
                    isSubscribedInGlobal = 0;*/


                    if (Integer.parseInt(Data.userPalupPoint) >= Limitation.GLOBAL_CHAT_ROOM_CREDITS) {
                        subscribeToGlobal();
                        isSubscribedInGlobal = 0;
                    } else {
                        insufficientCredits();
                        isSubscribedInGlobal = 0;
                    }


                }
            }
            // done

            @Override
            public void onFailure(Call<CheckUserAlreadyValidInGlobalChatRoom> call, Throwable t) {
                Log.d(ChatRoomFragment.class.getSimpleName(), t.getMessage());
            }
        });

    }


    public void insufficientCredits() {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.insufficient_credits);
        dialog.setCancelable(false);

        final Button payjoin = dialog.findViewById(R.id.payjoin);
        final Button notnow = dialog.findViewById(R.id.notnow);

        payjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra("BuyCredits", true);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();

    }


    public void subscribeToGlobal() {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.check_already_subscribe_for_global);
        dialog.setCancelable(false);

        final Button payjoin = dialog.findViewById(R.id.payjoin);
        final Button notnow = dialog.findViewById(R.id.notnow);

        payjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatRoom(Constants.GLOBAL);
                Data.userPalupPoint = String.valueOf(Integer.parseInt(Data.userPalupPoint) - 20);
                dialog.dismiss();
            }
        });
        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();

    }



    /*private void subscribeToGlobal() {
        final PrettyDialog dialog = new PrettyDialog(ChatRoomActivity.this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            dialog.setTitle("Chat Room")
                    .setTitleColor(R.color.pdlg_color_blue)
                    .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                    .setMessage("Discover many peoples from around the world!\n" +
                            "\n" +
                            "Chat with thousands of cool peoples who chatting in global chat room from around the world\n" +
                            "\n" +
                            "\n" +
                            "Cost of joining global chat room 20 credits")
                    .setMessageColor(R.color.pdlg_color_black)
                    .setAnimationEnabled(true)
                    // Join button
                    .addButton(
                            "Join",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.colorPrimary,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    // Do what you gotta do
                                    chatRoom(Constants.GLOBAL);
                                    Data.userPalupPoint = String.valueOf(Integer.parseInt(Data.userPalupPoint) - 20);
                                    dialog.dismiss();
                                }
                            }
                    )
// Cancel button
                    .addButton(
                            "Not Now",
                            R.color.pdlg_color_white,
                            R.color.pdlg_color_red,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    // Dismiss
                                    dialog.dismiss();
                                    finish();

                                }
                            }
                    ).show();
            dialog.setCancelable(false);
        } else {


            dialog.setTitle("Chat Room")
                    .setTitleColor(R.color.pdlg_color_blue)
                    .setMessage("Discover many peoples from around the world!\n" +
                            "\n" +
                            "Chat with thousands of cool peoples who chatting in global chat room from around the world\n" +
                            "\n" +
                            "Cost of joining global chat room 20 credits")
                    .setMessageColor(R.color.pdlg_color_black)
                    .setAnimationEnabled(true)
                    // Join button
                    .addButton(
                            "Join",                    // button text
                            R.color.pdlg_color_white,        // button text color
                            R.color.colorPrimary,        // button background color
                            new PrettyDialogCallback() {        // button OnClick listener
                                @Override
                                public void onClick() {
                                    // Do what you gotta do
                                    chatRoom(Constants.GLOBAL);
                                    Data.userPalupPoint = String.valueOf(Integer.parseInt(Data.userPalupPoint) - 20);
                                    dialog.dismiss();
                                }
                            }
                    )
// Cancel button
                    .addButton(
                            "Not Now",
                            R.color.pdlg_color_white,
                            R.color.pdlg_color_red,
                            new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    // Dismiss
                                    dialog.dismiss();
                                    finish();

                                }
                            }
                    ).show();
            dialog.setCancelable(false);

        }
    }*/

    public void chatRoom(final String type) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRoom> call = apiInterface.roomChat(Constants.SECRET_KEY, Data.userId, type, isSubscribedInGlobal);
        call.enqueue(new Callback<ChatRoom>() {
            @Override
            public void onResponse(Call<ChatRoom> call, Response<ChatRoom> response) {
                ChatRoom chatRoom = response.body();
                if (chatRoom.getIsAdmin().equals("1")) {
                    Data.isChatRoomAdmin = true;
                }
                if (chatRoom.getIsBanned().equals("1")) {
                    if (type.equals(Constants.GLOBAL)) {
                        ChatRoomFragment.isBannedFromGlobal = true;
                    } else if (type.equals(Constants.LOVE)) {
                        ChatRoomFragment.isBannedFromLove = true;
                    } else if (type.equals(Constants.FRIENDS)) {
                        ChatRoomFragment.isBannedFromFriends = true;
                    } else if (type.equals(Constants.DATE)) {
                        ChatRoomFragment.isBannedFromDate = true;
                    } else {
                        ChatRoomFragment.isBannedFromCountry = true;
                    }
                }
                isSubscribedInGlobal = 1;
                EventBus.getDefault().post(new Event(Constants.ENTERED_INTO_CHAT_ROOM + ""));
            }

            @Override
            public void onFailure(Call<ChatRoom> call, Throwable t) {
                Log.d(ChatRoomFragment.class.getSimpleName(), t.getMessage());
            }
        });
    }

    private void showEnteredIntoChatRoom(String currentRoomSelected, String action) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call;
        String currentType = currentRoomSelected;
        if (whichRoomIsClicked.equals("")) {
            currentType = Constants.FRIENDS;
            call = apiInterface.chatRoomGateway(Constants.SECRET_KEY, Data.userId, currentType, action);
        } else {
            call = apiInterface.chatRoomGateway(Constants.SECRET_KEY, Data.userId, currentType, action);
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("enter")) {
                    EventBus.getDefault().post(new Event(Constants.ENTERED_INTO_CHAT_ROOM + ""));
                } else if (response.body().equals("left")) {
                    EventBus.getDefault().post(new Event(Constants.ENTERED_INTO_CHAT_ROOM + ""));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(ChatRoomActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }
//    @OnClick(R.id.custom_back_button)
//    public void onCustomBackButtonClicked() {
//        setUpAlertDialog();
//    }

    void setAdapter(int position) {
        GlobalChatroomAdapter pagerAdapter = new GlobalChatroomAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        // when notify then set manually current position.
        viewPager.setCurrentItem(position);
        pagerAdapter.notifyDataSetChanged();
    }

    //
//    @OnClick(R.id.option_menu_chatroom)
//    public void showChatRoomInfo() {
//        if (!Data.chatRoomShowAdminAndUser.equals("")) {
//            Data.chatRoomShowAdminAndUser = "";
//        }
//        if (viewPager.getCurrentItem() == 0) {
//            Data.chatRoomShowAdminAndUser = country[1];
//        } else if (viewPager.getCurrentItem() == 1) {
//            Data.chatRoomShowAdminAndUser = Constants.GLOBAL;
//        } else {
//            if (whichInTheLast == 3) {
//                Data.chatRoomShowAdminAndUser = Constants.FRIENDS;
//            } else if (whichInTheLast == 4) {
//                Data.chatRoomShowAdminAndUser = Constants.LOVE;
//            } else {
//                Data.chatRoomShowAdminAndUser = Constants.DATE;
//            }
//        }
//        ChatRoomMainFragment fragment = new ChatRoomMainFragment();
//        fragment.show(getSupportFragmentManager().beginTransaction(), "chatroomlist");
//    }
//
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(Constants.GLOBAL_MESSAGE)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        setUpAlertDialog();
    }


    public void setUpAlertDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.leave_permission);
        dialog.setCancelable(false);

        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("yes", "onClick: yes");
                showEnteredIntoChatRoom(whichRoomIsClicked, "left");
                dialog.dismiss();
                finish();
            }
        });


        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //Farabi
    /*private void setUpAlertDialog() {

        final PrettyDialog dialog = new PrettyDialog(ChatRoomActivity.this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            dialog.setTitle("Action")
                    .setTitleColor(R.color.black)
                    .setMessage("Are you sure you want to leave the Chat room!  \n People will miss you, back soon again and enjoy your moment with them.")
                    .setMessageColor(R.color.pdlg_color_black)
                    .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                    .addButton("Yes", R.color.white, R.color.amber_900, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            showEnteredIntoChatRoom(whichRoomIsClicked, "left");
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .addButton("I will stay", R.color.red_900, R.color.white, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                        }
                    }).show();
            dialog.setCancelable(false);
        } else {
            dialog.setTitle("Warning !!!")
                    .setTitleColor(R.color.pdlg_color_blue)
                    .setMessage("Are you sure you want to leave the Chat room!  \n People will miss you, back soon again and enjoy your moment with them.")
                    .setMessageColor(R.color.pdlg_color_black)
                    .addButton("Yes", R.color.white, R.color.amber_900, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            showEnteredIntoChatRoom(whichRoomIsClicked, "left");
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .addButton("I will stay", R.color.red_900, R.color.white, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            dialog.dismiss();
                        }
                    }).show();
            dialog.setCancelable(false);
        }
    }*/


    public void ChatroomMenueWindowClick() {

        //rokan
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setTitle("info");
        dialog.setContentView(R.layout.chatroom_menu_window);

       /* Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        window.setAttributes(wlp);*/

        final TextView tvActiveRoom = dialog.findViewById(R.id.tvActiveRoom);
        final TextView tvFirstInctiveRoom = dialog.findViewById(R.id.tvFirstInctiveRoom);
        final TextView tvSecondInctiveRoom = dialog.findViewById(R.id.tvSecondInctiveRoom);


        tvActiveRoom.setText(activeTabe);

        /*if (SharedPref.getLastChatRoom(Constants.LAST_CHAT_ROOM).equalsIgnoreCase(Constants.FRIENDS)) {
            tvFirstInctiveRoom.setText(Constants.LOVE);
            tvSecondInctiveRoom.setText(Constants.DATE);
        } else if (SharedPref.getLastChatRoom(Constants.LAST_CHAT_ROOM).equals(Constants.LOVE)) {
            tvFirstInctiveRoom.setText(Constants.FRIENDS);
            tvSecondInctiveRoom.setText(Constants.DATE);
        } else if (SharedPref.getLastChatRoom(Constants.LAST_CHAT_ROOM).equals(Constants.DATE)) {
            tvFirstInctiveRoom.setText(Constants.FRIENDS);
            tvSecondInctiveRoom.setText(Constants.LOVE);
        }*/


        tvActiveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvActiveRoom.getText().toString().contains(Constants.FRIENDS)) {
                    getAllFriendsUserButtonClicked();
                    dialog.dismiss();
                } else if (tvActiveRoom.getText().toString().contains(Constants.DATE)) {
                    dialog.dismiss();
                    getAllDateUserButtonClicked();
                } else if (tvActiveRoom.getText().toString().contains(Constants.LOVE)) {
                    dialog.dismiss();
                    getAllLoveUserButtonClicked();
                } else if (tvActiveRoom.getText().toString().contains(country[1])) {
                    dialog.dismiss();
                    getAllCountryUserButtonClicked();
                } else if (tvActiveRoom.getText().toString().contains(Constants.GLOBAL)) {
                    dialog.dismiss();
                    showAllUserList();
                }


            }
        });


        dialog.show();

    }

    public void getAllFriendsUserButtonClicked() {
        Intent intent = new Intent(activity, ChatRoomAllUserActivity.class);
        String[] country = Data.userCountry.split(",");
        if (!country[1].equals("")) {
            intent.putExtra(Constants.COUNTRY, country[1]);
        }
        intent.putExtra(Constants.TYPE, Constants.FRIENDS);
        startActivity(intent);
    }

    public void getAllLoveUserButtonClicked() {
        Intent intent = new Intent(activity, ChatRoomAllUserActivity.class);
        String[] country = Data.userCountry.split(",");
        if (!country[1].equals("")) {
            intent.putExtra(Constants.COUNTRY, country[1]);
        }
        intent.putExtra(Constants.TYPE, Constants.LOVE);
        startActivity(intent);
    }


    public void getAllDateUserButtonClicked() {
        Intent intent = new Intent(activity, ChatRoomAllUserActivity.class);
        String[] country = Data.userCountry.split(",");
        if (!country[1].equals("")) {
            intent.putExtra(Constants.COUNTRY, country[1]);
        }
        intent.putExtra(Constants.TYPE, Constants.DATE);
        startActivity(intent);
    }


    public void getAllCountryUserButtonClicked() {
        Intent intent = new Intent(activity, ChatRoomAllUserActivity.class);
        /*String[] country = Data.userCountry.split(",");
        if (!Data.country.equals("")) {
            intent.putExtra(Constants.COUNTRY, country);
        }
        intent.putExtra(Constants.TYPE, Data.country);*/

        String[] country = Data.userCountry.split(",");
        if (!country[1].equals("")) {
            intent.putExtra(Constants.COUNTRY, country[1]);
        }
        intent.putExtra(Constants.TYPE, country[1]);

        startActivity(intent);
    }

    public void showAllUserList() {
        //TOdo get Global user list
        Intent intent = new Intent(activity, ChatRoomAllUserActivity.class);
        intent.putExtra(Constants.TYPE, Constants.GLOBAL);
        startActivity(intent);

    }

}

