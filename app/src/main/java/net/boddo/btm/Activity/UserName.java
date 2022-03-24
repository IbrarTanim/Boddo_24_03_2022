package net.boddo.btm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.boddo.btm.R;
import net.boddo.btm.Utills.Data;

public class UserName extends AppCompatActivity {

    private EditText edtUserName;
    private TextView counterUserName;
    private ImageView tvClearUserName;
    private TextView tvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name3);

        edtUserName = findViewById(R.id.edtUserName);
        counterUserName = findViewById(R.id.counterUserName);
        tvClearUserName = findViewById(R.id.tvClearUserName);
        tvBack = findViewById(R.id.tvBack);


        edtUserName.setText(Data.userName);
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counterUserName.setText(""+s.length()+"/25");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvClearUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtUserName.setText("");
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserName.this,ProfileAndAccountsActivity.class));
                finish();
            }
        });

    }
}