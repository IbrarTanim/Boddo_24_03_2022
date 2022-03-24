package net.boddo.btm.Services;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.boddo.btm.Activity.ChatRoomActivity;
import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Event.ChatRoomEvent;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Activity.PrivateChatActivity;
import net.boddo.btm.Activity.SplashScreenActivity;
import net.boddo.btm.Fragment.CountryChatroomFragment;
import net.boddo.btm.Fragment.DateChatroomFragment;
import net.boddo.btm.Fragment.FriendsChatroomFragment;
import net.boddo.btm.Fragment.GlobalChatroomFragment;
import net.boddo.btm.Fragment.LoveChatroomFragment;
import net.boddo.btm.Model.FirebaseCloudMessageResponse;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.interfaces.CountryChatRoomEventListener;

public class FirebaseCloudMessagingService extends FirebaseMessagingService{

    private static final String TAG = FirebaseCloudMessagingService.class.getSimpleName();
    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "image";
    private static final String USER_ID_EXTRA = "user_id";
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationManager notificationManager;
    LocalBroadcastManager broadcaster;
    Bitmap bitmap;
    boolean isImage = false;
    boolean isAppRunning = false;
    Uri defaultSoundUri;

    public static int bottom_chatButton_dot = 0;
    public static int bottom_notification_dot = 0;
    public static int bottom_like_fav_notification_dot = 0;
    public static int bottom_photo_blog_dot = 0;
    public static int countMessageRequest = 0;
    public static int countLike = 0;
    public static int countFevorite = 0;

