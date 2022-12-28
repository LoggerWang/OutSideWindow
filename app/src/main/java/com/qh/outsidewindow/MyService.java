package com.qh.outsidewindow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Date;

/**
 * @desc:
 * @author: wanglezhi
 * @createTime: 2022/12/28 10:17 上午
 */
public class MyService  extends Service {


    public int anHour; //记录间隔时间

    public int number = 0; //记录alertdialog出现次数

    private MediaPlayer mediaPlayer = new MediaPlayer();

    AlarmManager manager;
    PendingIntent pi;
    private int type = 0;//创建一个字符串，用来接收数据


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MyService.this,R.style.AlertDialogCustom);
//                    builder.setTitle("安卓气泡弹窗");
//                    builder.setMessage("这是由service拉起的气泡弹窗");
//                    builder.setCancelable(false);
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            mediaPlayer.reset();
//                            //initMediaPlayer();
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mediaPlayer.reset();
//                        }
//                    });
//                    final AlertDialog dialog = builder.create();
                    MyDialog dialog = new MyDialog(getApplicationContext());
                    dialog.updateTv(type);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//6.0 TYPE_APPLICATION_OVERLAY
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                    } else {
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    }
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_launcher_background);
                    dialog.show();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();//④返回Binder的事例
    }


    /**
     *②创建一个类继承Binder,来对data数据进行更新
     */
    public class Binder extends android.os.Binder{
        public void setData(int type){//③写一个公共方法，用来对data数据赋值。
            MyService.this.type = type;
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (number!=0) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.e("bai", "executed at " + new Date().toString()+"==type=="+type);
                    mHandler.sendEmptyMessage(1);
                }
            }).start();
        }
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        anHour = 10*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+(anHour);
        Intent i = new Intent(this,AlarmReceiver.class);
        pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        number++;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(pi);
    }

}
