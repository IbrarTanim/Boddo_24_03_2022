package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;

public class HeightActivity extends AppCompatActivity {

    HeightActivity activity;

    NumberPicker npHeight;
    TextView tvSaveHeihgt, tvBack;

    String value = "";
    String key = "height";
    boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        activity = this;

        //ButterKnife.bind(this);

        npHeight = findViewById(R.id.npHeight);
        npHeight.setMinValue(140);
        npHeight.setMaxValue(220);
        /*if (Data.userHeight != null || !Data.userHeight.equals("")) {
            npHeight.setValue(Integer.parseInt(Data.userHeight));
        }*/
        npHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                value = String.valueOf(newVal);
                isChanged = true;
                saveToServer();
            }
        });

        tvSaveHeihgt = findViewById(R.id.tvSaveHeihgt);
        tvBack = findViewById(R.id.tvBack);
        tvSaveHeihgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSave();
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void dataSave() {
        if (isChanged) {
            if (AboutUpdate.result) {
                Data.userHeight = value;
                Toast.makeText(this, "Update Successfull", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void saveToServer() {
        AboutUpdate update = new AboutUpdate(this);
        update.updateAbout(key, value);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HeightActivity.class);
        return intent;
    }
}
