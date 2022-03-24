package net.boddo.btm.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Adepter.CommentAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Event.Event;
import net.boddo.btm.Model.AllComments;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCommentsActivity extends AppCompatActivity {

    AllCommentsActivity activity;
    @BindView(R.id.emojicon_edit_text)
    EmojiconEditText emojiconEditText;

   /* @BindView(R.id.emoji_btn)
    ImageView emojiButton;*/

    @BindView(R.id.comment_title)
    TextView commentTitle;
    @BindView(R.id.submit_btn)
    ImageView submitButton;
    ImageView submitButtonTouch;


    //View viewWhiteBlank;

    /*@BindView(R.id.back_arrow)
    ImageView backButton;*/

    @BindView(R.id.recycler_view_comment)
    RecyclerView recyclerView;

    //varialble declaration
   /* EmojiconTextView textView;
    EmojIconActions emojIcon;*/

    ApiInterface apiInterface;
    CommentAdapter adapter;

    AllComments allComments;
    List<AllComments.Comment> commentList;
    String photoId = "";
    String commentDescription;
    LinearLayoutManager layout;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }*/
        setContentView(R.layout.activity_all_comments);


        activity = this;
        ButterKnife.bind(this);
        sharedpreferences = activity.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        photoId = sharedpreferences.getString(Constants.PHOTO_BLOG_PHOTO_ID, null);

        //emojiButton = findViewById(R.id.emoji_btn);
        submitButton = findViewById(R.id.submit_btn);
        submitButtonTouch = findViewById(R.id.submitButtonTouch);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);
        //viewWhiteBlank = findViewById(R.id.viewWhiteBlank);
        // closeKeyBoard();

       /* emojiconEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);*/

        emojiconEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //submitButton.setBackgroundResource(R.drawable.send_icon_20_03_01_2021_normal);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    Log.e("Textcount", "onTextChanged: "+s.length() );
                    Toast.makeText(getApplicationContext(), "closeKeyBoard", Toast.LENGTH_SHORT).show();
                   // closeKeyBoard();
                    submitButton.setVisibility(View.GONE);
                    submitButtonTouch.setVisibility(View.VISIBLE);
                } else {
                    submitButtonTouch.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                //submitButton.setBackgroundResource(R.drawable.send_icon_20_03_01_2021_normal);
            }
        });

        commentTitle = findViewById(R.id.comment_title);

        /*Bundle mArgs = getArguments();
        photoId = mArgs.getString(Constants.PHOTO_BLOG_PHOTO_ID);*/

        /*emojIcon = new EmojIconActions(activity, view, emojiconEditText, emojiButton);
        emojIcon.ShowEmojIcon();*/

        initRecyclerView();

        getAllComments();

        /*emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });*/


        if (commentList != null && commentList.size() > 0) {
            //commentTitle.setText("Comments(" + String.valueOf(commentList.size()) + ")");
            commentTitle.setText(commentList.size() + " " + "Comments");

        }


        /*view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
            }
        });*/


    }

    private void closeKeyBoard() {
       /* InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);*/

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
    }


    @OnClick(R.id.submit_btn)
    public void onSubmitClicked() {
        commentDescription = emojiconEditText.getText().toString();
        if (commentDescription.length() > 0) {
            makeComment();
        } else {
        }
    }

    //rokan
    @OnClick(R.id.comment_title)
    public void onBackPressed() {
        finish();

        //Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    private void initRecyclerView() {
        layout = new LinearLayoutManager(activity);
        layout.setReverseLayout(true);
        recyclerView.setLayoutManager(layout);
        layout.setStackFromEnd(true);
        layout.setReverseLayout(true);
    }

    private void makeComment() {
        Data.pd = new ProgressDialog(activity);
        Data.pd.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AllComments> call = apiInterface.putComment(Constants.SECRET_KEY, Data.userId, Integer.parseInt(photoId), commentDescription);
        call.enqueue(new Callback<AllComments>() {
            @Override
            public void onResponse(Call<AllComments> call, Response<AllComments> response) {
                AllComments allComments = response.body();
                if (allComments.getStatus().equals("success")) {
                    commentList = allComments.getComment();
                    initRecyclerView();
                    adapter = new CommentAdapter(activity, commentList);
                    recyclerView.setAdapter(adapter);
                    if (Data.pd != null) {
                        Data.pd.dismiss();
                    }
                    emojiconEditText.setText("");
                    submitButtonTouch.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.GONE);
                    commentTitle.setText(commentList.size() + " " + "Comments");

                    //viewWhiteBlank.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AllComments> call, Throwable t) {
                Log.d("BottomSheet", t.getMessage());
                if (Data.pd != null) {
                    Data.pd.dismiss();
                }
            }
        });
    }

    private void getAllComments() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AllComments> call = apiInterface.getAllComments(Constants.SECRET_KEY, Integer.parseInt(photoId));
        call.enqueue(new Callback<AllComments>() {
            @Override
            public void onResponse(Call<AllComments> call, Response<AllComments> response) {
                allComments = response.body();
                if (allComments.getStatus().equals("success")) {
                    commentList = allComments.getComment();
                    initRecyclerView();
                    adapter = new CommentAdapter(activity, commentList);
                    recyclerView.setAdapter(adapter);
                    if (commentList.size() > 0) {
                        //commentTitle.setText("Comments("+commentList.size()+")");
                        commentTitle.setText(commentList.size() + " " + "Comments");
                    } else {
                        commentTitle.setText("Be the first one to comment");
                    }

                }
            }

            @Override
            public void onFailure(Call<AllComments> call, Throwable t) {
                Log.d("BottomShet", t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllComments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new Event("updateComment"));
    }

}