package net.boddo.btm.Utills;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUpdate {
    ApiInterface apiInterface;
    Context context;

    public static boolean result = false;
    public AboutUpdate(Context context) {
        this.context = context;
    }

    public boolean updateAbout(String key, String value) {
        result = false;
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.e("key", "updateAbout: "+key );
        Log.e("key", "updateAbout: "+value );
        Call<User> call = apiInterface.updateAbout(Data.userId, Constants.SECRET_KEY, key, value);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user.getStatus().equals("success")) {
                    result = true;
                } else if (user.getStatus().equals("failed")) {
                    result = false;
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
        return result;
    }
}
