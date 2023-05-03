package com.example.dg_andriod.ui.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import static com.example.dg_andriod.data.model.User.*;

public class WebViewClientImpl extends android.webkit.WebViewClient {

    private Activity activity = null;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        String type = activity.getSharedPreferences(JWT_SHARED_PREF_KEY, Context.MODE_PRIVATE).getString(JWT_TYPE_PREF_KEY, null);
        String token = activity.getSharedPreferences(JWT_SHARED_PREF_KEY, Context.MODE_PRIVATE).getString(JWT_TOKEN_PREF_KEY, null);

        // set auth tokens from login
        view.evaluateJavascript(String.format("sessionStorage.setItem(\"%s\", \"%s\")", JWT_TOKEN_PREF_KEY, token), null);
        view.evaluateJavascript(String.format("sessionStorage.setItem(\"%s\", \"%s\")", JWT_TYPE_PREF_KEY, type), null);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (url.indexOf("10.0.2.2") > -1 ) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;
    }

}
