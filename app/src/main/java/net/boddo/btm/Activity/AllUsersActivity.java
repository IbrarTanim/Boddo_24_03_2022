package net.boddo.btm.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.boddo.btm.Adepter.AllUserAdapter;
import net.boddo.btm.Model.AllUser;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllUsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    @BindView(R.id.bt_clear)
    ImageButton btnClear;

    TextView tvBackActiveAllUsers;
    StaggeredGridLayoutManager layoutManager;

    @BindView(R.id.et_search)
    EditText editTextSearch;

    String URL = "https://bluetigermobile.com/palup/apis/all_user_list_sunny.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.allUserRecycler);
        tvBackActiveAllUsers = findViewById(R.id.tvBackActiveAllUsers);

        /*progressBar = findViewById(R.id.loadingBar);
        progressBar.setVisibility(View.VISIBLE);*/
        net.boddo.btm.Utills.ProgressDialog.show(this);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    searchAction();
                    return true;
                }
                return false;
            }
        });

        tvBackActiveAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                AllUser[] allUsers = gson.fromJson(response, AllUser[].class);
                //progressBar.setVisibility(View.GONE);
                net.boddo.btm.Utills.ProgressDialog.cancel();
                recyclerView.setVisibility(View.VISIBLE);

                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new AllUserAdapter(allUsers, AllUsersActivity.this));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AllUsersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    @OnClick(R.id.bt_clear)
    public void onBtnClear() {
        searchAction();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchAction() {
        Data.pd = new ProgressDialog(this);
        Data.pd.setTitle("Please Wait...");
        Data.pd.show();
        recyclerView.setVisibility(View.GONE);
        final String query = editTextSearch.getText().toString().trim();
        if (!query.equals("")) {

            StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_URL + "searchuser.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    AllUser[] allUsers = gson.fromJson(response, AllUser[].class);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new AllUserAdapter(allUsers, AllUsersActivity.this));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AllUsersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("secret_key", String.valueOf(Constants.SECRET_KEY));
                    map.put("keywords_user", editTextSearch.getText().toString().trim());
                    return map;
                }
            };


            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);


            Data.pd.dismiss();
        } else {
            Toast.makeText(this, "Please fill search input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
