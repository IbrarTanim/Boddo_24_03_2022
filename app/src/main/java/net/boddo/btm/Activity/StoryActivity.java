package net.boddo.btm.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ProgressDialog;

public class StoryActivity extends AppCompatActivity {

    private ProgressBar storyPB;
    private LinearLayout llmNoMatch;
    private TextView tvBackStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

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

        storyPB = findViewById(R.id.storyPB);
        llmNoMatch = findViewById(R.id.llmNoMatch);
        tvBackStory = findViewById(R.id.tvBackStory);

        tvBackStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                llmNoMatch.setVisibility(View.VISIBLE);
                ProgressDialog.cancel();
            }
        }, 2000);


    }

}