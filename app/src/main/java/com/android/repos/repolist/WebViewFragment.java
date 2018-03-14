package com.android.repos.repolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.repos.R;

import org.jetbrains.annotations.Nullable;

public class WebViewFragment extends Fragment {

    public WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_webview, container, false);

        mWebView = v.findViewById(R.id.webview);
        mWebView.loadUrl("google.com");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }

    @Nullable
    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }
}