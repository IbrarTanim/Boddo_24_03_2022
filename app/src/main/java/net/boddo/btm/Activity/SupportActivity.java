package net.boddo.btm.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;

public class SupportActivity extends AppCompatActivity {

    Button faqButton,sendButton;
    EditText messageboxEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        faqButton = findViewById(R.id.faq_button);
        sendButton = findViewById(R.id.faq_send_button);
        messageboxEditText = findViewById(R.id.support_mess_et);


    }
}
