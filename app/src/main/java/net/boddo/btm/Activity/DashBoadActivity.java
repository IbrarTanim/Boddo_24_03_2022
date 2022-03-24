package net.boddo.btm.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.billingclient.api.Purchase;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import net.boddo.btm.Activity.Settings.SettingsActivity;
import net.boddo.btm.Billing.BillingManager;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Fragment.ChatFragment;
import net.boddo.btm.Fragment.ChatRoomFragment;
import net.boddo.btm.Fragment.NotificationFragment;
import net.boddo.btm.Fragment.OthersFragment.OthersProfileFragment;
import net.boddo.btm.Fragment.PhotoBlogFragment;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Fragment.ProfileOneFragment;
import net.boddo.btm.Model.BlockData;
import net.boddo.btm.Model.Blocklist;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;
import net.boddo.btm.Services.FirebaseCloudMessagingService;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;
import net.boddo.btm.test.GoogleAdMob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import jp.wasabeef.blurry.Blurry;


public class DashBoadActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DashBoadActivity activity;
    TextView userName, navigation_moto;

    FrameLayout frameLayout;
    ImageView drawer_bg, navigationCancleButton;
    Button logOutButton;
    Dialog mDialog;
    Dialog motoDialog;
    boolean entry = true;

    public static List<BlockData> whoBlockMeList;
    public static List<Blocklist> myBlockList;

    RadioGroup radioGroup;

    //Firebase Value
    FirebaseDatabase database;
    DatabaseReference myRef;


    CircleImageView myImageView;
    CircleImageView otherUserImageView;

    private static final float BLUR_RADIUS = 25f;
    @BindView(R.id.badging_chatting_image_view)
    NotificationBadge badgingChattingImageView;
    @BindView(R.id.badging_notification_image_view)
    NotificationBadge badgingNotificationImageView;
    @BindView(R.id.badging_photo_blog_image_view)
    NotificationBadge badgingPhotoBlogImageView;

    private static final String TAG = "DashBoadActivity";

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    @BindView(R.id.navigation_bar_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_frame)
    FrameLayout framelayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.navi_tool_button)
    Button navigationButton;


    @BindView(R.id.tvPhoto_blogTab)
    TextView tvPhoto_blogTab;

    @BindView(R.id.tvChattingTab)
    TextView tvChattingTab;

    @BindView(R.id.tvChatroomsTab)
    TextView tvChatroomsTab;

    @BindView(R.id.tvNotificationTab)
    TextView tvNotificationTab;

    @BindView(R.id.tvProfileTab)
    TextView tvProfileTab;


    @BindView(R.id.photo_blog_image_view)
    ImageView photoBlogButton;

    @BindView(R.id.chatting_image_view)
    ImageView chatButton;

    @BindView(R.id.chat_room_image_view)
    ImageView chatRoomButton;

    @BindView(R.id.notification_image_view)
    ImageView notificationButton;

    @BindView(R.id.profile_image_view)
    ImageView profileButton;

    long message_count, request_count;
    ImageView profileCircleImage;
    boolean searchRequestResult = false;
    boolean doubleBackToExitPressedOnce = false;
    boolean likeFaviVisitorNotifi = false;
    boolean likeFaviVisiEntry = false;

    int likeCountNotifacition, favoriteCountNotifacition, visitorCountNotifacition;

    List<User> otherUserList;
    ApiInterface apiInterface;
    TextView userWallet;

    ProfileFragment profileFragment;
    ProfileOneFragment profileOneFragment;

    BillingManager billingManager;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void receiveCredits() {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.receive_credits);
        dialog.setCancelable(false);

        final Button notnow = dialog.findViewById(R.id.notnow);

        notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void initNavigationView() {
        View hView = navigationView.getHeaderView(0);
        TextView userFullName = (TextView) hView.findViewById(R.id.profile_name_navibar_Textview);
        userWallet = (TextView) hView.findViewById(R.id.palup_point_text);
        navigation_moto = (TextView) hView.findViewById(R.id.profile_moto_navibar_Textview);
        userWallet.setText(Data.userPalupPoint + " Credits");
        navigation_moto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(DashBoadActivity.this, BuyCreditActivity.class);
                intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
                startActivity(intent);*/

                Intent intent = new Intent(activity, BuyCreditActivity.class);
                intent.putExtra("Membership", true);
                activity.overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.finish();
                startActivity(intent);
            }
        });
        userFullName.setText(Data.userFirstName);
        profileCircleImage = hView.findViewById(R.id.profile_image_navibar_ImageView);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.hotlist_menu:
                //do somthing
                Intent intent = new Intent(DashBoadActivity.this, HotlistActivityNew.class);
                startActivity(intent);
                break;
            case R.id.search_user_menu:
                //do something
                Intent searchUserIntent = new Intent(DashBoadActivity.this, AllUsersActivity.class);
                startActivity(searchUserIntent);
                break;
          /*  case R.id.palup_balance_menu:
                // Boddo Credit
                Intent plaupIntent = new Intent(DashBoadActivity.this, BuyCreditActivity.class);
                startActivity(plaupIntent);
                break;*/
           /* case R.id.support_menu:
                //do somthing
                Intent supportIntent = new Intent(DashBoadActivity.this, SupportWebViewActivity.class);
                startActivity(supportIntent);
                break;*/
            case R.id.setting_menu:
                //do somthing
                Intent settingIntent = new Intent(DashBoadActivity.this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.block_list:
                //do somthing
                Intent blockIntent = new Intent(DashBoadActivity.this, BlockListActivity.class);
                startActivity(blockIntent);
                break;
            case R.id.log_out:
                logout();
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.logout(Data.userId, Constants.SECRET_KEY);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("success")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SharedPref.setLastChatRoom(Constants.LAST_CHAT_ROOM, "");
                    SharedPref.setIsLoggedIn(Constants.IS_LOGGED_IN, false);
                    SharedPref.setUserId(Constants.USER_ID, "");
                    SharedPref.setUserAccessToken(Constants.ACCESS_TOKEN, "");
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private Bitmap adjustedContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);
        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);
        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }
                G = Color.green(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }
                B = Color.blue(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return ret;
    }


    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        ImageView imageView;
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageView = (ImageView) findViewById(R.id.drawer_bg);
            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (ProfileFragment.imageList != null) {
                if (ProfileFragment.imageList.size() < 1) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green_shade_button_bg);
                } else {
                    bitmap = getBitmapFromURL(Data.profilePhoto);
                    assert bitmap != null;
                    bitmap = adjustedContrast(bitmap, -50);
                    bitmap = changeBitmapContrastBrightness(bitmap, 1, -100);
                }
            }
            //this method will be running on background thread so don't update UI frome here
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Bitmap blurredBitmap = blur(bitmap);
            imageView.setImageBitmap(blurredBitmap);
            if (ProfileFragment.imageList != null) {
                if (ProfileFragment.imageList.size() < 1) {
                    Picasso.get().load(R.drawable.default_profile_picture).into(profileCircleImage);
                } else {
                    Picasso.get().load(ProfileFragment.imageList.get(0).getPhoto()).into(profileCircleImage);
                }
            }
        }
    }

    @OnClick(R.id.photo_blog_image_view)
    public void onPhotoBlogClicked() {
        badgingPhotoBlogImageView.setNumber(0);
        FirebaseCloudMessagingService.bottom_photo_blog_dot = 0;
        setFragment(new PhotoBlogFragment());

        photoBlogButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
        tvPhoto_blogTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
        visibilityHandle(tvPhoto_blogTab, tvProfileTab, tvChatroomsTab, tvNotificationTab, tvChattingTab);


        chatButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChattingTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        notificationButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvNotificationTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        profileButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvProfileTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        chatRoomButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChatroomsTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        navigationButton.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.chatting_image_view)
    public void onChatClicked() {
        badgingChattingImageView.setNumber(0);
        FirebaseCloudMessagingService.bottom_chatButton_dot = 0;
        entry = true;
        setFragment(new ChatFragment());
        chatButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
        tvChattingTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
        visibilityHandle(tvChattingTab, tvNotificationTab, tvPhoto_blogTab, tvChatroomsTab, tvProfileTab);

        chatRoomButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChatroomsTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        notificationButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvNotificationTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        profileButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvProfileTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        photoBlogButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvPhoto_blogTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        navigationButton.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.chat_room_image_view)
    public void onChatRoomClicked() {
        setFragment(new ChatRoomFragment());
        //(new MessagesFragment());

        chatRoomButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
        tvChatroomsTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
        visibilityHandle(tvChatroomsTab, tvProfileTab, tvPhoto_blogTab, tvNotificationTab, tvChattingTab);

        chatButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChattingTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        notificationButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvNotificationTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        profileButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvProfileTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        photoBlogButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvPhoto_blogTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));
        navigationButton.setVisibility(View.INVISIBLE);

    }

    @OnClick(R.id.notification_image_view)
    public void onNotificationClicked() {
        badgingNotificationImageView.setNumber(0);
        likeFaviVisiEntry = false;
        FirebaseCloudMessagingService.bottom_like_fav_notification_dot = 0;
        setFragment(new NotificationFragment());
        chatRoomButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChatroomsTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        chatButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChattingTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        notificationButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
        tvNotificationTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
        visibilityHandle(tvNotificationTab, tvProfileTab, tvPhoto_blogTab, tvChatroomsTab, tvChattingTab);

        profileButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvProfileTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        photoBlogButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvPhoto_blogTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        navigationButton.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.profile_image_view)
    public void onProfileClicked() {
        //setFragment(profileFragment);
        setFragment(profileOneFragment);
        chatRoomButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChatroomsTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        chatButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvChattingTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        notificationButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvNotificationTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        profileButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
        tvProfileTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
        visibilityHandle(tvProfileTab, tvNotificationTab, tvPhoto_blogTab, tvChatroomsTab, tvChattingTab);


        photoBlogButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_default_color));
        tvPhoto_blogTab.setTextColor(this.getResources().getColor(R.color.menu_icon_default_color));

        navigationButton.setVisibility(View.VISIBLE);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    public static Intent newDashboardIntent(Context context) {
        Intent intent = new Intent(context, DashBoadActivity.class);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userWallet.setText(Data.userPalupPoint + " Credits");
    }

    @Override
    public void onStart() {
        super.onStart();
        userWallet.setText(Data.userPalupPoint + " Credits");
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
        myRef.child(Data.userId).setValue("0");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event.getEventType().equals(Constants.UPDATE_PALUP_POINT)) {
            userWallet.setText(Data.userPalupPoint + " Credits");
        }
        if (event.getEventType().equals(Constants.MATCHED)) {
            if (!event.getEventName().equals("")) {//Todo this is not running it will be implemented tomorrow
                final Dialog alertadd = new Dialog(DashBoadActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                LayoutInflater factory = LayoutInflater.from(DashBoadActivity.this);
                final View view = factory.inflate(R.layout.matching_model, null);
                Objects.requireNonNull(alertadd.getWindow()).setBackgroundDrawableResource(android.R.color.black);
                alertadd.setContentView(view);
                OthersProfileFragment.isMatch = true;
                Button cancelButton = view.findViewById(R.id.cancel_button);
                Button chatButton = view.findViewById(R.id.chat_button);
                TextView matchedMessage = view.findViewById(R.id.text_view_message);

                matchedMessage.setText(event.getEventName());
                myImageView = view.findViewById(R.id.user_profile_photo);
                otherUserImageView = view.findViewById(R.id.other_user_profile_photo);

                Picasso.get().load(Data.otherProfilePhoto).into(otherUserImageView);
                Picasso.get().load(Data.profilePhoto).into(myImageView);
                matchedMessage.setText(event.getEventName());
                chatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DashBoadActivity.this, PrivateChatActivity.class);
                        startActivity(intent);
                        alertadd.dismiss();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertadd.dismiss();
                    }
                });
                alertadd.show();
            }
        }

        if (entry) {
            if (event.getEventType().equals(Constants.MESSAGE_DOT_INCREASE) || event.getEventType().equals(Constants.MESSAGE_COUNT) || event.getEventType().equals(Constants.REQUEST_COUNT)) {
                badgingChattingImageView.setAnimationEnabled(true);
                badgingChattingImageView.setNumber(FirebaseCloudMessagingService.bottom_chatButton_dot);
                badgingChattingImageView.setTextColor(getResources().getColor(R.color.red));
            }
        }

        if (likeFaviVisiEntry) {
            if (event.getEventType().equals(Constants.LIKE_FAV_VISITOR)) {
                badgingNotificationImageView.setNumber(FirebaseCloudMessagingService.bottom_like_fav_notification_dot);
                badgingNotificationImageView.setTextColor(getResources().getColor(R.color.red));
                badgingNotificationImageView.setAnimationEnabled(true);
            }
        }

        if (event.getEventType().equals(Constants.PHOTO_BLOG_NOTIFICATION)) {
            badgingPhotoBlogImageView.setNumber(FirebaseCloudMessagingService.bottom_photo_blog_dot);
            badgingPhotoBlogImageView.setTextColor(getResources().getColor(R.color.red));
            badgingPhotoBlogImageView.setAnimationEnabled(true);

        }
        if (event.getEventType().equals(Constants.WARN)) {
            //warning showes
            if (!event.getEventName().equals("")) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    final PrettyDialog dialog = new PrettyDialog(this);
                    dialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setTitle("Waring !!!").setMessage(event.getEventName())
                            .addButton("Okay", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                } else {
                    final PrettyDialog dialog = new PrettyDialog(this);
                    dialog.setTitle("Waring !!!").setMessage(event.getEventName())
                            .addButton("Okay", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                }
            }
        }
        if (event.getEventType().equals(Constants.DISABLED)) {
            //while user get any event disable than he will be terminated and the application will be closed.final PrettyDialog  dialog = new PrettyDialog(this);

            final PrettyDialog dialog = new PrettyDialog(this);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                dialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setTitle("Waring !!!").setMessage("Dear " + Data.userName + ", due to violation of Palup terms and conditions we have closed your account! For more details please contact with the Palup Team.")
                        .addButton("Ok", R.color.white, R.color.red_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                finish();
                                System.exit(0);
                                startActivity(new Intent(DashBoadActivity.this, LoginActivity.class));
                                dialog.cancel();
                            }
                        });
                dialog.show();
                dialog.setCancelable(false);
            } else {
                dialog.setTitle("Waring !!!").setMessage("Dear " + Data.userName + ", due to violation of Palup terms and conditions we have closed your account! For more details please contact with the Palup Team.")
                        .addButton("Ok", R.color.white, R.color.red_600, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                finish();
                                System.exit(0);
                                startActivity(new Intent(DashBoadActivity.this, LoginActivity.class));
                                dialog.cancel();
                            }
                        });
                dialog.show();
                dialog.setCancelable(false);
            }
        }
        if (event.getEventType().equals(Constants.BLOCKED)) {
            myBlockList();
        }
        if (event.getEventType().equals(Constants.UNBLOCKED)) {
            myBlockList();
        }
        if (event.getEventType().equals(Constants.RESET_CID)) {
            // when cid will reset the user will redirected to the login page
            final PrettyDialog dialog = new PrettyDialog(this);
            dialog.setTitle("Waring !!!").setMessage("Something wrong with your account.Please login again.")
                    .addButton("Ok", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                        @Override
                        public void onClick() {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            dialog.cancel();
                            finish();
                        }
                    });
            dialog.show();
            dialog.setCancelable(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            lastExcessTime();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void lastExcessTime() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.lastExcessTime(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void whoBlockMeData() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<BlockData>> call = apiInterface.onBlockdata(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<List<BlockData>>() {
            @Override
            public void onResponse(Call<List<BlockData>> call, Response<List<BlockData>> response) {
                whoBlockMeList = response.body();
            }

            @Override
            public void onFailure(Call<List<BlockData>> call, Throwable t) {
                Log.d(DashBoadActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }

    private void myBlockList() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Blocklist>> call = apiInterface.onBlocklist(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<List<Blocklist>>() {
            @Override
            public void onResponse(Call<List<Blocklist>> call, Response<List<Blocklist>> response) {
                myBlockList = response.body();
            }

            @Override
            public void onFailure(Call<List<Blocklist>> call, Throwable t) {
                Log.d(DashBoadActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }


    private void visibilityHandle(TextView view1, TextView view2, TextView view3, TextView view4, TextView view5) {
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        view5.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_boad);
        //rokan26.01.2021  MobileAds.initialize(this, Constants.ADMOB_APP_ID);

        /*View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        getWindow().setStatusBarColor(getColor(R.color.transparent));

        //get status bar height for the device
        Data.STATUS_BAR_HEIGHT = GetStatusBarHeight();
        Log.e(TAG, "Height : " + Data.STATUS_BAR_HEIGHT);


        ButterKnife.bind(this);
        initNavigationView();
        whoBlockMeData();
        myBlockList();
        activity = this;
        motoDialog = new Dialog(this);
        motoDialog.setContentView(R.layout.custom_moto_alert_dialog);


        profileCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, new ProfileOneFragment()).disallowAddToBackStack();
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        if (getIntent().hasExtra("message_count")) {
            message_count = getIntent().getLongExtra("message_count", 0);
        }
        if (getIntent().hasExtra("message_request_count")) {
            request_count = getIntent().getLongExtra("message_request_count", 0);
        }
        if (getIntent().hasExtra("like_fav_visitor_Notifacation")) {
            likeFaviVisitorNotifi = getIntent().getBooleanExtra("like_fav_visitor_Notifacation", false);
        }


        if (likeFaviVisitorNotifi) {
            badgingNotificationImageView.setNumber(1);
            badgingNotificationImageView.setTextColor(getResources().getColor(R.color.red));
            likeFaviVisiEntry = false;
        } else {
            badgingNotificationImageView.setNumber(0);
            badgingNotificationImageView.setTextColor(getResources().getColor(R.color.red));
            likeFaviVisiEntry = true;
        }

        if (message_count > 0 || request_count > 0) {
            badgingChattingImageView.setNumber((int) message_count);
            badgingChattingImageView.setTextColor(getResources().getColor(R.color.red));
            entry = false;
        } else {
            badgingChattingImageView.setNumber(0);
            badgingChattingImageView.setTextColor(getResources().getColor(R.color.red));
            entry = true;
        }


        mDialog = new Dialog(this);
        profileFragment = new ProfileFragment();
        profileOneFragment = new ProfileOneFragment();
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }


        if (savedInstanceState == null) {
            Intent data = getIntent();
            if (data.hasExtra("profile")) {
                if (data.getStringExtra("profile").equals("profile")) {
                    //setFragment(profileFragment);
                    setFragment(profileOneFragment);
                    profileButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
                    tvProfileTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
                    visibilityHandle(tvProfileTab, tvPhoto_blogTab, tvChatroomsTab, tvNotificationTab, tvChattingTab);

                }
            } else if (data.hasExtra(Constants.CHAT_LIST)) {
                setFragment(new ChatFragment());
                //setFragment(new MessagesFragment());


                chatButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
                tvChattingTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
                visibilityHandle(tvChattingTab, tvPhoto_blogTab, tvChatroomsTab, tvNotificationTab, tvProfileTab);


            } else {
                setFragment(new PhotoBlogFragment());
                photoBlogButton.setColorFilter(this.getResources().getColor(R.color.menu_icon_after_click));
                tvPhoto_blogTab.setTextColor(this.getResources().getColor(R.color.menu_icon_after_click));
                visibilityHandle(tvPhoto_blogTab, tvChattingTab, tvChatroomsTab, tvNotificationTab, tvProfileTab);

            }
        }

        if (LoginActivity.isBonusAvailable != 0) {
            //final PrettyDialog dialog = new PrettyDialog(this);

            receiveCredits();
            /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                dialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).
                        setTitle("Yahoo !!!").setMessage("You have received " + LoginActivity.isBonusAvailable + ". Palup credits to visit us today, visit us tomorrow again for more free credits")
                        .addButton("Got it", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.cancel();
                            }
                        });
                dialog.show();
            } else {
                dialog.setTitle("Yahoo !!!").setMessage("You have received " + LoginActivity.isBonusAvailable + ". Palup credits to visit us today, visit us tomorrow again for more free credits")
                        .addButton("Got it", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.cancel();
                            }
                        });
                dialog.show();
            }*/
            LoginActivity.isBonusAvailable = 0;
        }
        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
                GoogleAdMob googleAdMob = new GoogleAdMob(DashBoadActivity.this);
                //rokan26.01.2021  googleAdMob.getGooleAdMob("navigationBar");
            }
        });
        //AsyncTask Call for Navigation Drawer Blurry profile pic
        new AsyncCaller().execute();
        //firebase User Online On

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("UserOnline");
        myRef.child(Data.userId).setValue("1");

        billingManager = new BillingManager(DashBoadActivity.this, new BillingManager.BillingUpdatesListener() {
            @Override
            public void onBillingClientSetupFinished() {
                billingManager.queryPurchases();
            }

            @Override
            public void onConsumeFinished(String token, int result) {
            }

            @Override
            public void onPurchasesUpdated(final List<Purchase> purchases) {
                if (purchases != null && purchases.size() > 0) {
                    Log.d(DashBoadActivity.class.getSimpleName(), purchases.toString());
                    if (purchases.get(purchases.size() - 1).getSku().contains("subscription")) {
                        Data.isPalupPlusSubcriber = true;
                    }
                }
            }
        });
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
