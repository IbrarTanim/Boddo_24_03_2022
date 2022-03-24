package net.boddo.btm.Activity;

import android.os.Handler;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import net.boddo.btm.Adepter.BlockListAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.Blocklist;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockListActivity extends AppCompatActivity {

    TextView notView,tvBackBlockList;
    SwipeRefreshLayout swipeRefreshLayout;

    List<Blocklist>blocklist;
    RecyclerView recyclerView;
    BlockListAdepter blockListAdepter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);

        notView = findViewById(R.id.not_visitor_view);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.block_recyclerView);
        tvBackBlockList = findViewById(R.id.tvBackBlockList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getBlock();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getBlock();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000) ;
            }
        });

        tvBackBlockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getBlock() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Blocklist>>call = apiInterface.onBlocklist(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<List<Blocklist>>() {
            @Override
            public void onResponse(Call<List<Blocklist>> call, Response<List<Blocklist>> response) {
                blocklist = response.body();
                    if (blocklist.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        notView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        notView.setVisibility(View.GONE);
                    }
                blockListAdepter = new BlockListAdepter(BlockListActivity.this,blocklist);
                    recyclerView.setAdapter(blockListAdepter);
                }
            @Override
            public void onFailure(Call<List<Blocklist>> call, Throwable t) {

            }
        });


    }
}
