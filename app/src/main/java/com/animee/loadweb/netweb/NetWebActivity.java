package com.animee.loadweb.netweb;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.animee.loadweb.R;

public class NetWebActivity extends AppCompatActivity {
    WebView webView;
    String url = "http://www.moviebase.cn/uread/app/viewArt/viewArt-0985242225a84c7eabe3eb62c9fa91bf.html?appVersion=1.7.0&osType=null&platform=2";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_web);
        webView = findViewById(R.id.id_netweb);
        //设置webview的信息配置
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        //加载网页信息
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //监听webview已经将网页加载完成了，添加图片的监听
                addImageClickListener(view);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        //因为在js当中调用了android的代码，所以需要设置通道
        webView.addJavascriptInterface(new NetJavaScriptInterface(this), "listener");
    }

    //    添加网页当中的图片监听的函数
    public void addImageClickListener(WebView webView) {
        webView.loadUrl("javascript:(function (){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "var head=objs[0].src;" +
                "var lazys = document.getElementsByClassName(\"lazy\");" +
                "var arr=[];" +
                "arr[0]=head;" +
                "for(var i=1;i<lazys.length;i++)  " +
                "{"
                + "      arr[i]=lazys[i-1].getAttribute('data-original');" +
                "}" +
                "for(var i=0;i<lazys.length;i++)  " +
                "{"
                + "    lazys[i].onclick=function()  " +
                "    {  "
                + "        window.listener.openImage(this.getAttribute('data-original'),arr);  " +
                "    }  " +
                "}" +
                "objs[0].onclick=function(){window.listener.openImage(this.src,arr);}" +
                "})()");
    }
}
