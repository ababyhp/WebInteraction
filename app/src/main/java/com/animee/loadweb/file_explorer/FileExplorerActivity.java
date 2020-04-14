package com.animee.loadweb.file_explorer;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.animee.loadweb.PermissonUtils;
import com.animee.loadweb.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileExplorerActivity extends AppCompatActivity {
    TextView pathTv;
    ImageButton backBtn;
    ListView fileLv;
    File currentParent;
    File[] currentFiles;
    File root;

    PermissonUtils.PermissionGrant permissionGrant = new PermissonUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            fileExplorer();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        pathTv = findViewById(R.id.id_tv_filepath);
        backBtn = findViewById(R.id.id_btn_back);
        fileLv = findViewById(R.id.id_lv_file);
        PermissonUtils.requestPermission(this, PermissonUtils.CODE_READ_EXTERNAL_STORAGE,permissionGrant );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissonUtils.requestPermissionsResult(this,requestCode,permissions,grantResults,permissionGrant);
    }

    private void fileExplorer() {
        //        判断手机中是否装载了sd卡
        boolean isLoadSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isLoadSDCard) {
//            获取sd卡的根目录
            root = Environment.getExternalStorageDirectory();
            currentParent = root;
//            获取当前文件夹下的所有文件
            currentFiles = currentParent.listFiles();
            Log.i("animee", "onCreate:currentFiles: "+currentFiles.length);
            inflateListView(currentFiles);
        }else {
            Toast.makeText(this,"SD卡没有被装载",Toast.LENGTH_SHORT).show();
        }

//        设置监听事件
        setListener();
    }

    private void setListener() {
        fileLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentFiles[position].isFile()) {
                    Toast.makeText(FileExplorerActivity.this,"无法打开此文件",Toast.LENGTH_SHORT).show();
                    return;
                }
//                获取当前点击的文件夹当中的所有文件
                File[] temp = currentFiles[position].listFiles();
                if (temp == null||temp.length == 0) {
                    Toast.makeText(FileExplorerActivity.this,"当前文件夹没有内容或者不能被访问",Toast.LENGTH_SHORT).show();
                }else {
//                    修改被点击的这项父目录
                    currentParent = currentFiles[position];
                    currentFiles = temp;
//                    数据源发生改变，重新设置适配器内容
                    inflateListView(currentFiles);
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 判断当前的目录是否为sd卡的根目录，如果是根目录，就直接退出activity。
                * 如果不是根目录，就获取当前目录的父目录，然后获得父目录的文件，在重新加载listview
                * */
                if (currentParent.getAbsolutePath().equals(root.getAbsolutePath())) {
                    FileExplorerActivity.this.finish();
                }else {
                    currentParent = currentParent.getParentFile();
                    currentFiles = currentParent.listFiles();
                    inflateListView(currentFiles);
                }
            }
        });
    }

    private void inflateListView(File[] currentFiles) {
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < currentFiles.length; i++) {
            Map<String,Object>map = new HashMap<>();
            map.put("filename",currentFiles[i].getName());
            if (currentFiles[i].isFile()) {
                map.put("icon",R.mipmap.file);
            }else {
                map.put("icon",R.mipmap.folder);
            }
            list.add(map);
        }
//       创建适配器对象
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_file_explorer, new String[]{"filename", "icon"}, new int[]{R.id.item_tv, R.id.item_icon});
        fileLv.setAdapter(adapter);
        pathTv.setText("当前路径:"+currentParent.getAbsolutePath());
    }
}
