package com.animee.loadweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.animee.loadweb.file_explorer.FileExplorerActivity;
import com.animee.loadweb.localweb.LocalWebActivity;
import com.animee.loadweb.netweb.NetWebActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn1:
                intent.setClass(this,ClockActivity.class);
                break;
            case R.id.btn2:
                intent.setClass(this,LocalWebActivity.class);
                break;
            case R.id.btn3:
                intent.setClass(this,NetWebActivity.class);
                break;
            case R.id.btn4:
                intent.setClass(this,FileExplorerActivity.class);
                break;
            case R.id.btn5:
                intent.setClass(this,CallPhoneActivity.class);
                break;
        }
        startActivity(intent);
    }
}
