package net.boddo.btm.test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.boddo.btm.Adepter.GalleryAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.UserPhotoBlogImages;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomGalleryBottomSheet extends BottomSheetDialogFragment {

    ApiInterface apiInterface;
    RecyclerView recyclerView;
    GalleryAdepter adapter;
    UserPhotoBlogImages[] userPhotoBlogImages;
    UserPhotoBlogImages uploadedAllImages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_gallery_bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_gallery);
        getAllImageList();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

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
                userPhotoBlogImages = response.body();
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new GalleryAdepter(getContext(), userPhotoBlogImages);
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
