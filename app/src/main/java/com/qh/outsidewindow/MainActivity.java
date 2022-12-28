package com.qh.outsidewindow;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    MyService.Binder binder;
    int type=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startMyService();
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binder != null){

                    binder.setData(type++);
                }
            }
        });


        findViewById(R.id.tv).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (binder != null){

                    binder.setData(type++);
//                    MyAppliction.isRunningForegroundToApp1(MainActivity.this, MainActivity.class);
                }
            }
        },5000);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startMyService() {
        boolean b = checkFloatPermission(this);
        Log.i("bbbbbbbb","" + b);
        String androidSDK = Build.VERSION.SDK;
        if(Integer.parseInt(androidSDK) >= 23 && !Settings.canDrawOverlays(MainActivity.this)){
            Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);

            intent2.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent2,1);
        }

        Intent startIntent = new Intent(this,MyService.class);
        startIntent.putExtra("type",0);
//        startService(startIntent);
        bindService(startIntent,this,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (checkFloatPermission(this)){
                Toast.makeText(MainActivity.this,"悬浮穿权限加载完成",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"悬浮穿权限加载失败",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        binder = (MyService.Binder) iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}