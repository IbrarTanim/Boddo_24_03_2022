package net.boddo.btm.Utills;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.boddo.btm.Activity.SplashScreenActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Helper {


    public static void showMessage(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showOtherUserProfile(Context context, String userId){
        Data.pd = new ProgressDialog(context);
        Data.pd.setTitle("Loading...");
        Data.pd.setMessage("Please wait for a while...");
        SearchUser userProfile = new SearchUser(context);
        Data.otherUserId = userId;
        userProfile.searchUserInfo();
        Data.pd.show();
    }

    public static long getDifferenceBetweenTwoDate(String serverTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        long days=0;
        try {

            Date oldDate = dateFormat.parse(serverTime);
            System.out.println(oldDate);

            Date currentDate = new Date();

            long diff = currentDate.getTime() - oldDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            days = hours / 24;

            if (oldDate.before(currentDate)) {

                Log.e("oldDate", "is previous date");
                Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                        + " hours: " + hours + " days: " + days);

            }

            // Log.e("toyBornTime", "" + toyBornTime);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return days;
    }


    public static void messageSeen(String conversationId){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Void> call = apiInterface.messageSeen(Constants.SECRET_KEY,conversationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
               Log.d("Converstation seen","Ok");
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Converstation seen",t.getMessage());
            }
        });
    }

    public static void getOtherUserDetails(String otherUserId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Pojo> call = apiInterface.searchOtherUser(Data.userId, otherUserId, Constants.SECRET_KEY);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                Pojo pojo = response.body();
                if (pojo.getStatus().equals("success")) {
                    final User user = pojo.getUser();
                    Data.saveOthersUserData(user);

                } else {
                    Log.d("Helper", "error");
                }
            }
            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {
                Log.d("Helper", t.getMessage());
            }
        });
    }

    public static void getMyInfo(String userID) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Pojo> call = apiInterface.autoLogin(userID,
                SharedPref.getUserAccessToken(Constants.ACCESS_TOKEN),
                Constants.SECRET_KEY,SharedPref.getFCMToken(Constants.FCM_TOKEN));
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                Pojo pojo = response.body();
                if (pojo.getStatus().equals("success")) {
                   User user = pojo.getUser();
                    Data.userId = user.getUserId();
                    Data.saveLoggedInData(user);

                } else if (pojo.getStatus().equals("failed")) {
                    SharedPref.setIsLoggedIn(Constants.IS_LOGGED_IN, false);
                    SharedPref.setUserId(Constants.USER_ID, "");
                    SharedPref.setUserAccessToken(Constants.ACCESS_TOKEN, "");
                }
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {
                Log.d(SplashScreenActivity.class.getSimpleName(), t.getMessage().toString());
            }
        });
    }

    public static void loadMyPhotos(String userId){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ProfileImageLoader> call = apiInterface.allPhotos(userId, Constants.SECRET_KEY);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                if (response.body().getStatus().equals("success")) {
                    ProfileImageLoader photos = response.body();
                    ProfileFragment.imageList = photos.getProfileImageInfo();
                    ProfileFragment profileFragment = new ProfileFragment();
                    if (ProfileFragment.imageList.size() < 1) {
                        Data.profilePhoto = "https://i.stack.imgur.com/XriZj.png";
                    } else {
                        Data.profilePhoto = profileFragment.imageList.get(0).getPhoto();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {
                Log.d("Helper",t.getMessage());
            }
        });
    }

    public static CharSequence getLastActionTime(String serverTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        long time = 0;
        try {
            time = sdf.parse(serverTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();
        CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return  ago;
    }

    public static long currentTimeStempToLongConversion(String serverTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        long time = 0;
        try {
            time = sdf.parse(serverTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }



    public static void changeFragment(Context context, int container, Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static boolean isAppRunning(Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getFormatedAmount(int amount) {

        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    public static int getGridSpanCount(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static int getStickersGridSpanCount(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.sticker_item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public boolean isValidEmail(String email) {

        if (TextUtils.isEmpty(email)) {
            return false;
        } else {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public boolean isValidLogin(String login) {

        String regExpn = "^([a-zA-Z]{4,24})?([a-zA-Z][a-zA-Z0-9_]{4,24})$";
        CharSequence inputStr = login;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }

    public boolean isValidSearchQuery(String query) {

        String regExpn = "^([a-zA-Z]{1,24})?([a-zA-Z][a-zA-Z0-9_]{1,24})$";
        CharSequence inputStr = query;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }

    public boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_$@$!%*?&]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }

    public static String getAge(String stringDate) {
        String[] stringArray = stringDate.split("/");
        int date = 0, month = 0, year = 0;

        date = Integer.parseInt(stringArray[0]);
        month = Integer.parseInt(stringArray[1]);
        year = Integer.parseInt(stringArray[2]);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, date);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = String.valueOf(ageInt);
        return ageS;
    }


    public static Long getSignUp_day = getDifferenceBetweenTwoDate(Data.userAccountCreated);


    public static String milliToString(long millis) {

        long hrs = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long min = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long sec = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        //millis = millis - (hrs * 60 * 60 * 1000); //alternative way
        //millis = millis - (min * 60 * 1000);
        //millis = millis - (sec * 1000);
        //long mls = millis ;
        long mls = millis % 1000;
        String toRet = String.format("%02d:%02d", hrs, min);
        //System.out.println(toRet);
        return toRet;
    }



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

}
