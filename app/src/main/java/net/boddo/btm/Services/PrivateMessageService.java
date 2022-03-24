package net.boddo.btm.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import io.reactivex.annotations.Nullable;

public class PrivateMessageService extends Service {


    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "image";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent.hasExtra("image")){
            Log.d("PrivateMessageService",intent.getExtras().getString("image"));
        }else if(intent.hasExtra("message")){
            Log.d("PrivateMessageService",intent.getExtras().getString("message"));
        }

        return null;
    }
}
