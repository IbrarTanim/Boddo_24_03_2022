package net.boddo.btm.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import net.boddo.btm.Activity.HotlistActivityNew;
import net.boddo.btm.Activity.OthersProfileActivity;
import net.boddo.btm.Activity.AllUsersActivity;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Pojo;
import net.boddo.btm.Model.ProfileImageLoader;
import net.boddo.btm.Model.User;
import net.boddo.btm.Activity.photoblog.PhotoBlogActivity;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearByFragment extends Fragment {

    public NearByFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.photoblog_button)
    Button photoBlogBTN;

    @BindView(R.id.other_user_id)
    EditText otherId;
    @BindView(R.id.find_user)
    Button search;
    @BindView(R.id.hotlist)
    Button hotlist;

    @BindView(R.id.allUser)
    Button allUser;

    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.find_user)
    public void onSearchClicked() {
        if (otherId.equals("")) {
            otherId.setError("please enter user Id");
        } else {
            Data.otherUserId = otherId.getText().toString();

            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Pojo> call = apiInterface.searchOtherUser(Data.userId,Data.otherUserId, Constants.SECRET_KEY);
            call.enqueue(new Callback<Pojo>() {
                @Override
                public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                    Pojo pojo = response.body();
                    if (pojo.getStatus().equals("success")) {
                        final User user = pojo.getUser();
                        Data.saveOthersUserData(user);
                        loadOthersProfileImages();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getContext(), OthersProfileActivity.class);
                                intent.putExtra("othersDetails", user);
                                startActivity(intent);
                            }
                        }, 3000);

                    } else {
                        Toast.makeText(getActivity(), "Sorry for now", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Pojo> call, Throwable t) {
                    Log.d("NearByFragment", t.getMessage());
                }
            });
        }
    }

    public void loadOthersProfileImages() {
        Call<ProfileImageLoader> call = apiInterface.allPhotos(Data.otherUserId, Constants.SECRET_KEY);
        call.enqueue(new Callback<ProfileImageLoader>() {
            @Override
            public void onResponse(Call<ProfileImageLoader> call, Response<ProfileImageLoader> response) {
                if (response.body().getStatus().equals("success")) {
                    ProfileImageLoader photos = response.body();
                    Data.othersImageList = photos.getProfileImageInfo();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageLoader> call, Throwable t) {
                Log.d("NearBy", t.getMessage());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setRetainInstance(true);
    }


    @OnClick(R.id.hotlist)
    public void gotoHotlist(){
        Intent intent = new Intent(getActivity(),HotlistActivityNew.class);
        startActivity(intent);
    }

    @OnClick(R.id.allUser)
    public void gotoallUser(){
        //Toast.makeText(getActivity(), "bbv", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),AllUsersActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.photoblog_button)
    public void photoBlog(){
        //Toast.makeText(getActivity(), "bbv", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(),PhotoBlogActivity.class);
        startActivity(intent);
    }

}
