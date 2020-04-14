package com.animee.loadweb.localweb;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class LocalJavaScript {

    Context context;
    public LocalJavaScript(Context context){
        this.context = context;
    }
    @JavascriptInterface
    public String callFromJS(String str){
        Toast.makeText(context,"调用了android当中的方法",Toast.LENGTH_SHORT).show();
        return "abc";
    }
}
