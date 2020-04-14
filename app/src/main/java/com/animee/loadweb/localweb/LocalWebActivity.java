package com.animee.loadweb.localweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.animee.loadweb.R;

public class LocalWebActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_web);
        webView = findViewById(R.id.id_localweb);
        //   进行配置
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);  //设置支持js代码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);  //设置支持打开文件
        settings.setAllowFileAccessFromFileURLs(true);
        //        设置加载显示的网页
        webView.loadUrl("file:///android_asset/webview/myjs.html");

        //        设置加载在本应用的webview当中显示
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl("file:///android_asset/webview/myjs.html");
                return false;
            }
        });

        //        设置处理js代码的客户端
        webView.setWebChromeClient(new WebChromeClient());

        //        设置在js当中代用android当中的代码
        webView.addJavascriptInterface(new LocalJavaScript(this), "animee");

    }

    public void onClick(View view) {
        //        在android当中调用js的函数
        webView.loadUrl("javascript:testAlert()");
    }
}
