package net.boddo.btm.Activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import net.boddo.btm.R;

public class FAQActivity extends AppCompatActivity {

    private WebView faqWebView;
    private String url ="https://bluetigermobile.com/palup/faq/faq.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faqWebView = findViewById(R.id.faq_webView);
        faqWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = faqWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        faqWebView.loadUrl(url);

    }
}
