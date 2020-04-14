package com.animee.loadweb;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.os.Build.VERSION_CODES.M;

public class CallPhoneActivity extends AppCompatActivity {
    String phoneNum = "1008611";
    int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_phone);
    }

    public void onClick(View view) {
//    检查权限
        checkPermission();

    }

    private void checkPermission() {
//        1.判断当前手机系统是否大于或等于6.0
        if (Build.VERSION.SDK_INT>=M) {
            // 2.如果大于6.0，则检查是否有打电话的权限授权
            int isPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);//用于检测程序是否包含某项权限
            if (isPermission == PackageManager.PERMISSION_GRANTED) {
//                //4.如果有打电话的授权，就直接拨打电话
                call(phoneNum);
            }else {
                //3.如果没有打电话的授权，就要去申请打电话的权限
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE);
            }
        }else {
            // 5.如果当前版本小于6.0，直接拨打电话
            call(phoneNum);
        }
    }
/* 此函数为申请权限的回调方法，无论成功或者失败，都会调用这个函数
* requestCode  ：上文中提到的请求码
* permissions  :申请的权限
* grantResults :申请的结果
* */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults!=null&&grantResults.length>0) {
//                判断用户是否授予了这个权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(phoneNum);
                }else {
                    Toast.makeText(this,"您拒绝了拨打电话的权限！",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void call(String phonenum) {
        /* 拨打电话的方法*/
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + phonenum);
        intent.setData(uri);
        startActivity(intent);
    }
}
