package net.boddo.btm.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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


public class PhotoFragment extends Fragment {

    public PhotoFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.user_photo_blog_recycler_view)
    RecyclerView recyclerView;

    UserPhotoBlogAdapter adapter;
    UserPhotoBlogImages[] userPhotoBlogImages;
    ApiInterface apiInterface;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this,view);
        getAllImageList();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    private void getAllImageList() {
        Data.pd = new ProgressDialog(getContext());
        Data.pd.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserPhotoBlogImages[]> call = apiInterface.userAllImagePhotoBlog(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<UserPhotoBlogImages[]>() {
            @Override
            public void onResponse(Call<UserPhotoBlogImages[]> call, Response<UserPhotoBlogImages[]> response) {
                if (response.body().length != 0 || response.body() != null){
                    userPhotoBlogImages = response.body();
                    adapter = new UserPhotoBlogAdapter(getContext(), userPhotoBlogImages);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<UserPhotoBlogImages[]> call, Throwable t) {
                Log.d("PhotoFragment", t.getMessage());
            }
        });
        Data.pd.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllImageList();
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
