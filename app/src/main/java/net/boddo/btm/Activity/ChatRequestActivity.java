package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Adepter.ChatRequestAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRequest;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRequestActivity extends AppCompatActivity {
    ChatRequestActivity activity;

    private RecyclerView rvChartRequest;
    private ArrayList<ChatRequest> chatRequestModelArrayList;
    private ApiInterface apiInterface;
    private ChatRequestAdepter chatRequestAdepter;
    private TextView tvBackChartRequest, tvUpdateYourProfileChatRequest;
    private LinearLayout llmNoChatRequest;
    private ChatRequest chatRequest;
    private List<ChatRequest.RequestedMessage> requestedMessageList;
    private ProgressBar charRequestPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_request);

        activity = this;

        /**
         * Set
         * Status
         * Bar
         * Size
         * Start
         * */
        View blankView = findViewById(R.id.blankView);
        if (Data.STATUS_BAR_HEIGHT != 0) {
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
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

        ProgressDialog.show(this);

        rvChartRequest = findViewById(R.id.rvChartRequest);
        tvBackChartRequest = findViewById(R.id.tvBackChartRequest);
        charRequestPB = findViewById(R.id.charRequestPB);
        tvUpdateYourProfileChatRequest = findViewById(R.id.tvUpdateYourProfileChatRequest);
        llmNoChatRequest = findViewById(R.id.llmNoChatRequest);
        chatRequestModelArrayList = new ArrayList<>();

        tvBackChartRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvUpdateYourProfileChatRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileOneActivity.class);
                startActivity(intent);
            }
        });


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRequest> call = apiInterface.getAllRequestedList(Constants.SECRET_KEY, Data.userId);
        call.enqueue(new Callback<ChatRequest>() {
            @Override
            public void onResponse(Call<ChatRequest> call, Response<ChatRequest> response) {
                if (response.isSuccessful()) {
                    if (response.body().getRequestedMessage().size() != 0) {
                        //dipto testing
                        chatRequest = response.body();
                        requestedMessageList = chatRequest.getRequestedMessage();
                        llmNoChatRequest.setVisibility(View.GONE);
                        //charRequestPB.setVisibility(View.GONE);
                        rvChartRequest.setVisibility(View.VISIBLE);
                        chatRequestAdepter = new ChatRequestAdepter(getApplicationContext(), requestedMessageList);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        rvChartRequest.setLayoutManager(llm);
                        rvChartRequest.setAdapter(chatRequestAdepter);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDialog.cancel();
                            }
                        }, 2000);

                    } else {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llmNoChatRequest.setVisibility(View.VISIBLE);
                                rvChartRequest.setVisibility(View.GONE);
                                //charRequestPB.setVisibility(View.GONE);
                                ProgressDialog.cancel();
                            }
                        },2000);

                    }
                }
            }

            @Override
            public void onFailure(Call<ChatRequest> call, Throwable t) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llmNoChatRequest.setVisibility(View.VISIBLE);
                        rvChartRequest.setVisibility(View.GONE);
                        //charRequestPB.setVisibility(View.GONE);
                        ProgressDialog.cancel();
                    }
                },2000);

            }
        });


    }



   /* @Override
    public void onClickChat(ArrayList<ChatRequest.RequestedMessage> chatRequestModelArrayList) {
        if(requestedMessageList!=null && requestedMessageList.size() > 0){

        }

    }*/
}