package net.boddo.btm.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.ChatRoomActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRoom;
import net.boddo.btm.Model.ChatRoomUser;
import net.boddo.btm.Model.CheckUserAlreadyValidInGlobalChatRoom;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Limitation;
import net.boddo.btm.Utills.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomFragment extends Fragment {

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    Dialog dialog;
    ImageView btClose;
    TextView applyButton;

    @BindView(R.id.layout_country)
    LinearLayout buttonCountry; //country
    @BindView(R.id.layout_global)
    LinearLayout buttonGlobal;//global
    @BindView(R.id.layout_friends)
    LinearLayout buttonFriends;//friends
    @BindView(R.id.layout_love)
    LinearLayout buttonLove;//love
    @BindView(R.id.layout_date)
    LinearLayout buttonDate;//date
    @BindView(R.id.layout_admin)
    LinearLayout buttonAdmin;//admin

    @BindView(R.id.country_user_text)
    TextView countryUser;

    @BindView(R.id.global_user_count)
    TextView globalUser;

    @BindView(R.id.friends_user_count)
    TextView friendsUser;

    @BindView(R.id.text_view_date_count)
    TextView dateUser;

    @BindView(R.id.love_user_count)
    TextView loveUser;

    @BindView(R.id.textViewAdmin)
    TextView adminUser;

    @BindView(R.id.tvCountryTitle)
    TextView tvCountryTitle;

    @BindView(R.id.tvGlobalTitle)
    TextView tvGlobalTitle;

    @BindView(R.id.tvFriendsTitle)
    TextView tvFriendsTitle;

    @BindView(R.id.tvLoveTitle)
    TextView tvLoveTitle;

    @BindView(R.id.tvDateTitle)
    TextView tvDateTitle;

    @BindView(R.id.tvAdminRoomTitle)
    TextView tvAdminRoomTitle;

    @BindView(R.id.ivChatRoomMenue)
    ImageView ivChatRoomMenue;


//  @BindView(R.id.charge_button)
//  Button buttonCharge;


    String[] country;
    ApiInterface apiInterface;

    public static boolean isBannedFromGlobal = false;
    public static boolean isBannedFromCountry = false;
    public static boolean isBannedFromFriends = false;
    public static boolean isBannedFromLove = false;
    public static boolean isBannedFromDate = false;
    public static int isSubscribed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
        ButterKnife.bind(this, view);

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

        // Inflate the layout for this fragment
        country = Data.userCountry.split(",");
        onlineUser();
        return view;
    }

    private void onlineUser() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRoomUser> call = apiInterface.onlineUser(country[1]);
        call.enqueue(new Callback<ChatRoomUser>() {
            @Override
            public void onResponse(Call<ChatRoomUser> call, Response<ChatRoomUser> response) {
                assert response.body() != null;
                globalUser.setText(String.valueOf(response.body().getGlobal()) + "  Active user");
                tvGlobalTitle.setText("Global");
                if (!country[1].equals("")) {
                    countryUser.setText(String.valueOf(response.body().getCountry()) + "  Active user");
                    tvCountryTitle.setText(country[1]);
                } else {
                    countryUser.setText("  Active user");
                    tvCountryTitle.setText("Unknown");
                }
                loveUser.setText(String.valueOf(response.body().getLove()) + "  Active user");
                tvLoveTitle.setText("Love");

                friendsUser.setText(String.valueOf(response.body().getFriends()) + "  Active user");
                tvFriendsTitle.setText("Friends");

                dateUser.setText(String.valueOf(response.body().getDate()) + "  Active user");
                tvDateTitle.setText("Date");

                adminUser.setText(String.valueOf(response.body().getFriends()) + "  Active user");
                tvAdminRoomTitle.setText("Room admin");
            }

            @Override
            public void onFailure(Call<ChatRoomUser> call, Throwable t) {
            }
        });
    }

   /* @OnClick(R.id.apply_admin_button)
    public void onApplyAdminButton() {
        dailogRoomAdmin();
    }

    @OnClick(R.id.guideline_button)
    public void onGuideLineButton() {
        dailogRoomAdmin();
    }*/

