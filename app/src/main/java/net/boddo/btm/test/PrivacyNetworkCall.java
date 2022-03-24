package net.boddo.btm.test;

import android.content.Context;
import android.widget.Toast;

import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyNetworkCall {

    ApiInterface apiInterface ;
    Context context;
    String status;

    public PrivacyNetworkCall(Context context) {
        this.context = context;
    }

    public void onPrivacy(int secritKey, final String type, int photoId, String userId){

    apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String>call = apiInterface.onPrivacy(secritKey,type,photoId,userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                status= response.body();

                if (status.equals("success")){

                    if (type.equals("delete")){

                        Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();

                    }else if (type.equals("public")){

                        Toast.makeText(context, "public Successful", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "private Successful", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


}
