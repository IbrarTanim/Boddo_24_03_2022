package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.boddo.btm.Activity.auth.RegistrantionActivity;
import net.boddo.btm.R;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    LandingActivity activity;
    LinearLayout llLogInByFacebook, llLogInByGmail;
    TextView tvSignUp, tvLogIn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        activity = this;
        llLogInByFacebook = findViewById(R.id.llLogInByFacebook);
        llLogInByGmail = findViewById(R.id.llLogInByGmail);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvLogIn = findViewById(R.id.tvLogIn);

        llLogInByFacebook.setOnClickListener(this);
        llLogInByGmail.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLogInByFacebook:
                Toast.makeText(activity, "Coming soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.llLogInByGmail:
                Toast.makeText(activity, "Coming soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvSignUp:
                Intent intent = new Intent(activity, RegistrantionActivity.class);
                startActivity(intent);
                break;
            case R.id.tvLogIn:
                intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}