//
//    @OnClick(R.id.charge_button)
//    public void onChargeButton() {
//        dailogRoomAdmin();
//    }


    @OnClick(R.id.layout_global)
    public void onGlobalChatRoomClicked() {
        if (Data.isPalupPlusSubcriber) {
            chatRoom(Constants.GLOBAL);
        } else {
            checkAlreadySubscribeForGlobal();
        }
    }

    @OnClick(R.id.ivChatRoomMenue)
    public void ChatroomClick() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setTitle("info");
        dialog.setContentView(R.layout.chatroom_menu_list);
        final TextView tvMenueAdmin = dialog.findViewById(R.id.tvMenueAdmin);
        tvMenueAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });
        final TextView tvMenueCountryChange = dialog.findViewById(R.id.tvMenueCountryChange);
        tvMenueCountryChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    public void insufficientCredits() {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.insufficient_credits);
        dialog.setCancelable(false);

        final Button payjoin = dialog.findViewById(R.id.payjoin);
        final Button notnow = dialog.findViewById(R.id.notnow);

        payjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuyCreditActivity.class);
                intent.putExtra("BuyCredits", true);
                getActivity().overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameSettings, fragment).disallowAddToBackStack();
        fragmentTransaction.commit();

    }


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
                        isSubscribed = 1;
                        chatRoom(Constants.GLOBAL);
                    } else {
                        if (Integer.parseInt(Data.userPalupPoint) >= Limitation.GLOBAL_CHAT_ROOM_CREDITS) {
                            setUpAlertDialog();
                            isSubscribed = 0;
                        } else {

                            insufficientCredits();
                            isSubscribed = 0;
                        }
                    }
                } else if (objCUAVIGCR.getStatus().equals("not exist")) {
                    if (Integer.parseInt(Data.userPalupPoint) >= Limitation.GLOBAL_CHAT_ROOM_CREDITS) {
                        setUpAlertDialog();
                        isSubscribed = 0;
                    } else {
                        insufficientCredits();
                        isSubscribed = 0;
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckUserAlreadyValidInGlobalChatRoom> call, Throwable t) {
                Log.d(ChatRoomFragment.class.getSimpleName(), t.getMessage());
            }
        });
    }

    @OnClick(R.id.layout_admin)
    public void onAdminChatRoomClicked() {
        Toast.makeText(getContext(), "Admin only", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.layout_country)
    public void onBangladeshChatRoomClicked() {
        if (!country[1].equals("")) {
            chatRoom(country[1]);
        }
    }

    @OnClick(R.id.layout_love)
    public void onLoveChatRoomClicked() {
        SharedPref.setLastChatRoom(Constants.LAST_CHAT_ROOM, Constants.LOVE);
        chatRoom(Constants.LOVE);
    }

    @OnClick(R.id.layout_friends)
    public void onFriendsChatRoomClicked() {
        SharedPref.setLastChatRoom(Constants.LAST_CHAT_ROOM, Constants.FRIENDS);
        chatRoom(Constants.FRIENDS);
    }

    @OnClick(R.id.layout_date)
    public void onDateChatRoomClicked() {
        SharedPref.setLastChatRoom(Constants.LAST_CHAT_ROOM, Constants.DATE);
        chatRoom(Constants.DATE);
//        goToChatRoomActivity(Constants.DATE);
    }

    public void goToChatRoomActivity(String type) {
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra(Constants.CHAT_ROOM, type);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setRetainInstance(true);
    }

    public void chatRoom(final String type) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRoom> call = apiInterface.roomChat(Constants.SECRET_KEY, Data.userId, type, isSubscribed);
        call.enqueue(new Callback<ChatRoom>() {
            @Override
            public void onResponse(Call<ChatRoom> call, Response<ChatRoom> response) {
                ChatRoom chatRoom = response.body();
                if (chatRoom.getIsAdmin().equals("1")) {
                    Data.isChatRoomAdmin = true;
                }
                if (chatRoom.getIsBanned().equals("1")) {
                    if (type.equals(Constants.GLOBAL)) {
                        isBannedFromGlobal = true;
                    } else if (type.equals(Constants.LOVE)) {
                        isBannedFromLove = true;
                    } else if (type.equals(Constants.FRIENDS)) {
                        isBannedFromFriends = true;
                    } else if (type.equals(Constants.DATE)) {
                        isBannedFromDate = true;
                    } else {
                        isBannedFromCountry = true;
                    }
                }
                goToChatRoomActivity(type);
            }

            @Override
            public void onFailure(Call<ChatRoom> call, Throwable t) {
                Log.d(ChatRoomFragment.class.getSimpleName(), t.getMessage());
            }
        });
    }

    private void dailogRoomAdmin() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dailog_room_admin);
        btClose = dialog.findViewById(R.id.bt_close);
        applyButton = dialog.findViewById(R.id.apply_button);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @SuppressLint("NewApi")
    public void setUpAlertDialog() {
        final Dialog dialog = new Dialog(getContext());
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
                isSubscribed = 1;
                dialog.dismiss();
            }
        });
        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }



    /*@SuppressLint("NewApi")
    private void setUpAlertDialog() {
        final PrettyDialog dialog = new PrettyDialog(getContext());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            dialog.setTitle("Chat Room")
                    .setTitleColor(R.color.pdlg_color_blue)
                    .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                    .setMessage("20 credits for joining to the Global Chat room...")
                    .setMessageColor(R.color.pdlg_color_black)
                    .setAnimationEnabled(true)
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
                                    isSubscribed = 1;
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
                                }
                            }
                    ).show();
        } else {
            dialog.setTitle("Chat Room")
                    .setTitleColor(R.color.pdlg_color_blue)
                    .setMessage("20 credits for joining to the Global Chat room...")
                    .setMessageColor(R.color.pdlg_color_black)
                    .setAnimationEnabled(true)
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
                                    isSubscribed = 1;
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
                                }
                            }
                    ).show();
        }
    }*/

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
