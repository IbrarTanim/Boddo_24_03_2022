package net.boddo.btm.Fragment.OthersFragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Adepter.UserPhotoBlogAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersProfilePhotoFragment extends Fragment {


    public OthersProfilePhotoFragment() {
        // Required empty public constructor
    }
    UserPhotoBlogAdapter adapter;
    UserPhotoBlogImages[] userPhotoBlogImages;
    UserPhotoBlogImages[] uploadedAllImages;
    @BindView(R.id.other_user_photo_blog)
    RecyclerView recyclerView;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_others_profile_photo, container, false);
        ButterKnife.bind(this, view);
        getAllImageList();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    private void getAllImageList() {
        Data.pd = new ProgressDialog(getContext());
        Data.pd.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserPhotoBlogImages[]> call = apiInterface.otherUserAllImagePhotoBlog(Constants.SECRET_KEY, Data.otherUserId);
        call.enqueue(new Callback<UserPhotoBlogImages[]>() {
            @Override
            public void onResponse(Call<UserPhotoBlogImages[]> call, Response<UserPhotoBlogImages[]> response) {
                uploadedAllImages = response.body();
                adapter = new UserPhotoBlogAdapter(getContext(), uploadedAllImages);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<UserPhotoBlogImages[]> call, Throwable t) {
                Log.d("PhotoFragment", t.getMessage());
            }
        });
        Data.pd.dismiss();
    }
}
