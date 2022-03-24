package net.boddo.btm.Utills;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.boddo.btm.Activity.BlockListActivity;
import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Activity.OthersProfileOneActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUser {

    ApiInterface apiInterface;
    Context context;
    public static boolean result = false;
    public SearchUser(Context context) {
        this.context = context;
    }
    public void searchUserInfo() {

        boolean isBlocked = false;
        boolean isBlockedMe = false;

        for (int i = 0; i < DashBoadActivity.myBlockList.size(); i++) {
            if (DashBoadActivity.myBlockList.get(i).getBlockedUserId().equals(Data.otherUserId)) {
                isBlocked = true;
                break;
            }
        }
        if (isBlocked) {

            final PrettyDialog myBlockDialog = new PrettyDialog(context);
            myBlockDialog.setContentView(R.layout.blocked_user);
            final Button blockedList =myBlockDialog.findViewById(R.id.BtnBlockedList);
            final Button cancel =myBlockDialog.findViewById(R.id.button_decline);


            blockedList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, BlockListActivity.class));
                    myBlockDialog.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myBlockDialog.dismiss();
                }
            });

            myBlockDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessage("You have  Blocked this user")
                    .setMessageColor(R.color.red_A700).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                @Override
                public void onClick() {

                    myBlockDialog.dismiss();

                }
            }).show();
            if (Data.pd != null){
                Data.pd.dismiss();
            }

        } else {

            for (int j = 0; j < DashBoadActivity.whoBlockMeList.size(); j++){

                if (DashBoadActivity.whoBlockMeList.get(j).getBlockBy().equals(Data.otherUserId)) {

                    isBlockedMe = true;
                    break;
                }

            }

            if (isBlockedMe) {

               /* final PrettyDialog myBlockDialog = new PrettyDialog(context);
                myBlockDialog.setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary).setMessage(" You have been Blocked by this user")
                        .setMessageColor(R.color.red_A700).addButton("Cancel", R.color.white, R.color.colorPrimary, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                        myBlockDialog.dismiss();
                    }
                }).show();
                if (Data.pd != null){
                    Data.pd.dismiss();
                }*/
                Toast.makeText(context, "Sorry, you have been blocked by this user", Toast.LENGTH_SHORT).show();



            }else {
                allOtherUser();
            }
        }

    }

    private void allOtherUser() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Pojo> call = apiInterface.searchOtherUser(Data.userId,Data.otherUserId, Constants.SECRET_KEY);
        call.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                Pojo pojo = response.body();
                if (pojo.getStatus().equals("success")) {
                    final User user = pojo.getUser();
                    Data.saveOthersUserData(user);
                    if (pojo.getIsLoved().equals("yes")){
                        Data.isLoved = pojo.getIsLoved();
                    }
                    if (pojo.getIsFavorite().equals("yes")){
                        Data.isFavorite = pojo.getIsFavorite();
                    }
                    if (pojo.getIsMatched().equals("yes")){
                        Data.isMatched = pojo.getIsMatched();
                    }
                    loadOthersProfileImages();
                } else {
                    Toast.makeText(context, "Sorry for now", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Pojo> call, Throwable t) {
                Log.d("NearByFragment", t.getMessage());
            }
        });

    }

    public void loadOthersProfileImages() {
        Call<ProfileImageLoader> call = apiInterface.allPhotos(Data.otherUserId, Constants.SECRET_KEY);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                if (response.body().getStatus().equals("success")) {
                    ProfileImageLoader photos = response.body();
                    Data.othersImageList = photos.getProfileImageInfo();
                    if (Data.pd != null){
                        Data.pd.dismiss();
                    }
                    //Intent intent = new Intent(context, OthersProfileActivity.class);
                    Intent intent = new Intent(context, OthersProfileOneActivity.class);
                    context.startActivity(intent);

                } else {
                    Data.isLoved = "";
                    Data.isFavorite = "";
                    Data.isMatched ="";
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {
                Log.d("NearBy", t.getMessage());
            }
        });
    }

}
