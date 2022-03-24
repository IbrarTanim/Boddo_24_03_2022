package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;

public class PersonalityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PersonalityActivity.class);
        return intent;
    }

}
