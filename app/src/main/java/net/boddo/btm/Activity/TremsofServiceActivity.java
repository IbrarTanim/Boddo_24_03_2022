package net.boddo.btm.Activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;

public class TremsofServiceActivity extends AppCompatActivity {

    private WebView tremsofServiceWebView;
    private String url = "https://bluetigermobile.com/palup/supportmessage.php?id=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tremsof_service);

        tremsofServiceWebView = findViewById(R.id.trems_of_service_webView);
        tremsofServiceWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = tremsofServiceWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        tremsofServiceWebView.loadUrl(url);
    }
}