    CountryChatRoomEventListener countryChat;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (Data.isNotificationOn) {

//       FirebaseCloudMessageResponse[] id = (FirebaseCloudMessageResponse[]) remoteMessage.getData().values().toArray();

            String values = remoteMessage.getData().get("notification");
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            final FirebaseCloudMessageResponse firebaseCloudMessageResponse = gson.fromJson(values, FirebaseCloudMessageResponse.class);


            if (firebaseCloudMessageResponse.getType().equals(Constants.CHAT_TYPE_ROOM)) {
                String title = firebaseCloudMessageResponse.getTitle();
                if (isActivityRunning(ChatRoomActivity.class)) {
                    if (title.equals("chatroom By Admin")) {
                        EventBus.getDefault().post(new Event(Constants.CHAT_ROOM));
                    }
                    chatRoomNotificationHandler(title,firebaseCloudMessageResponse.getBody(),firebaseCloudMessageResponse.getUserId(),firebaseCloudMessageResponse.getOtherUserPhoto(), firebaseCloudMessageResponse.getMessage());
                    if (firebaseCloudMessageResponse.getBody().equals("left")) {
                        EventBus.getDefault().post(new Event(Constants.LEFT_FROM_CHAT_ROOM));
                    }
                }
            } else if (firebaseCloudMessageResponse.getType().equals(Constants.PHOTO_BLOG)) {
                if (isActivityRunning(DashBoadActivity.class)) {
                    bottom_photo_blog_dot++;
                    Data.PhotoBlogCount++;
                    EventBus.getDefault().post(new Event(Constants.PHOTO_BLOG_NOTIFICATION));
                }
            } else if (firebaseCloudMessageResponse.getType().equals(Constants.TOP_PHOTO_BLOG)) {
                if (isActivityRunning(DashBoadActivity.class)) {
                    bottom_photo_blog_dot++;
                    Data.TopPhotoCount++;
                    EventBus.getDefault().post(new Event(Constants.PHOTO_BLOG_NOTIFICATION));
                }
            } else if (firebaseCloudMessageResponse.getType().equals(Constants.MESSAGE_REQUEST)) {
                if (isActivityRunning(DashBoadActivity.class)) {
                    bottom_chatButton_dot++;
                    EventBus.getDefault().post(new Event(Constants.MESSAGE_REQUEST));
                    EventBus.getDefault().post(new Event(Constants.MESSAGE_DOT_INCREASE));
                }

            } else if (firebaseCloudMessageResponse.getType().equals(Constants.PRIVATE_MESSAGE)) {
                //if the activity is in foreground than the message will go through localbroadcast
                if (isActivityRunning(PrivateChatActivity.class)) {
                    broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
                    Intent intent = new Intent(Constants.REQUEST_ACCEPT);
                    intent.putExtra("user_id", firebaseCloudMessageResponse.getUserId());

                    broadcaster.sendBroadcast(intent);
                } else if (isActivityRunning(DashBoadActivity.class)) {
                    bottom_chatButton_dot++;
                    Data.UnseenMessageCount ++;
                    EventBus.getDefault().post(new Event(Constants.NEW_MESSAGE_RECEIVED));
                } else {
                    //method
                    privateChatNotificationHandler(firebaseCloudMessageResponse);
                }
                EventBus.getDefault().post(new Event(Constants.MESSAGE_DOT_INCREASE));

            } else if (firebaseCloudMessageResponse.getType().equals(Constants.MATCHED)) {

                try {
                    //isAppRunning = new ForegroundCheckTask().execute(getBaseContext()).get();
                    isAppRunning = new ForegroundCheckTask().execute(getBaseContext()).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isAppRunning) {
                    Helper.getOtherUserDetails(firebaseCloudMessageResponse.getUserId());
                    if (isActivityRunning(DashBoadActivity.class)) {
                        EventBus.getDefault().post(new Event(firebaseCloudMessageResponse.getBody(), Constants.MATCHED));
                    }
                } else {
                    matchedNotificationHandler(firebaseCloudMessageResponse);
                }
            }else if (firebaseCloudMessageResponse.getType().equals(Constants.UNMATCHED)) {
                bottom_like_fav_notification_dot++;
                Data.LikeCount++;
                EventBus.getDefault().post(new Event(Constants.LIKE_FAV_VISITOR));


            } else if (firebaseCloudMessageResponse.getType().equals(Constants.FEVORITE)) {
                bottom_like_fav_notification_dot++;
                Data.FevoriteCount++;
                EventBus.getDefault().post(new Event(Constants.LIKE_FAV_VISITOR));

            } else if (firebaseCloudMessageResponse.getType().equals(Constants.VISITOR)) {
                bottom_like_fav_notification_dot++;
                Data.VisitorCount++;
                EventBus.getDefault().post(new Event(Constants.LIKE_FAV_VISITOR));

            } else if (firebaseCloudMessageResponse.getType().equals(Constants.WARN)) {
                if (isActivityRunning(DashBoadActivity.class)) {
                    EventBus.getDefault().post(new Event(firebaseCloudMessageResponse.getBody(), Constants.WARN));
                }
            } else if (firebaseCloudMessageResponse.getType().equals(Constants.DISABLED)) {
                EventBus.getDefault().post(new Event(Constants.DISABLED));
            } else if (firebaseCloudMessageResponse.getType().equals(Constants.RESET_CID)) {
                //Todo reset CID will be implemented here
                EventBus.getDefault().post(new Event(Constants.RESET_CID));
            }
        }
    }

    private void matchedNotificationHandler(FirebaseCloudMessageResponse firebaseCloudMessageResponse) {
        Log.d(TAG, "app is not running");
        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Helper.getOtherUserDetails(firebaseCloudMessageResponse.getUserId());
        notificationIntent.putExtra(Constants.MATCHED, "matched");
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        //You should use an actual ID instead
        int notificationId = Integer.parseInt(firebaseCloudMessageResponse.getUserId());
        Intent privateChatIntent = new Intent(this, SplashScreenActivity.class);
        privateChatIntent.putExtra(NOTIFICATION_ID_EXTRA, notificationId);

        PendingIntent actionActivity = PendingIntent.getService(this,
                notificationId + 1, privateChatIntent, PendingIntent.FLAG_ONE_SHOT);
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        Helper.getOtherUserDetails(firebaseCloudMessageResponse.getUserId());
        Helper.loadMyPhotos(firebaseCloudMessageResponse.getReceiver());
        Helper.getOtherUserDetails(firebaseCloudMessageResponse.getUserId());
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_palup_logo)
                        .setContentTitle(firebaseCloudMessageResponse.getTitle().replaceAll("$2y$12$5C05oYdYo0ZJkLZZVZNulu5GhjEnwDXOeJrh2yev4rWYgF04qSjeK",""))
                        .setContentText(firebaseCloudMessageResponse.getBody().replaceAll("$2y$12$5C05oYdYo0ZJkLZZVZNulu5GhjEnwDXOeJrh2yev4rWYgF04qSjeK",""))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .addAction(R.drawable.ic_reply_notification,
                                getString(R.string.notification_add_to_cart_button), actionActivity)
                        .setContentIntent(pendingIntent);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public String activityName() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getPackageName();
    }

    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Activity getRunningActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread")
                    .invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Didn't find the running activity");
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.topActivity.getClassName()))
                return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    public void chatRoomNotificationHandler(String title,String body,String otherUserId,String photo,String message) {
        String type;
        if (title.equalsIgnoreCase(Constants.GLOBAL)) {
            type = Constants.GLOBAL;
            if (GlobalChatroomFragment.isGlobalChatRoomInView)EventBus.getDefault().post(new ChatRoomEvent(type,body,otherUserId,photo,message));
        } else if (title.equalsIgnoreCase(Constants.LOVE)) {
            type = Constants.LOVE;
            if (LoveChatroomFragment.isLoveChatRoomInView)EventBus.getDefault().post(new ChatRoomEvent(type,body,otherUserId,photo,message));

        } else if (title.equalsIgnoreCase(Constants.FRIENDS)) {
            type = Constants.FRIENDS;
            if (FriendsChatroomFragment.isFriendChatRoomInView)EventBus.getDefault().post(new ChatRoomEvent(type,body,otherUserId,photo,message));
        } else if (title.equalsIgnoreCase(Constants.DATE)) {
            type = Constants.DATE;
            if (DateChatroomFragment.isDateChatRoomInView)EventBus.getDefault().post(new ChatRoomEvent(type,body,otherUserId,photo,message));
        } else {
            type = Constants.COUNTRY;
            if (CountryChatroomFragment.isCountryChatRoomInView)EventBus.getDefault().post(new ChatRoomEvent(type,body,otherUserId,photo,message));
        }

//        EventBus.getDefault().post(new ChatRoomEvent(type,body,otherUserId,photo,message));
    }

    public void privateChatNotificationHandler(FirebaseCloudMessageResponse firebaseCloudMessageResponse) {
        Log.d(TAG, "app is not running");
        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Helper.getOtherUserDetails(firebaseCloudMessageResponse.getUserId());
        notificationIntent.putExtra(Constants.CHAT_LIST, "activeChatList");
        notificationIntent.putExtra("badigeNotifacition","badigeNotifacition");
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        //You should use an actual ID instead
        int notificationId = Integer.parseInt(firebaseCloudMessageResponse.getUserId());
        Intent privateChatIntent = new Intent(this, PrivateMessageService.class);
        privateChatIntent.putExtra(NOTIFICATION_ID_EXTRA, notificationId);
        if (firebaseCloudMessageResponse.getBody().contains("https://bluetigermobile.com/palup/apis/images/")) {
            bitmap = getBitmapfromUrl(firebaseCloudMessageResponse.getBody());
            privateChatIntent.putExtra(IMAGE_URL_EXTRA, firebaseCloudMessageResponse.getBody());
            isImage = true;
        } else {
            privateChatIntent.putExtra(USER_ID_EXTRA, firebaseCloudMessageResponse.getUserId());
        }

        PendingIntent likePendingIntent = PendingIntent.getService(this,
                notificationId + 1, privateChatIntent, PendingIntent.FLAG_ONE_SHOT);
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

        Helper.getOtherUserDetails(firebaseCloudMessageResponse.getUserId());
        Helper.loadMyPhotos(firebaseCloudMessageResponse.getReceiver());
        if (isImage) {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.notification_palup_logo)
                            .setContentTitle(firebaseCloudMessageResponse.getTitle().replaceAll("$2y$12$5C05oYdYo0ZJkLZZVZNulu5GhjEnwDXOeJrh2yev4rWYgF04qSjeK",""))
                            .setLargeIcon(bitmap)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))/*Notification with Image*/
                            .setContentText("sent you an image!!!")
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .addAction(R.drawable.ic_reply_notification,
                                    getString(R.string.notification_add_to_cart_button), likePendingIntent)
                            .setContentIntent(pendingIntent);

            notificationManager.notify(notificationId, notificationBuilder.build());
            isImage = false;
        } else {
            Helper.getOtherUserDetails(firebaseCloudMessageResponse.getUserId());
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.notification_palup_logo)
                            .setContentTitle(firebaseCloudMessageResponse.getTitle())
                            .setContentText(firebaseCloudMessageResponse.getBody())
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .addAction(R.drawable.ic_reply_notification,
                                    getString(R.string.notification_add_to_cart_button), likePendingIntent)
                            .setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }
}
