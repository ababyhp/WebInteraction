package com.animee.loadweb.localweb;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.animee.loadweb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DisplayImageActivity extends AppCompatActivity {
    ViewPager viewPager;
    //    view的集合
    List<ImageView> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        viewPager = findViewById(R.id.id_vp);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        String[] imgs = intent.getStringArrayExtra("imgs");

        mDatas = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            Picasso.with(this).load(imgs[i]).into(iv);
            iv.setScaleType(ImageView.ScaleType.CENTER);
            mDatas.add(iv);
        }

        DisplayAdapter adapter = new DisplayAdapter(mDatas);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);
    }
}
