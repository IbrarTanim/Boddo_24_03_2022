package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;

public class UserLocationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, UserLocationActivity.class);
        return intent;
    }
}
