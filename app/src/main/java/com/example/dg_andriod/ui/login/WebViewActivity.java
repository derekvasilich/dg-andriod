package com.example.dg_andriod.ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.dg_andriod.R;

public class WebViewActivity extends Activity {

    private WebView webView;
    private String webViewUrl = "http://10.0.2.2:8081/";
    private WebSettings webSettings;
    private String token;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        token = getIntent().getStringExtra("token");
        type = getIntent().getStringExtra("type");

        webView = (WebView) findViewById(R.id.webview);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setAppCacheEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(true);

        webView.addJavascriptInterface(new WebAppInterface(this), "NativeWebInterface");
        webView.loadUrl(webViewUrl);
        webView.setWebViewClient(new WebViewClientImpl(this));
    }
}