package net.boddo.btm.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import net.boddo.btm.Adepter.ChatAppMsgAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Fragment.CustomBottomSheetChat;
import net.boddo.btm.Fragment.OthersFragment.OthersProfileFragment;
import net.boddo.btm.Model.ChatAppMsgDTO;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.FlagPreference;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.Utills.Limitation;
import net.boddo.btm.Utills.SearchUser;
import net.boddo.btm.test.CustomGalleryBottomSheet;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivateChatActivity extends AppCompatActivity {

    PrivateChatActivity activity;
    @BindView(R.id.two_name_online_textView)
    TextView textViewTwoUsersName;
    TextView second_name_online_textView;
    ImageView ivBackMessageChat;

    /*@BindView(R.id.online_status)
    TextView textViewOnlineStatus;*/


    @BindView(R.id.image_view_sender)
    CircleImageView imageViewSender;

    @BindView(R.id.text_view_sender_message_count)
    TextView senderMessageCount;
    //receiver
    @BindView(R.id.image_view_receiver)
    CircleImageView imageViewReceiver;
    @BindView(R.id.text_view_receiver_message_count)
    TextView receiverMessageCount;
    //recycer view
    @BindView(R.id.recycler_view_message)
    RecyclerView recyclerView;
    //emoji
   /* @BindView(R.id.emoji_btn)
    ImageView emojiButton;*/
    //more
    @BindView(R.id.more_btn)
    ImageView moreButton;

    @BindView(R.id.other_profile_image)
    ImageView otherProfile;

    @BindView(R.id.messageTV1)
    TextView messageTV1;
    TextView tvTopMessageFirstTime;
    TextView tvWait;
    RelativeLayout rvFirstTimeBG;

    @BindView(R.id.messageTV2)
    TextView messageTV2;

    @BindView(R.id.messageTV3)
    TextView messageTV3;

    @BindView(R.id.emojicon_edit_text)
    EmojiconEditText editTextMessage;
    @BindView(R.id.submit_btn)
    ImageView sendButton;

    @BindView(R.id.text_view_typing)
    TextView text_view_typing;

    @BindView(R.id.layout_footer)
    RelativeLayout layout_footer;

    @BindView(R.id.menu_name_online_textView)
    ImageView menu_name_online_textView;

    @BindView(R.id.RLPrivateChatScreen)
    RelativeLayout RLPrivateChatScreen;

    //  @BindView(R.id.chat_left_msg_text_view)
    TextView chat_left_msg_text_view;

    Dialog mDialog;
    PrettyDialog prettyDialog;

    //message
    LinearLayoutManager linearLayoutManager;
    String msgContent = "";
    ApiInterface apiInterface;
    public static List<ChatAppMsgDTO.Message> msgDtoList;
    ChatAppMsgDTO msgDto;
    ChatAppMsgDTO.Message message;
    ChatAppMsgAdapter chatAppMsgAdapter;
    int positionOfTheMessage = 0;
    int sizeOfArrayList = 0;
    CustomGalleryBottomSheet customGalleryBottomSheet;
    //image uploadking
    Uri imageUri;
    String imageName = "";
    String image = "";
    Bitmap bitmap;
    int leftMessageCount = 0;
    int rightMessageCount = 0;
    BroadcastReceiver receiver;
    boolean isCheck = false;
    BottomSheetBehavior bottomSheetBehavior;
    RelativeLayout bottomSheetLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference userOnlineRef;
    String conversationId = "";
    String otherUserNameFromActiveList = "";
    String otherUserId = "";
    //emoji
    EmojiconTextView textView;
    EmojIconActions emojIcon;
    private long mLastClickTime = 0;
    private List<String> likedMeList;

    View view;

    String[] firstPortionOfName;
    String[] firstPortionOfMyName;
    boolean isBlocked = false;
    boolean isBlockedMe = false;
    Boolean b = true;
    Boolean flag = false;
    String accepted;
    FlagPreference flagPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        ButterKnife.bind(this);

        activity = this;
        View rootView = getWindow().getDecorView().getRootView();
        /*emojIcon = new EmojIconActions(PrivateChatActivity.this, rootView, editTextMessage, emojiButton);
        emojIcon.ShowEmojIcon();*/

        tvWait = findViewById(R.id.tvWait);

        flagPreference = new FlagPreference(this);
        String f = FlagPreference.getFlag("flag");
        Log.e("f", "onCreate: " + f);
        if (f.equals("true")) {
            flag = true;
        } else {
            flag = false;
        }

        tvTopMessageFirstTime = findViewById(R.id.tvTopMessageFirstTime);
        rvFirstTimeBG = findViewById(R.id.rvFirstTimeBG);
        chat_left_msg_text_view = findViewById(R.id.chat_left_msg_text_view);
        second_name_online_textView = findViewById(R.id.second_name_online_textView);
        ivBackMessageChat = findViewById(R.id.ivBackMessageChat);
        ivBackMessageChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i = 0; i < DashBoadActivity.myBlockList.size(); i++) {
            if (DashBoadActivity.myBlockList.get(i).getBlockedUserId().equals(Data.otherUserId)) {
                isBlocked = true;
                break;
            }
        }
        sendButton.setEnabled(false);
        sendButton.setColorFilter(getResources().getColor(R.color.gray_light_light));
        if (isBlocked) {
            layout_footer.setVisibility(View.GONE);
            final PrettyDialog myBlockDialog = new PrettyDialog(this);
            myBlockDialog.setContentView(R.layout.blocked_user);
            myBlockDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessage(" You have  Blocked this user")
                    .setMessageColor(R.color.red_A700).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                @Override
                public void onClick() {
                    myBlockDialog.dismiss();

                }
            }).show();

        }

        try {
            if (getIntent().hasExtra(Constants.OTHER_USER_ID)) {
                if (!getIntent().getStringExtra(Constants.OTHER_USER_ID).equals("")) {
                    otherUserId = getIntent().getStringExtra(Constants.OTHER_USER_ID);
                    Picasso.get().load(Data.otherProfilePhoto).into(imageViewReceiver);
                    Helper.getOtherUserDetails(getIntent().getStringExtra(Constants.OTHER_USER_ID));
                }
            } else {
                Helper.getOtherUserDetails(Data.otherUserId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getIntent().hasExtra(Constants.CONVERSATION_ID)) {
            if (!getIntent().getStringExtra(Constants.CONVERSATION_ID).equals("")) {
                conversationId = getIntent().getStringExtra(Constants.CONVERSATION_ID);
            }
        }
        if (getIntent().hasExtra(Constants.RECEIVER_NAME)) {
            otherUserNameFromActiveList = getIntent().getStringExtra(Constants.RECEIVER_NAME);
        }
        if (Data.otherUserName.equals("")) {
            otherUserNameFromActiveList = getIntent().getStringExtra(Constants.RECEIVER_NAME);
        }

        //response.body().getRequest().equals("accepted")


        setUpUIForMessage();
        getAllMessage();
        getLocalBroadCastReceiver();
        getRequestAcceptDetails();


        if (accepted != null && accepted.equals("accepted")) {
            rvFirstTimeBG.setVisibility(View.GONE);
            Log.e("accepted", "onCreate: accepted");
        } else {
            rvFirstTimeBG.setVisibility(View.VISIBLE);
        }


        //menu_name_online_textView
        menu_name_online_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(PrivateChatActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.screen_color_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (b == true) {
                            b = false;
                            RLPrivateChatScreen.setBackgroundColor(Color.rgb(63, 66, 77));

                        } else {
                            b = true;
                            RLPrivateChatScreen.setBackgroundColor(Color.WHITE);

                        }
                        return false;
                    }
                });
            }
        });
        //menu_name_online_textView

        //firebase RealTime Database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Type");
        if (!Data.conversationID.equals("0")) {
            myRef.child(Data.conversationID).child(Data.userId).setValue("0");
        } else {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<String> call = apiInterface.getConversationId(Constants.SECRET_KEY, Data.userId, Data.otherUserId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    conversationId = response.body();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("PrivateChatActivity", t.getMessage());
                }
            });
        }


        if (!Data.conversationID.equals("0")) {
            myRef.child(Data.conversationID).child(Data.otherUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    if (dataSnapshot.getValue(String.class) != null) {
                        String value = dataSnapshot.getValue(String.class);
                        //Toast.makeText(PrivateChatActivity.this,value,Toast.LENGTH_LONG);
                        if (value.isEmpty()) {

                        } else {
                            if (value.contains("1")) {
                                text_view_typing.setVisibility(View.VISIBLE);
                                text_view_typing.setText(!Data.otherUserFirstName.equals("") ? otherUserNameFromActiveList + " is Typing...." : Data.otherUserFirstName + " is Typing....");
                            } else {
                                text_view_typing.setVisibility(View.GONE);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        }
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0) {
                    setTyping();
                } else {
                    setNotTyping();
                }
            }
        });
        userOnlineRef = database.getReference("UserOnline");
        /*userOnlineRef.child(Data.otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue(String.class) != null) {
                    String value = dataSnapshot.getValue(String.class);
                    //Toast.makeText(PrivateChatActivity.this,value,Toast.LENGTH_LONG);
                    if (value.isEmpty()) {

                    } else {
                        if (value.contains("1")) {
                            textViewOnlineStatus.setText("Online");
                        } else {
                            textViewOnlineStatus.setText("Offline");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });*/

        firstPortionOfMyName = Data.userFirstName.split(" ");

        try {
            if (otherUserNameFromActiveList.equals("")) {
                Log.e("otherUserNameFrom", "onCreate: " + otherUserNameFromActiveList);
                firstPortionOfName = Data.otherUserFirstName.split(" ");
                textViewTwoUsersName.setText(firstPortionOfName[0]);
                second_name_online_textView.setText(firstPortionOfMyName[0]);
            } else {
                firstPortionOfName = otherUserNameFromActiveList.split(" ");
                textViewTwoUsersName.setText(firstPortionOfName[0]);
                second_name_online_textView.setText(firstPortionOfMyName[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRequestAcceptDetails() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }


        mLastClickTime = SystemClock.elapsedRealtime();
        msgContent = editTextMessage.getText().toString().replace("'", "\'");
        if (msgContent.length() > 0 && !msgContent.equals("")) {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("secret_key", Constants.SECRET_KEY);
            jsonObject.addProperty("user_id", Data.userId);
            jsonObject.addProperty("other_user_id", Data.otherUserId);
            jsonObject.addProperty("message", msgContent);
            Log.e("responeCheck", "onSendButtonClicked: " + jsonObject.toString());
            Call<ChatAppMsgDTO> call = apiInterface.startChat(Constants.SECRET_KEY, Data.userId, Data.otherUserId, msgContent);
            call.enqueue(new Callback<ChatAppMsgDTO>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ChatAppMsgDTO> call, Response<ChatAppMsgDTO> response) {
                    String responeCheck = response.toString();

                    accepted = response.body().getRequest();
                    if (response.body().getRequest().equals("accepted")) {
                        // rvFirstTimeBG.setVisibility(View.GONE);

                        if (flag == true && Data.otherUserId.equals(otherUserId)) {
                            rvFirstTimeBG.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            messageTV2.setVisibility(View.VISIBLE);
                            messageTV3.setVisibility(View.VISIBLE);
                            messageTV3.setText("Chat request accepted");
                            messageTV2.setText("Start the conversation & write something impressive");
                        } else {
                            otherProfile.setVisibility(View.INVISIBLE);
                            rvFirstTimeBG.setVisibility(View.VISIBLE);
                            messageTV1.setVisibility(View.INVISIBLE);
                            tvTopMessageFirstTime.setVisibility(View.INVISIBLE);
                            messageTV2.setVisibility(View.VISIBLE);
                            messageTV2.setText("Start the conversation & write something impressive");
                            messageTV3.setVisibility(View.VISIBLE);
                            messageTV3.setText("Chat request accepted");
                            otherProfile.setVisibility(View.VISIBLE);
                            Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);
                            recyclerView.setVisibility(View.VISIBLE);
                            Log.e("accepted", "onResponse: accepted");
                        }

                    } else if (response.body().getStatus().equals("success")) {
                        if (response.body().getRequest().equals("accepted")) {

                            if (flag == true && flag == true && Data.otherUserId.equals(otherUserId)) {
                                rvFirstTimeBG.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                messageTV2.setVisibility(View.VISIBLE);
                                messageTV3.setVisibility(View.VISIBLE);
                                messageTV3.setText("Chat request accepted");
                                messageTV2.setText("Start the conversation & write something impressive");
                            } else {
                                message = response.body().getSingleMessage();
                                msgDtoList.add(message);
                                int newMsgPosition = msgDtoList.size() - 1;
                                // NotifyEvent recycler view insert one new data.
                                chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                                // Scroll RecyclerView to the last message.
                                recyclerView.scrollToPosition(newMsgPosition);
                                // Empty the input edit text box.

                                rvFirstTimeBG.setVisibility(View.VISIBLE);
                                messageTV3.setVisibility(View.VISIBLE);
                                //  messageTV3.setText("Chat request accepted");
                                tvTopMessageFirstTime.setVisibility(View.VISIBLE);
                                messageTV2.setVisibility(View.VISIBLE);
                                messageTV2.setText("Start the conversation & write something impressive");
                                otherProfile.setVisibility(View.VISIBLE);
                                Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);

                                tvTopMessageFirstTime.setVisibility(View.GONE);


                                editTextMessage.setText("");
                                //increase the sender message amount
                                countMessages();
                            }

                        } else if (response.body().getRequest().equals("requested")) {
                            if (msgDtoList.size() == 1) {

                                rvFirstTimeBG.setVisibility(View.VISIBLE);
                                tvWait.setVisibility(View.VISIBLE);
                                tvTopMessageFirstTime.setVisibility(View.VISIBLE);
                                tvTopMessageFirstTime.setText("Chat request sent");
                                messageTV1.setVisibility(View.VISIBLE);
                                messageTV1.setText("Your chat request sent, Please wait for user approval.");
                                otherProfile.setVisibility(View.VISIBLE);
                                Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);

                                moreButton.setVisibility(View.GONE);
                                sendButton.setVisibility(View.GONE);
                                editTextMessage.setText("");


                            } else {
                                message = response.body().getSingleMessage();
                                msgDtoList.add(message);
                                int newMsgPosition = msgDtoList.size() - 1;
                                chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                                recyclerView.scrollToPosition(newMsgPosition);
                                editTextMessage.setText("");
                                countMessages();
                            }
                        } else if (response.body().getRequest().equals("request limit expired")) {

                            dialogShow();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatAppMsgDTO> call, Throwable t) {
                    Log.d("PrivateChatActivity", t.getMessage());
                }
            });
        } else {
            Log.d(PrivateChatActivity.class.getSimpleName(), "Nothing ");
        }
    }

    private void chatRequestNotAcceptedMsg() {

        // flag = false;
        //    FlagPreference.setFlag("flag",flag.toString());

        rvFirstTimeBG.setVisibility(View.VISIBLE);
        tvWait.setVisibility(View.VISIBLE);
        tvTopMessageFirstTime.setVisibility(View.VISIBLE);
        tvTopMessageFirstTime.setText("Chat request sent");
        messageTV1.setVisibility(View.VISIBLE);
        messageTV1.setText("Your chat request sent, Please wait for user approval.");
        otherProfile.setVisibility(View.VISIBLE);
        // Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);

        moreButton.setVisibility(View.GONE);
        sendButton.setVisibility(View.GONE);
        editTextMessage.setText("");

    }

    /*public void dialogShow() {

        //dipto
        prettyDialog = new PrettyDialog(this);

        prettyDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                .setTitle("Dear " + Data.userName)
                .setMessage("You daily chat request limit is over!\n" +
                        "\n" +
                        "But it can be changed"+"\n"+"Five more chat request will cost 50 credits")
                .setMessageColor(R.color.red_A700)
                .addButton("GET MORE CHAT REQUEST", R.color.white, R.color.accent_material_light, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                        if (Integer.parseInt(Data.userPalupPoint) >= Limitation.PRIVATE_CHAT_REQUEST_NORMAL_USER_CREDITS) {

                            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<String> call = apiInterface.onPurchaseMessage(Constants.SECRET_KEY, Data.userId);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body().equals("success")) {

                                        PrettyDialog cc = new PrettyDialog(PrivateChatActivity.this);
                                        cc.setTitle("Congratulations")
                                                .setMessage("You have purchase 10 Chat Request").setMessageColor(R.color.green);
                                        cc.show();

                                    } else {
                                        Toast.makeText(PrivateChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                    prettyDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                    prettyDialog.dismiss();
                                }
                            });

                        } else {

                            PrettyDialog jj = new PrettyDialog(PrivateChatActivity.this);
                            jj.setTitle("Dear " + Data.userName)
                                    .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                                    .setMessage("You don't have sufficient balance").setMessageColor(R.color.red_A700);

                            jj.show();


                        }

                    }
                }).addButton("ACTIVE PALUP PLUS", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                palupDailog();
            }
        }).show();
    }*/

    public void dialogShow() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.chat_request_limit);
        dialog.setCancelable(false);

        final TextView tvFindoutMessage = dialog.findViewById(R.id.tvFindoutMessage);
        final Button payView = dialog.findViewById(R.id.payjoin);
        final Button btnDiscoverBoddoPlus = dialog.findViewById(R.id.btnDiscoverBoddoPlus);
        final Button notnow = dialog.findViewById(R.id.notnow);

        // tvFindoutMessage.setText("You have reached your daily chat request limits. Buy more chat request or activate Boddo Plus.");


        payView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPrettyDialog(holder, position, message);
                if (Integer.parseInt(Data.userPalupPoint) >= Limitation.LIKE_FAV_VISITOR_CREDITS) {

                    ApiInterface newApiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<String> buyCall = newApiInterface.onPurchaseMessage(Constants.SECRET_KEY, Data.userId);
                    buyCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body().equals("success")) {
                                        Toast.makeText(activity, "Congratulations, you have purchased 10 chat request.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "Internal server connection failed! Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(activity, "Internal server connection failed! Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Internal server connection failed! Please try again later.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(activity, "Internal server connection failed! Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {

                    insufficientCredits();

                }


                dialog.dismiss();
            }
        });

        btnDiscoverBoddoPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPalupPlusWindow();
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

    public void insufficientCredits() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.insufficient_credits_likefavorit_adapter);
        dialog.setCancelable(false);

        final Button payjoin = dialog.findViewById(R.id.payjoin);
        final Button btnDiscoverBoddoPlusInsufficient = dialog.findViewById(R.id.btnDiscoverBoddoPlusInsufficient);
        final Button notnow = dialog.findViewById(R.id.notnow);

        payjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivateChatActivity.this, BuyCreditActivity.class);
                intent.putExtra("BuyCredits", true);
                //context.overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        btnDiscoverBoddoPlusInsufficient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPalupPlusWindow();
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

    public void goToPalupPlusWindow() {

        Intent intent = new Intent(this, BuyCreditActivity.class);
        intent.putExtra("Membership", true);
        //overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);


    }


    private void palupDailog() {
        mDialog = new Dialog(this);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.setContentView(R.layout.custom_alert_dialog_subscription);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    public void getLocalBroadCastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.hasExtra("user_id")) {
                        String otherUserIdFromResponse = intent.getStringExtra("user_id");
                        if (otherUserIdFromResponse.equals(Data.otherUserId)) {
                            getAllMessage();
                        }
                    } else if (intent.hasExtra(Constants.UPDATE_UI_FOR_CHAT)) {
                        getAllMessage();
                        customGalleryBottomSheet.dismiss();
                    } else {
                        Log.d(PrivateChatActivity.class.getSimpleName(), "Nothing to do");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void getAllMessage() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatAppMsgDTO> call = apiInterface.getAllPrivateMessage(Constants.SECRET_KEY, Data.userId, Data.otherUserId);
        call.enqueue(new Callback<ChatAppMsgDTO>() {
            @Override
            public void onResponse(Call<ChatAppMsgDTO> call, Response<ChatAppMsgDTO> response) {

                if (response.body().getStatus().equals("success")) {
                    msgDtoList = response.body().getMessage();
                    sizeOfArrayList = msgDtoList.size();
                    Log.e("msgDtoList", "onResponse: " + msgDtoList.size());


                    if (isCheck) {
                        OthersProfileFragment.isMatch = true;
                    }

                    if (OthersProfileFragment.isMatch) {

                        if (sizeOfArrayList == 0) {
                            otherProfile.setVisibility(View.VISIBLE);
                            rvFirstTimeBG.setVisibility(View.VISIBLE);
                            messageTV1.setVisibility(View.INVISIBLE);
                            tvTopMessageFirstTime.setVisibility(View.INVISIBLE);
                            messageTV2.setVisibility(View.VISIBLE);
                            messageTV3.setVisibility(View.VISIBLE);
                            Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);
                            recyclerView.setVisibility(View.INVISIBLE);
                            OthersProfileFragment.isMatch = false;
                            isCheck = true;
                        } else if (sizeOfArrayList == 1) {
                            chatRequestNotAcceptedMsg();
                        } else {

                            otherProfile.setVisibility(View.INVISIBLE);
                            rvFirstTimeBG.setVisibility(View.INVISIBLE);
                            messageTV1.setVisibility(View.INVISIBLE);
                            tvTopMessageFirstTime.setVisibility(View.INVISIBLE);
                            messageTV2.setVisibility(View.INVISIBLE);
                            messageTV3.setVisibility(View.INVISIBLE);
                            Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                    } else {

                        if (sizeOfArrayList == 0) {
                            //farabi
                            otherProfile.setVisibility(View.VISIBLE);
                            rvFirstTimeBG.setVisibility(View.VISIBLE);
                            messageTV1.setVisibility(View.VISIBLE);
                            tvTopMessageFirstTime.setVisibility(View.VISIBLE);
                            messageTV2.setVisibility(View.INVISIBLE);
                            messageTV3.setVisibility(View.INVISIBLE);
                            Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else if (sizeOfArrayList == 1) {
                            chatRequestNotAcceptedMsg();
                        } else {

                            if (flag == true) {
                                rvFirstTimeBG.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {

                                accepted = response.body().getRequest();
                                otherProfile.setVisibility(View.INVISIBLE);
                                rvFirstTimeBG.setVisibility(View.VISIBLE);
                                messageTV1.setVisibility(View.INVISIBLE);
                                tvTopMessageFirstTime.setVisibility(View.INVISIBLE);
                                messageTV2.setVisibility(View.VISIBLE);
                                messageTV2.setText("Start the conversation & write something impressive");
                                messageTV3.setVisibility(View.VISIBLE);
                                messageTV3.setText("Chat request accepted");
                                otherProfile.setVisibility(View.VISIBLE);
                                Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                    positionOfTheMessage = msgDtoList.size();
                    chatAppMsgAdapter = new ChatAppMsgAdapter(PrivateChatActivity.this, msgDtoList);
                    recyclerView.scrollToPosition(msgDtoList.size() - 1);
                    recyclerView.setAdapter(chatAppMsgAdapter);
                    countMessages();
                }

            }

            @Override
            public void onFailure(Call<ChatAppMsgDTO> call, Throwable t) {
                Log.d(PrivateChatActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }

    private void setUpUIForMessage() {
        if (Data.profilePhoto.isEmpty()) {
            imageViewSender.setImageResource(R.drawable.bg2);
        } else {
            Picasso.get().load(Data.profilePhoto).into(imageViewSender);
        }

        if (Data.otherProfilePhoto.isEmpty()) {
            imageViewReceiver.setImageResource(R.drawable.bg2);
        } else {
            Picasso.get().load(Data.otherProfilePhoto).into(imageViewReceiver);
        }

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        initVariable();
    }

    private void initVariable() {
        msgDtoList = new ArrayList<ChatAppMsgDTO.Message>();
        chatAppMsgAdapter = new ChatAppMsgAdapter(PrivateChatActivity.this, msgDtoList);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.scrollToPosition(msgDtoList.size());
        recyclerView.setAdapter(chatAppMsgAdapter);
    }

    @OnClick(R.id.emojicon_edit_text)
    public void onEditTextClicked() {
        getAllMessage();
        setTyping();
    }

    @OnTextChanged(R.id.emojicon_edit_text)
    public void onEditTextChanged() {
        if (editTextMessage.getText().toString().length() > 0) {
            sendButton.setEnabled(true);
            sendButton.setColorFilter(getResources().getColor(R.color.colorPrimary));
            setTyping();
        } else {
            sendButton.setEnabled(false);
            sendButton.setColorFilter(getResources().getColor(R.color.gray_light_light));
        }
    }


    private void setTyping() {
        if (!Data.conversationID.equals("0")) {
            myRef.child(Data.conversationID).child(Data.userId).setValue("1");
        }
    }

    private void setNotTyping() {
        if (!Data.conversationID.equals("0")) {
            myRef.child(Data.conversationID).child(Data.userId).setValue("0");
        }
    }

    @OnClick(R.id.submit_btn)
    public void onSendButtonClicked() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }


        mLastClickTime = SystemClock.elapsedRealtime();
        msgContent = editTextMessage.getText().toString().replace("'", "\'");
        if (msgContent.length() > 0 && !msgContent.equals("")) {
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("secret_key", Constants.SECRET_KEY);
            jsonObject.addProperty("user_id", Data.userId);
            jsonObject.addProperty("other_user_id", Data.otherUserId);
            jsonObject.addProperty("message", msgContent);
            Log.e("responeCheck", "onSendButtonClicked: " + jsonObject.toString());
            Call<ChatAppMsgDTO> call = apiInterface.startChat(Constants.SECRET_KEY, Data.userId, Data.otherUserId, msgContent);
            call.enqueue(new Callback<ChatAppMsgDTO>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ChatAppMsgDTO> call, Response<ChatAppMsgDTO> response) {
                    String responeCheck = response.toString();
                    if (response.body().getStatus().equals("success")) {
                        if (response.body().getRequest().equals("accepted")) {
                            message = response.body().getSingleMessage();
                            msgDtoList.add(message);
                            int newMsgPosition = msgDtoList.size() - 1;
                            // NotifyEvent recycler view insert one new data.
                            chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                            // Scroll RecyclerView to the last message.
                            recyclerView.scrollToPosition(newMsgPosition);
                            // Empty the input edit text box.

                            rvFirstTimeBG.setVisibility(View.GONE);
                            flag = true;
                            FlagPreference.setFlag("flag", flag.toString());


                         /*   messageTV3.setVisibility(View.VISIBLE);
                           // messageTV3.setText("Send a chat request");
                            messageTV3.setText("Chat request accepted");
                            tvTopMessageFirstTime.setVisibility(View.VISIBLE);
                            messageTV2.setVisibility(View.VISIBLE);
                            messageTV2.setText("Start the conversation & write something impressive");
                            otherProfile.setVisibility(View.VISIBLE);
                            Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);*/

                            tvTopMessageFirstTime.setVisibility(View.GONE);


                            editTextMessage.setText("");
                            //increase the sender message amount
                            countMessages();
                        } else if (response.body().getRequest().equals("requested")) {
                            if (msgDtoList.size() == 1) {

                                rvFirstTimeBG.setVisibility(View.VISIBLE);
                                tvWait.setVisibility(View.VISIBLE);
                                tvTopMessageFirstTime.setVisibility(View.VISIBLE);
                                tvTopMessageFirstTime.setText("Chat request sent");
                                messageTV1.setVisibility(View.VISIBLE);
                                messageTV1.setText("Your chat request sent, Please wait for user approval.");
                                otherProfile.setVisibility(View.VISIBLE);
                                Picasso.get().load(Data.otherProfilePhoto).into(otherProfile);

                                moreButton.setVisibility(View.GONE);
                                sendButton.setVisibility(View.GONE);
                                editTextMessage.setText("");


                                //farabi
                                /*final Dialog dialog = new Dialog(PrivateChatActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                LayoutInflater inflater = (LayoutInflater) PrivateChatActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                view = inflater.inflate(R.layout.custom_alert_dialog_for_message, null, false);
                                ImageButton close = view.findViewById(R.id.bt_close); *//**//*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*//**//*
                                TextView closeButton = view.findViewById(R.id.close_button);
                                dialog.setContentView(view);
                                final Window window = dialog.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawableResource(android.R.color.transparent);
                                window.setGravity(Gravity.CENTER);
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                closeButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();*/
                                //editTextMessage.setText("");


                            } else {
                                message = response.body().getSingleMessage();
                                msgDtoList.add(message);
                                int newMsgPosition = msgDtoList.size() - 1;
                                chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                                recyclerView.scrollToPosition(newMsgPosition);
                                editTextMessage.setText("");
                                countMessages();
                            }
                        } else if (response.body().getRequest().equals("request limit expired")) {

                            dialogShow();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatAppMsgDTO> call, Throwable t) {
                    Log.d("PrivateChatActivity", t.getMessage());
                }
            });
        } else {
            Log.d(PrivateChatActivity.class.getSimpleName(), "Nothing ");
        }
    }

    @OnClick(R.id.image_view_receiver)
    public void onReceiverImageCliced() {
        //todo other profile image click
        SearchUser otherUserProfile = new SearchUser(this);
        otherUserProfile.searchUserInfo();

    }

    @OnClick(R.id.more_btn)
    public void moreButton() {
        try {
            customGalleryBottomSheet = new CustomGalleryBottomSheet();
            customGalleryBottomSheet.show(getSupportFragmentManager(), "gallery");
            Log.d("ProfileFrament", "Successfull");
        } catch (Exception e) {
            Log.d("private chat", e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(Constants.REQUEST_ACCEPT)
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        messageSeen();
    }

    private void messageSeen() {
        Helper.messageSeen(conversationId);
    }

    private void onBottomSheet() {
        CustomBottomSheetChat customBottomSheetChat = new CustomBottomSheetChat();
        customBottomSheetChat.show(getSupportFragmentManager(), "okk");
    }

    public String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void countMessages() {
        int totalMessage = msgDtoList.size();
        if (totalMessage > 0) {
            rightMessageCount = 0;
            leftMessageCount = 0;
            for (int i = 0; i < totalMessage; i++) {
                if (msgDtoList.get(i).getSender().equals(Data.userId)) {
                    rightMessageCount++;
                } else {
                    leftMessageCount++;
                }
            }
        }
        senderMessageCount.setText(String.valueOf(rightMessageCount));
        receiverMessageCount.setText(String.valueOf(leftMessageCount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocalBroadCastReceiver();
        //  chatRequestNotAcceptedMsg();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!conversationId.equals("")) {
            Helper.messageSeen(conversationId);
        }
        EventBus.getDefault().post(new Event(Constants.GET_CHAT_LIST));
    }
}