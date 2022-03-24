package net.boddo.btm.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;

public class AccountCloseActivity extends AppCompatActivity {
    AccountCloseActivity activity;
    TextView tvBackAllLikes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_close);

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

        activity = this;

        tvBackAllLikes = findViewById(R.id.tvBackAllLStory);

        tvBackAllLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(activity, FullPhotoViewActivity.class);
                startActivity(intent);*/

                finish();
            }
        });

    }
}