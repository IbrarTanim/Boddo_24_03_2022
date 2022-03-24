package net.boddo.btm.Activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;

public class PrivacyActivity extends AppCompatActivity {

    private WebView privacyWebView;
    private String url = "https://bluetigermobile.com/palup/supportmessage.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        privacyWebView = findViewById(R.id.privacy_webView);
        privacyWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = privacyWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        privacyWebView.loadUrl(url);
    }


    }

