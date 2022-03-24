package net.boddo.btm.Services;

import android.app.Activity;
import android.util.Log;

import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.SharedPref;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseIDService extends FirebaseMessagingService {

    private static final String TAG = FirebaseIDService.class.getSimpleName();
    public static String refreshedToken = "";
    ApiInterface apiInterface;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);


        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();


       /* refreshedToken = FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Activity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

            }
        });
*/

        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        if (SharedPref.getFCMToken(Constants.FCM_TOKEN) == null || SharedPref.getFCMToken(Constants.FCM_TOKEN).equals("")) {
            SharedPref.setFCMToken(Constants.FCM_TOKEN, refreshedToken);
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}


/*public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseIDService.class.getSimpleName();
    public static String refreshedToken="";
    ApiInterface apiInterface;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        if (SharedPref.getFCMToken(Constants.FCM_TOKEN) == null || SharedPref.getFCMToken(Constants.FCM_TOKEN).equals("")){
            SharedPref.setFCMToken(Constants.FCM_TOKEN,refreshedToken);
        }

    }


}*/



