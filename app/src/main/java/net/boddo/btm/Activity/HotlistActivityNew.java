package net.boddo.btm.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.boddo.btm.Adepter.HotlistAdapter;
import net.boddo.btm.Model.Hotlist;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.GridSpacingItemDecoration;
import net.boddo.btm.Utills.ProgressDialog;
import net.boddo.btm.dialogfragment.HotlistDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HotlistActivityNew extends AppCompatActivity implements HotlistDialogFragment.HotlistListener {

    //SwipeRefreshLayout swipeRefreshLayout;
    HotlistActivityNew activity;
    RecyclerView recyclerView;
    LinearLayout llHotListData;
    EditText bidamountText;
    TextView palupCreditText;
    String URL = "https://bluetigermobile.com/palup/apis/all_hotlist.php";
    public Hotlist[] hotlist;
    int previousBidAmount = 0;
    int userCurrentPalupPoint = 0;
    int bidAmount = 0;
    String bidAmout = "";
    Dialog mDialog;

    HotlistAdapter hotListNewAdapter;

    TextView tvBackHotList, tvJoinAtHotList;

    /*@BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.add_button)
    ImageView bidButton;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotlist_new);
        activity = this;
        ButterKnife.bind(this);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        //This code use for hide keyboard when start activity..
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        recyclerView = findViewById(R.id.hotListrecyclerView);
        llHotListData = findViewById(R.id.llHotListData);

        mDialog = new Dialog(this);

        //toolbarTitle.setText("Hotlist");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //swipeRefreshLayout = findViewById(R.id.refreshLayout);

        tvBackHotList = findViewById(R.id.tvBackHotList);
        tvBackHotList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvJoinAtHotList = findViewById(R.id.tvJoinAtHotList);

        tvJoinAtHotList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeHotlist();
            }
        });

        refresh();

       /* swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000) ;
            }
        });*/

        bidAmout = Data.userPalupPoint;
    }

    private void refresh() {
        ProgressDialog.show(this);
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                hotlist = gson.fromJson(response, Hotlist[].class);

                if (hotlist.length == 0) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.cancel();
                            tvJoinAtHotList.setVisibility(View.INVISIBLE);
                            llHotListData.setVisibility(View.INVISIBLE);
                            emptyHotlistDialog();
                        }
                    },2000);
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.cancel();
                            hotListNewAdapter = new HotlistAdapter(HotlistActivityNew.this, hotlist);
                            recyclerView.setAdapter(hotListNewAdapter);
                            tvJoinAtHotList.setVisibility(View.VISIBLE);
                            llHotListData.setVisibility(View.VISIBLE);
                        }
                    },2000);
                }


                /*recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(hotListNewAdapter);*/


                /*RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, GridSpacingItemDecoration.dpToPx(10, activity), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(hotListNewAdapter);*/


                for (int i = 0; i < hotlist.length; i++) {
                    if (hotlist[i].getUserId().equals(Data.userId)) {
                        previousBidAmount = Integer.parseInt(hotlist[i].getBid());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialog.cancel();
                        Toast.makeText(HotlistActivityNew.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void emptyHotlistDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setTitle("info");
        dialog.setContentView(R.layout.empty_hotlist_dialog);
        final Button btnDialogJoinAtHotList = dialog.findViewById(R.id.btnDialogJoinAtHotList);
        btnDialogJoinAtHotList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addMeHotlist();
            }
        });
        final Button btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(HotlistActivityNew.this, DashBoadActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }


    public void addMeHotlist() {
        HotlistDialogFragment dialogFragment = new HotlistDialogFragment(activity);
        dialogFragment.setOnHotlistListener(HotlistActivityNew.this);
        Bundle bundle = new Bundle();
        bundle.putInt("previous_bid_amount", previousBidAmount);
        bundle.putInt("current_balance", Data.userPalupPoint != null ? Integer.parseInt(Data.userPalupPoint) : 0);
        dialogFragment.setArguments(bundle);
        dialogFragment.show((HotlistActivityNew.this).getSupportFragmentManager(), "Image Dialog");
        //dialogFragment.setCancelable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void goToBuyCredit() {
        Intent intent = new Intent(HotlistActivityNew.this, BuyCreditActivity.class);
        startActivity(intent);
    }

    public void goToPalupPlus() {
        Intent intent = new Intent(HotlistActivityNew.this, BuyCreditActivity.class);
        intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
        startActivity(intent);
    }

    @Override
    public void onSuccessListener(int credits) {
        Data.userPalupPoint = String.valueOf(credits);
        refresh();
    }


}











