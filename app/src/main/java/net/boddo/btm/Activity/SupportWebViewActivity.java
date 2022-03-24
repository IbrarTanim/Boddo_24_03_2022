package net.boddo.btm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.SharedPref;

public class SupportWebViewActivity extends AppCompatActivity {

  private WebView supportWebView;
  private String url = "https://bluetigermobile.com/palup/supportmessage.php?id=";
  private Button faqButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_web_view);

        faqButton = findViewById(R.id.faq_button);
        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupportWebViewActivity.this, FAQActivity.class);
                startActivity(intent);
            }
        });

        supportWebView = findViewById(R.id.support_webView);
        supportWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = supportWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        supportWebView.loadUrl(url+ Data.userId+"&token="+ SharedPref.getUserAccessToken(Constants.ACCESS_TOKEN));


    }
}
