package net.boddo.btm.Activity;

import android.os.Handler;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockListActivity extends AppCompatActivity {

    TextView tvBackBlockList;
    SwipeRefreshLayout swipeRefreshLayout;

    List<Blocklist> blocklist;
    RecyclerView recyclerView;
    BlockListAdepter blockListAdepter;
    ConstraintLayout notBlockedView;
    CircleImageView emptyCIV;
    TextView emptyHeaderTV, emptyBodyTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.block_recyclerView);
        tvBackBlockList = findViewById(R.id.tvBackBlockList);
        notBlockedView = findViewById(R.id.not_block_view);
        emptyCIV = findViewById(R.id.empty_civ);
        emptyHeaderTV = findViewById(R.id.empty_header_tv);
        emptyBodyTV = findViewById(R.id.empty_body_tv);


        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = findViewById(R.id.blankView);
        int statusBarHeight = Data.STATUS_BAR_HEIGHT;
        if (statusBarHeight != 0) {
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = statusBarHeight;
            blankView.setLayoutParams(params);
            //Log.e(TAG, "Status Bar Height: " + statusBarHeight );
        }
        /**
         * Set
         * Status
         * Bar
         * Size
         * End
         * */

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
                }, 3000);
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
        Call<List<Blocklist>> call = apiInterface.onBlocklist(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<List<Blocklist>>() {
            @Override
            public void onResponse(Call<List<Blocklist>> call, Response<List<Blocklist>> response) {
                blocklist = response.body();
                if (blocklist.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyCIV.setImageDrawable(ContextCompat.getDrawable(BlockListActivity.this, R.drawable.block));
                    emptyHeaderTV.setText("No blocks yet.");
                    notBlockedView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    notBlockedView.setVisibility(View.GONE);
                }
                blockListAdepter = new BlockListAdepter(BlockListActivity.this, blocklist);
                recyclerView.setAdapter(blockListAdepter);
            }

            @Override
            public void onFailure(Call<List<Blocklist>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                emptyCIV.setImageDrawable(ContextCompat.getDrawable(BlockListActivity.this, R.drawable.block));
                emptyHeaderTV.setText("No blocks yet.");
                notBlockedView.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
