package com.animee.loadweb.netweb;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.animee.loadweb.localweb.DisplayImageActivity;

public class NetJavaScriptInterface {
    private Context context;

    public NetJavaScriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img, String[] imgs) {
        int size = imgs.length;
        int position = 0;
        for (int i = 0; i < size; i++) {
            if (img.equals(imgs[i])) {
                position = i;
            }
            Log.d("hyh", "imgs" + i + " :" + imgs[i]);
        }
        //跳转图片界面
        Intent intent = new Intent(context, DisplayImageActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("imgs", imgs);
        context.startActivity(intent);
    }

}
