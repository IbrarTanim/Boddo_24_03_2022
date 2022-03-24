package net.boddo.btm.test;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.boddo.btm.Event.Event;
import net.boddo.btm.Adepter.CommentAdapter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.Model.AllComments;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;


import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetNavigationComment extends BottomSheetDialogFragment {

    public BottomSheetNavigationComment() {
    }
    // private BottomSheetListener bottomSheetListener;


    //widget declaration
    @BindView(R.id.emojicon_edit_text)
    EmojiconEditText emojiconEditText;
    @BindView(R.id.emoji_btn)
    ImageView emojiButton;
    @BindView(R.id.comment_title)
    TextView commentTitle;
    @BindView(R.id.submit_btn)
    ImageView submitButton;

    /*@BindView(R.id.back_arrow)
    ImageView backButton;*/

    @BindView(R.id.recycler_view_comment)
    RecyclerView recyclerView;

    //varialble declaration
    EmojiconTextView textView;
    EmojIconActions emojIcon;
    ApiInterface apiInterface;
    CommentAdapter adapter;

    AllComments allComments;
    List<AllComments.Comment> commentList;
    String photoId = "";
    String commentDescription;
    LinearLayoutManager layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_navigation, container, false);
        ButterKnife.bind(this, view);

        emojiButton = (ImageView) view.findViewById(R.id.emoji_btn);
        submitButton = (ImageView) view.findViewById(R.id.submit_btn);
        emojiconEditText = (EmojiconEditText) view.findViewById(R.id.emojicon_edit_text);
        commentTitle = view.findViewById(R.id.comment_title);

        Bundle mArgs = getArguments();
        photoId = mArgs.getString(Constants.PHOTO_BLOG_PHOTO_ID);

        emojIcon = new EmojIconActions(getContext(), view, emojiconEditText, emojiButton);
        emojIcon.ShowEmojIcon();
        initRecyclerView();

        getAllComments();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });
        if (commentList != null && commentList.size() > 0) {
            commentTitle.setText("Comments(" + String.valueOf(commentList.size()) + ")");
        }


        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        //dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                        dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);


                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
            }
        });


        return view;
    }

    @OnClick(R.id.submit_btn)
    public void onSubmitClicked() {
        commentDescription = emojiconEditText.getText().toString();
        if (commentDescription.length() > 0) {
            makeComment();
        } else {
        }
    }

    @OnClick(R.id.comment_title)
    public void onBackPressed() {
        //getActivity().finish();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    private void initRecyclerView() {
        layout = new LinearLayoutManager(getContext());
        layout.setReverseLayout(true);
        recyclerView.setLayoutManager(layout);
        layout.setStackFromEnd(true);
        layout.setReverseLayout(true);
    }

    private void makeComment() {
        Data.pd = new ProgressDialog(getContext());
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
                    adapter = new CommentAdapter(getContext(), commentList);
                    recyclerView.setAdapter(adapter);
                    if (Data.pd != null) {
                        Data.pd.dismiss();
                    }
                    emojiconEditText.setText("");
                    commentTitle.setText("You have (" + String.valueOf(commentList.size()) + ") comments.");
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
                    adapter = new CommentAdapter(getContext(), commentList);
                    recyclerView.setAdapter(adapter);

                    ;
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
