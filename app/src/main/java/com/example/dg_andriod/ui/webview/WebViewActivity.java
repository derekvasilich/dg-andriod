package com.example.dg_andriod.ui.webview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.dg_andriod.BuildConfig;
import com.example.dg_andriod.R;

public class WebViewActivity extends Activity {

    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClientImpl(this));

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setAppCacheEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(true);

        webView.addJavascriptInterface(new WebAppInterface(this), "NativeWebInterface");
        webView.loadUrl(BuildConfig.WEB_VIEW_URL);
    }
}