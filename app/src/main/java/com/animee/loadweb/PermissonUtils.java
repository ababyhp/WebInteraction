package com.animee.loadweb;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 申请多个权限的工具类*/
public class PermissonUtils {
    public final static int CODE_RECERD_AUDIO = 0;
    public final static int CODE_GET_ACCOUNTS = 1;
    public final static int CODE_READ_PHONE_STATE = 2;
    public final static int CODE_CALL_PHONE = 3;
    public final static int CODE_CAMERA = 4;
    public final static int CODE_ACCESS_FINE_LOCATION = 5;
    public final static int CODE_ACCESS_COARSE_LOCATION = 6;
    public final static int CODE_READ_EXTERNAL_STORAGE = 7;
    public final static int CODE_WRITE_EXTERNAL_STORAGE = 8;
//    申请多个权限的请求码
    public final static int CODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_RECERD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final String[]requestPermissions = {
            PERMISSION_RECERD_AUDIO,PERMISSION_GET_ACCOUNTS,PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,PERMISSION_CAMERA,PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE
    };
// 表示授权成功的接口
    public  interface PermissionGrant{
        void onPermissionGranted(int requestCode);
    }
    /*
    * 封装请求权限的函数
    * */
    public static void requestPermission(Activity activity,int requestCode,PermissionGrant permissionGrant){
        if (activity == null) {
            return;
        }
//        排除不存在的请求码
        if(requestCode< 0||requestCode>=requestPermissions.length){
            return;
        }

        String requestPermission = requestPermissions[requestCode];
//       小于6.0默认授权状态
        if(Build.VERSION.SDK_INT<23){
            return;
        }
        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity,requestPermission);
        }catch (Exception e){
            Toast.makeText(activity,"请打开这个权限："+requestPermission,Toast.LENGTH_SHORT).show();
            return;
        }
        //判断是否被授权了
        if (checkSelfPermission!= PackageManager.PERMISSION_GRANTED) {
            // 没有被授权，需要进行申请
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,requestPermission)){
                shouldShowRationale(activity,requestCode,requestPermission);
            }else {
                ActivityCompat.requestPermissions(activity,new String[]{requestPermission},requestCode);
            }
        }else {
            //用户授权了，可以直接调用相关功能
            Toast.makeText(activity,"opened："+requestPermission,Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    private static void shouldShowRationale(final Activity activity, final int requestCode, final String requestPermission) {
        showMessageOKCancel(activity, "Rationale:" + requestPermission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity,new String[]{requestPermission},requestCode);
            }
        });
    }

    /*
    * 申请权限结果的方法
    * */
    public static void requestPermissionsResult(Activity activity, int requestCode,
                                                @NonNull String[] permissions, @NonNull int[] grantResults,PermissionGrant permissionGrant){
        if (activity == null) {
            return;
        }
        if(requestCode<0||requestCode>=requestPermissions.length){
            Toast.makeText(activity,"illegal requestCode:"+requestCode,Toast.LENGTH_SHORT).show();
            return;
        }

        if(grantResults.length==1&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
//            授权成功了
            permissionGrant.onPermissionGranted(requestCode);
        }else {
            String permissionError = permissions[requestCode];
            openSettingActivity(activity,"Result:"+permissionError);
        }
    }
    /*
    * 获取申请多个权限的结果
    * */
    public static void requestMultiResult(Activity activity,@NonNull String[] permissions, @NonNull int[] grantResults,
                                          PermissionGrant permissionGrant){
        if (activity == null) {
            return;
        }
        Map<String,Integer>perms = new HashMap<>();
        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i],grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }
        if (notGranted.size() == 0) {
            Toast.makeText(activity,"all permission succewss",Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }else {
            openSettingActivity(activity,"those permission need granted!");
        }

    }
    /*打开设置界面*/
    private static void openSettingActivity(final Activity activity, String msg) {
        showMessageOKCancel(activity, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }

    /* 弹出是否打开的对话框*/
    private static void showMessageOKCancel(Activity activity, String msg, DialogInterface.OnClickListener oklistener){
        new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setPositiveButton("OK",oklistener)
                .setNegativeButton("Cancel",null)
                .create().show();
    }

    /* 一次申请多个权限*/
    public static void requestMultiPermissions(final Activity activity, PermissionGrant grant){
        //获取没有被授权的权限
        ArrayList<String> permissionList = getNoGrantedPermission(activity, false);
        final ArrayList<String> shouldRationalePermissionList = getNoGrantedPermission(activity, true);
        if(permissionList==null||shouldRationalePermissionList==null){
            return;
        }

        if (permissionList.size()>0) {
            ActivityCompat.requestPermissions(activity,permissionList.toArray(new String[permissionList.size()]),
                    CODE_MULTI_PERMISSION);
        }else if (shouldRationalePermissionList.size()>0){
            showMessageOKCancel(activity, "should open those permissions", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(activity,
                            shouldRationalePermissionList.toArray(new String[shouldRationalePermissionList.size()]),CODE_MULTI_PERMISSION);
                }
            });
        }else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }
    }

    /*
    * 获取没有被授权的权限列表
    * */
    private static ArrayList<String> getNoGrantedPermission(Activity activity,boolean isShouldRationale) {
        ArrayList<String>permissions = new ArrayList<>();
        for (int i=0;i<requestPermissions.length;i++){
           String requestPermission =  requestPermissions[i];
           int checkSelfPermission = -1;
           try {
               checkSelfPermission = ActivityCompat.checkSelfPermission(activity,requestPermission);
           }catch (Exception e){
               Toast.makeText(activity,"please open those permission",Toast.LENGTH_SHORT).show();
               return null;
           }

           if (checkSelfPermission!=PackageManager.PERMISSION_GRANTED){
//               没有被授权需要去申请
               if (ActivityCompat.shouldShowRequestPermissionRationale(activity,requestPermission)) {
                   if (isShouldRationale) {
                       permissions.add(requestPermission);
                   }
               }else {
                   if (!isShouldRationale){
                       permissions.add(requestPermission);
                   }
               }
           }
        }

        return permissions;
    }
}
