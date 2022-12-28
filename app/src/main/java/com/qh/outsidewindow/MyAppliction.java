package com.qh.outsidewindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @desc:
 * @author: wanglezhi
 * @createTime: 2022/12/28 2:39 下午
 */
public class MyAppliction extends Application {
    public static MyAppliction sInstance;
    public static int mCount;
    public static boolean mFront;//是否前台

    private static Timer mTimer;
    static int cntStart = 0;
    static int waitTime = 10;
    static TimerTask mTask = null;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        registerActivityLifecycleCallbacks();
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
                if (!mFront) {
                    mFront = true;
                    Log.e("", "AppContext------->处于前台");
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount--;
                if (mCount == 0) {
                    mFront = false;
                    Log.e("", "AppContext------->处于后台");
                    timerTask();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    private static void timerTask() {


        if (mTimer == null && mTask == null) {
            mTimer = new Timer();
            mTask = new TimerTask() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void run() {
                    cntStart++;
                    Log.e("timerTask", "timerTask   " + cntStart);
                    if (waitTime == cntStart) {
//                        go();
//                        isRunningForegroundToApp1(sInstance.getBaseContext(), SenseActivity.class);
                        cancelTimer();
                        Log.e("timerTask", "timerTask 30  " + cntStart);

                    }
                }
            };
            mTimer.schedule(mTask, 0, 1000);
        }
    }


    @SuppressLint("NewApi")
    public static void isRunningForegroundToApp1(Context context, final Class clz) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(20);
        /**枚举进程*/

        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
            Log.e("timerTask", "==taskInfo== " + taskInfo.toString()+"==class=="+clz);
            //*找到本应用的 task，并将它切换到前台
            if (taskInfo.baseActivity.getPackageName().equals(context.getPackageName())) {
                Log.e("timerTask", "timerTask  pid " + taskInfo.id);
                Log.e("timerTask", "timerTask  processName " + taskInfo.topActivity.getPackageName());
                Log.e("timerTask", "timerTask  getPackageName " + context.getPackageName());
                activityManager.moveTaskToFront(taskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                Intent intent = new Intent(context, clz);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setAction(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
                break;
            }
        }
    }




    private static void cancelTimer() {
        cntStart = 0;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }
}