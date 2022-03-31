package net.boddo.btm.Activity.Chatroom.chatroomuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import net.boddo.btm.Adepter.chatroom.ChatRoomAdminAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.ChatRoomUserInfo;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomAllUserActivity extends AppCompatActivity {


    private String type = "";
    private String country = "";

    @BindView(R.id.chat_room_all_user_recycler_view)
    RecyclerView recyclerView;

    /*@BindView(R.id.toolbar)
    Toolbar toolbar;*/
    TextView tvBackChatRoomAllUser;
    ChatRoomUserInfo[] list;
    ChatRoomAdminAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @BindView(R.id.empty_view)
    TextView emptyTextView;

    private static final String TAG = "ChatRoomAllUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_all_user);
        ButterKnife.bind(this);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        tvBackChatRoomAllUser = findViewById(R.id.tvBackChatRoomAllUser);
        tvBackChatRoomAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*setSupportActionBar(toolbar);// action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        tvBackChatRoomAllUser.setTextColor(getResources().getColor(R.color.black));


        Intent intent = getIntent();

        if (intent.hasExtra(Constants.TYPE)) {
            if (intent.hasExtra(Constants.GLOBAL)) {
                type = Constants.GLOBAL;
                country = "";
            } else {
                type = intent.getStringExtra(Constants.TYPE);
                country = intent.getStringExtra(Constants.COUNTRY);
            }
        }
        //toolbar.setTitle(intent.getStringExtra(Constants.TYPE));

        String valueCheck = intent.getStringExtra(Constants.TYPE);
        tvBackChatRoomAllUser.setText(valueCheck);

        getAllChatRoomUser(type, country);

    }

    private void getAllChatRoomUser(String type, String cntry) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChatRoomUserInfo[]> call = apiInterface.getAllChatRoomUserInfo(Constants.SECRET_KEY, type, Data.userId, country);
        call.enqueue(new Callback<ChatRoomUserInfo[]>() {
            @Override
            public void onResponse(Call<ChatRoomUserInfo[]> call, Response<ChatRoomUserInfo[]> response) {
                if (response.code() == 200) {
                    list = response.body();
                    /*if (list.length == 0) {
                        emptyTextView.setVisibility(View.VISIBLE);
                    } else {
                        emptyTextView.setVisibility(View.GONE);*/
                        adapter = new ChatRoomAdminAdapter(ChatRoomAllUserActivity.this, list);
                        linearLayoutManager = new LinearLayoutManager(ChatRoomAllUserActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    //}
                }
            }

            @Override
            public void onFailure(Call<ChatRoomUserInfo[]> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
