package net.boddo.btm.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Adepter.othersuser.OtherUserListAdapter;
import net.boddo.btm.Model.User;
import net.boddo.btm.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    private static List<User> otherUserList;
    @BindView(R.id.other_user_recycler_view)
    RecyclerView otherUserRecyclerView;
    OtherUserListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        otherUserRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        otherUserRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OtherUserListAdapter(SearchActivity.this,otherUserList);
        otherUserRecyclerView.setAdapter(adapter);
    }

    public static Intent newIntent(Context context, List<User> userList){
        Intent intent = new Intent(context,SearchActivity.class);
        otherUserList = userList;
        return intent;
    }
}
